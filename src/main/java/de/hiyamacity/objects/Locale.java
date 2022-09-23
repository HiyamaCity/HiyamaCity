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
public class Locale {

	private String language;
	private String country;

	public java.util.Locale getJavaUtilLocale() {
		return new java.util.Locale(this.getLanguage(), this.getCountry());
	}

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
}