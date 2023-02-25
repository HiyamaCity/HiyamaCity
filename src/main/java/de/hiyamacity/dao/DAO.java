package de.hiyamacity.dao;

import org.jetbrains.annotations.NotNull;

public interface DAO<T, S> {

	T create(@NotNull T type);

	T read(@NotNull Class<T> classType, @NotNull S primaryKey);

	T update(@NotNull T type);

	boolean delete(@NotNull Class<T> classType, @NotNull S primaryKey);

}
