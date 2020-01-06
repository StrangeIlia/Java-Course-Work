package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.RANGE;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    VideoRepository videoRepository;

    public static final String filePath = "files";
    public static final Integer bufferSize = 1024;

    @GetMapping("**")
    public void getFile(HttpServletRequest request, HttpServletResponse response)
    {

        String filename = request.getRequestURL().toString();
        try {
            int start = filename.indexOf(filePath) + filePath.length() + 1;
            filename = filename.substring(start);
            File file = new File(filename);
            if(file.exists()) {
                if(filename.indexOf(VideoController.basePathForVideo) != -1)
                {
                    Optional<Video> optionalVideo = videoRepository.findByPath(filename);
                    if(optionalVideo.isPresent())
                    {
                        Video video = optionalVideo.get();
                        Optional<String> optionalRange = Optional.ofNullable(request.getHeader(RANGE));

                        response.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                        response.addHeader(HttpHeaders.DATE, video.getCreatedAt().toString());
                        response.addHeader(HttpHeaders.LAST_MODIFIED, video.getUpdatedAt().toString());
                        response.setContentType("video/" + FilenameUtils.getExtension(file.getName()));

                        if(optionalRange.isPresent()){
                            String range = optionalRange.get();
                            final String header = "bytes=";
                            if(range.indexOf(header) != -1)
                            {
                                range = range.replaceFirst(header, "");
                                int indexRange = range.indexOf('-');
                                if(indexRange != -1)
                                {
                                    String strIndexStart = range.substring(0, indexRange);
                                    String strIndexEnd = range.substring(indexRange + 1);

                                    Long indexStart = Long.valueOf(strIndexStart);
                                    Long indexEnd;
                                    if(strIndexEnd == "") {
                                        indexEnd = file.getTotalSpace();
                                    }
                                    else {
                                        indexEnd = Long.valueOf(strIndexEnd);
                                    }
                                    if(indexStart < indexEnd)
                                    {
                                        response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
                                        response.setContentLengthLong(indexEnd - indexStart);
                                        response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " +
                                                strIndexStart + "-" + strIndexEnd +
                                                "/" + (indexEnd - indexStart));

                                        RandomAccessFile inputStream = new RandomAccessFile(file, "r");
                                        OutputStream outputStream = response.getOutputStream();
                                        inputStream.seek(indexStart);
                                        byte[] buffer = new byte[bufferSize];
                                        while(inputStream.getFilePointer() < indexEnd)
                                        {
                                            int countAvailableBytes = (int) Long.min(indexEnd - inputStream.getFilePointer(), bufferSize);
                                            inputStream.read(buffer, 0, countAvailableBytes);
                                            outputStream.write(buffer, 0, countAvailableBytes);
                                            outputStream.flush();
                                        }
                                        return;
                                    }
                                    else
                                    {
                                        response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
                                        return;
                                    }
                                }
                            }
                        }
                        else {
                            response.setStatus(HttpStatus.NOT_MODIFIED.value());
                            response.setContentLengthLong(file.length());
                            return;
                        }
                    }
                }
                else
                {
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
                    response.setStatus(HttpStatus.OK.value());
                    InputStream inputStream = new FileInputStream(file);
                    OutputStream outputStream = response.getOutputStream();
                    int readCount;
                    byte[] buffer = new byte[bufferSize];
                    while((readCount = inputStream.read(buffer)) != -1){
                        outputStream.write(buffer, 0, readCount);
                    }
                    return;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }
}
