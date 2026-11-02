package com.esf.frota.service;

import com.esf.frota.model.ObrigacaoVeicular;
import com.esf.frota.repository.ObrigacaoVeicularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObrigacaoVeicularService {

    @Autowired
    private ObrigacaoVeicularRepository obrigacaoVeicularRepository;

    public List<ObrigacaoVeicular> listarTodas() {
        return obrigacaoVeicularRepository.findAll();
    }

    public Optional<ObrigacaoVeicular> buscarPorId(Long id) {
        return obrigacaoVeicularRepository.findById(id);
    }

    public ObrigacaoVeicular salvar(ObrigacaoVeicular obrigacao) {
        // Regras adicionais de obrigações (ex: verificar data de vencimento) podem entrar aqui
        return obrigacaoVeicularRepository.save(obrigacao);
    }

    public void deletar(Long id) {
        obrigacaoVeicularRepository.deleteById(id);
    }
}