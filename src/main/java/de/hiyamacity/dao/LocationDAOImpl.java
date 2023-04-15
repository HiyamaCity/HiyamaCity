package de.hiyamacity.dao;

import de.hiyamacity.Main;
import de.hiyamacity.jpa.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationDAOImpl extends CrudRepositoryImpl<Location, UUID> {

	public List<Location> findByBukkitLocation(@NotNull org.bukkit.Location houseSignLocation) {
		EntityManager entityManager = Main.getEntityManagerFactory().createEntityManager();

		try {
			List<Location> loc = entityManager.createQuery("select loc from Location loc where loc.world = ?6 and loc.x = ?1 and loc.y = ?2 and loc.z = ?3 and loc.yaw = ?4 and loc.pitch = ?5", Location.class)
					.setParameter(6, houseSignLocation.getWorld().getName())
					.setParameter(1, houseSignLocation.getX())
					.setParameter(2, houseSignLocation.getY())
					.setParameter(3, houseSignLocation.getZ())
					.setParameter(4, houseSignLocation.getYaw())
					.setParameter(5, houseSignLocation.getPitch())
					.getResultList();
			entityManager.close();
			return loc;
		} catch (NoResultException e) {
			return new ArrayList<>();
		}
	}
}
