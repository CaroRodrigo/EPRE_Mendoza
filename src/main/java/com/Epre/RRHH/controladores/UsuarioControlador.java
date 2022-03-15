/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.controladores;

import com.Epre.RRHH.entidades.Estado;
import com.Epre.RRHH.entidades.Usuario;
import com.Epre.RRHH.excepciones.WebException;
import com.Epre.RRHH.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Rodrigo Caro
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    
	@Autowired
	public UsuarioServicio usuarioService;

	@GetMapping("/registro")
	public String registroUsuario(Model model, @RequestParam(required = false) String id) {

		System.out.println(id);
		if (id != null) {
			Optional<Usuario> optional = usuarioService.findById(id);

			if (optional.isPresent()) {
				Usuario usuario = optional.get();

				model.addAttribute("usuario", usuario);
			} else {
				return "redirect:/usuario/list";
			}
		} else {
			model.addAttribute("usuario", new Usuario());
		}

		return "usuario-registro.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/list")
	public String lista(Model model, @RequestParam(required = false) String query) {
		if (query != null) {
			model.addAttribute("usuario", usuarioService.listAllByQ(query));
		} else {
			model.addAttribute("usuario", usuarioService.listAll());
		}
		return "/usuario-list.html";
	}

	@PostMapping("/registrado")
	public String registrado(Model model, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes,
			 ModelMap modelo) throws Exception {
		try {
			usuarioService.save(usuario);
		} catch (Exception w) {
			model.addAttribute("error", w.getMessage());
			model.addAttribute("usuario", usuario);
			return "usuario-registro.html";
		}

		/* url a la que redirecciono despues de registrar un usuario */
		return "redirect:/registro-exitoso";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam(required = true) String id, Model model) {
		try {
			usuarioService.deleteById(id);
			model.addAttribute("usuario", usuarioService.listAll());
			return "redirect:/usuario/list";
		} catch (WebException ex) {
			ex.getMessage();
		}
		return "redirect:/usuario/list";
	}

	// @GetMapping("/eliminar")
	// public String eliminar(@ModelAttribute Usuario usuario, Model model) throws
	// Exception{
	// try {
	// usuarioService.deleteByObject(usuario);
	// model.addAttribute("usuario", usuarioService.listAll());
	// return "redirect:/usuario/list";
	//
	// } catch(Exception ex){
	// Logger.getLogger(UsuarioRegistroController.class.getName()).log(Level.SEVERE,
	// null, ex);
	// }
	// return "redirect:/usuario/list";
	// }
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/usuarioSeleccionado")
	public String mostrarUsuario(String id, Model model) {
		if (id != null) {

			model.addAttribute("usuario", usuarioService.findById(id));

		}
		return "usuario-seleccionado";

	}

	@GetMapping("/asociarse")
	public String asociarse(@RequestParam(required = true) String id, ModelMap model) {
		try {
			Usuario usuario = usuarioService.encontrarPorId(id);
			model.addAttribute("perfil", usuario);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "persona-registro";
	}

	@PostMapping("/personaregistrada")
	public String personaRegistrada(Model model, @RequestParam(required = true) String id
                , @RequestParam(required = true) String legajo, @RequestParam(required = true) String apellido
                , @RequestParam(required = true) String nombre, @RequestParam(required = true) Date fechaDeIngreso
                , @RequestParam(required = true) Integer anios, @RequestParam(required = true) Integer dias
                , @RequestParam(required = true) Integer diasDisponibles, @RequestParam(required = true) String detalles
                , @RequestParam(required = true) Estado estado) throws Exception {

		Usuario user = usuarioService.encontrarPorId(id);
		try {
			usuarioService.cambioAPersona(user, legajo, apellido
                , nombre, fechaDeIngreso, anios, dias, diasDisponibles, detalles, estado);
		} catch (WebException | IOException e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:/asociarse";
		}
		/* url a la que redirecciono despues de registrar un usuario */
		return "/";

	}

}
