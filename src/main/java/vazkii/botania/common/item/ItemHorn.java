/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 11, 2014, 2:57:30 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.item.IHornHarvestable.EnumHornType;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ItemHorn extends ItemMod {
	public ItemHorn(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "vuvuzela"),
				(stack, worldIn, entityIn) -> stack.getDisplayName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0);
	}

	@Nonnull
	@Override
	public EnumAction getUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int time) {
		if(!player.world.isRemote) {
			if(time != getUseDuration(stack) && time % 5 == 0)
				breakGrass(player.world, stack, new BlockPos(player));
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.BLOCKS, 1F, 0.001F);
		}
	}

	public static void breakGrass(World world, ItemStack stack, BlockPos srcPos) {
		EnumHornType type = null;
		if (stack.getItem() == ModItems.grassHorn) {
			type = EnumHornType.WILD;
		} else if (stack.getItem() == ModItems.leavesHorn) {
			type = EnumHornType.CANOPY;
		} else if (stack.getItem() == ModItems.snowHorn) {
			type = EnumHornType.COVERING;
		}

		int range = 12 - type.ordinal() * 3;
		int rangeY = 3 + type.ordinal() * 4;
		List<BlockPos> coords = new ArrayList<>();

		for(BlockPos pos : BlockPos.getAllInBox(srcPos.add(-range, -rangeY, -range), srcPos.add(range, rangeY, range))) {
			Block block = world.getBlockState(pos).getBlock();
			if(block instanceof IHornHarvestable
					? ((IHornHarvestable) block).canHornHarvest(world, pos, stack, type)
							: type == EnumHornType.WILD && block instanceof BlockBush && !(block instanceof ISpecialFlower)
							|| type == EnumHornType.CANOPY && BlockTags.LEAVES.contains(block)
							|| type == EnumHornType.COVERING && block == Blocks.SNOW)
				coords.add(pos);
		}

		Collections.shuffle(coords, world.rand);

		int count = Math.min(coords.size(), 32 + type.ordinal() * 16);
		for(int i = 0; i < count; i++) {
			BlockPos currCoords = coords.get(i);
			IBlockState state = world.getBlockState(currCoords);
			Block block = state.getBlock();

			if(block instanceof IHornHarvestable && ((IHornHarvestable) block).hasSpecialHornHarvest(world, currCoords, stack, type))
				((IHornHarvestable) block).harvestByHorn(world, currCoords, stack, type);
			else {
				world.destroyBlock(currCoords, true);
			}
		}
	}

}
