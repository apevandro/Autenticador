package br.arruda.autenticador.service.file;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface FileService {
	int SIZE = 3;

	Map<String, LocalDateTime> loadKeys() throws IOException;

	void writeKeys(Map<String, LocalDateTime> keys) throws IOException;
}