package com.farmacia.dados.repositorio;

import com.farmacia.negocio.entidade.Funcionario;
import java.util.List;

public interface IRepositorioFuncionarios {
    void salvar(Funcionario funcionario);
    void atualizar(Funcionario funcionario);
    Funcionario buscarPorId(int id);
    Funcionario buscarPorLogin(String login);
    List<Funcionario> listarTodosAtivos();
}