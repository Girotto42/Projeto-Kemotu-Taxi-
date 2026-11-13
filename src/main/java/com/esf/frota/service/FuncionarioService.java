package com.esf.frota.service;

import com.esf.frota.model.Funcionario;
import com.esf.frota.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        if (funcionario.getSenha() != null && !funcionario.getSenha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        }
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario salvarPreservandoHash(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }
}
