package de.hiyamacity.objects;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hiyamacity.main.Main;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectTests {

	private static ServerMock server;
	private static Main plugin;
	final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	public static void setUp() {
		server = MockBukkit.mock();
		plugin = MockBukkit.load(Main.class);
	}

	@AfterAll
	public static void tearDown() {
		MockBukkit.unmock();
	}

	@Test
	public void TestIfUserJsonCreationIsWorkingCorrectly() {

		/* !!! NOTICE: This Test needs a running MySQL Server on localhost to be successful !!! */
		PlayerMock playerMock = server.addPlayer();
		Player player = playerMock.getPlayer();

		// Making sure the mocked player isn't null.
		assertNotNull(player);

		// Generating and instantiating a user object that is registered in the Database with its corresponding UUID..
		UUID uuid = player.getUniqueId();
		Optional<User> newUser = User.getUser(uuid);
		String userJson = null;

		try {
			// Converting the user object into a JSON string.
			userJson = objectMapper.writeValueAsString(newUser.orElse(null));
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

		// Testing the parsed user object.
		assertNotNull(user);
		assertEquals(user.getPurse(), 4000);
		assertEquals(user.getBank(), 2000);
		assertEquals(user.getPlayedMinutes(), 0);
		assertEquals(user.getPlayedHours(), 0);
		assertEquals(user.getKills(), 0);
		assertEquals(user.getDeaths(), 0);
		assertEquals(user.getUuid(), uuid);
		assertFalse(user.isAfk());

		// Cleaning up the database
		user.delete();
	}


	@Test
	public void TestIfHouseJsonCreationIsWorkingCorrectly() {

		// Example JSON string for comparison.
		final String resultJson = "{\"houseID\":\"%houseID%\",\"doorLocations\":[{\"world\":null,\"x\":1.0,\"y\":2.0,\"z\":3.0,\"yaw\":0.0,\"pitch\":0.0}],\"residents\":[{\"uuid\":\"%uuid%\",\"residentType\":\"OWNER\"}],\"address\":{\"street\":\"Street\",\"houseNumber\":69,\"postalCode\":696969,\"city\":\"City\"}}";

		// Generating random UUIDs for testing.
		UUID houseID = UUID.randomUUID();
		UUID uuid = UUID.randomUUID();

		// Creating a new location list for the house constructor.
		List<Location> doorLocationList = List.of(new Location(1, 2, 3));

		// Instantiating a new house object with predefined parameters..
		House house = new House(houseID, uuid, doorLocationList, new Address("Street", 69, 696969, "City"));

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
		assertEquals(jsonHouse.getHouseID(), house.getHouseID());
		Location doorLocation = jsonHouse.getDoorLocations().get(0);
		Resident residents = jsonHouse.getResidents().get(0);
		Address address = jsonHouse.getAddress();

		// doorLocations node testing
		assertNull(doorLocation.getWorld());
		assertEquals(doorLocation.getX(), 1);
		assertEquals(doorLocation.getY(), 2);
		assertEquals(doorLocation.getZ(), 3);
		assertEquals(doorLocation.getYaw(), 0);
		assertEquals(doorLocation.getPitch(), 0);

		// residents node testing
		assertEquals(residents.getUuid(), uuid);
		assertEquals(residents.getResidentType(), Resident.ResidentType.OWNER);

		// address node testing
		assertEquals(address.street(), "Street");
		assertEquals(address.postalCode(), 696969);
		assertEquals(address.city(), "City");
		assertEquals(address.houseNumber(), 69);

		// TODO: Hash code check
	}

	@Test
	public void TestIfBanJsonCreationIsWorkingCorrectly() {

		// Create random uuid and ban for testing.
		UUID uuid = UUID.randomUUID();
		Ban ban = new Ban(uuid);

		String banJson = null;
		try {
			banJson = objectMapper.writeValueAsString(ban);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertNotNull(banJson);

		Ban jsonBan = null;

		try {
			jsonBan = objectMapper.readValue(banJson, Ban.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertNotNull(jsonBan);
		assertTrue(jsonBan.isActive());
		assertEquals(jsonBan.getBanReason(), ban.getBanReason());
		assertEquals(jsonBan.getBanID(), ban.getBanID());
		assertEquals(jsonBan.getBanStart(), ban.getBanStart());
		assertEquals(jsonBan.getBanEnd(), ban.getBanEnd());
		assertEquals(jsonBan.getCreatedBy(), ban.getCreatedBy());

	}

	@Test
	public void TestIfAddressJsonCreationIsWorkingCorrectly() {

		// Creating testing Address.
		Address address = new Address("Street", 69, 696969, "City");

		// Testing if the address is null.
		assertNotNull(address);

		Address address1;

		try {
			// Creating a copy of the address after parsing it from a JSON string.
			address1 = objectMapper.readValue(objectMapper.writeValueAsString(address), Address.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		// Testing if the object got created correctly.
		assertEquals(address.getAsAddress(), address1.getAsAddress());
		assertEquals(address.street(), address1.street());
		assertEquals(address.houseNumber(), address1.houseNumber());
		assertEquals(address.postalCode(), address1.postalCode());
		assertEquals(address.city(), address1.city());
		assertEquals(address.hashCode(), address1.hashCode());

	}
}
