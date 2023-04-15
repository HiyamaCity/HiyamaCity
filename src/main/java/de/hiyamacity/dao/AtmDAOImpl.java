package de.hiyamacity.dao;

import de.hiyamacity.jpa.ATM;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AtmDAOImpl extends CrudRepositoryImpl<ATM, Long> {
	public List<ATM> findAll() {
		EntityManager entityManager = CrudRepositoryImpl.getEntityManagerFactory().createEntityManager();
		return entityManager.createQuery("SELECT atm FROM ATM atm", ATM.class).getResultList();
	}

}
