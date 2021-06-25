package io.dany.gmail.safe.adapter.in.util;

import io.dany.gmail.safe.adapter.in.mapper.UseCaseExceptionMapper;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class HttpServletResponseUtil {

    private HttpServletResponseUtil() {
    }

    public static void showException(HttpServletResponse response, UseCaseException exception) {
        try {
            response.setStatus(UseCaseExceptionMapper.to(exception));
            response.setContentType(ContentType.TEXT_PLAIN.getMimeType());
            response.setHeader("Content-Disposition", "");
            response.getOutputStream().write(exception.getMessage().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadZip(HttpServletResponse response) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=backup.zip");
        response.setStatus(HttpStatus.OK.value());
    }
}
