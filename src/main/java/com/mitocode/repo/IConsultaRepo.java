package com.mitocode.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitocode.model.Consulta;

public interface IConsultaRepo extends JpaRepository<Consulta, Integer> {

}
