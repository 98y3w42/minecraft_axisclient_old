package axis.module.modules.combat;

import axis.Axis;
import axis.command.Command;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class Regen extends Module {

	private final Value<String> currentMode = new Value("regen_mode", "Potion");
	private TimeHelper time = new TimeHelper();

	public Regen() {
		super("Regen", -8388608, ModuleManager.Category.COMBAT);
		setTag(currentMode.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("regen", "<mode>", new String[]{"cc"}) {
			public void run(String message) {
				if(message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("Potion")) {
						currentMode.setValue("Potion");
					} else if (message.split(" ")[2].equalsIgnoreCase("Bypass")) {
						currentMode.setValue("Bypass");
					} else if (message.split(" ")[2].equalsIgnoreCase("Old")) {
						currentMode.setValue("Old");
					}
					Logger.logChat("Regen Mode is " + currentMode.getValue());
					setTag(currentMode.getValue());
				}
			}
		});
	}


    public void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer && currentMode.getValue().equals("Bypass")) {
            C03PacketPlayer player = (C03PacketPlayer) event.getPacket();
            if (mc.thePlayer.onGround || BlockHelper.isOnLadder() || BlockHelper.isInLiquid(mc.thePlayer) || BlockHelper.isOnLiquid()) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.ticksExisted % 2 == 0) {
                	if (time.hasReached(210L)) {
                		time.reset();
                		for (int i = 0; i < 4; i++) {
                			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                		}
                	}
                }
            }
        }
    }

    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null && currentMode.getValue().equals("Potion")) {
            if (mc.thePlayer.onGround || BlockHelper.isOnLadder() || BlockHelper.isInLiquid(mc.thePlayer) || BlockHelper.isOnLiquid()) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
                    for (int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                }
            }
        }

        if (currentMode.getValue().equals("Old")) {
            if (mc.thePlayer.onGround || BlockHelper.isOnLadder() || BlockHelper.isInLiquid(mc.thePlayer) || BlockHelper.isOnLiquid()) {
            	if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.ticksExisted % 2 == 0) {
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            	}
            }
        }
    }
}
