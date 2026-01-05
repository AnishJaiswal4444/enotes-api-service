package Enotes_API_Service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* Enotes_API_Service.controller..*(..))")
    public Object jointPointController(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {} ()", className, methodName);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("End Calling :: {} :: {} () :: {} ms", className, methodName, duration);
        return result;
    }

    @Around("execution(* Enotes_API_Service.service..*(..))")
    public Object jointPointService(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {} ()", className, methodName);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("End Calling :: {} :: {} () :: {} ms", className, methodName, duration);
        return result;
    }
}
