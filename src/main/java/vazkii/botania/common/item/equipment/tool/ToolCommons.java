/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 13, 2014, 7:13:04 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

public final class ToolCommons {

	public static final Material[] materialsPick = new Material[]{ Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL };
	public static final Material[] materialsShovel = new Material[]{ Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY };
	public static final Material[] materialsAxe = new Material[]{ Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.GOURD };

	public static void damageItem(ItemStack stack, int dmg, EntityLivingBase entity, int manaPerDamage) {
		int manaToRequest = dmg * manaPerDamage;
		boolean manaRequested = entity instanceof EntityPlayer ? ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) entity, manaToRequest, true) : false;

		if(!manaRequested)
			stack.damageItem(dmg, entity);
	}

	/**
	 * Pos is the actual block coordinate, posStart and posEnd are deltas from pos
	 */
	public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, BlockPos pos, BlockPos posStart, BlockPos posEnd, Block block, Material[] materialsListing, boolean silk, int fortune, boolean dispose) {
		float blockHardness = block == null ? 1F : block.getBlockHardness(world.getBlockState(pos), world, pos);

		for (BlockPos iterPos : BlockPos.getAllInBox(pos.add(posStart), pos.add(posEnd))) {
			if (iterPos.equals(pos)) // skip original block space to avoid crash, vanilla code in the tool class will handle it
				continue;
			removeBlockWithDrops(player, stack, world, iterPos, pos, block, materialsListing, silk, fortune, blockHardness, dispose);
		}
	}

	public static boolean isRightMaterial(Material material, Material[] materialsListing) {
		for(Material mat : materialsListing)
			if(material == mat)
				return true;

		return false;
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos, BlockPos bPos, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose) {
		removeBlockWithDrops(player, stack, world, pos, bPos, block, materialsListing, silk, fortune, blockHardness, dispose, true);
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos, BlockPos bPos, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose, boolean particles) {
		if(!world.isBlockLoaded(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block blk = state.getBlock();

		if(block != null && blk != block)
			return;

		Material mat = world.getBlockState(pos).getMaterial();
		if(!world.isRemote && blk != null && !blk.isAir(state, world, pos) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0) {
			if(!blk.canHarvestBlock(player.world, pos, player) || !isRightMaterial(mat, materialsListing)) {
				return;
			}

			if(!player.capabilities.isCreativeMode) {
				TileEntity tile = world.getTileEntity(pos);
				IBlockState localState = world.getBlockState(pos);
				blk.onBlockHarvested(world, pos, localState, player);

				if(blk.removedByPlayer(state, world, pos, player, true)) {
					blk.onBlockDestroyedByPlayer(world, pos, state);

					if(!dispose || !ItemElementiumPick.isDisposable(blk))
						blk.harvestBlock(world, player, pos, state, tile, stack);
				}

				damageItem(stack, 1, player, 80);
			} else world.setBlockToAir(pos);

			if(particles && !world.isRemote && ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool)
				world.playEvent(2001, pos, Block.getStateId(state));
		}
	}

	public static int getToolPriority(ItemStack stack) {
		if(stack == null)
			return 0;

		Item item = stack.getItem();
		if(!(item instanceof ItemTool))
			return 0;

		ItemTool tool = (ItemTool) item;
		ToolMaterial material = tool.getToolMaterial();
		int materialLevel = 0;
		if(material == BotaniaAPI.manasteelToolMaterial)
			materialLevel = 10;
		if(material == BotaniaAPI.elementiumToolMaterial)
			materialLevel = 11;
		if(material == BotaniaAPI.terrasteelToolMaterial)
			materialLevel = 20;

		int modifier = 0;
		if(item == ModItems.terraPick)
			modifier = ItemTerraPick.getLevel(stack);

		int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		return materialLevel * 100 + modifier * 10 + efficiency;
	}

	/**
	 * @author mDiyo
	 */
	public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (player instanceof EntityPlayer)
			d1 += ((EntityPlayer) player).eyeHeight;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		Vec3d vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.rayTraceBlocks(vec3, vec31, par3);
	}

}
