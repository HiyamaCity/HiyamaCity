package de.hiyamacity.util;

import lombok.Getter;

@SuppressWarnings("unused")
public enum Distance {

	ATM(Math.pow(2, 2)),
	CHAT_MESSAGE_SMALL(Math.pow(4, 2)),
	CHAT_MESSAGE_MEDIUM(Math.pow(8, 2)),
	CHAT_MESSAGE_LARGE(Math.pow(12, 2)),
	CHAT_MESSAGE_HUGE(Math.pow(24, 2));

	@Getter
	private final double value;

	Distance(double i) {
		this.value = i;
	}

}
