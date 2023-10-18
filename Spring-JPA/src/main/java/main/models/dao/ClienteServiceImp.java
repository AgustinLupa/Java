package main.models.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.models.entities.Cliente;


@Service
public class ClienteServiceImp implements IClienteService {
	@Autowired
	private IClienteDao2 clientedao;
	
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> findAll(){
		return (List<Cliente>)clientedao.findAll();
	}
	
	@Transactional
	@Override
	public void save(Cliente cliente) {
		clientedao.save(cliente);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Cliente findOne(Long id) {
		return clientedao.findById(id).orElse(null);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		clientedao.deleteById(id);
	}
	
	
}
