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
public class Color {

	private int r;
	private int g;
	private int b;
	private int a;

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
	
}
