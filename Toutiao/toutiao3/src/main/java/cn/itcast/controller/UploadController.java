package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author
 * @create 2019-06-29 10:22
 */
@Controller
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private UploadService uploadService;
    @RequestMapping(path="/uploadImage", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String fileUrl = uploadService.uploadImage(file);
            if (fileUrl != null) {
                return ToutiaoUtil.getJson(0, fileUrl);
            }
            return ToutiaoUtil.getJson(1, "上传失败");
        }catch(Exception e){
            logger.info("上传异常" + e.getMessage());
            return ToutiaoUtil.getJson(1, "上传异常");
        }


    }
}
