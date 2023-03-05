package de.hiyamacity.dao;

import de.hiyamacity.entity.ATM;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ATMDAOImpl extends GeneralDAO<ATM, Long> implements DAO<ATM, Long> {
	public List<ATM> findAll() {
		EntityManager entityManager = GeneralDAO.getEntityManagerFactory().createEntityManager();
		return entityManager.createQuery("SELECT atm FROM ATM atm", ATM.class).getResultList();
	}

}
