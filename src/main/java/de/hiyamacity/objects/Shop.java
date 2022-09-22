package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
public class Shop {

	@Expose
	private String name;
	@Expose
	private UUID[] owner;
	@Expose
	private ShopType[] shopType;
	@Expose
	private Location location;
	public Shop(String name, UUID[] owner, ShopType[] shopType, Location location) {
		this.name = name;
		this.owner = owner;
		this.shopType = shopType;
		this.location = location;
	}

	public static Shop fromJson(String string) {
		return new GsonBuilder().create().fromJson(string, Shop.class);
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}

	@Getter
	public enum ShopType {

		FOOD(),
		DRINK(),
		AGRICULTURE(),
		SOUVENIRS(),
		ORES(),
		ARMOR(),
		PET(),
		TIMBER(),
		STONES(),
		NETHER(),
		END(),
		DECORATION(),
		BLOCKS(),
		WEAPONS();

		ShopType() {

		}

		public static List<String> getAllTypes() {
			return Stream.of(ShopType.values())
					.map(ShopType::name)
					.collect(Collectors.toList());
		}

	}

}