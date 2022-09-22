package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Resident {

	@Expose
	private UUID uuid;
	@Expose
	private ResidentType residentType;
	public Resident(UUID uuid, ResidentType renterType) {
		this.uuid = uuid;
		this.residentType = renterType;
	}

	public Resident() {
	}

	public static Resident fromJson(String string) {
		return new GsonBuilder().create().fromJson(string, Resident.class);
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}

	public enum ResidentType {
		OWNER(),
		RENTER()
	}

}