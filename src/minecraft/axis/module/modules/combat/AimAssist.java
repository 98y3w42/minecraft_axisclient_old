package axis.module.modules.combat;

import java.util.Optional;

import axis.Axis;
import axis.command.Command;
import axis.event.events.AttackEvent;
import axis.event.events.LeftClickEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.EntityUtils;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AimAssist extends Module {

	public final Value<Double> range = new Value<>("aimassist_range", 4.8D);
	public final Value<Integer> fov = new Value<>("aimassist_fov", 25);
	private final Value<String> type = new Value("aimassist_type", String.valueOf("anni"));
	private final Value<Boolean> players = new Value<>("aimassist_players", true);
	private final Value<Boolean> mobs = new Value<>("aimassist_mobs", true);
	private final Value<Boolean> animals = new Value<>("aimassist_animals", true);
	private final Value<Boolean> invisible = new Value<>("aimassist_invisible", true);

	public AimAssist() {
	      super("AimAssist", 12337730, Category.COMBAT);
	      Axis.getCommandManager().getContents().add(new Command("aimassist", "<animals/players/mobs>", new String[]{"aa"}) {
	          public void run(String message) {
	             if(message.split(" ")[1].equalsIgnoreCase("players")) {
	                AimAssist.this.players.setValue(Boolean.valueOf(!((Boolean)AimAssist.this.players.getValue()).booleanValue()));
	                Logger.logChat("Aim Assist will " + (((Boolean)AimAssist.this.players.getValue()).booleanValue()?"now":"no longer") + " aim at players.");
	             } else if(message.split(" ")[1].equalsIgnoreCase("mobs")) {
	                AimAssist.this.mobs.setValue(Boolean.valueOf(!((Boolean)AimAssist.this.mobs.getValue()).booleanValue()));
	                Logger.logChat("Aim Assist will " + (((Boolean)AimAssist.this.mobs.getValue()).booleanValue()?"now":"no longer") + " aim at players.");
	             } else if(message.split(" ")[1].equalsIgnoreCase("animals")) {
	                AimAssist.this.animals.setValue(Boolean.valueOf(!((Boolean)AimAssist.this.animals.getValue()).booleanValue()));
	                Logger.logChat("Aim Assist will " + (((Boolean)AimAssist.this.animals.getValue()).booleanValue()?"now":"no longer") + " aim at animals.");
	             } else if(message.split(" ")[1].equalsIgnoreCase("type")) {
	             	if (message.split(" ")[2].equalsIgnoreCase("none")) {
	             		AimAssist.this.type.setValue("none");
	             		Logger.logChat("Kill KillAura will no longer bypass");
	             	} else if (message.split(" ")[2].equalsIgnoreCase("anni")) {
	             		AimAssist.this.type.setValue("anni");
	             		Logger.logChat("Kill KillAura will now bypass Anni NPC.");
	             	} else if (message.split(" ")[2].equalsIgnoreCase("minez")) {
	             		AimAssist.this.type.setValue("MineZ");
	             		Logger.logChat("Kill KillAura will now bypass MineZ NPC.");
	             	}
	            } else if(message.split(" ")[1].equalsIgnoreCase("range")) {
	            	if(message.split(" ")[2].equalsIgnoreCase("-d")) {
	            		AimAssist.this.range.setValue((Double)AimAssist.this.range.getDefaultValue());
	            	} else {
	            		AimAssist.this.range.setValue(Double.valueOf(Double.parseDouble(message.split(" ")[2])));
	            	}

	            	if(((Double)AimAssist.this.range.getValue()).doubleValue() > 6.0D) {
	            		AimAssist.this.range.setValue(Double.valueOf(6.0D));
	            	} else if(((Double)AimAssist.this.range.getValue()).doubleValue() < 1.0D) {
	            		AimAssist.this.range.setValue(Double.valueOf(1.0D));
	            	}

	            	Logger.logChat("Aim Assist Reach set to: " + AimAssist.this.range.getValue());
	            } else if(message.split(" ")[1].equalsIgnoreCase("fov")) {
	            	if(message.split(" ")[2].equalsIgnoreCase("-d")) {
	            		AimAssist.this.fov.setValue(AimAssist.this.fov.getDefaultValue());
	            	} else {
	            		AimAssist.this.fov.setValue(Integer.valueOf(Integer.parseInt(message.split(" ")[2])));
	            	}

	            	if(AimAssist.this.fov.getValue() > 360) {
	            		AimAssist.this.fov.setValue(360);
	            	} else if((AimAssist.this.fov.getValue()).doubleValue() < 1) {
	            		AimAssist.this.fov.setValue(1);
	            	}

	            	Logger.logChat("Aim Assist Fov set to: " + AimAssist.this.fov.getValue());
	            } else {
	            	Logger.logChat("Option not valid! Available options: animals, players, mobs.");
	            }
	          }
	       });
	}

	private void onLeftClick(LeftClickEvent event) {
		Optional<Entity> firstValidEntity = mc.theWorld.loadedEntityList.stream()
			.filter(entity -> entity instanceof EntityLivingBase)
			.filter(entity -> isValidEntity((EntityLivingBase) entity))
			.filter(entity -> ((EntityLivingBase) entity).hurtTime == 0 && ((EntityLivingBase) entity).deathTime == 0)
			.sorted((entity1, entity2) -> {
                double entity1Distance = mc.thePlayer.getDistanceToEntity((Entity)entity1);
                double entity2Distance = mc.thePlayer.getDistanceToEntity((Entity)entity2);
                return entity1Distance > entity2Distance ? 1 : entity2Distance > entity1Distance ? -1 : 0;
			}).findFirst();

			if (firstValidEntity.isPresent()) {
				event.setCancelled(true);
				onAttack(firstValidEntity.get());
				firstValidEntity = null;
			}
	}

	public void onAttack(Entity entity) {
	      int original = mc.thePlayer.inventory.currentItem;
	          int itemb4 = mc.thePlayer.inventory.currentItem;
	          boolean b4sprinting = mc.thePlayer.isSprinting();

	         int oldDamage = 0;
	         if(mc.thePlayer.getCurrentEquippedItem() != null) {
	            oldDamage = mc.thePlayer.getCurrentEquippedItem().getItemDamage();
	         }

	         if(mc.thePlayer.isBlocking()) {
	           mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
	         }

	         AttackEvent event = new AttackEvent(entity);
	         event.call();

	         mc.thePlayer.swingItem();

	         mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

	         if(mc.thePlayer.getCurrentEquippedItem() != null) {
	            mc.thePlayer.getCurrentEquippedItem().setItemDamage(oldDamage);
	         }

	         if(b4sprinting) {
	            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
	         }


	}

	public boolean isValidEntity(EntityLivingBase entity) {
		return isValidEntity(entity, range.getValue());
	}

	public boolean isValidEntity(EntityLivingBase entity, double range) {
		if (entity == null)
			return false;
		if (entity == mc.thePlayer)
			return false;
		if (EntityUtils.getAngle(EntityUtils.getEntityRotations(entity)) > fov.getValue())
			return false;
		if (!entity.isEntityAlive())
			return false;
		if (mc.thePlayer.getDistanceToEntity(entity) > range)
			return false;
		if (!invisible.getValue() && entity.isInvisibleToPlayer(mc.thePlayer))
			return false;
		if (entity instanceof EntityPlayer) {
			if (entity.getDisplayName() == mc.thePlayer.getDisplayName())
				return false;
			if (type.getValue().equalsIgnoreCase("anni") && (entity.getTeam() == null || entity.getTeam().isSameTeam(mc.thePlayer.getTeam())))
				return false;
			if(type.getValue().equalsIgnoreCase("minez")) {

                int armorAir = 0;
                int offset;
                for(offset = 3; offset >= 0; --offset) {
                   ItemStack xPos = ((EntityPlayer) entity).inventory.armorInventory[offset];
                   if(xPos == null) {
                	   armorAir += 1;
                   }
                }

                int inventoryAir = 0;
                int offset1;
                for(offset1 = 35; offset1 >= 0; --offset1) {
                   ItemStack xPos = ((EntityPlayer) entity).inventory.getStackInSlot(offset1);
                   if(xPos == null) {
                	   inventoryAir += 1;
                   }
                }

                if (inventoryAir >= 36 && armorAir >= 4) {
                	return false;
                }

                return true;
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

}
