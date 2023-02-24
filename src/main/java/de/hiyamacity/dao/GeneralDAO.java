package de.hiyamacity.dao;

import de.hiyamacity.Main;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class GeneralDAO<T, S> {
	
	@Getter public static @NotNull final EntityManagerFactory entityManagerFactory;
	
	static {
		Thread.currentThread().setContextClassLoader(Main.class.getClassLoader());
		entityManagerFactory = Persistence.createEntityManagerFactory("default");
	}
	
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
	
	public T update(@NotNull T type) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(type);
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		return type;
	}

	public boolean delete(@NotNull Class<T> classType, @NotNull S primaryKey) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		T type = entityManager.getReference(classType, primaryKey);
		
		if(type == null) return false;
		
		entityManager.remove(type);
		entityManager.flush();
		
		entityManager.getTransaction().commit();
		entityManager.close();

		return true;
	}
	
}
