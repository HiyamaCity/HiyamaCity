package de.hiyamacity.dao;

import de.hiyamacity.entity.ATM;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ATMDAOImpl extends GeneralDAO<ATM, Long> implements DAO<ATM> {
	
	@Override
	public @NotNull ATM create(@NotNull ATM type) {
		return super.create(type);
	}
	
	public ATM read(@NotNull Class<ATM> classType, @NotNull long primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public ATM update(@NotNull ATM type) {
		return super.update(type);
	}
	
	public boolean delete(@NotNull Class<ATM> classType, @NotNull long primaryKey) {
		return super.delete(classType, primaryKey);
	}
	
	public List<ATM> getATMs() {
		EntityManager entityManager = GeneralDAO.getEntityManagerFactory().createEntityManager();
		return entityManager.createQuery("SELECT atm FROM ATM atm", ATM.class).getResultList();
	}
}
