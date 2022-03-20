package hello.itemservice.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class MyNumberFormatterTest {

    MyNumberFormatter formatter = new MyNumberFormatter();

    @Test
    void parse() throws ParseException {

        Number result = formatter.parse("1,000", Locale.KOREA);

        // parse() 의 결과가 Long 이기 때문에 비교할 때 마지막에 L 을 넣어주어야 한다.
        assertThat(result).isEqualTo(1000L);
    }

    @Test
    void print() {

        String result = formatter.print(1000, Locale.KOREA);

        assertThat(result).isEqualTo("1,000");
    }
}