package de.hiyamacity.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "houses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class House {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long id;

	@ManyToMany(mappedBy = "owned_houses")
	private Set<User> owners = new LinkedHashSet<>();

	@ManyToMany(mappedBy = "rented_houses")
	private Set<User> renters = new LinkedHashSet<>();

	@Column(name = "house_hold_cash", nullable = false)
	private long houseHoldCash;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Location> doorLocations = new LinkedHashSet<>();

	@OneToOne(optional = false, orphanRemoval = true)
	@JoinColumn(name = "sign_location_id", nullable = false, unique = true)
	private Location signLocation;

	@SneakyThrows
	@Override
	public String toString() {
		return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

}