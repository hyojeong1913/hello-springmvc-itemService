package hello.itemservice;

import hello.itemservice.web.filter.LogFilter;
import hello.itemservice.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 필터 설정
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        // 등록할 필터 지정
        filterFilterRegistrationBean.setFilter(new LogFilter());

        // 필터는 체인으로 동작하므로 순서가 필요. 낮을수록 먼저 동작
        filterFilterRegistrationBean.setOrder(1);

        // 필터를 적용할 URL 패턴 지정. 한번에 여러 패턴을 지정 가능
        // 필터를 등록할 때 urlPattern 을 /* 로 등록했기 때문에 모든 요청에 해당 필터가 적용됨.
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean loginCheckFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        // 로그인 필터 등록
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());

        // 위 로그 필터 다음에 로그인 필터 적용
        filterFilterRegistrationBean.setOrder(2);

        // 모든 요청에 로그인 필터를 적용
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }
}
