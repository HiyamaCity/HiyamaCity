package de.hiyamacity.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class Address {

	@Expose
	private String street;
	@Expose
	private long postalCode;
	@Expose
	private String city;
	@Expose
	private long houseNumber;

	public Address(String street, long houseNumber, String city, long postalCode) {
		this.street = street;
		this.houseNumber = houseNumber;
		this.city = city;
		this.postalCode = postalCode;
	}

	public Address() {
	}

	/**
	 * @param address Address that is queried.
	 *
	 * @return Returns whether an Address exists or not.
	 */
	public static boolean isAddressExists(Address address) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.address.street\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.postalCode\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.city\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.houseNumber\") = ?")) {
				ps.setString(1, "[\"" + address.getStreet() + "\"]");
				ps.setString(2, "[\"" + address.getPostalCode() + "\"]");
				ps.setString(3, "[\"" + address.getCity() + "\"]");
				ps.setString(4, "[\"" + address.getHouseNumber() + "\"");
				ResultSet rs = ps.executeQuery();
				if (rs.next())
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}

	@JsonIgnore
	public String getAsAddress() {
		return this.getStreet() + ", " + this.getHouseNumber() + "\n" + this.getPostalCode() + ", " + this.getCity();
	}
}