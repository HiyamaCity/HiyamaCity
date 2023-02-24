package de.hiyamacity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "playtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Playtime {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "minutes", nullable = false)
	private long minutes;

	@Column(name = "hours", nullable = false)
	private long hours;

}