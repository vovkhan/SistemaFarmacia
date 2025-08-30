package com.farmacia.dados.repositorio;

import com.farmacia.negocio.entidade.Cliente;
import java.util.List;

public interface IRepositorioClientes {
    void salvar(Cliente cliente);
    Cliente buscarPorId(int id);
    Cliente buscarPorCpf(String cpf);
    void atualizar(Cliente cliente);
    List<Cliente> listarTodosAtivos();
}