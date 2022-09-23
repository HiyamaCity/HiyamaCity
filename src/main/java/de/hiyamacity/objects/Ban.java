package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ban {

	private String banReason;
	private String banID = RandomStringUtils.randomAlphanumeric(12);
	private long banStart = System.currentTimeMillis();
	private long banEnd;
	private boolean isActive = true;
	private UUID createdBy;

	public Ban(UUID createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
}
