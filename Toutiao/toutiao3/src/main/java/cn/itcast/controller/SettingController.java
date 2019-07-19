package cn.itcast.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * @author
 * @create 2019-06-29 0:37
 */
@Controller
@RequestMapping(path="/setting")
public class SettingController {
    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);
    private final String  IMG_PATH_PREFIX = "static/upload/1.png";
    @RequestMapping(path = "/hello")
    @ResponseBody
    public String hello(){
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        System.out.println(fileDirPath);
        File file = new File(fileDirPath);
        System.out.println(file.exists());
        return "hello";
    }
}
