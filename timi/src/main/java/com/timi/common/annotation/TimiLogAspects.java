package com.timi.common.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.timi.common.config.TimiLogProperties;
import com.timi.common.constant.TimiConstant;
import com.timi.modules.log.entity.TimiLogEntity;
import com.timi.modules.user.holder.UserContentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @author hhh
 * @date 2021/6/8
 */
@Slf4j
@Aspect
public class TimiLogAspects {
    @Resource(name = "timiLogProperties")
    private TimiLogProperties timiLogProperties;
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource(name = "logRestTemplate")
    private RestTemplate restTemplate;
    //@Pointcut("execution(* com..*Controller.*(..))")
    @Pointcut("@annotation(com.timi.common.annotation.TimiLog)")
    public void logPointCut() {
    }

    //配置织入点

    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) throws Exception {
        handleLog(joinPoint, null, jsonResult);
    }
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) throws Exception {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) throws Exception {
        log.info("开始执行------------------------------>");
        try {  // 获得注解
            TimiLog controllerLog = getAnnotationLog(joinPoint);
            if (null == controllerLog) {
                return;
            }
            
            TimiLogEntity entity = new TimiLogEntity();

            // 返回参数
            if (jsonResult != null) {
                entity.setResponseParam(JSONObject.toJSONString(jsonResult));
            }
            if (e != null) {
                entity.setResponseParam(e.getMessage());
            }
            //设置url
            HttpServletRequest request = getRequest();
            entity.setUserName(UserContentHolder.getContext().getUsername());
            entity.setRequestUrl(request.getRequestURI());
            // 设置请求方式
            entity.setRequestMethod(request.getMethod());
            entity.setRequestTime(new Date());
            //获取ip
            log.info("IP                 : {}", request.getRemoteAddr());
            entity.setSourceIp(request.getHeader("sourceIp"));
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, entity);
            log.info("--gaeaLog:requestUrl--{}", entity.getRequestUrl());
            log.info("--gaeaLog:requestData--{}", entity.getRequestParam());
            log.info("--gaeaLog:requestAllData--{}", JSON.toJSONString(entity));
            //执行回调
            if (!StringUtils.isEmpty(timiLogProperties.getCallbackUrl())) {
                threadPoolTaskExecutor.execute(() -> {
                    restTemplateCallback(entity, request);
                });
            }
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("--gaeaLog:error--{}", e.getMessage());
        }
    }


    /**
     * 微服务模式下，通过回调方法传输日志数据
     *
     * @param TimiLogEntity
     */
    private void restTemplateCallback(TimiLogEntity TimiLogEntity, HttpServletRequest request) {
        String url = timiLogProperties.getCallbackUrl();
        try {
            log.info("--gaeaLog:callBack:url-{}--", url);
            HttpHeaders headers_new = new HttpHeaders();
            headers_new.setContentType(MediaType.APPLICATION_JSON);
            headers_new.set("Accept", "application/json;charset=UTF-8");
            headers_new.set("Authorization", request.getHeader(TimiConstant.Authorization));
            HttpEntity entity = new HttpEntity(TimiLogEntity, headers_new);
            JSONObject responseBody = restTemplate.postForObject(url, entity, JSONObject.class);
            log.info("--gaeaLog:callBack:response-{}", responseBody);
        } catch (Exception e) {
            log.error("--gaeaLog:callBack:error--{}", e.getMessage());
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param entity 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, TimiLog log, TimiLogEntity entity) throws Exception {
        // 设置标题
        entity.setPageTitle(log.pageTitle());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            //  ，传入到数据库中。
            setRequestValue(joinPoint, entity);
        }
        if (!log.isSaveResponseData()) {
            entity.setResponseParam(null);
        }
    }


    /**
     * 获取请求的参数，放到log中
     *
     * @param entity 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, TimiLogEntity entity) throws Exception {
        String requestMethod = entity.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            entity.setRequestParam(params);
        } else if (HttpMethod.GET.name().equals(requestMethod)) {
            Map<String, String[]> paramMap = getRequest().getParameterMap();
            entity.setRequestParam(JSON.toJSONString(paramMap));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) getRequest().getAttribute("HandlerMapping" + ".uriTemplateVariables");
            if (null != paramsMap) {
                entity.setRequestParam(paramsMap.toString());
            }
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    try {
                        Object jsonObj = JSON.toJSON(paramsArray[i]);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }


    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private TimiLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(TimiLog.class);
        }
        return null;
    }
}
