package hello.itemservice.web.typeconverter.converter;

import hello.itemservice.web.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * 문자를 IpPort 객체로 변환하는 타입 컨버터
 */
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {

        log.info("convert source = {}", source);

        String[] split = source.split(":");

        String ip = split[0];
        int port = Integer.parseInt(split[1]);

        return new IpPort(ip, port);
    }
}
