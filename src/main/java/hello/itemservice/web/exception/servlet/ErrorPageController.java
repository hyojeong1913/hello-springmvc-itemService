package hello.itemservice.web.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 해당 오류를 처리할 컨트롤러
 */
@Slf4j
@Controller
public class ErrorPageController {

    // RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {

        log.info("errorPage 404");

        printErrorInfo(request);

        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {

        log.info("errorPage 500");

        printErrorInfo(request);

        return "error-page/500";
    }

    /**
     * API 를 요청했을 경우 API 로 JSON 형식으로 데이터 반환되도록
     *
     * produces = MediaType.APPLICATION_JSON_VALUE
     *  : 클라이언트가 요청하는 HTTP Header 의 Accept 의 값이 application/json 일 때 해당 메서드가 호출
     *
     * @param request
     * @param response
     * @return 응답 데이터를 위해서 Map 을 만들고 status , message 키에 값을 할당
     */
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();

        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);

        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }

    /**
     * request.attribute 에 서버가 담아준 정보
     *
     * javax.servlet.error.exception : 예외
     * javax.servlet.error.exception_type : 예외 타입
     * javax.servlet.error.message : 오류 메시지
     * javax.servlet.error.request_uri : 클라이언트 요청 URI
     * javax.servlet.error.servlet_name : 오류가 발생한 서블릿 이름
     * javax.servlet.error.status_code : HTTP 상태 코드
     * 
     * @param request
     */
    private void printErrorInfo(HttpServletRequest request) {

        log.info("ERROR_EXCEPTION = {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE = {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE = {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI = {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME = {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE = {}", request.getAttribute(ERROR_STATUS_CODE));

        log.info("dispatchType = {}", request.getDispatcherType());
    }
}
