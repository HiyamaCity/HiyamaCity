package de.hiyamacity.dao;

import de.hiyamacity.entity.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationDAOImpl extends GeneralDAO<Location, UUID> implements DAO<Location> {

	@Override
	public @NotNull Location create(@NotNull Location type) {
		return super.create(type);
	}

	@Override
	public Location read(@NotNull Class<Location> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public Location update(@NotNull Location type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<Location> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
}
