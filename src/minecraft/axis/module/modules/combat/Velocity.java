package axis.module.modules.combat;

import axis.Axis;
import axis.command.Command;
import axis.event.events.PacketReceiveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.speed.modes.Bhop2;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

	public final Value<Integer> velocity = new Value<>("velocity_velocity", 0);

	public Velocity() {
		super("Velocity", -4731698, ModuleManager.Category.COMBAT);
		setTag(velocity.getValue() + "%");
		Axis.getCommandManager().getContents().add(new Command("velocity", "<amount>", new String[] { "vel", "v" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("-d")) {
					Velocity.this.velocity.setValue((Integer) Velocity.this.velocity.getDefaultValue());
				} else {
					Velocity.this.velocity.setValue(Integer.valueOf(Integer.parseInt(message.split(" ")[1])));
				}
				if (((Integer) Velocity.this.velocity.getValue()).intValue() > 100) {
					Velocity.this.velocity.setValue(Integer.valueOf(100));
				} else if (((Integer) Velocity.this.velocity.getValue()).intValue() < -100) {
					Velocity.this.velocity.setValue(Integer.valueOf(-100));
				}
				setTag(velocity.getValue() + "%");
				Logger.logChat("Velocity set to: " + Velocity.this.velocity.getValue() + "%");
			}
		});
	}

	public void onPacketRecive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			packet.motionX = (int) ((float) packet.motionX * (((Integer) this.velocity.getValue()).intValue()) / 100);
			packet.motionY = (int) ((float) packet.motionY * (((Integer) this.velocity.getValue()).intValue()) / 100);
			packet.motionZ = (int) ((float) packet.motionZ * (((Integer) this.velocity.getValue()).intValue()) / 100.);
			if (packet.motionX == 0 && packet.motionY == 0 && packet.motionZ == 0) {
				event.setCancelled(true);
			}
		}

		if (event.getPacket() instanceof S27PacketExplosion) {
			S27PacketExplosion packet1 = (S27PacketExplosion) event.getPacket();
			packet1.field_149152_f *= ((Integer) this.velocity.getValue()).intValue() / 100;
			packet1.field_149153_g *= ((Integer) this.velocity.getValue()).intValue() / 100;
			packet1.field_149159_h *= ((Integer) this.velocity.getValue()).intValue() / 100;
		}
	}
}