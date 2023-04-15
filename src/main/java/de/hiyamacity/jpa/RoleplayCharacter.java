package de.hiyamacity.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "roleplay_characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleplayCharacter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@Enumerated
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@OneToOne(mappedBy = "roleplayCharacter", orphanRemoval = true)
	private User user;

}