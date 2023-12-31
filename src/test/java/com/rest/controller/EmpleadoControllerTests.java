package com.rest.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.model.Empleado;
import com.rest.service.EmpleadoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;
    
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

    @Test
    void testGuardarEmpleado() throws Exception {
        //given
        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.empleado)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception{
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Lopez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Rodriguez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ruiz").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Diaz").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listaEmpleados.size())));
    }

    @Test
    void testObtenerEmpleadoPorId() throws Exception{
        //given
        long empleadoId = 1L;
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(this.empleado));

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testActualizarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Daniel")
                .apellido("Ramirez")
                .email("dramirez@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("chramirez@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
