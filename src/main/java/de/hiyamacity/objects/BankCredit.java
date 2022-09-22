package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankCredit {

	/**
	 * Credited Amount
	 */
	@Expose
	private long creditAmount;
	/**
	 * Amount that has been paid back for the credit.
	 */
	@Expose
	private long paidBackAmount;
	/**
	 * Whether the credit is completely paid back.
	 */
	@Expose
	private boolean closed;

	public BankCredit(long creditAmount) {
		this.creditAmount = creditAmount;
		this.paidBackAmount = 0;
		this.closed = false;
	}

	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
