package com.esf.frota.service;

import com.esf.frota.model.Manutencao;
import com.esf.frota.repository.ManutencaoRepository;
import com.esf.frota.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;
    @Autowired private VeiculoRepository veiculoRepository;

    public List<Manutencao> listarTodas() {
        return manutencaoRepository.findAll();
    }

    public Optional<Manutencao> buscarPorId(Long id) {
        return manutencaoRepository.findById(id);
    }

    public Manutencao salvar(Manutencao manutencao) {
        if (manutencao.getVeiculo() != null && manutencao.getVeiculo().getIdVeiculo() != null)
            manutencao.setVeiculo(veiculoRepository.getReferenceById(manutencao.getVeiculo().getIdVeiculo()));
        return manutencaoRepository.save(manutencao);
    }

    public void deletar(Long id) {
        manutencaoRepository.deleteById(id);
    }
}
