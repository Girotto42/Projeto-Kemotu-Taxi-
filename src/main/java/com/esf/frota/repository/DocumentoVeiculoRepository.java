package com.esf.frota.repository;

import com.esf.frota.model.DocumentoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoVeiculoRepository extends JpaRepository<DocumentoVeiculo, Long> {
}