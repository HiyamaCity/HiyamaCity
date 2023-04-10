package de.hiyamacity.dao;

import de.hiyamacity.Main;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class GeneralDAO<T, S> {

	@Getter
	@NotNull
	public static final EntityManagerFactory entityManagerFactory = Main.getEntityManagerFactory();

	public @NotNull T create(@NotNull T type) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.persist(type);
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return type;
	}

	public T read(@NotNull Class<T> classType, @NotNull S primaryKey) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		return entityManager.find(classType, primaryKey);
	}

	public void update(@NotNull T type) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(type);
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public boolean delete(@NotNull Class<T> classType, @NotNull S primaryKey) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		T type = entityManager.getReference(classType, primaryKey);

		if (type == null) return false;

		entityManager.remove(type);
		entityManager.flush();

		entityManager.getTransaction().commit();
		entityManager.close();

		return true;
	}

}
