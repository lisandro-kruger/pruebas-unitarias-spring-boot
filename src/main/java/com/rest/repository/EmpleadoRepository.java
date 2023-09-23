package com.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.model.Empleado;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {

    Optional<Empleado> findByEmail(String email);
}
