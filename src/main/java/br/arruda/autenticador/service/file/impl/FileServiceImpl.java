package br.arruda.autenticador.service.file.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.arruda.autenticador.service.file.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	private final String PATH = "./files/";
	private final String KEY = "keys.txt";
	private final String DATE = "dates.txt";

	@Override
	public Map<String, LocalDateTime> loadKeys() throws IOException {
		List<String> keys = load(KEY);
		List<LocalDateTime> dates = load(DATE).stream().map(LocalDateTime::parse).collect(Collectors.toList());
		Map<String, LocalDateTime> map = new LinkedHashMap<>();
		for (int index = 0; index < keys.size(); index++) {
			map.put(keys.get(index), dates.get(index));
		}
		return map;
	}

	private List<String> load(String name) throws IOException {
		File directory = new File(PATH);
		if (!directory.exists()) {
			directory.mkdir();
		}

		File file = new File(PATH + name);
		if (!file.exists()) {
			file.createNewFile();
			file.setReadable(true);
			file.setWritable(true);
		}

		logger.info("Carregando arquivo " + name + "...");
		List<String> list = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(file);
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
			bufferedReader.lines().limit(SIZE).forEach(line -> list.add(line));
		}
		return list;
	}

	@Override
	public void writeKeys(Map<String, LocalDateTime> map) throws IOException {
		Iterator<String> iterator = map.keySet().iterator();
		write(iterator, KEY);
		iterator = map.values().stream().map(LocalDateTime::toString).iterator();
		write(iterator, DATE);
	}

	private void write(Iterator<String> iterator, final String name) throws IOException {
		logger.info("Escrevendo dados no arquivo " + name + "...");
		FileOutputStream fileOutputStream = new FileOutputStream(PATH + name);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {
			while (iterator.hasNext()) {
				bufferedWriter.write(iterator.next());
				bufferedWriter.newLine();
			}
		}
	}

}