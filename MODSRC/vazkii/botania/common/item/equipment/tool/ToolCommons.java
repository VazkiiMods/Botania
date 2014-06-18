/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 13, 2014, 7:13:04 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

public final class ToolCommons {

	public static void damageItem(ItemStack stack, int dmg, EntityLivingBase entity, int manaPerDamage) {
		int manaToRequest = dmg * manaPerDamage;
		int manaRequested = entity instanceof EntityPlayer ? ManaItemHandler.requestMana(stack, (EntityPlayer) entity, manaToRequest, true) : 0;

		int finalDamage = dmg - manaRequested / manaPerDamage;
		if(finalDamage > 0)
			stack.damageItem(finalDamage, entity);
	}

	public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int xs, int ys, int zs, int xe, int ye, int ze, Block block, Material[] materialsListing, boolean silk, int fortune) {
		float blockHardness = block == null ? 1F : block.getBlockHardness(world, x, y, z);

		for(int x1 = xs; x1 < xe; x1++)
			for(int y1 = ys; y1 < ye; y1++)
				for(int z1 = zs; z1 < ze; z1++)
					if(x != x1 && y != y1 && z != z1)
						removeBlockWithDrops(player, stack, world, x1 + x, y1 + y, z1 + z, x, y, z, block, materialsListing, silk, fortune,blockHardness);
	}

	public static boolean isRightMaterial(Material material, Material[] materialsListing) {
		for(Material mat : materialsListing)
			if(material == mat)
				return true;

		return false;
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int bx, int by, int bz, Block block, Material[] materialsListing, boolean silk, int fortune,float blockHardness) {
		if(!world.blockExists(x, y, z))
			return;

		Block blk = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(block != null && blk != block)
			return;

		Material mat = world.getBlock(x, y, z).getMaterial();
		if(blk != null && !blk.isAir(world, x, y, z) && blk.getPlayerRelativeBlockHardness(player, world, x, y, z) != 0) {
			new ArrayList();

			if(!blk.canHarvestBlock(player, meta) || !isRightMaterial(mat, materialsListing))
				return;

			if(!player.capabilities.isCreativeMode && blk != Blocks.bedrock) {
				int localMeta = world.getBlockMetadata(x, y, z);
				if (blk.removedByPlayer(world, player, x, y, z))
					blk.onBlockDestroyedByPlayer(world, x, y, z, localMeta);

				damageItem(stack, 1, player, 80);
				blk.harvestBlock(world, player, x, y, z, localMeta);
				blk.onBlockHarvested(world, x, y, z, localMeta, player);
			} else world.setBlockToAir(x, y, z);

			if(ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool)
				world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blk) + (meta << 12));
		}
	}

	/**
	 * @author mDiyo
	 */
	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (!world.isRemote && player instanceof EntityPlayer)
			d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP)
			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.rayTraceBlocks(vec3, vec31, par3);
	}

}
