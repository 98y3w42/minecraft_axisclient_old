package axis.module.modules.movement;

import java.util.List;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import axis.Axis;
import axis.command.Command;
import axis.event.Event;
import axis.event.events.BlockDataEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.event.events.WalkingEvent;
import axis.management.managers.CommandManager;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.EntityHelper;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.util.TimeHelper;
import axis.value.Value;

public class ScaffoldWalk
		extends Module {
	private BlockDataEvent blockData = null;
	private TimeHelper time = new TimeHelper();
	private int delay;

	public ScaffoldWalk() {
		super("ScaffoldWalk", 9623002, ModuleManager.Category.MOVEMENT);
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
			if (this.time.hasReached(this.delay)) {
				if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position))) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				}
				this.delay = 90;
				if ((mc.gameSettings.keyBindJump.pressed) && (!mc.thePlayer.onGround)) {
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
		return mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, -1, 0), EnumFacing.UP)
				: mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(-1, 0, 0), EnumFacing.EAST)
						: mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(1, 0, 0), EnumFacing.WEST)
								: mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, 0, -1), EnumFacing.SOUTH)
										: mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air ? new BlockDataEvent(pos.add(0, 0, 1), EnumFacing.NORTH) : null;
	}

	public void onWalking(WalkingEvent event) {
		event.setSafeWalk(true);
	}
}