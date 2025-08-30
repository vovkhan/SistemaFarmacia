package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.Funcionario;
import com.farmacia.dados.repositorio.IRepositorioFuncionarios;
import com.farmacia.negocio.excecao.funcionario.AutenticacaoException;
import com.farmacia.negocio.excecao.funcionario.FuncionarioNaoEncontradoException;
import com.farmacia.negocio.excecao.funcionario.LoginJaCadastradoException;

import java.util.Comparator;
import java.util.List;

public class FuncionarioService {

    private final IRepositorioFuncionarios funcionarioRepository;

    public FuncionarioService(IRepositorioFuncionarios funcionarioRepo) {
        this.funcionarioRepository = funcionarioRepo;
    }

    /**
     * Autentica um funcionário no sistema.
     */
    public Funcionario autenticar(String login, String senha) {
        Funcionario funcionario = funcionarioRepository.buscarPorLogin(login);
        if (funcionario != null && funcionario.autenticar(senha)) {
            return funcionario;
        } else {
            throw new AutenticacaoException();
        }
    }

    public void cadastrarNovoFuncionario(Funcionario novoFuncionario) {
        if (funcionarioRepository.buscarPorLogin(novoFuncionario.getLogin()) != null) {
            throw new LoginJaCadastradoException(novoFuncionario.getLogin());
        }
        funcionarioRepository.salvar(novoFuncionario);
    }

    /**
     * Desativa um funcionário no sistema.
     */
    public void desativarFuncionario(int idFuncionario) {
        Funcionario funcionario = funcionarioRepository.buscarPorId(idFuncionario);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(idFuncionario);
        }
        funcionario.desativar();
        funcionarioRepository.atualizar(funcionario);
    }

    /**
     * Atualiza os dados cadastrais de um funcionário.
     */
    public void atualizarDadosFuncionario(int idFuncionario, String novoNome, String novoLogin) {
        Funcionario funcionarioParaAtualizar = funcionarioRepository.buscarPorId(idFuncionario);
        if (funcionarioParaAtualizar == null) {
            throw new FuncionarioNaoEncontradoException(idFuncionario);
        }

        Funcionario funcionarioExistenteComLogin = funcionarioRepository.buscarPorLogin(novoLogin);
        if (funcionarioExistenteComLogin != null && funcionarioExistenteComLogin.getId() != idFuncionario) {
            throw new LoginJaCadastradoException(novoLogin);
        }

        funcionarioParaAtualizar.setNome(novoNome);
        funcionarioParaAtualizar.setLogin(novoLogin);

        funcionarioRepository.atualizar(funcionarioParaAtualizar);
    }

    /**
     * Busca um funcionário específico pelo seu ID interno.
     */
    public Funcionario buscarFuncionarioPorId(int id) {
        return funcionarioRepository.buscarPorId(id);
    }

    /**
     * Retorna uma lista com todos os funcionários ativos, ordenados por nome.
     */
    public List<Funcionario> listarTodosFuncionariosAtivos() {
        List<Funcionario> funcionarios = funcionarioRepository.listarTodosAtivos();
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));
        return funcionarios;
    }
}