package de.hiyamacity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "atms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ATM {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@OneToOne(optional = false, orphanRemoval = true)
	@JoinColumn(name = "location_id", nullable = false, unique = true)
	private Location location;

	@Column(name = "amount", nullable = false)
	private long amount;

	@Column(name = "maximum_amount", nullable = false)
	private long maximumAmount;

}