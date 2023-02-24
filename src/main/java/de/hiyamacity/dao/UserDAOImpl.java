package de.hiyamacity.dao;

import de.hiyamacity.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class UserDAOImpl extends GeneralDAO<User, UUID> implements DAO<User> {

	@Override
	public @NotNull User create(@NotNull User type) {
		return super.create(type);
	}

	@Override
	public @NotNull User read(@NotNull Class<User> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public User update(@NotNull User type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<User> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
	
	public Optional<User> getUserByPlayerUniqueID(@NotNull UUID uuid) {
		EntityManager entityManager = GeneralDAO.getEntityManagerFactory().createEntityManager();
		try {
			Optional<User> userOptional = Optional.ofNullable((User) entityManager.createQuery("SELECT user from User user where user.playerUniqueID = ?1").setParameter(1, uuid).getSingleResult()); 
			entityManager.close();
			return userOptional;
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
}
