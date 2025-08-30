package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.*;

/**
 * Serviço responsável por criar os códigos formatados para as entidades do sistema.
 * Centraliza toda a lógica de formatação de IDs para manter o código organizado
 * e facilitar futuras alterações no formato dos códigos.
 */
public class GeradorDeCodigoService {

    /**
     * Gera código para um Produto, com prefixo diferente para cada categoria.
     */
    public String gerarCodigoParaProduto(Produto produto, int proximoIdNumerico) {
        if (produto instanceof Medicamento) {
            return "P-100-" + proximoIdNumerico;
        } else if (produto instanceof Cosmetico) {
            return "P-200-" + proximoIdNumerico;
        } else if (produto instanceof Higiene) {
            return "P-300-" + proximoIdNumerico;
        } else if (produto instanceof Conveniencia) {
            return "P-400-" + proximoIdNumerico;
        } else {
            return "P-900-" + proximoIdNumerico;
        }
    }

    /**
     * Gera código para uma Venda, usando o ano da transação.
     */
    public String gerarCodigoParaVenda(Venda venda, int proximoIdNumerico) {
        int ano = venda.getDataHora().getYear();
        return "V-" + ano + "-" + proximoIdNumerico;
    }

    /**
     * Gera código para um Funcionario, com prefixo diferente por cargo.
     */
    public String gerarCodigoParaFuncionario(Funcionario funcionario, int proximoIdNumerico) {
        String prefixo;
        if (funcionario instanceof Atendente) {
            prefixo = "A-"; // Atendente
        } else if (funcionario instanceof Supervisor) {
            prefixo = "S-"; // Supervisor
        } else if (funcionario instanceof Gerente) {
            prefixo = "G-"; // Gerente
        } else {
            prefixo = "F-"; // Funcionário
        }
        return prefixo + (100 + proximoIdNumerico);
    }
}