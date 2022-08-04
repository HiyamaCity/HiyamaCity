package de.hiyamacity.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hiyamacity.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public record Address(String street, int houseNumber, long postalCode, String city) {

	/**
	 * @param address Address that is queried.
	 *
	 * @return Returns whether an Address exists or not.
	 */
	public static boolean isAddressExists(Address address) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.address.street\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.postalCode\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.city\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.houseNumber\") = ?")) {
				ps.setString(1, "[\"" + address.street() + "\"]");
				ps.setString(2, "[\"" + address.postalCode() + "\"]");
				ps.setString(3, "[\"" + address.city() + "\"]");
				ps.setString(4, "[\"" + address.houseNumber() + "\"");
				ResultSet rs = ps.executeQuery();
				if (rs.next())
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@JsonIgnore
	public String getAsAddress() {
		return this.street + ", " + this.houseNumber + "\n" + this.postalCode + ", " + this.city;
	}
}
