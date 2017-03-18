package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.Event.State;
import axis.event.events.PacketSentEvent;
import axis.event.events.StepEvent;
import axis.event.events.TickEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {

	private final Value<String> currentMode = new Value("step_mode", "New");
	public final Value<Double> height = new Value<>("step_height", 1.5D);

	private int delay;
	private boolean editingPackets;

	public Step() {
		super("Step", 0x00BFFF, Category.MOVEMENT);
		setTag(currentMode.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("step", "<mode>", new String[]{}) {
			public void run(String message) {
				if(message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("New")) {
						currentMode.setValue("New");
					} else if (message.split(" ")[2].equalsIgnoreCase("Old")) {
						currentMode.setValue("Old");
					} else if (message.split(" ")[2].equalsIgnoreCase("Jump")) {
						currentMode.setValue("Jump");
					}
					Logger.logChat("Step Mode is " + currentMode.getValue());
					setTag(currentMode.getValue());
				}
			}
		});
	}

	public void onTick(TickEvent event) {
	height.setValue(1D);
		if (delay > 0)
			delay--;
	}

	public void onStep(StepEvent event) {
		if (event.state == State.PRE) {
			if (Speed.canStep) {
				double yDifference = mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
				boolean yDiffCheck = yDifference == 0.0D;

				if (!currentMode.getValue().equalsIgnoreCase("Jump") && delay == 0 && yDiffCheck && event.canStep() && mc.thePlayer.fallDistance == 0.0D && (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0) && this.mc.thePlayer.isCollidedVertically) {
					event.setEdit(true);
					event.stepHeight = height.getValue();
					delay = 1;
				}
			}
		}

		if (event.state == State.POST) {
			if (Speed.canStep) {
				this.editingPackets = (!BlockHelper.isInLiquid(this.mc.thePlayer));
				MineZ.wait = 8;
				if (currentMode.getValue().equalsIgnoreCase("NEW")) {
					mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41, mc.thePlayer.posZ, true));
					mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75, mc.thePlayer.posZ, true));
				}
			}
		}
	}

	public void onPacketSent(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			if (Speed.canStep) {
				C03PacketPlayer player = (C03PacketPlayer)event.getPacket();
				if ((Step.this.editingPackets) && currentMode.getValue().equalsIgnoreCase("OLD")) {
					if (Step.this.mc.thePlayer.posY - Step.this.mc.thePlayer.lastTickPosY >= 0.75D) {
						player.setY(player.getPositionY() + 0.0646D);
					}
					Step.this.editingPackets = false;
				}
				else if (currentMode.getValue().equalsIgnoreCase("Jump")) {
					if ((Step.this.mc.thePlayer.isCollidedHorizontally) && (Step.this.mc.thePlayer.onGround)) {
						Step.this.mc.thePlayer.motionY = 0.3815D;
						Step.this.mc.thePlayer.isAirBorne = true;
					}
					Step.this.editingPackets = false;
				}
				else if (currentMode.getValue().equalsIgnoreCase("NEW")) {
					if (Step.this.mc.thePlayer.onGround) {
						Step.this.editingPackets = false;
					}
				}
			}
		}
	}
}

