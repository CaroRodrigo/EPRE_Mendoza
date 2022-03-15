/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.servicios;

import com.Epre.RRHH.entidades.Estado;
import com.Epre.RRHH.excepciones.WebException;
import com.Epre.RRHH.repositorios.EstadoRepositorio;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rodrigo Caro
 */
@Service
public class EstadoServicio {

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @Autowired
    private PersonaServicio personaServicio;

    public Estado save(Estado estado) throws WebException, IOException {
        if (estado.getEstado().isEmpty() || estado.getEstado() == null) {
            throw new WebException("Debe ingresar un nuevo estado en el que se podria encontrar un empleado");
        }

        return estadoRepositorio.save(estado);

    }

    public List<Estado> listAll() {
        List<Estado> lista = estadoRepositorio.findAll();
        return lista;
    }

    public List<Estado> listallByQ(String query) {
        List<Estado> lista = estadoRepositorio.findAllByQ("%" + query + "%");
        return lista;
    }

    public Optional<Estado> findById(String id) {
        return estadoRepositorio.findById(id);
    }

    public Estado findById2(Estado estado) {
        Optional<Estado> optional1 = estadoRepositorio.findById(estado.getId());
        if (optional1.isPresent()) {
            estado = optional1.get();
        }
        return estado;
    }

    @Transactional
    public void delete(Estado estado) {
        estadoRepositorio.delete(estado);
    }

    public Estado encontrarPorId(String id) {
        return estadoRepositorio.encontrarPorId(id);
    }

    @Transactional
    public void deleteById(String id) {
        Optional<Estado> optional = estadoRepositorio.findById(id);
        if (optional.isPresent()) {
            Estado estado = optional.get();
            personaServicio.deleteFieldEstado(estado);
            estadoRepositorio.delete(estado);
        }

    }
}
