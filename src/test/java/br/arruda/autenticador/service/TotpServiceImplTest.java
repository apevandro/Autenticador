package br.arruda.autenticador.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

import java.security.InvalidKeyException;
import java.time.Instant;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.arruda.autenticador.service.message.MessageService;
import br.arruda.autenticador.service.totp.TotpService;
import br.arruda.autenticador.service.totp.impl.TotpServiceImpl;
import br.arruda.autenticador.totp.HmacOneTimePasswordGenerator;
import br.arruda.autenticador.totp.TimeBasedOneTimePasswordGenerator;

@ExtendWith(MockitoExtension.class)
class TotpServiceImplTest {

	private String algorithm = HmacOneTimePasswordGenerator.HOTP_HMAC_ALGORITHM;
	private String encodedKey = "TL6LJWAO2VKQQSG6";
	private String expectedToken = "012345";
	private int token = 12345;

	private static TotpService service;
	private static TimeBasedOneTimePasswordGenerator totp = Mockito.mock(TimeBasedOneTimePasswordGenerator.class);
	private static MessageService messageService = Mockito.mock(MessageService.class);

	@BeforeAll
	static void setup() {
		service = new TotpServiceImpl(totp, messageService);
	}

	@Test
	void testGerarToken() throws InvalidKeyException {
		when(totp.getAlgorithm()).thenReturn(algorithm);
		when(totp.generateOneTimePassword(any(SecretKey.class), any(Instant.class))).thenReturn(token);
		String result = service.gerarToken(encodedKey);
		assertThat(result).isEqualTo(expectedToken);
	}

	@Test
	void testIsInvalid() {
		String encodedKey = "TL6LJWAO2VKQQSG"; // 15 digits
		boolean result = invokeMethod(service, "isInvalid", encodedKey); // For private method, ReflectionTestUtils
		assertThat(result).isTrue();
	}

	@Test
	void testIsInvalidB() {
		String encodedKey = "TL6LJWAO2VKQQSG9"; // has invalid digit
		boolean result = invokeMethod(service, "isInvalid", encodedKey); // For private method, ReflectionTestUtils
		assertThat(result).isTrue();
	}

	@Test
	void testIsInvalidC() {
		boolean result = invokeMethod(service, "isInvalid", encodedKey); // For private method, ReflectionTestUtils
		assertThat(result).isFalse();
	}

	@Test
	void testAdjustNumberOfDigits() {
		String result = invokeMethod(service, "adjustNumberOfDigits", token); // For private method, ReflectionTestUtils
		assertThat(result).isEqualTo(expectedToken);
	}

}