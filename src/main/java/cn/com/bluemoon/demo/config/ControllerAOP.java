package cn.com.bluemoon.demo.config;

import cn.com.bluemoon.demo.common.ResultBean;
import cn.com.bluemoon.demo.exception.DescribeException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAOP {

    @Pointcut("execution(* cn.com.bluemoon.demo.controller..*.*(..))")
    public void log(){
    }

    @Around("log()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        log.info("args:"+ JSON.toJSONString(pjp.getArgs()));
        ResultBean<?> result;
        try {
            result = (ResultBean<?>) pjp.proceed();
            log.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            result = handlerException(pjp, e);
        }

        return result;
    }

    /**
     * 封装异常信息，注意区分已知异常（自己抛出的）和未知异常
     */
    private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean();

        // 已知异常
        if (e instanceof DescribeException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(ResultBean.FAIL_CODE);
        }  else {
            log.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg(e.toString());
            result.setCode(ResultBean.FAIL_CODE);
        }
        return result;
    }
}
