package hello.itemservice.typeconverter.converter;

import hello.itemservice.web.typeconverter.converter.IntegerToStringConverter;
import hello.itemservice.web.typeconverter.converter.IpPortToStringConverter;
import hello.itemservice.web.typeconverter.converter.StringToIntegerConverter;
import hello.itemservice.web.typeconverter.converter.StringToIpPortConverter;
import hello.itemservice.web.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.*;

/**
 * 컨버전 서비스 - ConversionService 테스트
 *
 * 스프링이 제공하는 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능
 */
public class ConversionServiceTest {

    @Test
    void conversionService() {

        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();

        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 사용
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
    }
}
