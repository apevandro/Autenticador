package br.arruda.autenticador.service.key.impl;

import static br.arruda.autenticador.service.file.FileService.SIZE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import br.arruda.autenticador.service.file.FileService;
import br.arruda.autenticador.service.key.KeyService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {

	@NonNull private final FileService fileService;

	@Getter private String encodedKey = "";    // Última chave
	private Map<String, LocalDateTime> keys;

	@Override
	public void putKey(String encodedKey) {
		this.encodedKey = encodedKey;
	    if (keys.size() == SIZE && !keys.containsKey(encodedKey)) {
            String firstKey = keys.keySet().iterator().next();
            keys.remove(firstKey);
        }
	    keys.put(encodedKey, LocalDateTime.now());
	    keys = sortKeys(keys);
	}

	@Override
	public List<String> getDatasDeAcesso() {
		return keys.values().stream().map(this::getFormattedDate).collect(Collectors.toList());
	}

	private String getFormattedDate(LocalDateTime dateTime) {
		final String PATTERN = "dd-MMM-YYYY HH:mm";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
	    return dateTime.format(formatter);
	}

	@Override
	public Set<String> getChaves() {
		return keys.keySet();
	}

	@Override
	public boolean isEmpty() {
		return keys.isEmpty();
	}

	@Override
	public void selecionarChave(final int index) {
		Iterator<String> iterator = keys.keySet().iterator();
		for (int i = 0; i <= index; i++) {
			encodedKey = iterator.next();
		}
		keys.put(encodedKey, LocalDateTime.now());    // Update encodedKey's date and time
		keys = sortKeys(keys);
	}

	private Map<String, LocalDateTime> sortKeys(Map<String, LocalDateTime> keys) {
		return keys.entrySet().stream()
				.sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2, LinkedHashMap::new));
	}

	@EventListener(ApplicationReadyEvent.class)
	public void loadKeys() throws IOException {
		keys = fileService.loadKeys();
	}

	@PreDestroy
    public void writeKeys() throws IOException {
		fileService.writeKeys(keys);
    }

}