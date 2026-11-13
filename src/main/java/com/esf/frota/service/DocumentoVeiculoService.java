package com.esf.frota.service;

import com.esf.frota.model.DocumentoVeiculo;
import com.esf.frota.repository.DocumentoVeiculoRepository;
import com.esf.frota.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoVeiculoService {

    @Autowired
    private DocumentoVeiculoRepository documentoVeiculoRepository;
    @Autowired private VeiculoRepository veiculoRepository;

    public List<DocumentoVeiculo> listarTodos() {
        return documentoVeiculoRepository.findAll();
    }

    public Optional<DocumentoVeiculo> buscarPorId(Long id) {
        return documentoVeiculoRepository.findById(id);
    }

    public DocumentoVeiculo salvar(DocumentoVeiculo documento) {
        if (documento.getVeiculo() != null && documento.getVeiculo().getIdVeiculo() != null)
            documento.setVeiculo(veiculoRepository.getReferenceById(documento.getVeiculo().getIdVeiculo()));
        return documentoVeiculoRepository.save(documento);
    }

    public void deletar(Long id) {
        documentoVeiculoRepository.deleteById(id);
    }
}
