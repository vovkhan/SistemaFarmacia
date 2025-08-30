package com.farmacia.dados.persistencia;

import com.farmacia.dados.repositorio.IRepositorioVendas;
import com.farmacia.negocio.entidade.Venda;
import com.farmacia.negocio.servico.GeradorDeCodigoService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioVendasArquivo implements IRepositorioVendas {

    private ArrayList<Venda> vendas;
    private int proximoId = 1;
    private final GeradorDeCodigoService geradorDeCodigo = new GeradorDeCodigoService();
    private static final String NOME_ARQUIVO = "vendas.dat";

    public RepositorioVendasArquivo() {
        this.vendas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void salvar(Venda venda) {
        venda.setId(this.proximoId);
        String codigo = geradorDeCodigo.gerarCodigoParaVenda(venda, this.proximoId);
        venda.setCodigo(codigo);

        this.vendas.add(venda);
        this.proximoId++;
        salvarNoArquivo();
    }

    @Override
    public Venda buscarPorId(int id) {
        return this.vendas.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Venda buscarPorCodigo(String codigo) {
        return this.vendas.stream().filter(v -> v.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);
    }

    @Override
    public List<Venda> listarTodos() {
        return new ArrayList<>(this.vendas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(this.vendas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar vendas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            this.vendas = (ArrayList<Venda>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar vendas: " + e.getMessage());
            this.vendas = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.vendas.isEmpty()) {
            this.proximoId = this.vendas.stream().mapToInt(Venda::getId).max().orElse(0) + 1;
        }
    }
}
