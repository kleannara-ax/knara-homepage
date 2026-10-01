package com.kleannara.config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.util.HtmlUtils;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value != null) {
            return HtmlUtils.htmlUnescape(value);  // HTML 디코딩
        }
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }

        int length = values.length;
        String[] decodedValues = new String[length];
        for (int i = 0; i < length; i++) {
            decodedValues[i] = HtmlUtils.htmlUnescape(values[i]);  // HTML 디코딩
        }
        return decodedValues;
    }
}
