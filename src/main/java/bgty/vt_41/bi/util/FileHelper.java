package bgty.vt_41.bi.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileHelper {
    public static String rebase(MultipartFile file, String filename) throws IOException {
        if (file == null) return null;
        File savedFile = new File(filename);
        if (!savedFile.exists()) return null;
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(savedFile));
        return savedFile.getPath();
    }

    public static void deleteFile(String filename) {
        Path filePath = Paths.get(filename);
        try {
            Files.deleteIfExists(filePath); //Удаляем файл

            //Удаляем все пустые директории
            /*while (true){
                filePath = filePath.getParent();
                long fileCount = Files.list(filePath).count();
                if(fileCount == 0L)
                    Files.deleteIfExists(filePath);
                else break;
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saveFile(MultipartFile file, String basePath) {
        if (file == null) return null;
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
