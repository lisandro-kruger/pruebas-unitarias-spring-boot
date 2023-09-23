package com.rest.service;

import java.util.List;
import java.util.Optional;

import com.rest.model.Empleado;

public interface EmpleadoService {

    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleados();

    Optional<Empleado> getEmpleadoById(long id);

    Empleado updateEmpleado(Empleado empleadoActualizado);

    void deleteEmpleado(long id);

}
