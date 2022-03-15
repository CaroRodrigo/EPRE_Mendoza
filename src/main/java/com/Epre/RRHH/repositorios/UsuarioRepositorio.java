/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.repositorios;

import com.Epre.RRHH.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rodrigo Caro
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
   @Query("select u from Usuario u where u.username = :username")
    Usuario findByUserName(@Param("username") String username);
    
    @Query("select u from Usuario u where u.id = :id")
	Usuario encontrarPorId(@Param("id") String id);
}
