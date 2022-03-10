package hello.itemservice;

import hello.itemservice.web.argumentresolver.LoginMemberArgumentResolver;
import hello.itemservice.web.exception.interceptor.LogInterceptor;
import hello.itemservice.web.filter.LogFilter;
import hello.itemservice.web.filter.LoginCheckFilter;
import hello.itemservice.web.interceptor.LoginCheckInterceptor;
import hello.itemservice.web.interceptor.LoginInterceptor;
import hello.itemservice.web.resolver.MyHandlerExceptionResolver;
import hello.itemservice.web.resolver.UserHandlerExceptionResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

/**
 * 필터 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 서블릿 예외 처리 - 필터
     *
     * @return
     */
//    @Bean
    public FilterRegistrationBean logFilter() {

        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        // 클라이언트 요청은 물론이고, 오류 페이지 요청에서도 필터 호출
        // default : DispatcherType.REQUEST = 클라이언트의 요청이 있는 경우에만 필터가 적용
        // 특별히 오류 페이지 경로도 필터를 적용할 것이 아니면 기본 값을 그대로 사용
        // 오류 페이지 요청 전용 필터를 적용하고 싶으면 DispatcherType.ERROR 만 지정
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

        return filterRegistrationBean;
    }

    /**
     * 서블릿 예외처리 인터셉터
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**");
    }

    /**
     * 기본 설정을 유지하면서 추가
     *
     * configureHandlerExceptionResolvers(..) 를 사용하면 스프링이 기본으로 등록하는 ExceptionResolver 가 제거되므로 주의,
     * extendHandlerExceptionResolvers 를 사용
     *
     * @param resolvers
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    /**
     * 공통 작업이 필요할 때 컨트롤러를 더욱 편리하게 사용 가능
     *
     * @param resolvers
     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//
//        resolvers.add(new LoginMemberArgumentResolver());
//    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        // 인터셉터 등록
//        registry.addInterceptor(new LoginInterceptor())
//                .order(1) // 인터셉터의 호출 순서를 지정. 낮을 수록 먼저 호출
//                .addPathPatterns("/**") // 인터셉터를 적용할 URL 패턴 지정
//                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 인터셉터에서 제외할 패턴 지정
//
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        // 로그인 처리 - 필터, 인터셉터 관련 URL
//                        "/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error",
//                        // 예외 처리와 오류 페이지 관련 URL
//                        "/error-ex", "/error-404", "/error-500", "/error-page/**"
//                );
//    }

//    @Bean
//    public FilterRegistrationBean logFilter() {
//
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//
//        // 등록할 필터 지정
//        filterFilterRegistrationBean.setFilter(new LogFilter());
//
//        // 필터는 체인으로 동작하므로 순서가 필요. 낮을수록 먼저 동작
//        filterFilterRegistrationBean.setOrder(1);
//
//        // 필터를 적용할 URL 패턴 지정. 한번에 여러 패턴을 지정 가능
//        // 필터를 등록할 때 urlPattern 을 /* 로 등록했기 때문에 모든 요청에 해당 필터가 적용됨.
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//
//        return filterFilterRegistrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean loginCheckFilter() {
//
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//
//        // 로그인 필터 등록
//        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
//
//        // 위 로그 필터 다음에 로그인 필터 적용
//        filterFilterRegistrationBean.setOrder(2);
//
//        // 모든 요청에 로그인 필터를 적용
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//
//        return filterFilterRegistrationBean;
//    }
}
