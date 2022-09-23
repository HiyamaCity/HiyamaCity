package de.hiyamacity.objects;

import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.misc.Distances;
import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class House {

	private UUID houseID;
	private List<de.hiyamacity.objects.Location> doorLocations;
	private List<Resident> residents;
	private Address address;

	/**
	 * Instantiates a new House object and registers it in the Database.
	 *
	 * @param owner         UUID of the House Owner.
	 * @param doorLocations DoorLocation Array containing the coordinates for the locations of the doors.
	 * @param address       Address containing the address for the new House.
	 */
	public House(UUID houseID, UUID owner, List<de.hiyamacity.objects.Location> doorLocations, Address address) {
		this.houseID = houseID;
		this.doorLocations = doorLocations;
		this.address = address;
		this.residents = new ArrayList<>();
		this.residents.add(new Resident(owner, Resident.ResidentType.OWNER));
	}

	/**
	 * @param loc Location of Openable.
	 *
	 * @return Returns if that Block is associated with a door in the Database.
	 */
	public static boolean isLockedDoor(Location loc) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\") FROM HOUSES")) {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String world = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\")").replace("[", "").replace("]", "").replace("\"", "");
					String xString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\")").replace("[", "").replace("]", "");
					String yString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\")").replace("[", "").replace("]", "");
					String zString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\")").replace("[", "").replace("]", "");
					Location doorLocation = new Location(Bukkit.getWorld(world), Double.parseDouble(xString), Double.parseDouble(yString), Double.parseDouble(zString));
					if (loc.distanceSquared(doorLocation) <= Distances.HOUSE_DOOR_INTERACTION_MARGIN) return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param uuid UUID of the Person that tried to open something.
	 * @param loc  Location of the clicked Block.
	 *
	 * @return Returns if the Player is allowed to open that door.
	 */
	public static boolean allowedToOpen(UUID uuid, Location loc) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			List<House> houses = new ArrayList<>();
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.residents[*].uuid\") = ?")) {
				ps.setString(1, "[\"" + uuid.toString() + "\"]");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					houses.add(House.fromJson(rs.getString("HOUSE")));
				}
			}
			for (House house : houses) {
				for (de.hiyamacity.objects.Location doorLocation : house.getDoorLocations()) {
					if (new Location(Bukkit.getWorld(doorLocation.getWorld()), doorLocation.getX(), doorLocation.getY(), doorLocation.getZ()).distanceSquared(loc) <= Distances.HOUSE_DOOR_INTERACTION_MARGIN)
						return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return Returns a UUID that is not occupied in the Database
	 */
	public static UUID generateNonOccupiedUUID() {
		UUID uuid = UUID.randomUUID();
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE UUID = ?")) {
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) generateNonOccupiedUUID();
				return uuid;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param loc Location by which is queried in the Database.
	 *
	 * @return Returns a House object queried by its location.
	 */
	public static House getHouse(Location loc) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT HOUSE, JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\") FROM HOUSES")) {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String world = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\")").replace("[", "").replace("]", "").replace("\"", "");
					String xString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\")").replace("[", "").replace("]", "");
					String yString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\")").replace("[", "").replace("]", "");
					String zString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\")").replace("[", "").replace("]", "");
					Location doorLocation = new Location(Bukkit.getWorld(world), Double.parseDouble(xString), Double.parseDouble(yString), Double.parseDouble(zString));
					if (loc.distanceSquared(doorLocation) <= Distances.HOUSE_DOOR_INTERACTION_MARGIN)
						return House.fromJson(rs.getString("HOUSE"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param houseID houseID queried in the Database to return the corresponding House object.
	 *
	 * @return Returns a House object corresponding to its houseID.
	 */
	public static House getHouse(UUID houseID) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT HOUSE FROM HOUSES WHERE UUID = ?")) {
				ps.setString(1, houseID.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) return House.fromJson(rs.getString("HOUSE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param resident UUID of the Houses' resident queried in the Database to return the corresponding House object.
	 *
	 * @return Returns a List of House objects corresponding to its houseID.
	 */
	public static List<House> getHouses(UUID resident) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			List<House> houses = new ArrayList<>();
			try (PreparedStatement ps = con.prepareStatement("SELECT HOUSE FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.residents[*].uuid\") = ?")) {
				ps.setString(1, "[\"" + resident.toString() + "\"]");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) houses.add(House.fromJson(rs.getString("HOUSE")));
				return houses;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Deletes a House by its HouseID.
	 *
	 * @param houseID HouseID of the House to delete.
	 */
	public static void deleteHouse(UUID houseID) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("DELETE * FROM HOUSES WHERE UUID = ?")) {
				ps.setString(1, houseID.toString());
				ps.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a House by its Address.
	 *
	 * @param address Address of the House to delete.
	 */
	public static void deleteHouse(Address address) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("DELETE * FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.address.street\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.postalCode\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.city\") = ? AND JSON_EXTRACT(HOUSE, \"$.address.houseNumber\") = ?")) {
				ps.setString(1, "[\"" + address.street() + "\"]");
				ps.setString(2, "[\"" + address.postalCode() + "\"]");
				ps.setString(3, "[\"" + address.city() + "\"]");
				ps.setString(4, "[\"" + address.houseNumber() + "\"");
				ps.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static House fromJson(String string) {
		return JsonHandler.getJsonAsObject(string, House.class);
	}

	/**
	 * Registers a new House in the Database.
	 */
	public void registerHouse() {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("INSERT INTO HOUSES (UUID, HOUSE) VALUES (?,?)")) {
				ps.setString(1, this.getHouseID().toString());
				ps.setString(2, this.toString());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}
}