package de.hiyamacity.util;

import lombok.Getter;

@SuppressWarnings("unused")
public enum Distance {

	ATM(Math.pow(2, 2)),
	CHAT_MESSAGE_SMALL(Math.pow(8, 2)),
	CHAT_MESSAGE_MEDIUM(Math.pow(16, 2)),
	CHAT_MESSAGE_LARGE(Math.pow(32, 2));

	@Getter
	private final double value;

	Distance(double i) {
		this.value = i;
	}

}
