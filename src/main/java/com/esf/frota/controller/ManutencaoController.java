package com.esf.frota.controller;

import com.esf.frota.model.Manutencao;
import com.esf.frota.service.ManutencaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
@CrossOrigin(origins = "*")
public class ManutencaoController {

    @Autowired
    private ManutencaoService manutencaoService;

    @GetMapping
    public List<Manutencao> listar() {
        return manutencaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manutencao> buscarPorId(@PathVariable Long id) {
        return manutencaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Manutencao> criar(@RequestBody @Valid Manutencao manutencao) {
        Manutencao novaManutencao = manutencaoService.salvar(manutencao); // ou manutencaoService.salvar
        return ResponseEntity.status(HttpStatus.CREATED).body(novaManutencao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manutencao> atualizar(@PathVariable Long id, @RequestBody @Valid Manutencao manutencao) {
        return manutencaoService.buscarPorId(id)
                .map(m -> {
                    manutencao.setIdManutencao(id);
                    return ResponseEntity.ok(manutencaoService.salvar(manutencao));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (manutencaoService.buscarPorId(id).isPresent()) {
            manutencaoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}