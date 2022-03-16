package hello.itemservice.web.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * 숫자를 문자로 변환하는 타입 컨버터
 */
@Slf4j
public class IntegerToStringConverter implements Converter<Integer, String> {

    @Override
    public String convert(Integer source) {

        log.info("convert source = {}", source);

        return String.valueOf(source);
    }
}
