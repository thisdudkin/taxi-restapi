package by.dudkin.passenger.rest.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;

/**
 * @author Alexander Dudkin
 */
public class ApiUtil {
    public ApiUtil() {
    }

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = (HttpServletResponse) req.getNativeResponse(HttpServletResponse.class);
            if (res != null) {
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Type", contentType);
                res.getWriter().println(example);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
