package com.farmacia.dados.repositorio;

import com.farmacia.negocio.entidade.Venda;
import java.util.List;

public interface IRepositorioVendas {
    void salvar(Venda venda);
    Venda buscarPorId(int id);
    Venda buscarPorCodigo(String codigo);
    List<Venda> listarTodos();
}
