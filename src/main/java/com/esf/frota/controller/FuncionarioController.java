package com.esf.frota.controller;

import com.esf.frota.model.Funcionario;
import com.esf.frota.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

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
                    if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) {
                        funcionario.setSenha(f.getSenha());
                        return ResponseEntity.ok(funcionarioService.salvarPreservandoHash(funcionario));
                    }
                    Funcionario funcionarioAtualizado = funcionarioService.salvar(funcionario);
                    return ResponseEntity.ok(funcionarioAtualizado);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (funcionarioService.buscarPorId(id).isPresent()) {
            funcionarioService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
