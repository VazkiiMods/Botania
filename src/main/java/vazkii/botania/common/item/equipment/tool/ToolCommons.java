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
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class ToolCommons {

	public static final List<Material> materialsPick = Arrays.asList(Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL);
	public static final List<Material> materialsShovel = Arrays.asList(Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY);
	public static final List<Material> materialsAxe = Arrays.asList(Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.GOURD);

	public static void damageItem(ItemStack stack, int dmg, EntityLivingBase entity, int manaPerDamage) {
		int manaToRequest = dmg * manaPerDamage;
		boolean manaRequested = entity instanceof EntityPlayer && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) entity, manaToRequest, true);

		if(!manaRequested)
			stack.damageItem(dmg, entity);
	}

	public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, BlockPos centerPos,
											   Vec3i startDelta, Vec3i endDelta, Predicate<IBlockState> filter,
											   boolean dispose) {
		for (BlockPos iterPos : BlockPos.getAllInBox(centerPos.add(startDelta), centerPos.add(endDelta))) {
			if (iterPos.equals(centerPos)) // skip original block space to avoid crash, vanilla code in the tool class will handle it
				continue;
			removeBlockWithDrops(player, stack, world, iterPos, filter, dispose);
		}
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos,
											Predicate<IBlockState> filter,
											boolean dispose) {
		removeBlockWithDrops(player, stack, world, pos, filter, dispose, true);
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos,
											Predicate<IBlockState> filter,
											boolean dispose, boolean particles) {
		if(!world.isBlockLoaded(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if(!world.isRemote && filter.test(state)
				&& !block.isAir(state, world, pos) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0
				&& block.canHarvestBlock(player.world, pos, player)) {
			int exp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
			if(exp == -1)
				return;

			if(!player.capabilities.isCreativeMode) {
				TileEntity tile = world.getTileEntity(pos);

				if(block.removedByPlayer(state, world, pos, player, true)) {
					block.onPlayerDestroy(world, pos, state);

					if(!dispose || !ItemElementiumPick.isDisposable(block)) {
						block.harvestBlock(world, player, pos, state, tile, stack);
						block.dropXpOnBlockBreak(world, pos, exp);
					}
				}

				damageItem(stack, 1, player, 80);
			} else world.setBlockToAir(pos);

			if(particles && ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool)
				world.playEvent(2001, pos, Block.getStateId(state));
		}
	}

	public static int getToolPriority(ItemStack stack) {
		if(stack.isEmpty())
			return 0;

		Item item = stack.getItem();
		if(!(item instanceof ItemTool))
			return 0;

		ItemTool tool = (ItemTool) item;
		ToolMaterial material = tool.toolMaterial;
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

	// [VanillaCopy] of Item.rayTrace, edits noted
	public static RayTraceResult raytraceFromEntity(World worldIn, Entity playerIn, boolean useLiquids, double range) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = range; // Botania - use custom range param, don't limit to reach distance
		/*if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP)
		{
			d3 = ((net.minecraft.entity.player.EntityPlayerMP)playerIn).interactionManager.getBlockReachDistance();
		}*/
		Vec3d vec3d1 = vec3d.add((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

}
