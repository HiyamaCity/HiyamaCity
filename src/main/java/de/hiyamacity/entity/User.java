package de.hiyamacity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "purse", nullable = false)
	private long purse;

	@Column(name = "is_afk", nullable = false)
	private boolean isAfk;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "roleplay_character_id")
	private RoleplayCharacter roleplayCharacter;

	@Column(name = "locale")
	private Locale locale;

	@Column(name = "player_unique_id", nullable = false, unique = true)
	private UUID playerUniqueID;
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "bank_account_id")
	private BankAccount bankAccount;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_owned_houses",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "house_id"))
	private Set<House> owned_houses = new LinkedHashSet<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_rented_houses",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "house_id"))
	private Set<House> rented_houses = new LinkedHashSet<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "afk_location_id")
	private AfkLocation nonAfkLocation;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}