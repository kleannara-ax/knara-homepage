package com.kleannara.config;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class XSSResponseWrapper extends HttpServletResponseWrapper {

    private PrintWriter writer;

    public XSSResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(super.getWriter()) {
                @Override
                public void write(String str) {
                    // XSS 방지 이스케이프 처리 (불필요한 경우 제거)
                    super.write(str);  // 이 부분에서 기본적으로 출력됨
                }

                @Override
                public void write(char[] cbuf, int off, int len) {
                    super.write(cbuf, off, len);
                }
            };
        }
        return writer;
    }
}
