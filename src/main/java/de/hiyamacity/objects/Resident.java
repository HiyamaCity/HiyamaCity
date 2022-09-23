package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resident {

	private UUID uuid;
	private ResidentType residentType;

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}

	public enum ResidentType {
		OWNER(),
		RENTER()
	}

}