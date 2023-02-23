package de.hiyamacity.dao;

import de.hiyamacity.Main;
import de.hiyamacity.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class UserDao {

	public static Optional<User> getUserByPlayerUUID(@NotNull final UUID playerUniqueID) {
		EntityManager entityManager = Main.getEntityManager();

		try {
			User user = (User) entityManager.createQuery("select u from User u where u.playerUniqueID = ?1").setParameter(1, playerUniqueID).getSingleResult();
			entityManager.detach(user);
			return Optional.ofNullable(user);
		} catch (NoResultException e) {
			return Optional.empty();
		} finally {
			entityManager.close();
		}
	}

	public static void updateUser(@NotNull User user) {
		EntityManager entityManager = Main.getEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(user);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

}
