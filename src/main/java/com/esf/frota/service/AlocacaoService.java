package com.esf.frota.service;

import com.esf.frota.model.Alocacao;
import com.esf.frota.repository.AlocacaoRepository;
import com.esf.frota.repository.FuncionarioRepository;
import com.esf.frota.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlocacaoService {

    @Autowired
    private AlocacaoRepository alocacaoRepository;

    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private VeiculoRepository veiculoRepository;

    public List<Alocacao> listarTodas() {
        return alocacaoRepository.findAll();
    }

    public Optional<Alocacao> buscarPorId(Long id) {
        return alocacaoRepository.findById(id);
    }

    public Alocacao salvar(Alocacao alocacao) {
        if (alocacao.getFuncionario() != null && alocacao.getFuncionario().getIdFuncionario() != null)
            alocacao.setFuncionario(funcionarioRepository.getReferenceById(alocacao.getFuncionario().getIdFuncionario()));
        if (alocacao.getVeiculo() != null && alocacao.getVeiculo().getIdVeiculo() != null)
            alocacao.setVeiculo(veiculoRepository.getReferenceById(alocacao.getVeiculo().getIdVeiculo()));
        return alocacaoRepository.save(alocacao);
    }

    public void deletar(Long id) {
        alocacaoRepository.deleteById(id);
    }
}
