package hello.itemservice.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Converter 는 입력과 출력 타입에 제한이 없는, 범용 타입 변환 기능을 제공
 *
 * Locale
 *  - 날짜 숫자의 표현 방법은 Locale 현지화 정보가 사용 가능
 *
 * 포맷터 (Formatter)
 *  - 객체를 특정한 포멧에 맞추어 문자로 출력하거나 또는 그 반대의 역할을 하는 것에 특화된 기능
 *
 * Converter vs Formatter
 *  - Converter 는 범용(객체 -> 객체)
 *  - Formatter 는 문자에 특화(객체 -> 문자, 문자 -> 객체) + 현지화(Locale)
 */
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    /**
     * 문자를 객체로 변경
     *
     * @param text
     * @param locale
     * @return
     * @throws ParseException
     */
    @Override
    public Number parse(String text, Locale locale) throws ParseException {

        log.info("text = {}, locale = {}", text, locale);

        // 예) "1,000" -> 1000
        NumberFormat format = NumberFormat.getInstance(locale);

        return format.parse(text);
    }

    /**
     * 객체를 문자로 변경
     *
     * @param object
     * @param locale
     * @return
     */
    @Override
    public String print(Number object, Locale locale) {

        log.info("object = {}, locale = {}", object, locale);

        NumberFormat instance = NumberFormat.getInstance(locale);

        return instance.format(object);
    }
}
