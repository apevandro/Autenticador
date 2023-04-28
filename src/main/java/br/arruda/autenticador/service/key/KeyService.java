package br.arruda.autenticador.service.key;

import java.util.List;
import java.util.Set;

public interface KeyService {
    void putKey(String encodedKey);
    List<String> getDatasDeAcesso();
    Set<String> getChaves();
    boolean isEmpty();
    void selecionarChave(final int index);
    String getEncodedKey();
}