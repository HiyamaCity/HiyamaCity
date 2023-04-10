package de.hiyamacity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "plot")
public class Plot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "x_1", nullable = false)
	private double x1;

	@Column(name = "y_1", nullable = false)
	private double y1;

	@Column(name = "x_2", nullable = false)
	private double x2;

	@Column(name = "y_2", nullable = false)
	private double y2;

	@OneToMany(mappedBy = "ownedPlots", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<User> owners = new LinkedHashSet<>();

	@OneToMany(mappedBy = "ownedPlots", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<User> renters = new LinkedHashSet<>();

}