package de.hiyamacity.objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectTests {

	final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void TestUserJsonCreationWorkingCorrectly() {

		// Generating a random UUID and instantiating a user object.
		UUID randomUUID = UUID.randomUUID();
		User randomUser = new User(randomUUID);

		String userJson = null;

		try {
			// Converting the user object into a JSON string.
			userJson = objectMapper.writeValueAsString(randomUser);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertNotNull(userJson);

		User user = null;

		try {
			// Reading the JSON tree of the converted user object.
			user = objectMapper.readValue(userJson, User.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertNotNull(user);

		assertEquals(user.getPurse(), 4000);
		assertEquals(user.getBank(), 2000);
		assertEquals(user.getPlayedMinutes(), 0);
		assertEquals(user.getPlayedHours(), 0);
		assertEquals(user.getKills(), 0);
		assertEquals(user.getDeaths(), 0);
		assertEquals(user.getUuid(), randomUUID);
		assertFalse(user.isAfk());
	}

	@Test
	public void TestHouseJsonCreationWorkingCorrectly() {

		// Example JSON string for comparison.
		final String resultJson = "{\"houseID\":\"%houseID%\",\"doorLocations\":[{\"world\":null,\"x\":1.0,\"y\":1.0,\"z\":1.0,\"yaw\":0.0,\"pitch\":0.0}],\"residents\":[{\"uuid\":\"%uuid%\",\"residentType\":\"OWNER\"}],\"address\":{\"street\":\"Street\",\"postalCode\":696969,\"city\":\"City\",\"houseNumber\":69}}";

		// Generating random UUIDs for testing.
		UUID houseID = UUID.randomUUID();
		UUID uuid = UUID.randomUUID();

		// Creating a new location list for the house constructor.
		List<Location> doorLocationList = List.of(new Location(1, 1, 1));

		// Instantiating a new house object with predefined parameters..
		House house = new House(houseID, uuid, doorLocationList, new Address("Street", 69, "City", 696969));

		String houseJson = null;

		try {
			// Converting the house object into a JSON string.
			houseJson = objectMapper.writeValueAsString(house);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// Checking if the JSON string was created correctly.
		assertNotNull(houseJson);
		assertEquals(houseJson, resultJson.replace("%houseID%", houseID.toString()).replace("%uuid%", uuid.toString()));

		House jsonHouse = null;

		try {
			// Converting the JSON string into separate nodes.
			jsonHouse = objectMapper.readValue(houseJson, House.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// testing raw node value
		assertNotNull(jsonHouse);
		assertEquals(jsonHouse.getHouseID(), houseID);
		Location doorLocation = jsonHouse.getDoorLocations().get(0);
		Resident residents = jsonHouse.getResidents().get(0);
		Address address = jsonHouse.getAddress();

		// doorLocations node testing
		assertNull(doorLocation.getWorld());
		assertEquals(doorLocation.getX(), 1);
		assertEquals(doorLocation.getY(), 1);
		assertEquals(doorLocation.getZ(), 1);
		assertEquals(doorLocation.getYaw(), 0);
		assertEquals(doorLocation.getPitch(), 0);

		// residents node testing
		assertEquals(residents.getUuid(), uuid);
		assertEquals(residents.getResidentType(), Resident.ResidentType.OWNER);

		// address node testing
		assertEquals(address.getStreet(), "Street");
		assertEquals(address.getPostalCode(), 696969);
		assertEquals(address.getCity(), "City");
		assertEquals(address.getHouseNumber(), 69);

	}
}