package cn.com.bluemoon.demo.controller;

import cn.com.bluemoon.demo.common.ResultBean;
import cn.com.bluemoon.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shiweisen
 * @since 2018-08-31
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;


    @RequestMapping("/test")
    public ResultBean test(String msg) {
        logger.info("controller msg:{}",msg);
        String res = demoService.test(msg);
        return new ResultBean(res);
    }


    @RequestMapping("/error")
    public ResultBean error(String msg) {
        logger.info("controller msg:{}",msg);
        String res = demoService.error(msg);
        return new ResultBean(res);
    }


}
