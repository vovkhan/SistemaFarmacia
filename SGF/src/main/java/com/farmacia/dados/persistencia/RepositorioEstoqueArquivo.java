package com.farmacia.dados.persistencia;

import com.farmacia.dados.repositorio.IRepositorioEstoque;
import com.farmacia.negocio.entidade.Lote;
import com.farmacia.negocio.entidade.Produto;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioEstoqueArquivo implements IRepositorioEstoque {

    private ArrayList<Lote> lotes;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "lotes.dat";

    public RepositorioEstoqueArquivo() {
        this.lotes = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void salvar(Lote lote) {
        lote.setId(this.proximoId++);
        this.lotes.add(lote);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Lote lote) {
        salvarNoArquivo();
    }

    @Override
    public Lote buscarPorId(int id) {
        return this.lotes.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Lote> listarTodos() {
        return new ArrayList<>(this.lotes);
    }

    @Override
    public List<Lote> listarPorProduto(Produto produto) {
        return this.lotes.stream()
                .filter(lote -> lote.getProduto().getId() == produto.getId() && lote.getQuantidade() > 0)
                .collect(Collectors.toList());
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.lotes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar lotes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.lotes = (ArrayList<Lote>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar lotes: " + e.getMessage());
            this.lotes = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.lotes.isEmpty()) {
            this.proximoId = this.lotes.stream().mapToInt(Lote::getId).max().orElse(0) + 1;
        }
    }

}