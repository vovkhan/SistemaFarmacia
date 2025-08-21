package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.*;

public class GeradorDeCodigoService {

    public String gerarCodigoParaProduto(Produto produto, int proximoIdNumerico) {
        if (produto instanceof Medicamento) {
            return "P-100-" + proximoIdNumerico;
        } else if (produto instanceof Cosmetico) {
            return "P-200-" + proximoIdNumerico;
        } else if (produto instanceof Conveniencia) {
            return "P-300-" + proximoIdNumerico;
        } else if (produto instanceof Higiene) {
            return "P-400-" + proximoIdNumerico;
        }
        else {
            return "P-900-" + proximoIdNumerico;
        }
    }

    public String gerarCodigoParaVenda(Venda venda, int proximoIdNumerico) {
        int ano = venda.getDataHora().getYear();
        return "V-" + ano + "-" + proximoIdNumerico;
    }

    public String gerarCodigoParaFuncionario(Funcionario funcionario, int proximoIdNumerico) {
        String prefixo = "F-";
        if (funcionario instanceof Atendente) {
            prefixo = "A-";
        } else if (funcionario instanceof Supervisor) {
            prefixo = "S-";
        } else if (funcionario instanceof Gerente) {
            prefixo = "G-";
        }
        return prefixo + (100 + proximoIdNumerico);
    }
}