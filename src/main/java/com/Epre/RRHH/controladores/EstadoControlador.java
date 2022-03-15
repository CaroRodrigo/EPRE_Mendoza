/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.controladores;

import com.Epre.RRHH.entidades.Estado;
import com.Epre.RRHH.excepciones.WebException;
import com.Epre.RRHH.servicios.EstadoServicio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Rodrigo Caro
 */
@Controller
@RequestMapping("/estado")
public class EstadoControlador {
    @Autowired
    private EstadoServicio estadoServicio;

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public String listarEstado(Model model,@RequestParam(required = false) String q) {
        if (q != null) {
            model.addAttribute("estados", estadoServicio.listallByQ(q));
        }else{
            model.addAttribute("estados", estadoServicio.listAll());
        }
        return "estado-list";
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/form")
    public String crearEstado(Model model, @RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Estado> optional = estadoServicio.findById(id);
            if (optional.isPresent()) {
                model.addAttribute("estado",optional.get());
            }else {
            return "redirect:/estado/list";
            } 
        }else{
           model.addAttribute("estado",new Estado()); 
        }
        return "estado-form";
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public String guardarEstado(RedirectAttributes redirectAttributes,Estado estado) throws IOException {
        
        try {
            estadoServicio.save(estado);
            redirectAttributes.addFlashAttribute("success", "Estado guardada con exito");  
        } catch (WebException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());        }
        return "redirect:/estado/list";
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) String id) {
        estadoServicio.deleteById(id);
        return "redirect:/estado/list";
    }
    
}
