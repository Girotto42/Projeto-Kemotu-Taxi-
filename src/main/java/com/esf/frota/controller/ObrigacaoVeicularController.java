package com.esf.frota.controller;

import com.esf.frota.model.ObrigacaoVeicular;
import com.esf.frota.service.ObrigacaoVeicularService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obrigacoes")
public class ObrigacaoVeicularController {

    @Autowired
    private ObrigacaoVeicularService obrigacaoVeicularService;

    @GetMapping
    public List<ObrigacaoVeicular> listar() {
        return obrigacaoVeicularService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObrigacaoVeicular> buscarPorId(@PathVariable Long id) {
        return obrigacaoVeicularService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ObrigacaoVeicular> criar(@RequestBody @Valid ObrigacaoVeicular obrigacao) {
        ObrigacaoVeicular novaObrigacao = obrigacaoVeicularService.salvar(obrigacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaObrigacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObrigacaoVeicular> atualizar(@PathVariable Long id, @RequestBody @Valid ObrigacaoVeicular obrigacao) {
        return obrigacaoVeicularService.buscarPorId(id)
                .map(o -> {
                    obrigacao.setIdObrigacao(id);
                    return ResponseEntity.ok(obrigacaoVeicularService.salvar(obrigacao));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (obrigacaoVeicularService.buscarPorId(id).isPresent()) {
            obrigacaoVeicularService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}