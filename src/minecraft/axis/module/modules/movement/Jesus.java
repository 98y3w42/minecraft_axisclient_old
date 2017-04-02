package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.Event;
import axis.event.Event.State;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.MoveEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.LiquidUtils;
import axis.util.Logger;
import axis.util.MathUtils;
import axis.util.moveutil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Jesus extends Module {

	private int stage = 1;
	private double moveSpeed = 0.2873D;
	private Speed speed;

	public Jesus() {
		super("Jesus", -8355712, ModuleManager.Category.MOVEMENT);
		setTag((String) values.getValue("mode"));
		Axis.getAxis().getCommandManager().getContents().add(new Command("jesus", "<mode>", new String[] { "jesus" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("NCP")) {
						values.setValue("mode", "NCP");
						Logger.logChat("Jesus mode set to: NCP");
						setTag((String) values.getValue("mode"));
					} else if (message.split(" ")[2].equalsIgnoreCase("AAC")) {
						values.setValue("mode", "AAC");
						Logger.logChat("Jesus mode set to: AAC");
						setTag((String) values.getValue("mode"));
					}
				}
			}
		});
	}

	public void onValueSetup() {
		super.onValueSetup();
		values.addValue("mode", "NCP");
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if (values.getValue("mode").equals("NCP")) {
				if (mc.thePlayer.movementInput.sneak) {
					net.minecraft.util.Timer.timerSpeed = 1.0F;
					return;
				}
				if (mc.gameSettings.keyBindJump.isPressed()) {
					net.minecraft.util.Timer.timerSpeed = 1.0F;
				}
				boolean water = false;

				if (LiquidUtils.isInLiquid() && mc.thePlayer.isInsideOfMaterial(Material.air)) {
					mc.thePlayer.onGround = true;
					mc.thePlayer.motionY = 0.03799999877810478D;
					mc.thePlayer.fallDistance = 0.29F;
					mc.thePlayer.cameraPitch = 0.0F;

					switch (this.stage) {
					case 1:
						event.y += 1.0E-4D;
						this.stage += 1;
						if (mc.thePlayer.getItemInUseCount() == 0) {
							net.minecraft.util.Timer.timerSpeed = 1.5F;
							mc.thePlayer.motionX *= 1.1F;
							mc.thePlayer.motionZ *= 1.1F;
						}
						break;
					case 2:
						event.y += 2.0E-4D;
						this.stage += 1;
						net.minecraft.util.Timer.timerSpeed = 1.0F;
						if (mc.thePlayer.getItemInUseCount() == 0) {
							mc.thePlayer.motionX *= 0.95F;
							mc.thePlayer.motionZ *= 0.95F;
						}
						break;
					default:
						this.stage = 1;
						Speed.yOffset = event.y - this.mc.thePlayer.posY;
						break;
					}
				} else {
					net.minecraft.util.Timer.timerSpeed = 1.0F;
				}

				if (!mc.thePlayer.isInsideOfMaterial(Material.air)) {
					net.minecraft.util.Timer.timerSpeed = 1.0F;
				}
			} else if (values.getValue("mode").equals("AAC")) {
				if (mc.thePlayer.isInWater() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
					moveutil.setSpeed(0.19F);
					mc.thePlayer.motionY = 0.0D;
				}
			}
		}
	}

	public void onEnabled() {
		super.onEnabled();
		this.stage = 1;
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			Timer.timerSpeed = 1.0F;
		}
	}
}