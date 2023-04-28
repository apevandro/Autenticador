package br.arruda.autenticador.totp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TimeBasedOneTimePasswordGeneratorTest extends HmacOneTimePasswordGeneratorTest {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String HMAC_SHA512_ALGORITHM = "HmacSHA512";

    private static final Key HMAC_SHA1_KEY = new SecretKeySpec("12345678901234567890".getBytes(StandardCharsets.US_ASCII), "RAW");

    private static final Key HMAC_SHA256_KEY = new SecretKeySpec("12345678901234567890123456789012".getBytes(StandardCharsets.US_ASCII), "RAW");
    private static final Key HMAC_SHA512_KEY = new SecretKeySpec("1234567890123456789012345678901234567890123456789012345678901234".getBytes(StandardCharsets.US_ASCII), "RAW");

    @Override
    protected HmacOneTimePasswordGenerator getDefaultGenerator() throws NoSuchAlgorithmException {
        return new TimeBasedOneTimePasswordGenerator();
    }

    @Test
    void testGetTimeStep() throws NoSuchAlgorithmException {
        final Duration timeStep = Duration.ofSeconds(97);
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(timeStep);
        assertEquals(timeStep, totp.getTimeStep());
    }

    @ParameterizedTest
    @MethodSource("totpTestVectorSource")
    void testGenerateOneTimePassword(final String algorithm, final Key key, final long epochSeconds, final int expectedOneTimePassword) throws Exception {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30), 8, algorithm);
        final Instant timestamp = Instant.ofEpochSecond(epochSeconds);
        assertEquals(expectedOneTimePassword, totp.generateOneTimePassword(key, timestamp));
    }

    static Stream<Arguments> totpTestVectorSource() {
        return Stream.of(arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 59L, 94287082),
                arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 1111111109L, 7081804),
                arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 1111111111L, 14050471),
                arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 1234567890L, 89005924),
                arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 2000000000L, 69279037),
                arguments(HMAC_SHA1_ALGORITHM, HMAC_SHA1_KEY, 20000000000L, 65353130),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 59L, 46119246),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 1111111109L, 68084774),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 1111111111L, 67062674),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 1234567890L, 91819424),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 2000000000L, 90698825),
                arguments(HMAC_SHA256_ALGORITHM, HMAC_SHA256_KEY, 20000000000L, 77737706),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 59L, 90693936),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 1111111109L, 25091201),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 1111111111L, 99943326),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 1234567890L, 93441116),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 2000000000L, 38618901),
                arguments(HMAC_SHA512_ALGORITHM, HMAC_SHA512_KEY, 20000000000L, 47863826));
    }

}