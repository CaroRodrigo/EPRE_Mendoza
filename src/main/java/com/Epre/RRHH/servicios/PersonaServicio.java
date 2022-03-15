/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.servicios;

import com.Epre.RRHH.entidades.Estado;
import com.Epre.RRHH.entidades.Persona;
import com.Epre.RRHH.enums.Role;
import com.Epre.RRHH.excepciones.WebException;
import com.Epre.RRHH.repositorios.PersonaRepositorio;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Rodrigo Caro
 */
@Service
public class PersonaServicio {
    @Autowired
    private PersonaRepositorio personaRepositorio;
    
    public Persona save(Persona persona) throws WebException, IOException{
        if (persona.getApellido().isEmpty() || persona.getApellido() == null) {
            throw new WebException("Debe ingresar el apellido de la persona");
        }
        
        if (persona.getNombre().isEmpty() || persona.getNombre() == null) {
            throw new WebException("Debe ingresar el nombre de la persona");
        }
        
        if (persona.getLegajo() == null) {
            throw new WebException("Debe ingresar el legajo de la persona");
        }
        
        if (persona.getFechaDeIngreso() == null) {
            throw new WebException("Debe ingresar fecha de ingreso de la persona a la empresa");
        }
       
        if (persona.getAnios() < 0 || persona.getAnios() == null) {
            throw new WebException("Debe ingresar los años que lleva el empleado en la empresa");
        }
        
        if (persona.getDias() < 0 || persona.getDias() == null) {
            throw new WebException("Debe ingresar los dias de vacaciones que le corresponde al empleado");
        }
        
        if (persona.getDiasDisponibles() < 0 || persona.getDiasDisponibles() == null) {
            throw new WebException("Debe los dias que le quedan al empleado de vacaciones");
        }
        persona.setRol(Role.PERSONAL);
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        persona.setPassword(encoder.encode(persona.getPassword()));
        return personaRepositorio.save(persona);

    }

    public List<Persona> listAll(){
        List<Persona> lista = personaRepositorio.findAll();
        return lista;
    }
    //dejar este servicio
	 public List<Persona> listallByQ(String query){
        List<Persona> lista = personaRepositorio.findAllByQ("%"+query+"%");
        return lista;
    }
 
          
    public Optional<Persona> findById(String id) {
        return personaRepositorio.findById(id);
    }
    
    public Persona findById2(Persona persona) {
        Optional<Persona> optional1 = personaRepositorio.findById(persona.getId());
            if (optional1.isPresent()) {
                persona = optional1.get();
            }
        return persona;
    }
    
    @Transactional
    public void delete(Persona persona){
        personaRepositorio.delete(persona);
    }
    
    
    @Transactional
    public void deleteById(String id){
        Optional<Persona> optional = personaRepositorio.findById(id);
        if (optional.isPresent()) {
            personaRepositorio.delete(optional.get());
        }
    }

    public Persona encontrarPorId(String id) {   
        return  personaRepositorio.encontrarPorId(id);
    }

      @Transactional
        public void deleteFieldEstado(Estado estado){
        List<Persona> persona = personaRepositorio.findAllByEstado(estado.getEstado());
        for (Persona persona1 : persona) {
            persona1.setEstado(null);
        }
        personaRepositorio.saveAll(persona);
    }
 
}
