package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.interceptors.NoLoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {

        if(ex instanceof NoLoginException){
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常");

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            if(null == responseBody){
                if(ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("code",pe.getCode());
                    mv.addObject("msg",pe.getMsg());
                }
                return mv;
            }else{
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试");

                if(ex instanceof ParamsException){
                    ParamsException pe = (ParamsException)ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out =null;
                try {
                    out = resp.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(out != null){
                        out.close();
                    }
                }
                return null;
            }
        }
        return mv;
    }
}
