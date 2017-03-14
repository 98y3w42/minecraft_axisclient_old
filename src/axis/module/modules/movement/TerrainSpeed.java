package axis.module.modules.movement;

import org.lwjgl.input.Keyboard;

import axis.event.Event.State;
import axis.event.events.MoveEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.BlockHelper;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class TerrainSpeed extends Module {

	public TerrainSpeed() {
		super("TerrainSpeed", -3308225, ModuleManager.Category.MOVEMENT);
	}

	   public void onUpdate(UpdateEvent event) {
		   if(mc.thePlayer != null){
		       if ((BlockHelper.isOnIce() && (!mc.thePlayer.onGround))) {
	               Blocks.ice.slipperiness = 0.39F;
	               Blocks.packed_ice.slipperiness = 0.39F;
	           } else if (!mc.thePlayer.onGround){
	               Blocks.ice.slipperiness = 0.38F;
	               Blocks.packed_ice.slipperiness = 0.38F;
	           }
		   }
	  }

	   public void onMove(MoveEvent event){
	       if(mc.thePlayer.isOnLadder()){
	           event.setY(event.getY() * 2.25D);
	       }else if(!mc.thePlayer.isOnLadder()){
	    	   int posX = MathHelper.floor_double(mc.thePlayer.posX);
	           int minY = MathHelper.floor_double(mc.thePlayer.boundingBox.minY);
	           int maxY = MathHelper.floor_double(mc.thePlayer.boundingBox.minY + 1.0D);
	           int posZ = MathHelper.floor_double(mc.thePlayer.posZ);
	    	   Block thePlayer = BlockHelper.getBlockAtPos(new BlockPos(posX, minY, posZ));
	            if ((!(thePlayer instanceof BlockLadder)) && (!(thePlayer instanceof BlockVine)))
	            {
	              thePlayer = BlockHelper.getBlockAtPos(new BlockPos(posX, maxY, posZ));
	              if (((thePlayer instanceof BlockLadder)) || ((thePlayer instanceof BlockVine))) {
	                mc.thePlayer.motionY = 0.5D;
	              }
	            }
	       }
	   }
}
