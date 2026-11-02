package com.esf.frota.repository;

import com.esf.frota.model.ObrigacaoVeicular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObrigacaoVeicularRepository extends JpaRepository<ObrigacaoVeicular, Long> {
}