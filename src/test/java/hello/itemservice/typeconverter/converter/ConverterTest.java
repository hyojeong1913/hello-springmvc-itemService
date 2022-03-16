package hello.itemservice.typeconverter.converter;

import hello.itemservice.web.typeconverter.converter.IntegerToStringConverter;
import hello.itemservice.web.typeconverter.converter.IpPortToStringConverter;
import hello.itemservice.web.typeconverter.converter.StringToIntegerConverter;
import hello.itemservice.web.typeconverter.converter.StringToIpPortConverter;
import hello.itemservice.web.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ConverterTest {

    @Test
    void stringToInteger() {

        StringToIntegerConverter converter = new StringToIntegerConverter();

        Integer result = converter.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    void integerToString() {

        IntegerToStringConverter converter = new IntegerToStringConverter();

        String result = converter.convert(10);

        assertThat(result).isEqualTo("10");
    }

    @Test
    void ipPortToString() {

        IpPortToStringConverter converter = new IpPortToStringConverter();

        IpPort source = new IpPort("127.0.0.1", 8080);

        String result = converter.convert(source);

        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void stringToIpPort() {

        StringToIpPortConverter converter = new StringToIpPortConverter();

        String source = "127.0.0.1:8080";

        IpPort result = converter.convert(source);

        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }
}
