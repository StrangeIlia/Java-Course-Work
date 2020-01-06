package bgty.vt_41.bi.web;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/files")
public class FileController {
    public static final String filePath = "files";

    @RequestMapping("**")
    @ResponseBody
    public ResponseEntity<Resource> getFile(HttpServletRequest request)
    {
        String filename = request.getRequestURL().toString();
        try {
            // Регулярки конечно хорошо, но и без них прекрасно
            //Pattern pattern = Pattern.compile("(files).+");
            //Matcher matcher = pattern.matcher(filename);
            //matcher.find();
            //start =  matcher.start();
            int start = filename.indexOf(filePath) + filePath.length() + 1; // 1 - это символ '/'
            filename = filename.substring(start);
            File file = new File(filename);
            if(file.exists()){
                final Resource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachemt; filename=\"" + file.getName() + "\"")
                        .body(resource);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
