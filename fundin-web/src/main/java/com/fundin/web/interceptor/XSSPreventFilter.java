package com.fundin.web.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class XSSPreventFilter extends OncePerRequestFilter {

    /**
     * Regex pattern to filter XSS code.
     */
    private static final String REPLACE_PATTERN = "<.*>";

    private static final String EXCLUDE_PARAM_NAME = "content";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
    		FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new ParameterRequestWrapper(request), response);
    }

    private static class ParameterRequestWrapper extends HttpServletRequestWrapper {

        public ParameterRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private String replaceXss(String paramValue) {
            if (StringUtils.isNotBlank(paramValue)) {
            	return StringUtils.replacePattern(paramValue, REPLACE_PATTERN, "");
            }
            return paramValue;
        }

        public String getParameter(String name) {
            String paramValue = getRequest().getParameter(name);
            if (StringUtils.isBlank(paramValue) || 
            		EXCLUDE_PARAM_NAME.equalsIgnoreCase(name)) {
            	return paramValue;
            }
            return replaceXss(paramValue);
        }

        public String[] getParameterValues(String name) {
            String[] paramValues = getRequest().getParameterValues(name);
            if (paramValues == null || 
            		EXCLUDE_PARAM_NAME.equalsIgnoreCase(name)) {
                return paramValues;
            }
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                paramValues[i] = replaceXss(paramValue);
            }
            return paramValues;
        }
    }
    
}
