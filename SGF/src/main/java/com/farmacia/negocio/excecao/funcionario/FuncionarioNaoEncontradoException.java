package com.farmacia.negocio.excecao.funcionario;

public class FuncionarioNaoEncontradoException extends FuncionarioException {
    public FuncionarioNaoEncontradoException(int id) {
        super("Funcionário com ID " + id + " não encontrado.");
    }
}
