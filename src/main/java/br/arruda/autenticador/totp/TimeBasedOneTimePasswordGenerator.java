package br.arruda.autenticador.totp;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class TimeBasedOneTimePasswordGenerator extends HmacOneTimePasswordGenerator {
	private final Duration timeStep;
	public static final Duration DEFAULT_TIME_STEP = Duration.ofSeconds(10);
	public static final String TOTP_ALGORITHM_HMAC_SHA1 = "HmacSHA1";
	public static final String TOTP_ALGORITHM_HMAC_SHA256 = "HmacSHA256";

	public static final String TOTP_ALGORITHM_HMAC_SHA512 = "HmacSHA512";

	public TimeBasedOneTimePasswordGenerator() throws NoSuchAlgorithmException {
		this(DEFAULT_TIME_STEP);
	}

	public TimeBasedOneTimePasswordGenerator(final Duration timeStep) throws NoSuchAlgorithmException {
		this(timeStep, HmacOneTimePasswordGenerator.DEFAULT_PASSWORD_LENGTH);
	}

	public TimeBasedOneTimePasswordGenerator(final Duration timeStep, final int passwordLength)
			throws NoSuchAlgorithmException {
		this(timeStep, passwordLength, TOTP_ALGORITHM_HMAC_SHA1);
	}

	public TimeBasedOneTimePasswordGenerator(final Duration timeStep, final int passwordLength, final String algorithm)
			throws NoSuchAlgorithmException {
		super(passwordLength, algorithm);

		this.timeStep = timeStep;
	}

	public int generateOneTimePassword(final Key key, final Instant timestamp) throws InvalidKeyException {
		return this.generateOneTimePassword(key, timestamp.toEpochMilli() / this.timeStep.toMillis());
	}

	public Duration getTimeStep() {
		return this.timeStep;
	}
}