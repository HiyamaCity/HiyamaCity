package de.hiyamacity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	@ManyToMany(mappedBy = "owners")
	private Set<House> ownedHouses = new LinkedHashSet<>();

	@JsonIgnore
	@ManyToMany(mappedBy = "renters")
	private Set<House> rentedHouses = new LinkedHashSet<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "afk_location_id")
	private Location nonAfkLocation;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "playtime_id")
	private Playtime playtime;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "owned_plot_id")
	private Plot ownedPlots;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rented_plot_id")
	private Plot rentedPlots;

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