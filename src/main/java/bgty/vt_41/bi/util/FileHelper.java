package bgty.vt_41.bi.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileHelper {
    public static void rebase(MultipartFile file, String filename) throws IOException {
        File savedFile = new File(filename);
        if(!savedFile.exists()) return;
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(savedFile));
    }

    public static String saveFile(MultipartFile file, String basePath)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String path = basePath + "/" + dateFormat.format(date);
        String newFileName = UUID.randomUUID().toString() + "." + extension;
        newFileName = newFileName.replaceAll("-", "");
        File savedFile = new File(path);
        savedFile.mkdirs();
        savedFile = new File(path + "/" + newFileName);
        try {
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(savedFile));
            //file.transferTo(savedFile); //отказывается работать (ссылается на неизвестный путь)
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return path + "/" + newFileName;
    }
}
