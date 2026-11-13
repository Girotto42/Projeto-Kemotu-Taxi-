package com.esf.frota.controller;

import com.esf.frota.model.Alocacao;
import com.esf.frota.service.AlocacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alocacoes")
public class AlocacaoController {

    @Autowired
    private AlocacaoService alocacaoService;

    @GetMapping
    public List<Alocacao> listar() {
        return alocacaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alocacao> buscarPorId(@PathVariable Long id) {
        return alocacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Alocacao> criar(@RequestBody @Valid Alocacao alocacao) {
        Alocacao novaAlocacao = alocacaoService.salvar(alocacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAlocacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alocacao> atualizar(@PathVariable Long id, @RequestBody @Valid Alocacao alocacao) {
        return alocacaoService.buscarPorId(id)
                .map(a -> {
                    alocacao.setIdAlocacao(id);
                    return ResponseEntity.ok(alocacaoService.salvar(alocacao));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (alocacaoService.buscarPorId(id).isPresent()) {
            alocacaoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}