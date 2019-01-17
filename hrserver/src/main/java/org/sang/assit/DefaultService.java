package org.sang.assit;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yq
 * @date 2018/12/05 11:09
 * @description 默认service,提供token
 * @since V1.0.0
 */
public interface DefaultService {

    /**
     * 获取请求
     * @return request
     */
    default HttpServletRequest getRequest(){
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes() ;
        HttpServletRequest request =  sra.getRequest();
        if(request == null){
            throw new RuntimeException("请求非法");
        }
        return request;
    }
}
