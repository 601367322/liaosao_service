package com.xl.exception;

import com.xl.util.MyJSONUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Shen on 2015/12/26.
 */
public class MyExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        try {
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(MyJSONUtil.getErrorInfoJsonObject(e.getMessage()).toString());
            writer.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
