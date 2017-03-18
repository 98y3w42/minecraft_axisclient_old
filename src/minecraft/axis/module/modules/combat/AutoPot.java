package axis.module.modules.combat;


import axis.Axis;
import axis.command.Command;
import axis.event.Event.State;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.Logger;
import axis.util.RotationUtils;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot extends Module {

	private TimeHelper timer = new TimeHelper();
	private TimeHelper time1 = new TimeHelper();

	public final Value<Long> delay = new Value<>("autopot_delay", 35L);
	public final Value<Double> health = new Value<>("autopot_health", 4.5D);

	private boolean predict;
	private boolean splashUp;
	private static int expectedPackets;
	private int lockedTicks;
	private boolean splashed;
	private static int potSlot;
	private static ItemStack originalStack;
	private static int currentItem;

	public AutoPot() {
			super("AutoPot", 12337730, Category.COMBAT);
			setDisplayName("Auto Pot");
			Axis.getAxis().getCommandManager().getContents().add(new Command("autopot", "<mode>", new String[]{"ap"}) {
				public void run(String message) {
					if(message.split(" ")[1].equalsIgnoreCase("health")) {
						if(message.split(" ")[2].equalsIgnoreCase("-d")) {
							health.setValue((Double)health.getDefaultValue());
						} else {
							health.setValue(Double.valueOf(Double.parseDouble(message.split(" ")[2])));
						}

						if(((Double)health.getValue()).doubleValue() > 10.0D) {
							health.setValue(Double.valueOf(10.0D));
						} else if(((Double)health.getValue()).doubleValue() < 1.0D) {
							health.setValue(Double.valueOf(1.0D));
						}

						Logger.logChat("AutoPot health set to: " + health.getValue());
					}
				}
			});
	}

	private void onUpdate(UpdateEvent event)
	{
		if (event.state == State.PRE)
		{
			if (splashed && timer.hasReached((float)150L)) {
				swap(potSlot, currentItem);
				timer.reset();
				splashed = false;
			}
			if (lockedTicks >= 0)
			{
				if (lockedTicks == 0) {
					event.y = 1.3D;
				} else {
					event.setCancelled(true);
				}
				lockedTicks -= 1;
			}
			else
			{
				int potSlot = getPotionFromInventory();
				if ((potSlot != -1) && (mc.thePlayer.getHealth() <= health.getValue() * 2.0D) && timer.hasReached((float)150L) && !splashed) {
					if ((splashUp) && (mc.thePlayer.motionX == 0.0D) && (mc.thePlayer.motionZ == 0.0D) && (mc.thePlayer.isCollidedVertically))
					{
						event.pitch = -90.0F;
					}
					else
					{
						event.pitch = 98.0F;
						if (predict)
						{
							double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 16.0D;
							double movedPosY = mc.thePlayer.boundingBox.minY - 3.6D;
							double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 16.0D;
							float yaw = RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY)[0];
							float pitch = RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY)[1];
							event.yaw = yaw;
							event.pitch = pitch;
						}
					}
				}
			}
		}
		else if (event.state == State.POST) {
			if (lockedTicks < 0)
			{
				int potSlot = getPotionFromInventory();
				if ((potSlot != -1) && (mc.thePlayer.getHealth() <= health.getValue() * 2.0D) && timer.hasReached((float)150L) && !splashed)
				{
					originalStack = mc.thePlayer.inventoryContainer.getSlot(36 + mc.thePlayer.inventory.currentItem).getStack();
					this.potSlot = potSlot;
					currentItem = mc.thePlayer.inventory.currentItem;
					boolean blocking = mc.thePlayer.isBlocking();
					swap(potSlot, mc.thePlayer.inventory.currentItem);
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					splashed = true;
					expectedPackets = 4;
					if (splashUp)
					{
						lockedTicks = 6;
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ, true));
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75D, mc.thePlayer.posZ, true));
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ, true));
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.166D, mc.thePlayer.posZ, true));
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.24D, mc.thePlayer.posZ, true));
					}
				}
			}
		}
	}

	public static void resetItems()
	  {
	    if (expectedPackets > 0)
	    {
	      expectedPackets -= 1;
	      mc.thePlayer.inventoryContainer.putStackInSlot(potSlot, null);
	      mc.thePlayer.inventoryContainer.putStackInSlot(36 + currentItem, originalStack);
	    }
	  }

	  protected void swap(int slot, int hotbarNum)
	  {
		  this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
	  }

	  private int getPotionFromInventory()
	  {
	    int pot = -1;
	    int counter = 0;
	    for (int i = 1; i < 45; i++) {
	      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
	      {
	        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	        Item item = is.getItem();
	        if ((item instanceof ItemPotion))
	        {
	          ItemPotion potion = (ItemPotion)item;
	          if (potion.getEffects(is) != null) {
	            for (Object o : potion.getEffects(is))
	            {
	              PotionEffect effect = (PotionEffect)o;
	              if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage())))
	              {
	                counter++;
	                pot = i;
	              }
	            }
	          }
	        }
	      }
	    }
	    return pot;
	  }

}
