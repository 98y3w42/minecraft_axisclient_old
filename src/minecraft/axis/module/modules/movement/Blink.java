package axis.module.modules.movement;

import io.netty.util.concurrent.GenericFutureListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovementInput;

public class Blink
  extends Module
{
  private final List<Packet> packets = new CopyOnWriteArrayList();
  private final List<double[]> positions = new ArrayList();
  private double[] startingPosition;

  public Blink()
  {
    super("Blink", -14308402, ModuleManager.Category.MOVEMENT);
  }

  Minecraft mc = Minecraft.getMinecraft();

  public void onDisabled()
  {
    super.onDisabled();
    if (this.mc.theWorld != null) {
      this.mc.theWorld.removeEntityFromWorld(-1);
    }
    Iterator var2 = this.packets.iterator();
    for (Packet packet : this.packets) {
      this.mc.getNetHandler().getNetworkManager().sendPacket(packet, null, new GenericFutureListener[0]);
    }
    this.packets.clear();
    this.positions.clear();
    this.setTag("" + this.packets.size());
  }

  public void onEnabled()
  {
    super.onEnabled();
    if ((this.mc.thePlayer != null) && (this.mc.theWorld != null))
    {
      double x = this.mc.thePlayer.posX;
      double y = this.mc.thePlayer.posY;
      double z = this.mc.thePlayer.posZ;
      float yaw = this.mc.thePlayer.rotationYaw;
      float pitch = this.mc.thePlayer.rotationPitch;
      EntityOtherPlayerMP ent = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
      ent.inventory = this.mc.thePlayer.inventory;
      ent.inventoryContainer = this.mc.thePlayer.inventoryContainer;
      ent.setPositionAndRotation(x, y, z, yaw, pitch);
      ent.rotationYawHead = this.mc.thePlayer.rotationYawHead;
      this.mc.theWorld.addEntityToWorld(-1, ent);
    }
  }

  public void packet(PacketSentEvent event)
  {
    if ((event.packet instanceof C03PacketPlayer))
    {
      if ((this.mc.thePlayer.movementInput.moveForward != 0.0F) || (this.mc.gameSettings.keyBindJump.pressed) ||
        (this.mc.thePlayer.movementInput.moveStrafe != 0.0F))
      {
        this.packets.add(event.packet);
        this.setTag("" + this.packets.size());
      }
      event.setCancelled(true);
    }
    else if (((event.packet instanceof C08PacketPlayerBlockPlacement)) ||
      ((event.packet instanceof C07PacketPlayerDigging)) || ((event.packet instanceof C09PacketHeldItemChange)) ||
      ((event.packet instanceof C02PacketUseEntity)) || ((event.packet instanceof C0BPacketEntityAction)))
    {
      this.packets.add(event.packet);
      this.setTag("" + this.packets.size());
      event.setCancelled(true);
    }
  }

  public void update(UpdateEvent e)
  {
    if (this.mc.thePlayer.movementInput.moveForward != 0.0F) {
      this.positions.add(new double[] { this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ });
    }
  }
}
