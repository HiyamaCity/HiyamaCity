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
public class BankCredit {

	/**
	 * Credited Amount
	 */
	private long creditAmount;
	/**
	 * Amount that has been paid back for the credit.
	 */
	private long paidBackAmount;
	/**
	 * Whether the credit is completely paid back.
	 */
	private boolean closed;

	public BankCredit(long creditAmount) {
		this.creditAmount = creditAmount;
		this.paidBackAmount = 0;
		this.closed = false;
	}

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
}
