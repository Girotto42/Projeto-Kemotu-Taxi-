package com.esf.frota.service;

import com.esf.frota.model.Alocacao;
import com.esf.frota.repository.AlocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlocacaoService {

    @Autowired
    private AlocacaoRepository alocacaoRepository;

    public List<Alocacao> listarTodas() {
        return alocacaoRepository.findAll();
    }

    public Optional<Alocacao> buscarPorId(Long id) {
        return alocacaoRepository.findById(id);
    }

    public Alocacao salvar(Alocacao alocacao) {
        // Aqui você pode adicionar regras de negócio, ex: verificar se o veículo está disponível
        return alocacaoRepository.save(alocacao);
    }

    public void deletar(Long id) {
        alocacaoRepository.deleteById(id);
    }
}