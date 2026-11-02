package com.esf.frota.controller;

import com.esf.frota.model.DocumentoVeiculo;
import com.esf.frota.service.DocumentoVeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoVeiculoController {

    @Autowired
    private DocumentoVeiculoService documentoVeiculoService;

    @GetMapping
    public List<DocumentoVeiculo> listar() {
        return documentoVeiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoVeiculo> buscarPorId(@PathVariable Long id) {
        return documentoVeiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<DocumentoVeiculo> criar(@RequestBody @Valid DocumentoVeiculo documento) {
        DocumentoVeiculo novoDocumento = documentoVeiculoService.salvar(documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDocumento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoVeiculo> atualizar(@PathVariable Long id, @RequestBody @Valid DocumentoVeiculo documento) {
        return documentoVeiculoService.buscarPorId(id)
                .map(d -> {
                    documento.setIdDocumento(id);
                    return ResponseEntity.ok(documentoVeiculoService.salvar(documento));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (documentoVeiculoService.buscarPorId(id).isPresent()) {
            documentoVeiculoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}