package axis.management.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;

import axis.Axis;
import axis.management.MapManager;
import net.minecraft.entity.player.EntityPlayer;

public final class FriendManager extends MapManager {
	public void addFriend(String name, String alias) {
		this.contents.put(name, alias);
	}

	public boolean isFriend(String name) {
		return this.contents.containsKey(name);
	}

	public void removeFriend(String name) {
		this.contents.remove(name);
	}

	public String replaceNames(String message, boolean color) {
		Iterator var4 = this.contents.keySet().iterator();
		String name;
		for (; var4.hasNext(); message = message.replaceAll("(?i)" + name,
				Matcher.quoteReplacement(color ? "§f[§eF§f]§a" +
						(String) this.contents.get(name) + "§r" : (String) this.contents.get(name)))) {
			name = (String) var4.next();
		}
		return message;
	}

	public boolean isOnSameTeamFriend(EntityPlayer entity) {
		if (entity.isOnSameTeam(entity)) {
			return this.contents.containsKey(entity.getName());
		}
		return false;
	}

	public void setup() {
		this.contents = new HashMap();
	}
}