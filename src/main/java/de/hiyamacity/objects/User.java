package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.lang.LanguageHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

@Getter
@Setter
public class User {

	/**
	 * The amount of money that the user carries in their pocket.
	 */
	@Expose
	private long purse;
	/**
	 * The amount of money that the user has stored in the bank.
	 */
	@Expose
	private long bank;
	/**
	 * The amount of minutes played on the server.
	 */
	@Expose
	private long playedMinutes;
	/**
	 * The amount of hours played on the server.
	 */
	@Expose
	private long playedHours;
	/**
	 * The amount of kills that the user made.
	 */
	@Expose
	private long kills;
	/**
	 * The amount of deaths that the user suffered.
	 */
	@Expose
	private long deaths;
	/**
	 * The unique user id of the user.
	 *
	 * @see UUID
	 */
	@Expose
	private UUID uuid;
	/**
	 * The identity card of the user.
	 *
	 * @see IdentityCard
	 */
	@Expose
	private IdentityCard identityCard;
	/**
	 * The locale that the user specified.
	 *
	 * @see Locale
	 */
	@Expose
	private Locale locale;
	/**
	 * List of bans that the user experienced.
	 *
	 * @see Ban
	 */
	@Expose
	private List<Ban> bans;
	/**
	 * List of skills that the user has.
	 *
	 * @see Skill
	 */
	@Expose
	private List<Skill> skills;
	@Expose
	private List<BankCredit> bankCredits;
	@Expose
	private Location nonAfkLocation;
	@Expose
	private boolean isAfk;
	@Expose
	private List<Contract> contracts;

	/**
	 * Instantiates a new User object that gets registered in the Database with it's corresponding UUID.
	 *
	 * @param uuid Identifier by which is queried in the Database and which allows the object to be associated with a Player on the Server.
	 */
	public User(UUID uuid) {
		this.purse = 4000;
		this.bank = 2000;
		this.playedMinutes = 0;
		this.playedHours = 0;
		this.uuid = uuid;
		register();
	}

	public User() {
	}

	/**
	 * @param uuid Unique user ID of the Player.
	 *
	 * @return Return if the User is present in the Database or not.
	 */
	public static boolean isUserExist(UUID uuid) {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM PLAYERS WHERE UUID = ?")) {
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Registers a new User Object and its corresponding UUID in the Database.
	 */
	private void register() {
		if (isUserExist(this.uuid)) return;
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("INSERT INTO PLAYERS (UUID, PLAYER) VALUES (?,?)")) {
				ps.setString(1, uuid.toString());
				ps.setString(2, this.toString());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the User Object in the Database. Used for Object manipulation.
	 */
	public void update() {
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("UPDATE PLAYERS SET PLAYER = ? WHERE UUID = ?")) {
				ps.setString(1, this.toString());
				ps.setString(2, this.uuid.toString());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param uuid Unique user ID of the Player.
	 *
	 * @return Returns a User Object from the Database by its corresponding UUID.
	 */
	public static Optional<User> getUser(UUID uuid) {
		if (!isUserExist(uuid)) return Optional.empty();
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT PLAYER FROM PLAYERS WHERE UUID = ?")) {
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) return Optional.ofNullable(fromJson(rs.getString("PLAYER")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	/**
	 * Adds Experience to a specific SkillType and sends a message to the Player (if online) that they leveled up.
	 *
	 * @param skillType SkillType of the Skill to add Experience to.
	 * @param amount    Amount of Experience to add.
	 */
	public void addXP(Skill.SkillType skillType, int amount) {
		Skill skill = this.getSkills().stream().filter(filteredSkill -> filteredSkill.getSkillType().equals(skillType)).toList().get(0);
		if (skill.addXP(amount)) {
			UUID uuid = this.uuid;
			ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
			Optional<Player> p = Optional.ofNullable(Bukkit.getPlayer(uuid));
			p.ifPresent(player -> player.sendMessage(rs.getString("levelUp").replace("%skill%", skill.toString()).replace("%level%", String.valueOf(skill.getLevel()))));
		}
	}

	public void delete() {
		if (!isUserExist(this.uuid)) return;
		try (Connection con = ConnectionPool.getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("DELETE FROM PLAYERS WHERE UUID = ?")) {
				ps.setString(1, this.uuid.toString());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
	}

	public static User fromJson(String string) {
		return new GsonBuilder().create().fromJson(string, User.class);
	}

}
