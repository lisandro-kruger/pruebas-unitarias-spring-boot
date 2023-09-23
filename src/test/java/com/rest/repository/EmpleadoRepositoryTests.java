package com.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.rest.model.Empleado;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTests {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
        		.id(1L)
                .nombre("Lisandro")
                .apellido("Kruger")
                .email("lkruger@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //given - dado o condición previa o configuración

        //when - acción o el comportamiento que vamos a probar
        Empleado empleadoGuardado = empleadoRepository.save(this.empleado);

        //then - verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar a los empleados")
    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado2 = Empleado.builder()
                .nombre("Julen")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();
        empleadoRepository.save(empleado2);
        empleadoRepository.save(this.empleado);

        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    void testObtenerEmpleadoPorId(){
        empleadoRepository.save(empleado);

        //when - comportamiento o accion que vamos a probar
        Empleado empleadoBD = empleadoRepository.findById(this.empleado.getId()).get();

        //then
        assertThat(empleadoBD).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado(){
        empleadoRepository.save(this.empleado);

        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("jraul@gmail.com");
        empleadoGuardado.setNombre("Jose Raul");
        empleadoGuardado.setApellido("Lopez Cucitini");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("jraul@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Jose Raul");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado(){
        empleadoRepository.save(this.empleado);

        //when
        empleadoRepository.deleteById(this.empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(this.empleado.getId());

        //then
        assertThat(empleadoOptional).isEmpty();
    }
}
