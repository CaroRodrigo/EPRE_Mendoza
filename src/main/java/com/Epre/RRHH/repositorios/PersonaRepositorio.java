/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.repositorios;

import com.Epre.RRHH.entidades.Persona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rodrigo Caro
 */
@Repository
public interface PersonaRepositorio extends JpaRepository<Persona, String> {
    
@Query("select j from Persona j where j.username LIKE :query "
        + "or j.legajo LIKE :query "
        + "or j.apellido LIKE :query "
        + "or j.nombre LIKE :query "
        + "or j.fechaDeIngreso LIKE :query "
        + "or j.anios LIKE :query "
        + "or j.dias LIKE :query "
        + "or j.diasDisponibles LIKE :query "
        + "or j.detalles LIKE :query "
        + "or j.estado.estado LIKE :query")
    List<Persona> findAllByQ(@Param("query") String query);

    @Query("select j from Persona j where j.id = :id")
    Persona encontrarPorId(@Param("id") String id);
    
    @Query("select j from Persona j where j.estado.estado = :q")
    List<Persona> findAllByEstado(@Param("q") String q);

    @Override
    @Query("select j from Persona j order by j.legajo")
    List<Persona> findAll();
}
