package br.arruda.autenticador.service.totp;

import java.time.Duration;

public interface TotpService {
	String gerarToken(String encodedKey);
	Duration getTimeStep();
}