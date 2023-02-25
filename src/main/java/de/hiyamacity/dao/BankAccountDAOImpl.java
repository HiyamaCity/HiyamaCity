package de.hiyamacity.dao;

import de.hiyamacity.entity.BankAccount;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BankAccountDAOImpl extends GeneralDAO<BankAccount, UUID> implements DAO<BankAccount, UUID> {

	@Override
	public @NotNull BankAccount create(@NotNull BankAccount type) {
		return super.create(type);
	}

	@Override
	public BankAccount read(@NotNull Class<BankAccount> classType, @NotNull UUID primaryKey) {
		return super.read(classType, primaryKey);
	}

	@Override
	public BankAccount update(@NotNull BankAccount type) {
		return super.update(type);
	}

	@Override
	public boolean delete(@NotNull Class<BankAccount> classType, @NotNull UUID primaryKey) {
		return super.delete(classType, primaryKey);
	}
}
