package main.controllers;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import main.models.dao.IClienteDao;
import main.models.dao.IClienteService;
import main.models.entities.Cliente;

@Controller
public class ClienteController {

	@Autowired
	private IClienteService clienteDao;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Lista de clientes");
		model.addAttribute("clientes",clienteDao.findAll());
		return("listar");
	}
	
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente= new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Ingreso de datos");
		return "form";
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			//Map<String, String> errores= new HashMap<>();
			//resultado.getFieldErrors().forEach(
			//		err -> {
			//			errores.put(err.getField(), null)
			//	}
			//);
			return "form";
		}
		clienteDao.save(cliente);		
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Map<String, Object> model) {
		Cliente cliente= null;
		if (id>0) {
			cliente= clienteDao.findOne(id);
		}
		else {
			return("redirect:/listar");
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, Map<String, Object> model) {
		if (id > 0)
			clienteDao.delete(id);
		return "redirect:/listar";
	}
	
}