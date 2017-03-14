package axis.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class LiquidUtils {
	public static boolean isOnLiquid()
	{
		AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0D, -0.01D, 0.0D).contract(0.001D, 0.001D, 0.001D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
		Vec3 var11 = new Vec3(0.0D, 0.0D, 0.0D);
		for (int var12 = var4; var12 < var5; var12++) {
			for (int var13 = var6; var13 < var7; var13++) {
				for (int var14 = var8; var14 < var9; var14++)
				{
					Block var15 = Minecraft.getMinecraft().theWorld.getBlock(var12, var13, var14);
					if ((!(var15 instanceof BlockAir)) && (!(var15 instanceof BlockLiquid))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean isInLiquid() {
		AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001D, 0.001D, 0.001D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		Vec3 var11 = new Vec3(0.0D, 0.0D, 0.0D);
		for (int var12 = var4; var12 < var5; var12++) {
			for (int var13 = var6; var13 < var7; var13++) {
				for (int var14 = var8; var14 < var9; var14++)
				{
					Block var15 = Minecraft.getMinecraft().theWorld.getBlock(var12, var13, var14);
					if ((var15 instanceof BlockLiquid)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
