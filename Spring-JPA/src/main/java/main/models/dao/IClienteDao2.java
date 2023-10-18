package main.models.dao;

import org.springframework.data.repository.CrudRepository;

import main.models.entities.Cliente;

public interface IClienteDao2 extends CrudRepository<Cliente, Long>{
	
}
