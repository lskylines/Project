package cn.itcast.service;

import cn.itcast.Utils.ToutiaoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author
 * @create 2019-06-29 11:20
 */
@Service
public class UploadService {

    public String uploadImage(MultipartFile file) throws IOException {

        int postPoint = file.getOriginalFilename().lastIndexOf(".");
        if(postPoint<0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(postPoint+1).toLowerCase();
        if(!ToutiaoUtil.isAllow(fileExt)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        File uploadDir  =new File(ToutiaoUtil.IMG_PATH);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMG_PATH+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        String fileUrl = "/newsImage/" + fileName;
        return fileUrl;
    }
}
