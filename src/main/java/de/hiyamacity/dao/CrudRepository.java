package de.hiyamacity.dao;

import org.jetbrains.annotations.NotNull;

public sealed interface CrudRepository<T, S> permits CrudRepositoryImpl {

	T create(@NotNull T type);

	T read(@NotNull Class<T> classType, @NotNull S primaryKey);

	void update(@NotNull T type);

	boolean delete(@NotNull Class<T> classType, @NotNull S primaryKey);

}
