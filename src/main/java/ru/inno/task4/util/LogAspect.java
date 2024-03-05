package ru.inno.task4.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.service.Log;
import ru.inno.task4.service.Logable;

import java.time.Instant;
import java.util.Arrays;
import java.util.StringJoiner;

@Aspect
@Component
public class LogAspect {
    @Pointcut("@annotation(ru.inno.task4.annotations.LogTransformation)")
    public void callLogTransformation(){}

    @Before("callLogTransformation()")
    public void beforeLogTransformation(JoinPoint jp){
        putLog(jp,"Before");
    }
    @After("callLogTransformation()")
    public void afterLogTransformation(JoinPoint jp) {
        putLog(jp,"After");
    }
    private void putLog(JoinPoint jp, String addInfo){
        MethodSignature signature = (MethodSignature) jp.getSignature();
        var realMethod = signature.getMethod();
        if (realMethod.isAnnotationPresent(LogTransformation.class)) {
            try (Logable logFile = new Log(realMethod.getAnnotation(LogTransformation.class).logPath())){
                var log = new StringJoiner("\t")
                        .add(Instant.now().toString())
                        .add(addInfo)
                        .add(signature.getDeclaringType().getName())
                        .add(Arrays.toString(jp.getArgs()))
                        ;
                logFile.putLog(log.toString());
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
