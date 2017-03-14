package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.Event;
import axis.event.events.BlockDataEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.EntityHelper;
import axis.util.EntityUtils;
import axis.util.Logger;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class ScaffoldWalk
		extends Module {
	private BlockDataEvent blockData = null;
	private TimeHelper time = new TimeHelper();
	private int delay;

	public ScaffoldWalk() {
		super("ScaffoldWalk", 9623002, Category.MOVEMENT);
		setDisplayName("Scaffold");
	}

	public void onEnabled() {
		super.onEnabled();
		this.blockData = null;
	}

	public void onUpdata(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			this.blockData = null;
			if ((mc.thePlayer.getHeldItem() != null) && (!mc.thePlayer.isSneaking()) && ((mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))) {
				BlockPos player = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
				if (mc.theWorld.getBlockState(player).getBlock() == Blocks.air) {
					this.blockData = getBlockData(player);
					if (this.blockData != null) {
						float[] values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
						event.yaw = values[0];
						event.pitch = values[1];
					} else {
						player = new BlockPos(mc.thePlayer.posX - 1.0D, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
						this.blockData = getBlockData(player);
						if (this.blockData != null) {
							float[] values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);

							event.yaw = values[0];
							event.pitch = values[1];
						} else {
							player = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ - 1.0D);
							this.blockData = getBlockData(player);
							if (this.blockData != null) {
								float[] values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);

								event.yaw = values[0];
								event.pitch = values[1];
							} else {
								player = new BlockPos(mc.thePlayer.posX - 1.0D, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);

								this.blockData = getBlockData(player);
								if (this.blockData != null) {
									float[] values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);

									event.yaw = values[0];
									event.pitch = values[1];
								}
							}
						}
					}
				}
			}
		}
		if (event.state == Event.State.POST) {
			if (this.blockData == null) {
				// return;
			}
			if (this.time.hasReached(this.delay)) {
				if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position))) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				}
				this.delay = 90;
				if ((mc.gameSettings.keyBindJump.pressed) && (!mc.thePlayer.onGround)) {
					mc.thePlayer.setSprinting(false);
					mc.thePlayer.motionX *= 0.5D;
					mc.thePlayer.motionY *= 0.5D;
					mc.thePlayer.jump();
					this.delay = 100;
				}
			}
		}
	}

	public void onPakcet(PacketSentEvent event) {
		if ((event.getPacket() instanceof C03PacketPlayer)) {
			C03PacketPlayer player1 = (C03PacketPlayer) event.getPacket();
			if (this.blockData == null) {
				return;
			}
			float[] values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
			player1.yaw = values[0];
			player1.pitch = values[1];
		} else {
			event.setCancelled(false);
		}
	}

	public BlockDataEvent getBlockData(BlockPos pos) {
		return mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, 0, 1), EnumFacing.NORTH)
				: mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, 0, -1), EnumFacing.SOUTH)
						: mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(1, 0, 0), EnumFacing.WEST)
								: mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(-1, 0, 0), EnumFacing.EAST)
										: mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, -1, 0), EnumFacing.UP) : null;
	}
}