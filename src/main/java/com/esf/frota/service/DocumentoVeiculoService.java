package com.esf.frota.service;

import com.esf.frota.model.DocumentoVeiculo;
import com.esf.frota.repository.DocumentoVeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoVeiculoService {

    @Autowired
    private DocumentoVeiculoRepository documentoVeiculoRepository;

    public List<DocumentoVeiculo> listarTodos() {
        return documentoVeiculoRepository.findAll();
    }

    public Optional<DocumentoVeiculo> buscarPorId(Long id) {
        return documentoVeiculoRepository.findById(id);
    }

    public DocumentoVeiculo salvar(DocumentoVeiculo documento) {
        return documentoVeiculoRepository.save(documento);
    }

    public void deletar(Long id) {
        documentoVeiculoRepository.deleteById(id);
    }
}