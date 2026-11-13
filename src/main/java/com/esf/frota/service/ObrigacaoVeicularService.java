package com.esf.frota.service;

import com.esf.frota.model.ObrigacaoVeicular;
import com.esf.frota.repository.ObrigacaoVeicularRepository;
import com.esf.frota.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObrigacaoVeicularService {

    @Autowired
    private ObrigacaoVeicularRepository obrigacaoVeicularRepository;
    @Autowired private VeiculoRepository veiculoRepository;

    public List<ObrigacaoVeicular> listarTodas() {
        return obrigacaoVeicularRepository.findAll();
    }

    public Optional<ObrigacaoVeicular> buscarPorId(Long id) {
        return obrigacaoVeicularRepository.findById(id);
    }

    public ObrigacaoVeicular salvar(ObrigacaoVeicular obrigacao) {
        if (obrigacao.getVeiculo() != null && obrigacao.getVeiculo().getIdVeiculo() != null)
            obrigacao.setVeiculo(veiculoRepository.getReferenceById(obrigacao.getVeiculo().getIdVeiculo()));
        return obrigacaoVeicularRepository.save(obrigacao);
    }

    public void deletar(Long id) {
        obrigacaoVeicularRepository.deleteById(id);
    }
}
