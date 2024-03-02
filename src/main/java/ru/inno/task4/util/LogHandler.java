package ru.inno.task4.util;

import lombok.AllArgsConstructor;
import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.exception.WriteLogFileException;
import ru.inno.task4.service.Log;
import ru.inno.task4.service.Logable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;

@AllArgsConstructor
public class LogHandler implements InvocationHandler {
    private final Object obj;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var realMethod = obj.getClass().getMethod( method.getName(), (Class[]) method.getGenericParameterTypes());
        System.out.println("LogHandler working");
        var result = method.invoke(obj,args);
        if (realMethod.isAnnotationPresent(LogTransformation.class)) {
            try (Logable logFile = new Log(realMethod.getAnnotation(LogTransformation.class).logPath())){
                logFile.putLog(Instant.now().toString());
            }
            catch (Exception e){
                throw new WriteLogFileException();
            }
        }
        return result;
    }
}
