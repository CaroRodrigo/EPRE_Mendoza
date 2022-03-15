/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Rodrigo Caro
 */
@Controller
@RequestMapping("")
public class MainControlador {
    @GetMapping("/")
    public String inicio() {
        return "index.html";
    }
     
    @GetMapping("/panel-admin")
    public String login() {
        return "panel-admin.html";
    }
}
