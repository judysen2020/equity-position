package com.github.judysenequityposition.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggerAop {
    Logger logger= LoggerFactory.getLogger(LoggerAop.class);
    @Pointcut("execution(* com.github.judysenequityposition..*.*(..))")
    public void mypointcut() {
    }
    //环绕增强
    @Around("mypointcut()")
    public Object aroundLogger(ProceedingJoinPoint jp) {
        logger.info("在==>>" + jp.getTarget().getClass().getName() + "类里面使用AOP环绕增强==");
        logger.info("*环绕增强*调用【" + jp.getTarget().getClass().getSimpleName() + "】的【 " + jp.getSignature().getName()
                + "】方法。方法入参【" + Arrays.toString(jp.getArgs()) + "】");
        try {
            Object result = jp.proceed();
            logger.info("*环绕增强*调用 " + jp.getTarget() + "的【 "
                    + jp.getSignature().getName() + "】方法。方法返回值【" + result + "】");
            return result;
        } catch (Throwable e) {
            logger.error(jp.getSignature().getName() + " 方法发生异常【" + e + "】");
            //--TODO--返回统一的错误信息
            return null;
        } finally {
            logger.info("*环绕增强*执行finally【" + jp.getSignature().getName() + "】方法结束执行<<==。");
        }
    }
}
