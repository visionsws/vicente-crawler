package cn.com.bluemoon.demo.service.impl;

import cn.com.bluemoon.demo.common.ExceptionEnum;
import cn.com.bluemoon.demo.exception.DescribeException;
import cn.com.bluemoon.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String test(String msg) {
        System.out.println(msg);
        logger.info("最后的运行结果为:{}",msg);
        return msg;
    }

    @Override
    public String error(String msg) {
        throw new DescribeException(ExceptionEnum.USER_NOT_FIND);
    }

}
