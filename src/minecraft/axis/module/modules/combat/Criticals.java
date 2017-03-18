package axis.module.modules.combat;

import axis.Axis;
import axis.command.Command;
import axis.event.events.AttackEvent;
import axis.event.events.PacketSentEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.Speed;
import axis.util.BlockHelper;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class Criticals extends Module {

	private final Value<String> currentMode = new Value("criticals_mode", "Packet");
	private float fallDist;
	private Speed speed;

	public Criticals() {
		super("Criticals", -351136, ModuleManager.Category.COMBAT);
		setTag(currentMode.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("criticals", "<mode>", new String[] { "cc" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("MiniJump")) {
						currentMode.setValue("MiniJump");
					} else if (message.split(" ")[2].equalsIgnoreCase("Float")) {
						currentMode.setValue("Float");
					} else if (message.split(" ")[2].equalsIgnoreCase("Packet")) {
						currentMode.setValue("Packet");
					}
					Logger.logChat("Criticals Mode is " + currentMode.getValue());
					setTag(currentMode.getValue());
				}
			}
		});
	}

	public void onAttack(AttackEvent event) {
		if (!(event.getEntity() instanceof EntityLivingBase)) {
			return;
		}

		double posY = this.mc.thePlayer.posY + Speed.yOffset;

		if (currentMode.getValue().equals("MiniJump")) {
			if (BlockHelper.isOnLiquid() || BlockHelper.isInLiquid(mc.thePlayer) || !mc.thePlayer.isCollidedVertically)
				return;
			EntityLivingBase living = (EntityLivingBase) event.getEntity();
			if (living.hurtTime < 5) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.05, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.012511, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
		}

		if (currentMode.getValue().equals("Packet") && mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isPressed() && !AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			crit();
			EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			float sharpParticles = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
			mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, new Vec3(entity.getPositionVector().xCoord, entity.getPositionVector().yCoord, entity.getPositionVector().zCoord)));
			this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, posY, this.mc.thePlayer.posZ, false));
		}
	}

	public void onPacketSent(PacketSentEvent event) {
		if (!this.currentMode.getValue().equals("Float")) {
			return;
		}
		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer player = (C03PacketPlayer) event.getPacket();
			if (isSafe())
				fallDist += mc.thePlayer.fallDistance;
			if (!isSafe() && fallDist >= 3.0F) {
				player.setOnGround(true);
				fallDist = 0.0F;
				mc.thePlayer.fallDistance = 0.0F;

				if (mc.thePlayer.onGround && !BlockHelper.isOnLiquid() && !BlockHelper.isInLiquid(mc.thePlayer)) {
					mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.01, mc.thePlayer.posZ, false));
					mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
					fallDist += 1.01F;
				}
			} else if (fallDist > 0.0F) {
				player.setOnGround(false);
			}
		}
	}

	public float getFallDistance() {
		return fallDist;
	}

	public void setFallDistance(float fallDist) {
		this.fallDist = fallDist;
	}

	private boolean isSafe() {
		return !mc.thePlayer.isInWater() && !mc.thePlayer.isInsideOfMaterial(Material.lava) && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null;
	}

	public void onEnabled() {
		super.onEnabled();
		setTag(currentMode.getValue());
		if (mc.thePlayer != null && this.currentMode.getValue().equals("Float")) {
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.01, mc.thePlayer.posZ, false));
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			fallDist += 1.01F;
		}
	}

	public void crit() {
		if (!mc.thePlayer.isCollidedVertically) {
			return;
		}
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625D, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
	}

	public void onDisabled() {
		super.onDisabled();
		fallDist = 0.0F;
	}
}
