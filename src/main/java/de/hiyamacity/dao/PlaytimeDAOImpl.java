package de.hiyamacity.dao;

import de.hiyamacity.entity.Playtime;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaytimeDAOImpl extends GeneralDAO<Playtime, UUID> implements DAO<Playtime, UUID> {

	@Override
	public @NotNull Playtime create(@NotNull Playtime type) {
		return super.create(type);
	}

	@Override
	public Playtime read(@NotNull Class<Playtime> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public Playtime update(@NotNull Playtime type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<Playtime> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
}
