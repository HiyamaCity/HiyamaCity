package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayOfBirth {

	private int day;
	private int month;
	private int year;

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
	
}