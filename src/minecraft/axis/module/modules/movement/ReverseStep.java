package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.Event.State;
import axis.event.Event;
import axis.event.events.PacketSentEvent;
import axis.event.events.StepEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.movement.speed.SpeedMode;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class ReverseStep extends Module {

	private boolean reverse = true;
	private int groundTicks;
	private int recentStepTicks;
	private TimeHelper timer = new TimeHelper();

	public ReverseStep() {
		super("ReverseStep", 0x00BFFF, Category.MOVEMENT);
	}

	private void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if (Speed.speedmode != null || Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
				if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()
						|| (((Speed.speedmode.equals("Bhop2") || Speed.speedmode.equals("Bhop") || (Speed.speedmode.equals("SlowHop"))) && Axis.getAxis().getModuleManager().getModuleByName("Speed").isEnabled()))) {
					return;
				}
			}
			if (mc.gameSettings.keyBindJump.isKeyDown() || (getBlock(-0.1D) instanceof BlockStaticLiquid) || (getBlock(-1.1D) instanceof BlockStaticLiquid) || (getBlock(-0.1D) instanceof BlockAir) || (mc.thePlayer.isOnLadder())) {
				this.reverse = false;
			}
			if (mc.thePlayer.onGround && !Axis.getAxis().getModuleManager().getModuleByName("Fly").isEnabled() && !(getBlock(-0.1D) instanceof BlockStaticLiquid) && !(getBlock(-1.1D) instanceof BlockStaticLiquid)) {
				this.reverse = true;
			}
			if ((this.reverse) && (!mc.gameSettings.keyBindJump.pressed) && (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInsideOfMaterial(Material.water))
					&& (!mc.thePlayer.isInsideOfMaterial(Material.lava)) && (!mc.thePlayer.isInWater())
					&& (((!(getBlock(-1.1D) instanceof BlockAir)) && (!(getBlock(-1.1D) instanceof BlockAir))) || ((!(getBlock(-0.1D) instanceof BlockAir)) && (mc.thePlayer.motionX != 0.0D)
							&& (mc.thePlayer.motionZ != 0.0D) && (this.reverse) && (!mc.thePlayer.onGround) && (mc.thePlayer.fallDistance < 3.0F) && (mc.thePlayer.fallDistance > 0.05D)))) {
				mc.thePlayer.motionY = -4.0D;
			}
			this.recentStepTicks += 1;
			if (mc.thePlayer.onGround) {
				this.groundTicks += 1;
			} else {
				this.groundTicks = 0;
			}
		}
	}

	private void onStep(StepEvent event) {
		if ((this.timer.hasReached(300.0F)) && (mc.thePlayer.movementInput != null) && (this.recentStepTicks >= 2) && (this.groundTicks >= 2) && (!mc.thePlayer.movementInput.jump)) {
			this.timer.reset();
		}
	}

	public Block getBlock(AxisAlignedBB bb) {
		int y = (int) bb.minY;
		for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++) {
				Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}

	public Block getBlock(double offset) {
		return getBlock(mc.thePlayer.getEntityBoundingBox().offset(0.0D, offset, 0.0D));
	}
}
