package de.hiyamacity.dao;

import de.hiyamacity.entity.AfkLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AfkLocationDAOImpl extends GeneralDAO<AfkLocation> implements DAO<AfkLocation> {

	@Override
	public @NotNull AfkLocation create(@NotNull AfkLocation type) {
		return super.create(type);
	}

	@Override
	public AfkLocation read(@NotNull Class<AfkLocation> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public AfkLocation update(@NotNull AfkLocation type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<AfkLocation> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
}
