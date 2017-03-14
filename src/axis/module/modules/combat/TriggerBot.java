package axis.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Mouse;

import axis.Axis;
import axis.event.Event;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.TimeHelper;

public class TriggerBot extends Module {

	private double speed = 4.5D;
	  public static double range = 4.5D;
	  public static double hitboxSize = 1.5D;
	  public static boolean autoclicker;
	  private boolean players = true;
	  private boolean monsters;
	  private boolean criticals;
	  private TimeHelper timer = new TimeHelper();

	public TriggerBot() {
		super("TriggerBot", 9623002, Category.COMBAT);
	}
	private void onPostUpdate(UpdateEvent event)
	  {
	    if (event.state == Event.State.POST) {
	      if (((autoclicker) && (Mouse.isButtonDown(0))) || (!autoclicker)) {
	        if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.entityHit != null) && (attackChecks(this.mc.objectMouseOver.entityHit)) && (this.timer.hasReached((float)(1000.0D / this.speed))))
	        {
	          if (this.criticals) {
	            crit();
	          }
	          this.mc.thePlayer.swingItem();
	          this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));

	          this.timer.reset();
	        }
	      }
	    }
	  }

	  private void crit()
	  {
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.05D, this.mc.thePlayer.posZ, false));
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.012511D, this.mc.thePlayer.posZ, false));
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
	  }

	  private boolean attackChecks(Entity ent)
	  {
	    if (ent == null) {
	      return false;
	    }
	    if (!(ent instanceof EntityLivingBase)) {
	      return false;
	    }
	    if (ent == this.mc.thePlayer) {
	      return false;
	    }
	    if (ent.getDistanceToEntity(this.mc.thePlayer) > range) {
	      return false;
	    }
	    if (!ent.isEntityAlive()) {
	      return false;
	    }
	    boolean isVehicle = ((ent instanceof EntityMinecart)) || ((ent instanceof EntityBoat));
	    if ((ent instanceof EntityPlayer))
	    {
	      if (!this.players) {
	        return false;
	      }
	      EntityPlayer player = (EntityPlayer)ent;
	      if (!this.players) {
	        return false;
	      }
	    }
	    if (isVehicle) {
	      return false;
	    }
	    if (((ent instanceof EntityMob)) && (!this.monsters)) {
	      return false;
	    }
	    if (((ent instanceof EntitySlime)) && (!this.monsters)) {
	      return false;
	    }
	    return true;
	  }
}
