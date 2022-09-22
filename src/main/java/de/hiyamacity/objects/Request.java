package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Request<T> {


	@Expose
	private UUID requester;
	@Expose
	private UUID recipient;
	@Expose
	private long requestedAt;
	@Expose
	private RequestType requestType;
	public Request(UUID requester, UUID recipient, long requestedAt, RequestType requestType) {
		this.requester = requester;
		this.recipient = recipient;
		this.requestedAt = requestedAt;
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}

	public enum RequestType {
		CONTRACT, TELEPORT, JOIN
	}

}
