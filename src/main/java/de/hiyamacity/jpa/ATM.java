package de.hiyamacity.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

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
	private long id;

	@OneToOne(optional = false, orphanRemoval = true)
	@JoinColumn(name = "location_id", nullable = false, unique = true)
	private Location location;

	@Column(name = "amount", nullable = false)
	private long amount;

	@Column(name = "maximum_amount", nullable = false)
	private long maximumAmount;

	@SneakyThrows
	@Override
	public String toString() {
		return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}
}