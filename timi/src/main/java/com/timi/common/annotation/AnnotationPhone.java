package com.timi.common.annotation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hhh
 * @date 2021/5/27
 */
@Slf4j
public class AnnotationPhone extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMapping)) {
            log.warn("UnSupport handler");
            return true;
        }
        //获取方法上加了注解的参数名称
        List<String> list = getParamsName((HandlerMethod) handler);
        for (String s : list) {
            //判空
            String parameter = request.getParameter(s);
            if (StringUtils.isBlank(parameter) || "".equals(parameter.trim())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "必传参数错误");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.getWriter().write(jsonObject.toJSONString());
                return false;
            }
        }
        //如果获取的对象为空,说明方法上没有此注解,直接放行
        return true;

    }


    /**拿到在参数上加了该注解的参数名称*/
    private List<String> getParamsName(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        List<String> list = new ArrayList<String>();
        for (Parameter parameter : parameters) {
            if(parameter.isAnnotationPresent(Phone.class)){
                list.add(parameter.getName());
            }
        }
        return list;
    }

}
