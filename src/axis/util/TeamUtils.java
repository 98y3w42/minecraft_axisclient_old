package axis.util;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;

public class TeamUtils {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static String username;

	public static void setUsername(String name) {
		username = name;
	}

	public static Boolean isTeam(EntityLivingBase entity) {
		Iterator list = mc.getNetHandler().playerInfoMap.values().iterator();
		while(list.hasNext()) {
			Object o = list.next();
			NetworkPlayerInfo info = (NetworkPlayerInfo)o;
			if (info.getGameProfile().getName().equals(username) && entity.getTeam().isSameTeam(info.getPlayerTeam())) {
				return true;
			}
		}
		return false;
	}
}
