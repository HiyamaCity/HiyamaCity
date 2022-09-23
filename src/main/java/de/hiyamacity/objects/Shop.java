package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

	private String name;
	private UUID[] owner;
	private ShopType[] shopType;
	private Location location;

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
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