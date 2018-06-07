package com.optimal.web.optimal_web.config;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogInterceptor {

	public static String GLOBAL_MSG_ID = "_global_msg_id";
	
	public static String URI = "URI";

	@Before("execution (* com.optimal.web.optimal_web.action.*.*(..))")
	public void before(JoinPoint joinPoint) {
		
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		MDC.put(URI, request.getRequestURI());
		String ids = UUID.randomUUID().toString();
		ids = ids.replaceAll("-", "");
		MDC.put(GLOBAL_MSG_ID, ids);
//		log.info("请求url->{}", request.getRequestURL());
	}

	@Around("execution (* com.optimal.web.optimal_web.action.*.*(..))")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
		StopWatch sw = new StopWatch(methodName);
        sw.start();
		Object result = pjp.proceed();
		sw.stop();
		log.info(JSON.toJSONString(buildBizLogInfo(pjp, result)));
        log.info(sw.shortSummary());
		return result;
		
//		String ids = MDC.get(GLOBAL_MSG_ID);
//		log.info("{} {}执行结果->{}", ids, methodName, result);
	}
	
	private BizLogInfo buildBizLogInfo(JoinPoint joinPoint, Object result){
        BizLogInfo bizLogInfo = new BizLogInfo();
        bizLogInfo.setOpFinger(getOpFinger(joinPoint));
        bizLogInfo.setInput(joinPoint.getArgs());
        bizLogInfo.setUri(MDC.get(URI));
        bizLogInfo.setResult(result);
        return bizLogInfo;
    }
	
	private String getOpFinger(JoinPoint joinPoint){
		String name = joinPoint.getTarget().getClass().getName();
		return name.replaceAll("\\$.*", "") + "." + joinPoint.getSignature().getName();
    }

}
