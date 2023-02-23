package de.hiyamacity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

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
	private UUID id;

	@ManyToMany(mappedBy = "owned_houses")
	private Set<User> owners = new LinkedHashSet<>();

	@ManyToMany(mappedBy = "rented_houses")
	private Set<User> renters = new LinkedHashSet<>();

}