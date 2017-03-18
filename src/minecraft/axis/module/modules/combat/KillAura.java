package axis.module.modules.combat;

import java.util.Collection;
import java.util.Random;

import axis.Axis;
import axis.command.Command;
import axis.event.events.AttackEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.module.modules.combat.killaura.AuraMode;
import axis.module.modules.combat.killaura.modes.Multi;
import axis.module.modules.combat.killaura.modes.Switch;
import axis.module.modules.exploits.AutoSetting;
import axis.util.EntityUtils;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

public class KillAura extends Module {

	public final Value<AuraMode> currentMode = new Value<>("killaura_mode", new Multi(this));
	public final Value<Long> delay = new Value<>("killaura_delay", 80L);
	public final Value<Long> randomDelay = new Value<>("killaura_random_delay", 5L);
	public final Value<Double> range = new Value<>("killaura_range", 4.8D);
	public final Value<Integer> maxTarget = new Value<>("killaura_maxtarget", 3);
	public final Value<Integer> fov = new Value<>("killaura_fov", 360);
	private final Value<String> type = new Value("killaura_type", String.valueOf("Anni"));
	public final Value<Boolean> silent = new Value<>("killaura_silent", false);
	private final Value<Boolean> players = new Value<>("killaura_players", true);
	private final Value<Boolean> mobs = new Value<>("killaura_mobs", false);
	private final Value<Boolean> animals = new Value<>("killaura_animals", false);
	private final Value<Boolean> invisible = new Value<>("killaura_invisible", true);
	private final Value<Boolean> autoblock = new Value<>("killaura_autoblcok", false);
	public final Value<Boolean> renderbox = new Value<>("killaura_renderbox", true);
	private final Value<Boolean> tick = new Value<>("killaura_tick", false);
	private final Value<Boolean> hurttime = new Value<>("killaura_hurttime", false);
	private final Value<Boolean> sameteam = new Value<>("killaura_sameteam", true);
	private final Random random = new Random();
	private KillAura aura1 = this;

	public KillAura() {
		super("KillAura", 0xFFC6172B, ModuleManager.Category.COMBAT);
		String name = "";
		if (currentMode.getValue() instanceof Multi) {
			name = "Multi";
		} else if (currentMode.getValue() instanceof Switch) {
			name = "Switch";
		}
		this.setDisplayName(name + " Aura");
		setTag(KillAura.this.type.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("killaura", "<animals/players/mobs/mode>", new String[] { "ka" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("players")) {
					KillAura.this.players.setValue(Boolean.valueOf(!((Boolean) KillAura.this.players.getValue()).booleanValue()));
					Logger.logChat("Kill Aura will " + (((Boolean) KillAura.this.players.getValue()).booleanValue() ? "now" : "no longer") + " aim at players.");
				} else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
					KillAura.this.mobs.setValue(Boolean.valueOf(!((Boolean) KillAura.this.mobs.getValue()).booleanValue()));
					Logger.logChat("Kill Aura will " + (((Boolean) KillAura.this.mobs.getValue()).booleanValue() ? "now" : "no longer") + " aim at players.");
				} else if (message.split(" ")[1].equalsIgnoreCase("animals")) {
					KillAura.this.animals.setValue(Boolean.valueOf(!((Boolean) KillAura.this.animals.getValue()).booleanValue()));
					Logger.logChat("Kill Aura will " + (((Boolean) KillAura.this.animals.getValue()).booleanValue() ? "now" : "no longer") + " aim at animals.");
				} else if (message.split(" ")[1].equalsIgnoreCase("renderbox")) {
					KillAura.this.renderbox.setValue(Boolean.valueOf(!((Boolean) KillAura.this.renderbox.getValue()).booleanValue()));
					Logger.logChat("RenderBox: " + KillAura.this.renderbox.getValue().booleanValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("tick")) {
					KillAura.this.tick.setValue(Boolean.valueOf(!((Boolean) KillAura.this.tick.getValue()).booleanValue()));
					Logger.logChat("Tick: " + KillAura.this.tick.getValue().booleanValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("hurttime")) {
					KillAura.this.hurttime.setValue(Boolean.valueOf(!((Boolean) KillAura.this.hurttime.getValue()).booleanValue()));
					Logger.logChat("HurtTime: " + KillAura.this.hurttime.getValue().booleanValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("sameteam")) {
					KillAura.this.sameteam.setValue(Boolean.valueOf(!((Boolean) KillAura.this.sameteam.getValue()).booleanValue()));
					Logger.logChat("SameTeam: " + KillAura.this.sameteam.getValue().booleanValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("type")) {
					if (message.split(" ")[2].equalsIgnoreCase("None")) {
						KillAura.this.type.setValue("None");
						Logger.logChat("Kill Aura will no longer bypass");
						setTag("None");
					} else if (message.split(" ")[2].equalsIgnoreCase("Anni")) {
						KillAura.this.type.setValue("Anni");
						Logger.logChat("Kill Aura will now bypass Anni NPC.");
						setTag("Anni");
					} else if (message.split(" ")[2].equalsIgnoreCase("MineZ")) {
						KillAura.this.type.setValue("MineZ");
						Logger.logChat("Kill Aura will now bypass MineZ NPC.");
						setTag("MineZ");
					} else if (message.split(" ")[2].equalsIgnoreCase("Hypixel")) {
						KillAura.this.type.setValue("Hypixel");
						Logger.logChat("Kill Aura will now bypass Hypixel NPC.");
						setTag("Hypixel");
					}
				} else if (message.split(" ")[1].equalsIgnoreCase("range")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						KillAura.this.range.setValue((Double) KillAura.this.range.getDefaultValue());
					} else {
						KillAura.this.range.setValue(Double.valueOf(Double.parseDouble(message.split(" ")[2])));
					}

					if (((Double) KillAura.this.range.getValue()).doubleValue() > 6.0D) {
						KillAura.this.range.setValue(Double.valueOf(6.0D));
					} else if (((Double) KillAura.this.range.getValue()).doubleValue() < 1.0D) {
						KillAura.this.range.setValue(Double.valueOf(1.0D));
					}

					Logger.logChat("Kill Aura Reach set to: " + KillAura.this.range.getValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("delay")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						KillAura.this.delay.setValue((Long) KillAura.this.delay.getDefaultValue());
					} else {
						KillAura.this.delay.setValue(Long.valueOf(Long.parseLong(message.split(" ")[2])));
					}

					if (((Long) KillAura.this.delay.getValue()).doubleValue() > 1000) {
						KillAura.this.delay.setValue(Long.valueOf(1000));
					} else if (((Long) KillAura.this.delay.getValue()).doubleValue() < 0) {
						KillAura.this.delay.setValue(Long.valueOf(0));
					}

					Logger.logChat("Kill Aura Delay set to: " + KillAura.this.delay.getValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("fov")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						KillAura.this.fov.setValue(KillAura.this.fov.getDefaultValue());
					} else {
						KillAura.this.fov.setValue(Integer.valueOf(Integer.parseInt(message.split(" ")[2])));
					}

					if (KillAura.this.fov.getValue() > 360) {
						KillAura.this.fov.setValue(360);
					} else if ((KillAura.this.fov.getValue()).doubleValue() < 1) {
						KillAura.this.fov.setValue(1);
					}

					Logger.logChat("Kill Aura Fov set to: " + KillAura.this.fov.getValue());

				} else if (message.split(" ")[1].equalsIgnoreCase("target")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						KillAura.this.maxTarget.setValue((Integer) KillAura.this.maxTarget.getDefaultValue());
					} else {
						KillAura.this.maxTarget.setValue(Integer.valueOf(Integer.parseInt(message.split(" ")[2])));
					}

					if (((Integer) KillAura.this.maxTarget.getValue()).intValue() > 50) {
						KillAura.this.maxTarget.setValue(Integer.valueOf(50));
					} else if (((Integer) KillAura.this.maxTarget.getValue()).intValue() < 0) {
						KillAura.this.maxTarget.setValue(Integer.valueOf(0));
					}

					Logger.logChat("Kill Aura Max Target set to: " + KillAura.this.maxTarget.getValue());
				} else if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("Multi")) {
						currentMode.setValue(new Multi(aura1));
						Logger.logChat("Kill Aura mode set to Multi");
						setDisplayName("Multi Aura");
					} else if (message.split(" ")[2].equalsIgnoreCase("Switch")) {
						currentMode.setValue(new Switch(aura1));
						Logger.logChat("Kill Aura mode set to Switch");
						setDisplayName("Switch Aura");
					}
				} else if (message.split(" ")[1].equalsIgnoreCase("getValue")) {
					Logger.logChat("Type: " + KillAura.this.type.getValue());
					Logger.logChat("MaxTarget: " + KillAura.this.maxTarget.getValue());
					Logger.logChat("Delay: " + KillAura.this.delay.getValue());
					Logger.logChat("Reach: " + KillAura.this.range.getValue());
					Logger.logChat("RenderBox: " + KillAura.this.renderbox.getValue().booleanValue());
					Logger.logChat("Tick: " + KillAura.this.tick.getValue().booleanValue());
					Logger.logChat("HurtTime: " + KillAura.this.hurttime.getValue().booleanValue());
					Logger.logChat("SameTeam: " + KillAura.this.sameteam.getValue().booleanValue());
					setTag("" + KillAura.this.type.getValue());
				} else {
					Logger.logChat("Option not valid! Available options: animals, players, mobs, type, target, fov, mode, delay, getValue.");
				}
			}
		});
	}

	public void onEnabled() {
		super.onEnabled();
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			this.maxTarget.setValue(Integer.valueOf(2));
			this.delay.setValue(200L);
			this.tick.setValue(false);
			this.hurttime.setValue(true);
			this.sameteam.setValue(true);
			currentMode.setValue(new Multi(aura1));
			KillAura.this.type.setValue("Anni");
			setDisplayName("Multi Aura");
			setTag("Anni");
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			this.maxTarget.setValue(Integer.valueOf(1));
			this.delay.setValue(144L);
			this.hurttime.setValue(false);
			this.tick.setValue(true);
			currentMode.setValue(new Multi(aura1));
			KillAura.this.type.setValue("Hypixel");
			setDisplayName("Multi Aura");
			this.setTag("Hypixel");
		}
	}

	public void onUpdate(UpdateEvent event) {
		currentMode.getValue().onUpdate(event);
	}

	public void onPacketSent(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
			currentMode.getValue().onMotionPacket(packet);
		}
	}

	public void onRender(Render3DEvent event) {
		currentMode.getValue().onRender(event);
	}

	public void onAttack(Entity entity) {
		ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
		int original = mc.thePlayer.inventory.currentItem;
		int itemb4 = mc.thePlayer.inventory.currentItem;
		boolean b4sprinting = mc.thePlayer.isSprinting();
		boolean wasSneaking = mc.thePlayer.isSneaking();
		boolean wasBlocking = currentItem != null && currentItem.getItem().getItemUseAction(currentItem) == EnumAction.BLOCK && mc.thePlayer.isBlocking();

		if (wasSneaking) {
			mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}

		if (wasBlocking) {
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}

		int oldDamage = 0;
		if (mc.thePlayer.getCurrentEquippedItem() != null) {
			oldDamage = mc.thePlayer.getCurrentEquippedItem().getItemDamage();
		}

		this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));

		AttackEvent event = new AttackEvent(entity);
		event.call();

		Timer.timerSpeed = 1.0F;
		mc.thePlayer.swingItem();
		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

		if (mc.thePlayer.getCurrentEquippedItem() != null) {
			mc.thePlayer.getCurrentEquippedItem().setItemDamage(oldDamage);
		}

		if (this.autoblock.getValue() && !mc.thePlayer.isBlocking()) {
		}

		if (b4sprinting) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		}

		if (wasSneaking) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}

		if (wasBlocking) {
			mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		}
	}

	public boolean isValidEntity(EntityLivingBase entity) {
		return isValidEntity(entity, range.getValue());
	}

	public boolean isValidEntity(EntityLivingBase entity, double range) {
		if (entity == null) {
			return false;
		}
		if (entity == mc.thePlayer) {
			return false;
		}
		if (EntityUtils.getAngle(EntityUtils.getEntityRotations(entity)) > fov.getValue()) {
			return false;
		}
		if (!entity.isEntityAlive()) {
			return false;
		}
		if (mc.thePlayer.getDistanceToEntity(entity) > range) {
			return false;
		}
		if (entity instanceof EntityPlayer) {
			if (Axis.getAxis().getFriendManager().isFriend(entity.getName())) {
				return false;
			}
			if (entity.getDisplayName() == mc.thePlayer.getDisplayName()) {
				return false;
			}
			if (Axis.getAxis().getFriendManager().isFriend(entity.getName())) {
				return false;
			}
			if (this.tick.getValue().booleanValue()) {
				if (!entity.onGround) {
					float var10000 = (float) entity.ticksExisted;
					if (var10000 <= 120.0F * Timer.timerSpeed) {
						Logger.logChat("Tick");
						return false;
					}
				}
			}
			if (this.hurttime.getValue().booleanValue()) {
				if (!(this.currentMode.getValue() instanceof Multi) || Multi.targets1size >= 3) {
					if (entity.hurtTime >= 8) {
						return false;
					}
				}
			}
			if (this.sameteam.getValue().booleanValue()) {
				if (entity.getTeam() != null) {
					NetworkPlayerInfo networkplayerinfo = mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getGameProfile().getId());
					if (networkplayerinfo.getPlayerTeam().isSameTeam(entity.getTeam())) {
						return false;
					}
				}
			}
			if (type.getValue().equalsIgnoreCase("Anni")) {
				if (!isNameValid((EntityPlayer) entity)) {
					return false;
				}
				if (((EntityLivingBase) entity).getHealth() <= 0.0F) {
					return false;
				}
				if (entity.getEntityId() >= 1070000000) {
					return false;
				}
				if (entity.getEntityId() == -1) {
					return false;
				}
				if (entity.getTeam() == null) {
					return false;
				}
				if (!isIDValid((EntityPlayer) (entity))) {
					Logger.logChat("ID");
					return false;
				}
			}
			if (type.getValue().equalsIgnoreCase("MineZ")) {
				int armorAir = 0;
				int offset;
				for (offset = 3; offset >= 0; --offset) {
					ItemStack xPos = ((EntityPlayer) entity).inventory.armorInventory[offset];
					if (xPos == null) {
						armorAir += 1;
					}
				}

				int inventoryAir = 0;
				int offset1;
				for (offset1 = 35; offset1 >= 0; --offset1) {
					ItemStack xPos = ((EntityPlayer) entity).inventory.getStackInSlot(offset1);
					if (xPos == null) {
						inventoryAir += 1;
					}
				}

				if (inventoryAir >= 36 && armorAir >= 4) {
					return false;
				}
			}
			if (type.getValue().equalsIgnoreCase("Hypixel")) {
				if (!isNameValid((EntityPlayer) entity)) {
					return false;
				}
				if (((EntityLivingBase) entity).getHealth() <= 0.0F) {
					return false;
				}
				if (entity.getEntityId() >= 1070000000) {
					return false;
				}
				if (entity.getEntityId() == -1) {
					return false;
				}
				if (!isIDValid((EntityPlayer) (entity))) {
					Logger.logChat("ID");
					return false;
				}
			}

			return players.getValue();

		} else if (entity instanceof IAnimals && !(entity instanceof IMob)) {
			if (entity instanceof EntityHorse) {
				EntityHorse horse = (EntityHorse) entity;
				return animals.getValue() && horse.riddenByEntity != mc.thePlayer;
			}
			return animals.getValue();
		} else if (entity instanceof IMob) {
			return mobs.getValue();
		}

		return false;
	}

	public boolean isValidEntity2(EntityLivingBase entity) {
		return isValidEntity2(entity, range.getValue());
	}

	public boolean isValidEntity2(EntityLivingBase entity, double range) {
		if (entity == null)
			return false;
		if (entity == mc.thePlayer)
			return false;
		if (EntityUtils.getAngle(EntityUtils.getEntityRotations(entity)) > fov.getValue())
			return false;
		if (mc.thePlayer.getDistanceToEntity(entity) > range)
			return true;
		if (!entity.isEntityAlive() && entity.deathTime < 5) {
			Logger.logChat("" + (entity.deathTime > 5));
			return true;
		}
		return false;
	}

	public Long getDelay() {
		return delay.getValue();
	}

	private boolean isNameValid(EntityPlayer player) {
		Collection<NetworkPlayerInfo> playerInfos = this.mc.thePlayer.sendQueue.getPlayerInfoMap();
		for (NetworkPlayerInfo info : playerInfos) {
			if (info.getGameProfile().getName().matches(player.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isIDValid(EntityPlayer player) {
		Collection<NetworkPlayerInfo> playerInfos = this.mc.thePlayer.sendQueue.getPlayerInfoMap();
		for (NetworkPlayerInfo info : playerInfos) {
			if (info.getGameProfile().getId().equals(player.getUUID(player.getGameProfile()))) {
				return true;
			}
		}
		return false;
	}
}
