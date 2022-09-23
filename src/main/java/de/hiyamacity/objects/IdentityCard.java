package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentityCard {

	private String forename;
	private String surname;
	private Color favoriteColor;
	private DayOfBirth dayOfBirth;
	private List<Address> residentialAddresses;

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}

}
