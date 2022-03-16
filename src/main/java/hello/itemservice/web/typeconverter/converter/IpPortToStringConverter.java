package hello.itemservice.web.typeconverter.converter;

import hello.itemservice.web.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * IpPort 객체를 문자로 변환하는 타입 컨버터
 */
@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {

        log.info("convert source = {}", source);

        return source.getIp() + ":" + source.getPort();
    }
}
