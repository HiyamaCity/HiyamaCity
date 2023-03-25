package de.hiyamacity.dao;

import de.hiyamacity.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class UserDAOImpl extends GeneralDAO<User, UUID> implements DAO<User, UUID> {
	public Optional<User> getUserByPlayerUniqueId(@NotNull UUID uuid) {
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
