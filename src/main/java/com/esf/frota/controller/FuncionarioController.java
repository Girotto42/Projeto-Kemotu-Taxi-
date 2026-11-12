package com.esf.frota.controller;

import com.esf.frota.model.Funcionario;
import com.esf.frota.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> fazerLogin(@RequestBody Funcionario credenciais) {
        // Busca o funcionário pelo e-mail enviado no login
        Optional<Funcionario> funcionarioOpt = funcionarioService.buscarPorEmail(credenciais.getEmail());

        if (funcionarioOpt.isPresent()) {
            Funcionario func = funcionarioOpt.get();
            // Valida se a senha confere
            if (func.getSenha().equals(credenciais.getSenha())) {
                return ResponseEntity.ok(func);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha incorretos.");
    }

    @GetMapping
    public List<Funcionario> listar() {
        return funcionarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        return funcionarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody @Valid Funcionario funcionario) {
        Funcionario novoFuncionario = funcionarioService.salvar(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @RequestBody @Valid Funcionario funcionario) {
        return funcionarioService.buscarPorId(id)
                .map(f -> {
                    funcionario.setIdFuncionario(id);
                    Funcionario funcionarioAtualizado = funcionarioService.salvar(funcionario);
                    return ResponseEntity.ok(funcionarioAtualizado);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deLeader(@PathVariable Long id) {
        if (funcionarioService.buscarPorId(id).isPresent()) {
            funcionarioService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}