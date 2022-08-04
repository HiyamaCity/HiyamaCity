package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

@Getter
@Setter
public class Ban {

	@Expose
	private String banReason;
	@Expose
	private String banID = RandomStringUtils.randomAlphanumeric(12);
	@Expose
	private long banStart = System.currentTimeMillis();
	@Expose
	private long banEnd;
	@Expose
	private boolean isActive = true;
	@Expose
	private UUID createdBy;

	public Ban(UUID createdBy) {
		this.createdBy = createdBy;
	}

	public Ban() {
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}
}
