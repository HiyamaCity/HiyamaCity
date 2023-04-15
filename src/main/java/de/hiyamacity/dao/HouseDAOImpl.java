package de.hiyamacity.dao;

import de.hiyamacity.jpa.House;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HouseDAOImpl extends CrudRepositoryImpl<House, Long> {

	public Optional<House> getHouseBySignLocation(@NotNull Location location) {
		EntityManager entityManager = CrudRepositoryImpl.getEntityManagerFactory().createEntityManager();
		try {
			Optional<House> houseOptional = Optional.ofNullable((House) entityManager.createQuery("SELECT h from House h where h.signLocation.x = ?1 and h.signLocation.y = ?2 and h.signLocation.z = ?3 and h.signLocation.yaw = ?4 and h.signLocation.pitch = ?5")
					.setParameter(1, location.getX())
					.setParameter(2, location.getY())
					.setParameter(3, location.getZ())
					.setParameter(4, location.getYaw())
					.setParameter(5, location.getPitch())
					.getSingleResult());
			entityManager.close();
			return houseOptional;
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public List<House> findAll() {
		EntityManager entityManager = CrudRepositoryImpl.getEntityManagerFactory().createEntityManager();
		try {
			List<House> set = entityManager.createQuery("SELECT h from House h", House.class).getResultList();
			entityManager.close();
			return set;
		} catch (NoResultException e) {
			return new ArrayList<>();
		}
	}
}
