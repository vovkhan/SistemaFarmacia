package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.Atendente;
import com.farmacia.negocio.entidade.Cliente;
import com.farmacia.dados.repositorio.IRepositorioClientes;
import com.farmacia.dados.repositorio.IRepositorioFuncionarios;
import com.farmacia.negocio.excecao.cliente.ClienteNaoEncontradoException;
import com.farmacia.negocio.excecao.cliente.CpfJaCadastradoException;

import java.util.Comparator;
import java.util.List;

public class ClienteService {

    private final IRepositorioClientes clienteRepository;
    private final IRepositorioFuncionarios funcionarioRepository;

    public ClienteService(IRepositorioClientes clienteRepo, IRepositorioFuncionarios funcionarioRepo) {
        this.clienteRepository = clienteRepo;
        this.funcionarioRepository = funcionarioRepo;
    }

    public void cadastrarNovoCliente(Cliente novoCliente, Atendente atendente) {
        if (clienteRepository.buscarPorCpf(novoCliente.getCpf()) != null) {
            throw new CpfJaCadastradoException();
        }
        clienteRepository.salvar(novoCliente);
        atendente.registrarNovoCliente();
        funcionarioRepository.atualizar(atendente);
    }

    public void atualizarDadosCliente(int idCliente, String novoNome, String novoCpf, String novoEmail, String novoTelefone) {
        Cliente clienteParaAtualizar = clienteRepository.buscarPorId(idCliente);
        if (clienteParaAtualizar == null) {
            throw new ClienteNaoEncontradoException(idCliente);
        }
        Cliente clienteExistenteComCpf = clienteRepository.buscarPorCpf(novoCpf);
        if (clienteExistenteComCpf != null && clienteExistenteComCpf.getId() != idCliente) {
            throw new CpfJaCadastradoException();
        }
        clienteParaAtualizar.setNome(novoNome);
        clienteParaAtualizar.setCpf(novoCpf);
        clienteParaAtualizar.setEmail(novoEmail);
        clienteParaAtualizar.setTelefone(novoTelefone);

        clienteRepository.atualizar(clienteParaAtualizar);
    }

    /**
     * Inativa um cliente no sistema.
     */
    public void removerCliente(int idCliente) {
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null || !cliente.isAtivo()) {
            throw new ClienteNaoEncontradoException(idCliente);
        }
        cliente.desativar();
        clienteRepository.atualizar(cliente);
    }

    public void atualizarNomeCliente(int idCliente, String novoNome) throws Exception {
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null) {
            throw new Exception("Cliente com ID " + idCliente + " não encontrado.");
        }

        cliente.setNome(novoNome);
        clienteRepository.atualizar(cliente);
    }

    public Cliente buscarClientePorCpf(String cpf) {
        Cliente cliente = clienteRepository.buscarPorCpf(cpf);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException(cpf);
        }
        return cliente;
    }

    public Cliente buscarClientePorId(int id) {
        Cliente cliente = clienteRepository.buscarPorId(id);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException(id);
        }
        return cliente;
    }

    public List<Cliente> listarTodosClientesAtivos() {
        List<Cliente> clientes = clienteRepository.listarTodosAtivos();
        clientes.sort(Comparator.comparing(Cliente::getNome));
        return clientes;
    }
}