package axis.module.modules.player;

import axis.event.events.PacketSentEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
	private Value<Boolean> nocheat = new Value("nofall_nocheat", Boolean.valueOf(false));
	private final TimeHelper time = new TimeHelper();

	public NoFall() {
		super("NoFall", -3730043, ModuleManager.Category.PLAYER);
		setDisplayName("No Fall");
	}

	public void onEventCalled(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			if (mc.thePlayer.fallDistance < 3)
				return;
			C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
			packet.setOnGround(true);
		}
	}
}