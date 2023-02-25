package de.hiyamacity.dao;

import de.hiyamacity.entity.House;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HouseDAOImpl extends GeneralDAO<House, UUID> implements DAO<House, UUID> {

	@Override
	public @NotNull House create(@NotNull House type) {
		return super.create(type);
	}

	@Override
	public House read(@NotNull Class<House> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public House update(@NotNull House type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<House> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
}
