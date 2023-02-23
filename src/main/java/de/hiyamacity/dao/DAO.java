package de.hiyamacity.dao;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DAO<T> {

	default T create(@NotNull T type) {
		return null;
	}

	default T read(@NotNull Class<T> classType, @NotNull UUID primaryKey) {
		return null;
	}

	default T update(@NotNull T type) {
		return null;
	}

	default boolean delete(@NotNull Class<T> classType, @NotNull UUID primaryKey) {
		return false;
	}

}
