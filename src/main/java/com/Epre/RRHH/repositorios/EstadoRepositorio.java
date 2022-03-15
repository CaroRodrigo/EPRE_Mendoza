/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.repositorios;

import com.Epre.RRHH.entidades.Estado;
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
public interface EstadoRepositorio extends JpaRepository<Estado, String>{
     @Query("select j from Estado j where j.estado LIKE :query")
    List<Estado> findAllByQ(@Param("query") String query);

    @Query("select j from Estado j where j.id = :id")
    Estado encontrarPorId(@Param("id") String id);

    @Override
    @Query("select j from Estado j order by j.estado")
    List<Estado> findAll();
}

