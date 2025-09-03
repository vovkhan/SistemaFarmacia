package com.farmacia.dados;

import java.io.File;

public class ConfiguracaoPersistencia {
    public static final String PASTA_DADOS = "dados";

    /**
     * Cria/Checa se existe o diretório de dados.
     */
    public static void garantirPastaDeDados() {
        File diretorio = new File(PASTA_DADOS);
        if (!diretorio.exists()) {
            boolean sucesso = diretorio.mkdirs();
            if (sucesso) {
                System.out.println("[INFO] Diretório de dados '" + PASTA_DADOS + "' criado com sucesso.");
            } else {
                System.err.println("[ERRO] Falha ao criar o diretório de dados '" + PASTA_DADOS + "'.");
            }
        }
    }

    /**
     * caminho para o arquivo de dados.
     */
    public static String getCaminhoCompleto(String nomeArquivo) {
        return PASTA_DADOS + File.separator + nomeArquivo;
    }
}
