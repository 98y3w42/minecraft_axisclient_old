package axis.module.modules.movement;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.command.Command;
import axis.event.events.BlockBreakingEvent;
import axis.event.events.RayTraceEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.util.moveutil;
import axis.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Teleport
		extends Module {

	private double x;
	private double y;
	private double z;

	public Teleport() {
		super("Teleport", 0, ModuleManager.Category.MOVEMENT);
	}

	Minecraft mc = Minecraft.getMinecraft();
	public static BlockPos toPos;

	public void onEvent(BlockBreakingEvent event) {
		if (event.getState() == BlockBreakingEvent.EnumBlock.CLICK) {
			toPos = event.getPos();
			event.setCancelled(true);
		}
	}

	public void update(UpdateEvent e) {
		if (toPos == null) {
			return;
		}
		if (!Mouse.isButtonDown(1)) {
			return;
		}
		try {
			double x = this.mc.thePlayer.posX;
			double y = this.mc.thePlayer.posY;
			double z = this.mc.thePlayer.posZ;

			double toX = toPos.getX() + 0.5D;
			double toY = toPos.getY();
			double toZ = toPos.getZ() + 0.5D;
			Block getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ));
			while (!getBlockAtPos.getMaterial().equals(Material.air)) {
				toY += 1.0D;
				getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ));
			}
			double allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);

			int loops = 0;
			boolean error = false;

			String goUpOrDown = "Neutral";
			if (y - toY < 0.0D) {
				goUpOrDown = "Up";
			}
			if (y - toY > 0.0D) {
				goUpOrDown = "Down";
			}
			while (allDifference > 0.0D) {
				allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);

				Block gbap = BlockHelper.getBlockAtPos(new BlockPos(x, y + 0.1D, z));
				Double distance = (double) MathHelper.sqrt_double(mc.thePlayer.getDistanceSq(this.toPos));
				if ((!gbap.isPassable(this.mc.theWorld, new BlockPos(x, y + 0.1D, z))) && (distance <= 30)) {
					error = true;
					break;
				}
				if (loops > 10000) {
					error = true;
					break;
				}
				double differenceX = x - toX;
				double differenceY = y - toY;
				double differenceZ = z - toZ;

				double differenceXZ = Math.abs(differenceX) + Math.abs(differenceZ);

				boolean handleYFirst = (goUpOrDown.equals("Up")) && (Math.abs(differenceY) > 0.0D);
				boolean handleYLast = (goUpOrDown.equals("Down")) && (Math.abs(differenceY) > 0.0D) && (differenceXZ == 0.0D);
				if ((handleYFirst) && (error == false)) {
					if (Math.abs(differenceY) > 0.2D) {
						y += 0.2D;
					} else {
						y += Math.abs(differenceY);
					}
				} else {
					if (differenceX < 0.0D) {
						if (Math.abs(differenceX) > 0.2D) {
							x += 0.2D;
						} else {
							x += Math.abs(differenceX);
						}
					}
					if (differenceX > 0.0D) {
						if (Math.abs(differenceX) > 0.2D) {
							x -= 0.2D;
						} else {
							x -= Math.abs(differenceX);
						}
					}
					if (differenceZ < 0.0D) {
						if (Math.abs(differenceZ) > 0.2D) {
							z += 0.2D;
						} else {
							z += Math.abs(differenceZ);
						}
					}
					if (differenceZ > 0.0D) {
						if (Math.abs(differenceZ) > 0.2D) {
							z -= 0.2D;
						} else {
							z -= Math.abs(differenceZ);
						}
					}
					if (handleYLast) {
						if (Math.abs(differenceY) > 0.2D) {
							y -= 0.2D;
						} else {
							y -= Math.abs(differenceY);
						}
					}
				}
				this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, this.mc.thePlayer.onGround));
				loops++;
			}

			if (error) {
				this.mc.thePlayer.playSound("random.levelup", 0.2F, 1.0F);
			} else {
				this.mc.thePlayer.setPosition(toX, toY, toZ);
				this.mc.thePlayer.playSound("mob.endermen.portal", 0.3F, 1.0F);
			}
		} catch (Exception localException) {
		}
	}

	public void onRender(Render3DEvent event) {
		checkSetupFBO();
		onRenderBlockOutline(event.getPartialTicks());
	}

	public void onRenderBlockOutline(float partialTicks) {
		if (toPos != null) {
			GL11.glDisable(2896);
			GL11.glDisable(3553);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(2929);
			GL11.glEnable(2848);
			GL11.glDepthMask(false);
			GL11.glLineWidth(0.3F);
			Double distance = (double) MathHelper.sqrt_double(mc.thePlayer.getDistanceSq(this.toPos));
			GL11.glColor4f(0.981F, 0.84F, 0.84F, 0.25F);
			double x = this.toPos.getX() - mc.getRenderManager().viewerPosX;
			double y = this.toPos.getY() - mc.getRenderManager().viewerPosY;
			double z = this.toPos.getZ() - mc.getRenderManager().viewerPosZ;
			AxisAlignedBB box = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
			RenderUtils.drawFilledBox(box);
			GL11.glColor4f(0.981F, 0.84F, 0.84F, 0.4F);
			RenderGlobal.func_181561_a(box);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDepthMask(true);
			GL11.glDisable(2848);
			GL11.glEnable(2929);
			GL11.glDisable(3042);
			GL11.glEnable(2896);
			GL11.glEnable(3553);
		}
	}

	public static void checkSetupFBO() {
		Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
		if (fbo != null) {
			if (fbo.depthBuffer > -1) {
				setupFBO(fbo);

				fbo.depthBuffer = -1;
			}
		}
	}

	public static void setupFBO(Framebuffer fbo) {
		EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);

		int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();

		EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);

		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);

		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	}

	public void onDisabled() {
		super.onDisabled();
		this.toPos = null;
	}

	public void rayTrace(RayTraceEvent e) {
		e.reach = 30.0D;
	}
}
