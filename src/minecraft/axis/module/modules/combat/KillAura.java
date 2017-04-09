package axis.module.modules.combat;

import java.util.ArrayList;
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
import axis.util.TimeHelper;
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
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

public class KillAura extends Module {

	public final Value<AuraMode> currentMode = new Value<>("killaura_mode", new Multi());
	private final Random random = new Random();
	private KillAura aura1 = this;
	private ArrayList array = new ArrayList<Integer>();
	private boolean test1 = false;
	private TimeHelper timer = new TimeHelper();
	private int kl = 0;

	public KillAura() {
		super("KillAura", 0xFFC6172B, ModuleManager.Category.COMBAT);
		String name = "";
		if (currentMode.getValue() instanceof Multi) {
			name = "Multi";
		} else if (currentMode.getValue() instanceof Switch) {
			name = "Switch";
		}
		this.setDisplayName(name + " Aura");
		setTag((String) values.getValue("type"));
		Axis.getAxis().getCommandManager().getContents().add(new Command("killaura", "<animals/players/mobs/mode>", new String[] { "ka" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("players")) {
					values.setValue("players", !(Boolean) values.getValue("players"));
					Logger.logChat("Kill Aura will " + ((Boolean) values.getValue("players") ? "now" : "no longer") + " aim at players.");
				} else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
					values.setValue("mobs", !(Boolean) values.getValue("mobs"));
					Logger.logChat("Kill Aura will " + ((Boolean) values.getValue("players") ? "now" : "no longer") + " aim at players.");
				} else if (message.split(" ")[1].equalsIgnoreCase("animals")) {
					values.setValue("animals", !(Boolean) values.getValue("animals"));
					Logger.logChat("Kill Aura will " + ((Boolean) values.getValue("players") ? "now" : "no longer") + " aim at animals.");
				} else if (message.split(" ")[1].equalsIgnoreCase("renderbox")) {
					values.setValue("renderbox", !(Boolean) values.getValue("renderbox"));
					Logger.logChat("RenderBox: " + (Boolean) values.getValue("renderbox"));
				} else if (message.split(" ")[1].equalsIgnoreCase("tick")) {
					values.setValue("tick", !(Boolean) values.getValue("tick"));
					Logger.logChat("Tick: " + (Boolean) values.getValue("tick"));
				} else if (message.split(" ")[1].equalsIgnoreCase("hurttime")) {
					values.setValue("hurttime", !(Boolean) values.getValue("hurttime"));
					Logger.logChat("HurtTime: " + (Boolean) values.getValue("hurttime"));
				} else if (message.split(" ")[1].equalsIgnoreCase("sameteam")) {
					values.setValue("sameteam", !(Boolean) values.getValue("sameteam"));
					Logger.logChat("SameTeam: " + (Boolean) values.getValue("sameteam"));
				} else if (message.split(" ")[1].equalsIgnoreCase("autoblock")) {
					values.setValue("autoblock", !(Boolean) values.getValue("autoblock"));
					Logger.logChat("AutoBlock: " + (Boolean) values.getValue("autoblock"));
				} else if (message.split(" ")[1].equalsIgnoreCase("type")) {
					if (message.split(" ")[2].equalsIgnoreCase("None")) {
						values.setValue("type", "None");
						Logger.logChat("Kill Aura will no longer bypass");
						setTag("None");
					} else if (message.split(" ")[2].equalsIgnoreCase("Anni")) {
						values.setValue("type", "Anni");
						Logger.logChat("Kill Aura will now bypass Anni NPC.");
						setTag("Anni");
					} else if (message.split(" ")[2].equalsIgnoreCase("MineZ")) {
						values.setValue("type", "MineZ");
						Logger.logChat("Kill Aura will now bypass MineZ NPC.");
						setTag("MineZ");
					} else if (message.split(" ")[2].equalsIgnoreCase("Hypixel")) {
						values.setValue("type", "Hypixel");
						Logger.logChat("Kill Aura will now bypass Hypixel NPC.");
						setTag("Hypixel");
					}
				} else if (message.split(" ")[1].equalsIgnoreCase("range")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						values.setValue("range", values.getDefaultValue("range"));
					} else {
						values.setValue("range", Double.parseDouble(message.split(" ")[2]));
					}

					if ((Double) values.getValue("range") > 6.0D) {
						values.setValue("range", 6.0);
					} else if ((Double) values.getValue("range") < 1.0D) {
						values.setValue("range", 1.0);
					}

					Logger.logChat("Kill Aura Reach set to: " + values.getValue("range"));
				} else if (message.split(" ")[1].equalsIgnoreCase("delay")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						values.setValue("delay", values.getDefaultValue("delay"));
					} else {
						values.setValue("delay", Long.parseLong(message.split(" ")[2]));
					}

					if ((Long) values.getValue("delay") > 1000) {
						values.setValue("delay", 1000);
					} else if ((Long) values.getValue("delay") < 0) {
						values.setValue("delay", 0);
					}

					Logger.logChat("Kill Aura Delay set to: " + values.getValue("delay"));
				} else if (message.split(" ")[1].equalsIgnoreCase("fov")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						values.setValue("fov", values.getDefaultValue("fov"));
					} else {
						values.setValue("fov", Integer.parseInt(message.split(" ")[2]));
					}

					if ((Integer) values.getValue("fov") > 360) {
						values.setValue("fov", 360);
					} else if ((Integer) values.getValue("fov") < 1) {
						values.setValue("fov", 1);
					}

					Logger.logChat("Kill Aura Fov set to: " + (Integer) values.getValue("fov"));

				} else if (message.split(" ")[1].equalsIgnoreCase("target")) {
					if (message.split(" ")[2].equalsIgnoreCase("-d")) {
						values.setValue("maxtarget", values.getDefaultValue("maxtarget"));
					} else {
						values.setValue("maxtarget", Integer.parseInt(message.split(" ")[2]));
					}

					if ((Integer) values.getValue("maxtarget") > 50) {
						values.setValue("maxtarget", 50);
					} else if ((Integer) values.getValue("maxtarget") < 0) {
						values.setValue("maxtarget", 0);
					}

					Logger.logChat("Kill Aura Max Target set to: " + (Integer) values.getValue("maxtarget"));
				} else if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("Multi")) {
						currentMode.setValue(new Multi());
						Logger.logChat("Kill Aura mode set to Multi");
						setDisplayName("Multi Aura");
					} else if (message.split(" ")[2].equalsIgnoreCase("Switch")) {
						currentMode.setValue(new Switch());
						Logger.logChat("Kill Aura mode set to Switch");
						setDisplayName("Switch Aura");
					}
				} else if (message.split(" ")[1].equalsIgnoreCase("getValue")) {
					Logger.logChat("Type: " + (String) values.getValue("type"));
					Logger.logChat("MaxTarget: " + (Integer) values.getValue("maxtarget"));
					Logger.logChat("Delay: " + values.getValue("delay"));
					Logger.logChat("Reach: " + values.getValue("range"));
					Logger.logChat("RenderBox: " + (Boolean) values.getValue("renderbox"));
					Logger.logChat("Tick: " + (Boolean) values.getValue("tick"));
					Logger.logChat("HurtTime: " + (Boolean) values.getValue("hurttime"));
					Logger.logChat("SameTeam: " + (Boolean) values.getValue("sameteam"));
				} else {
					Logger.logChat("Option not valid! Available options: animals, players, mobs, type, target, fov, mode, delay, getValue.");
				}
			}
		});
	}

	public void onValueSetup() {
		super.onValueSetup();
		Logger.logChat("test");
		values.addValue("delay", 144L);
		values.addValue("randomdelay", 5L);
		values.addValue("range", 4.8);
		values.addValue("maxtarget", 2);
		values.addValue("fov", 360);
		values.addValue("type", "None");
		values.addValue("lockview", false);
		values.addValue("players", true);
		values.addValue("mobs", true);
		values.addValue("animals", true);
		values.addValue("invisible", true);
		values.addValue("renderbox", true);
		values.addValue("tick", false);
		values.addValue("hurttime", false);
		values.addValue("sameteam", true);
		values.addValue("autoblock", false);
	}

	public void onEnabled() {
		super.onEnabled();
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			values.setValue("maxtarget", 3);
			values.setValue("hurttime", true);
			currentMode.setValue(new Multi());
			values.setValue("type", "Anni");
			setDisplayName("Multi Aura");
			setTag((String) values.getValue("type"));
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			values.setValue("maxtarget", 1);
			values.setValue("hurttime", false);
			values.setValue("tick", false);
			currentMode.setValue(new Multi());
			values.setValue("type", "Hypixel");
			setDisplayName("Multi Aura");
			setTag((String) values.getValue("type"));
		}
		if (mc.thePlayer != null) {
			if (values.getValue("type").equals("Hypixel")) {
				this.test1 = false;
				this.test();
				this.timer.reset();
				this.kl = 0;
			}
		}
	}

	public void onUpdate(UpdateEvent event) {
		if (values.getValue("type").equals("Hypixel")) {
			if (Multi.targets1size == 0 && Switch.pseudoTarget == null) {
				this.kl++;
				if (this.kl >= 700) {
					Logger.logChat("Reload");
					this.test();
					this.kl = 0;
				}
			} else {
				this.kl = 0;
			}
		}
		if ((Boolean) values.getValue("autoblock") && !mc.thePlayer.isBlocking() && ((Multi.targets1size > 0) || (Switch.pseudoTarget != null)) && (mc.thePlayer.getHeldItem() != null)
				&& (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
			mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 100);
		}
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
		return isValidEntity(entity, (Double) values.getValue("range"));
	}

	public boolean isValidEntity(EntityLivingBase entity, double range) {
		if (entity == null) {
			return false;
		}
		if (entity == mc.thePlayer) {
			return false;
		}
		if (EntityUtils.getAngle(EntityUtils.getEntityRotations(entity)) > (Integer) values.getValue("fov")) {
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
			if ((Boolean) values.getValue("tick")) {
				if (!entity.onGround) {
					float var10000 = (float) entity.ticksExisted;
					if (var10000 <= 120.0F * Timer.timerSpeed) {
						Logger.logChat("Tick");
						return false;
					}
				}
			}
			if ((Boolean) values.getValue("hurttime")) {
				if (!(this.currentMode.getValue() instanceof Multi) || Multi.targets1size >= 4) {
					if (entity.hurtTime >= 8) {
						return false;
					}
				}
			}
			if ((Boolean) values.getValue("sameteam")) {
				if (entity.getTeam() != null) {
					NetworkPlayerInfo networkplayerinfo = mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getGameProfile().getId());
					if (networkplayerinfo.getPlayerTeam().isSameTeam(entity.getTeam())) {
						return false;
					}
				}
			}
			if (((String) values.getValue("type")).equalsIgnoreCase("Anni")) {
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
			if (((String) values.getValue("type")).equalsIgnoreCase("MineZ")) {
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
			if (((String) values.getValue("type")).equalsIgnoreCase("Hypixel")) {
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
					return false;
				}
				if (!this.array.contains(entity.getName() + ", " + entity.getEntityId())) {
					return false;
				}
			}

			return (Boolean) values.getValue("players");

		} else if (entity instanceof IAnimals && !(entity instanceof IMob)) {
			if (entity instanceof EntityHorse) {
				EntityHorse horse = (EntityHorse) entity;
				return (Boolean) values.getValue("animals") && horse.riddenByEntity != mc.thePlayer;
			}
			return (Boolean) values.getValue("animals");
		} else if (entity instanceof IMob) {
			return (Boolean) values.getValue("mobs");
		}

		return false;
	}

	public boolean isValidEntity2(EntityLivingBase entity) {
		return isValidEntity2(entity, (Double) values.getValue("range"));
	}

	public boolean isValidEntity2(EntityLivingBase entity, double range) {
		if (entity == null)
			return false;
		if (entity == mc.thePlayer)
			return false;
		if (EntityUtils.getAngle(EntityUtils.getEntityRotations(entity)) > (Integer) values.getValue("fov"))
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
		return (Long) values.getValue("delay") + (random.nextInt(10) - 5);
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

	private void test() {
		for (Object o : this.mc.theWorld.loadedEntityList) {
			if ((o instanceof EntityPlayer) && (o != this.mc.thePlayer)) {
				EntityPlayer entity = (EntityPlayer) o;
				boolean dis = (Math.abs(entity.posX - mc.thePlayer.posX) <= 2.0F) && (Math.abs(entity.posZ - mc.thePlayer.posZ) <= 2.0F) && (mc.thePlayer.posY + 6 <= entity.posY);
				boolean dis1 = (Math.abs(entity.posX - mc.thePlayer.posX) <= 2.5F) && (Math.abs(entity.posZ - mc.thePlayer.posZ) <= 2.5F) && (mc.thePlayer.posY + 6 <= entity.posY);
				if (!this.test1) {
					if (this.isNameValid(entity) && (!dis) && (entity.getEntityId() != -1)) {
						this.array.add(entity.getName() + ", " + entity.getEntityId());
						Logger.logChat("Add: " + entity.getDisplayName().getFormattedText() + ", §f" + entity.getEntityId());
					}
				} else {
					if ((this.isNameValid(entity)) && (mc.thePlayer.getDistanceToEntity(entity) <= 3.0F) && (!this.array.contains(entity.getEntityId()) && (dis1))) {
						this.array.add(entity.getName() + ", " + entity.getEntityId());
						Logger.logChat("Add: " + entity.getDisplayName().getFormattedText() + ", §f" + entity.getEntityId());
					}
				}
			}
		}
		this.test1 = true;
	}
}
