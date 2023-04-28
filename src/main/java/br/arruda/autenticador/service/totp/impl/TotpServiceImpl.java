package br.arruda.autenticador.service.totp.impl;

import java.security.InvalidKeyException;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.google.common.io.BaseEncoding;

import br.arruda.autenticador.service.message.MessageService;
import br.arruda.autenticador.service.totp.TotpService;
import br.arruda.autenticador.totp.TimeBasedOneTimePasswordGenerator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotpServiceImpl implements TotpService {

	private final @NonNull TimeBasedOneTimePasswordGenerator totp;
	private final @NonNull MessageService messageService;

	@Override
	public String gerarToken(String encodedKey) {
		if (encodedKey.isEmpty()) {
			showEmptyKeyInformation();
			return "0";
		}

		if (isInvalid(encodedKey)) {
			showInvalidKeyAlert();
			return "0";
		}

		int password = 0;
		byte[] decodedKey = BaseEncoding.base32().decode(encodedKey);
		SecretKey key = new SecretKeySpec(decodedKey, totp.getAlgorithm());
		try {
			password = totp.generateOneTimePassword(key, Instant.now());
			log.debug("MOCK TOTP! " + password);
		} catch (InvalidKeyException e) {
			showInvalidKeyError();
		}

		return adjustNumberOfDigits(password);
	}

	@Override
	public Duration getTimeStep() {
		return TimeBasedOneTimePasswordGenerator.DEFAULT_TIME_STEP;
	}

	private void showEmptyKeyInformation() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(messageService.getMessage("chave.vazia"));
		alert.showAndWait();
	}

	private boolean isInvalid(String encodedKey) {
		if (encodedKey.length() != 16) {
			return true;
		}
		String value = encodedKey.replaceAll("[A-Z2-7]", "");
		return value.isEmpty() ? false : true;
	}

	private void showInvalidKeyAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setContentText(messageService.getMessage("chave.invalida"));
		alert.showAndWait();
	}

	private void showInvalidKeyError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(messageService.getMessage("chave.inapropriada"));
		alert.showAndWait();
	}

	private String adjustNumberOfDigits(int password) {
		String pwd = String.valueOf(password);
		return pwd.length() == 6 ? pwd : String.format("%06d", password);
	}

}