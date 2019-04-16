package me.own.learn.commons.base.exception.handler;

import me.own.learn.commons.base.exception.BusinessException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestApiExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
            ModelAndView modelAndView = new ModelAndView(mappingJackson2JsonView);
            modelAndView.addObject("error_code", businessException.getErrorCode());
            modelAndView.addObject("error_msg", businessException.getErrorMsg());
            modelAndView.addObject("error_classname", businessException.getClass().getName());
            if (null != businessException.getAttachment()) {
                modelAndView.addObject("attachment", businessException.getAttachment());
            }
            modelAndView.setStatus(businessException.getHttpStatus());
            return modelAndView;
        }
        return null;
    }
}
