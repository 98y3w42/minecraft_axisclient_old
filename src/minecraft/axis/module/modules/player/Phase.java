package axis.module.modules.player;

import axis.Axis;
import axis.event.Event;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.PushOutOfBlocksEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.util.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase
		extends Module {

	private TimeHelper time = new TimeHelper();

	public Phase() {
		super("Phase", -4135069, ModuleManager.Category.PLAYER);
	}

	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					AxisAlignedBB boundingBox;
					if ((block != null) && (!(block instanceof BlockAir)) && ((boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null) &&
							(mc.thePlayer.boundingBox.intersectsWith(boundingBox))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void onUpdate(UpdateEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
			return;
		}
		if (event.state == Event.State.POST) {
			double multiplier = 0.3D;
			double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
			double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
			double xoffset = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
			double zoffset = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
			if ((mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isOnLadder()) && (!isInsideBlock())) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
				for (int i = 1; i < 11; i++) {
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.MAX_VALUE * i, mc.thePlayer.posZ, false));
				}
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - (BlockHelper.isOnLiquid() ? 9000.0D : 0.1D), mc.thePlayer.posZ, false));
				mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				mc.thePlayer.onGround = false;
				mc.thePlayer.setSprinting(true);
				mc.thePlayer.motionY = 0.0F;
				if (mc.gameSettings.keyBindForward.isKeyDown() && Axis.getAxis().getModuleManager().getModuleByName("Sprint").isEnabled()) {
					mc.thePlayer.setSprinting(true);
				}
			}
			if (isInsideBlock()) {
				if (isInsideFence()) {
					multiplier = 0.3D;
					mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
					mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
					x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
					z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
				}
				mc.thePlayer.noClip = true;
				if (mc.gameSettings.keyBindForward.isKeyDown() && Axis.getAxis().getModuleManager().getModuleByName("Sprint").isEnabled()) {
					mc.thePlayer.setSprinting(true);
				}
			}
		}
	}

	public void onPush(PushOutOfBlocksEvent event) {
		event.setCancelled(true);
	}

	public void onBoundingBox(BoundingBoxEvent event) {
		mc.thePlayer.noClip = true;
		if (event.pos.getY() > mc.thePlayer.posY + (isInsideBlock() ? 0 : 1)) {
			event.boundingBox = null;
		}
		if ((mc.thePlayer.isCollidedHorizontally) && (event.pos.getY() > mc.thePlayer.boundingBox.minY - 0.4D)) {
			event.boundingBox = null;
		}
		if (isInsideBlock()) {
			if ((((int) (event.pos.getX()) == (int) (mc.thePlayer.posX)) || ((int) (event.pos.getZ()) == (int) (mc.thePlayer.posZ) || (int) (event.pos.getX() - 1) == (int) (mc.thePlayer.posX))
					|| ((int) (event.pos.getZ() - 1) == (int) (mc.thePlayer.posZ) || (int) (event.pos.getX() + 1) == (int) (mc.thePlayer.posX)) || ((int) (event.pos.getZ() + 1) == (int) (mc.thePlayer.posZ)))
					&& (!((int) (event.pos.getY()) == (int) (mc.thePlayer.posY - 1)))) {
				event.boundingBox = null;
			}
		}
	}

	public static boolean isInsideFence() {
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					AxisAlignedBB boundingBox;
					if ((block != null) && ((block instanceof BlockFence) || (block instanceof BlockPane))
							&& ((boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null) &&
							(mc.thePlayer.boundingBox.intersectsWith(boundingBox))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
