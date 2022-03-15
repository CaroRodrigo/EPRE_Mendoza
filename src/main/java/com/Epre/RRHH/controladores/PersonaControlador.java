/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.controladores;

import com.Epre.RRHH.entidades.Persona;
import com.Epre.RRHH.servicios.EstadoServicio;
import com.Epre.RRHH.servicios.PersonaServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Rodrigo Caro
 */

@Controller
@RequestMapping("/persona")
public class PersonaControlador {
    @Autowired
    private PersonaServicio personaServicio;

    @Autowired
    private EstadoServicio estadoServicio;


    @GetMapping("/registro")
    public String registroPersona(Model model, @RequestParam(required = false) String id) {
        System.out.println(id);
        if (id != null) {
            Optional<Persona> optional = personaServicio.findById(id);

            if (optional.isPresent()) {
                Persona persona = optional.get();

                model.addAttribute("persona", persona);
            } else {
                return "redirect:/persona/list";
            }
        } else {
            model.addAttribute("persona", new Persona());
        }
        model.addAttribute("estados", estadoServicio.listAll());
        return "persona-registro.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_SOCIO')")
    @GetMapping("/carnet")
    public String carnet(Model model, @RequestParam(required = true) String id) {
        if (id != null) {
            Optional<Persona> optional = personaServicio.findById(id);
            Persona persona = optional.get();
            model.addAttribute("persona", persona);
            return "perfil-persona.html";
        } else {
            return "redirect:/";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public String lista(Model model, @RequestParam(required = false) String query) {
        if (query != null) {
            model.addAttribute("persona", personaServicio.listallByQ(query));
        } else {
            model.addAttribute("persona", personaServicio.listAll());
        }
        return "/html-administracion/persona/persona-list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam(required = true) String id, Model model) {
        personaServicio.deleteById(id);
        model.addAttribute("persona", personaServicio.listAll());
        return "redirect:/persona/list";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/personaSeleccionado")
    public String mostrarPersona(String id, Model model) {
        if (id != null) {
            model.addAttribute("persona", personaServicio.findById(id));
        }
        return "persona-seleccionado";
    }

}
