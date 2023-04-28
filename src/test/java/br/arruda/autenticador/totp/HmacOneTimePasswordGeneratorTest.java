package br.arruda.autenticador.totp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HmacOneTimePasswordGeneratorTest {

    private static final Key HOTP_KEY = new SecretKeySpec(
            "12345678901234567890".getBytes(StandardCharsets.US_ASCII), "RAW");

    @Test
    void testHmacOneTimePasswordGeneratorWithShortPasswordLength() {
        assertThrows(IllegalArgumentException.class, () -> new HmacOneTimePasswordGenerator(5));
    }

    @Test
    void testHmacOneTimePasswordGeneratorWithLongPasswordLength() {
        assertThrows(IllegalArgumentException.class, () -> new HmacOneTimePasswordGenerator(9));
    }

    @Test
    void testHmacOneTimePasswordGeneratorWithBogusAlgorithm() {
        assertThrows(NoSuchAlgorithmException.class,
                () -> new HmacOneTimePasswordGenerator(6, "Definitely not a real algorithm"));
    }

    @Test
    void testGetPasswordLength() throws NoSuchAlgorithmException {
        final int passwordLength = 7;
        assertEquals(passwordLength,
                new HmacOneTimePasswordGenerator(passwordLength).getPasswordLength());
    }

    @Test
    void testGetAlgorithm() throws NoSuchAlgorithmException {
        final String algorithm = "HmacSHA256";
        assertEquals(algorithm, new HmacOneTimePasswordGenerator(6, algorithm).getAlgorithm());
    }

    @ParameterizedTest
    @MethodSource("hotpTestVectorSource")
    void testGenerateOneTimePassword(final int counter, final int expectedOneTimePassword)
            throws Exception {
        assertEquals(expectedOneTimePassword,
                this.getDefaultGenerator().generateOneTimePassword(HOTP_KEY, counter));
    }

    static Stream<Arguments> hotpTestVectorSource() {
        return Stream.of(arguments(0, 755224), arguments(1, 287082), arguments(2, 359152),
                arguments(3, 969429), arguments(4, 338314), arguments(5, 254676),
                arguments(6, 287922), arguments(7, 162583), arguments(8, 399871),
                arguments(9, 520489));
    }

    protected HmacOneTimePasswordGenerator getDefaultGenerator() throws NoSuchAlgorithmException {
        return new HmacOneTimePasswordGenerator();
    }
}