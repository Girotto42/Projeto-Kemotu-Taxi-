package com.esf.frota.service;

import com.esf.frota.model.Funcionario;
import com.esf.frota.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }


    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario salvar(Funcionario funcionario) {
        String senha = funcionario.getSenha();
        // só codifica se ainda não for um hash BCrypt (começa com $2a$, $2b$ ou $2y$)
        if (senha != null && !senha.startsWith("$2")) {
            funcionario.setSenha(passwordEncoder.encode(senha));
        }
        return funcionarioRepository.save(funcionario);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }
}