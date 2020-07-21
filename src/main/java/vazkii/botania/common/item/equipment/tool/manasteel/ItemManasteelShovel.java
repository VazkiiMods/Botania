/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.mixin.AccessorHoeItem;

import javax.annotation.Nonnull;

import java.util.function.Consumer;

public class ItemManasteelShovel extends ShovelItem implements IManaUsingItem, ISortableTool {

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelShovel(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props);
	}

	public ItemManasteelShovel(IItemTier mat, Properties props) {
		super(mat, 1.5F, -3.0F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
		ToolCommons.damageItem(stack, 1, attacker, getManaPerDamage());
		return true;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, getManaPerDamage());
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		if (super.onItemUse(ctx) == ActionResultType.SUCCESS) {
			return ActionResultType.SUCCESS;
		}

		ItemStack stack = ctx.getItem();
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		if (player == null || !player.canPlayerEdit(pos, ctx.getFace(), stack)) {
			return ActionResultType.PASS;
		}

		UseHoeEvent event = new UseHoeEvent(ctx);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return ActionResultType.FAIL;
		}

		if (event.getResult() == Event.Result.ALLOW) {
			ToolCommons.damageItem(stack, 1, player, getManaPerDamage());
			return ActionResultType.SUCCESS;
		}

		Block block = world.getBlockState(pos).getBlock();
		BlockState converted = AccessorHoeItem.getConversions().get(block);
		if (converted == null) {
			return ActionResultType.PASS;
		}

		if (ctx.getFace() != Direction.DOWN && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up())) {
			world.playSound(null, pos, converted.getSoundType().getStepSound(),
					SoundCategory.BLOCKS,
					(converted.getSoundType().getVolume() + 1.0F) / 2.0F,
					converted.getSoundType().getPitch() * 0.8F);

			if (world.isRemote) {
				return ActionResultType.SUCCESS;
			} else {
				world.setBlockState(pos, converted);
				ToolCommons.damageItem(stack, 1, player, getManaPerDamage());
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		if (!world.isRemote && player instanceof PlayerEntity && stack.getDamage() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, (PlayerEntity) player, getManaPerDamage() * 2, true)) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public int getSortingPriority(ItemStack stack) {
		return ToolCommons.getToolPriority(stack);
	}
}
