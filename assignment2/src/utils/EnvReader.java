package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe utilitária para ler variáveis de configuração do ficheiro .env.
 * 
 * Autor: Pedro Carneiro
 */
public class EnvReader {
    private static final Properties props = new Properties();

    static {
        try (FileReader reader = new FileReader("src/.env")) {
            props.load(reader);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o ficheiro .env: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Obtém o valor de uma variável como string.
     * @param key nome da variável
     * @return valor correspondente
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Obtém o valor de uma variável como inteiro.
     * @param key nome da variável
     * @return valor convertido para inteiro
     */
    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
}
