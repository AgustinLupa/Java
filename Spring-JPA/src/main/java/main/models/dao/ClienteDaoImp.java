package main.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import main.models.entities.Cliente;

@Repository
public class ClienteDaoImp implements IClienteDao{

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly= true)
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		if (cliente.getId() != null && cliente.getId() > 0) {
			em.merge(cliente);
			return;
		}
		em.persist(cliente);
	}

	@Override
	@Transactional(readOnly= true)
	public Cliente findOne(Long id) {
		return em.find(Cliente.class, id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Cliente cliente= findOne(id);
		em.remove(cliente);
	}
	
	

}