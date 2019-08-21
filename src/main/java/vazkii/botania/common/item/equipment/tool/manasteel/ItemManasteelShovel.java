/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 13, 2014, 7:14:54 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ItemManasteelShovel extends ShovelItem implements IManaUsingItem, ISortableTool {

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelShovel(Properties props) {
		this(BotaniaAPI.MANASTEEL_ITEM_TIER, props);
	}

	public ItemManasteelShovel(IItemTier mat, Properties props) {
		super(mat, 1.5F, -3.5F, props);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLivingBase, @Nonnull LivingEntity par3EntityLivingBase) {
		ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, MANA_PER_DAMAGE);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entity) {
		if (state.getBlockHardness(world, pos) != 0F)
			ToolCommons.damageItem(stack, 1, entity, MANA_PER_DAMAGE);

		return true;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		if(player == null || !player.canPlayerEdit(pos, ctx.getFace(), stack))
			return ActionResultType.PASS;
		else {
			UseHoeEvent event = new UseHoeEvent(ctx);
			if(MinecraftForge.EVENT_BUS.post(event))
				return ActionResultType.FAIL;

			if(event.getResult() == Event.Result.ALLOW) {
				ToolCommons.damageItem(stack, 1, player, MANA_PER_DAMAGE);
				return ActionResultType.SUCCESS;
			}

			Block block = world.getBlockState(pos).getBlock();

			if(ctx.getFace() != Direction.DOWN && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up()) && (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.GRASS_PATH)) {
				Block block1 = Blocks.GRASS_PATH;
				if(block == block1)
					block1 = Blocks.FARMLAND;
				
				world.playSound(null, pos, block1.getDefaultState().getSoundType().getStepSound(),
						SoundCategory.BLOCKS,
						(block1.getDefaultState().getSoundType().getVolume() + 1.0F) / 2.0F,
						block1.getDefaultState().getSoundType().getPitch() * 0.8F);

				if (world.isRemote)
					return ActionResultType.SUCCESS;
				else {
					world.setBlockState(pos, block1.getDefaultState());
					ToolCommons.damageItem(stack, 1, player, MANA_PER_DAMAGE);
					return ActionResultType.SUCCESS;
				}
			}

			return ActionResultType.PASS;
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof PlayerEntity && stack.getDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (PlayerEntity) player, MANA_PER_DAMAGE * 2, true))
			stack.setDamage(stack.getDamage() - 1);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ToolType getSortingType(ItemStack stack) {
		return ToolType.SHOVEL;
	}

	@Override
	public int getSortingPriority(ItemStack stack) {
		return ToolCommons.getToolPriority(stack);
	}
}
