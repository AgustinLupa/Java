package main.controllers;

import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String guardar(@Valid Cliente cliente, BindingResult resultado, Model model, @RequestParam("file") MultipartFile foto) {
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
		if(!foto.isEmpty()) {
			if(cliente.getId() != null && cliente.getId()>0 && cliente.getFoto()!= null && cliente.getFoto().length()>0) {
				Path rootpath = Paths.get("uplooads").resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootpath.toFile();
				if(archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}
			}
			String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
			Path rootpath= Paths.get("uploads").resolve(uniqueFileName);
			Path rootAbsolute = rootpath.toAbsolutePath();
			try {
				Files.copy(foto.getInputStream(), rootAbsolute);
				cliente.setFoto(uniqueFileName);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
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
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") long id, Map<String, Object> model) {
		Cliente cliente = clienteDao.findOne(id);
		if(cliente ==null) {
			return "redirect:/Listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "detalle cliente");
		return "ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, Map<String, Object> model) {
		if (id > 0)
			clienteDao.delete(id);
		return "redirect:/listar";
	}
	
}