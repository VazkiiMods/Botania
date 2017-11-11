/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 11, 2014, 2:56:39 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemTerraformRod extends ItemMod implements IManaUsingItem, IBlockProvider {

	private static final int COST_PER = 55;

	private static final List<String> validBlocks = ImmutableList.of(
			"stone",
			"dirt",
			"grass",
			"sand",
			"gravel",
			"hardenedClay",
			"snowLayer",
			"mycelium",
			"podzol",
			"sandstone",
			"blockDiorite",
			"stoneDiorite",
			"blockGranite",
			"stoneGranite",
			"blockAndesite",
			"stoneAndesite",

			// Mod support
			"marble",
			"blockMarble",
			"limestone",
			"blockLimestone"
			);

	public ItemTerraformRod() {
		super(LibItemNames.TERRAFORM_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		if(count != getMaxItemUseDuration(stack) && count % 10 == 0 && living instanceof EntityPlayer)
			terraform(stack, living.world, (EntityPlayer) living);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	private void terraform(ItemStack par1ItemStack, World world, EntityPlayer player) {
		int range = IManaProficiencyArmor.Helper.hasProficiency(player, par1ItemStack) ? 22 : 16;

		BlockPos startCenter = new BlockPos(player).down();

		if(startCenter.getY() < world.getSeaLevel()) // Not below sea level
			return;

		List<CoordsWithBlock> blocks = new ArrayList<>();

		for(BlockPos pos : BlockPos.getAllInBoxMutable(startCenter.add(-range, -range, -range), startCenter.add(range, range, range))) {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == Blocks.AIR)
				continue;
			else if(Item.getItemFromBlock(state.getBlock()) == Items.AIR)
				continue;
			int[] ids = OreDictionary.getOreIDs(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
			for(int id : ids)
				if(validBlocks.contains(OreDictionary.getOreName(id))) {
					List<BlockPos> airBlocks = new ArrayList<>();

					for(EnumFacing dir : EnumFacing.HORIZONTALS) {
						BlockPos pos_ = pos.offset(dir);
						Block block_ = world.getBlockState(pos_).getBlock();
						if(block_.isAir(world.getBlockState(pos_), world, pos_) || block_.isReplaceable(world, pos_) || block_ instanceof BlockFlower && !(block_ instanceof ISpecialFlower) || block_ == Blocks.DOUBLE_PLANT) {
							airBlocks.add(pos_);
						}
					}

					if(!airBlocks.isEmpty()) {
						if(pos.getY() > startCenter.getY())
							blocks.add(new CoordsWithBlock(pos, Blocks.AIR));
						else for(BlockPos coords : airBlocks) {
							if(world.getBlockState(coords.down()).getBlock() != Blocks.AIR)
								blocks.add(new CoordsWithBlock(coords, Blocks.DIRT));
						}
					}
					break;
				}
		}

		int cost = COST_PER * blocks.size();

		if(world.isRemote || ManaItemHandler.requestManaExactForTool(par1ItemStack, player, cost, true)) {
			if(!world.isRemote)
				for(CoordsWithBlock block : blocks)
					world.setBlockState(block, block.block.getDefaultState());

			if(!blocks.isEmpty()) {
				for(int i = 0; i < 10; i++)
					world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 1F, 0.4F);
				for(int i = 0; i < 120; i++)
					Botania.proxy.sparkleFX(startCenter.getX() - range + range * 2 * Math.random(), startCenter.getY() + 2 + (Math.random() - 0.5) * 2, startCenter.getZ() - range + range * 2 * Math.random(), 0.35F, 0.2F, 0.05F, 2F, 5);
			}
		}
	}

	private static class CoordsWithBlock extends BlockPos {

		private final Block block;

		private CoordsWithBlock(BlockPos pos, Block block) {
			super(pos.getX(), pos.getY(), pos.getZ());
			this.block = block;
		}

	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(block == Blocks.DIRT && meta == 0)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, ItemDirtRod.COST, true);
		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		if(block == Blocks.DIRT && meta == 0)
			return -1;
		return 0;
	}
}
