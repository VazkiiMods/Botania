/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ItemManasteelAxe extends AxeItem implements IManaUsingItem, ISortableTool {

	private static final Pattern SAPLING_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)sapling)|(?:(?:[a-z-_.:]|^)Sapling))(?:[A-Z-_.:]|$)");

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelAxe(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props);
	}

	public ItemManasteelAxe(Tier mat, Properties props) {
		super(mat, 6F, -3.1F, props);
	}

	public static <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		int manaPerDamage = ((ItemManasteelAxe) stack.getItem()).getManaPerDamage();
		return ToolCommons.damageItemIfPossible(stack, amount, entity, manaPerDamage);
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();
		if (player != null) {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stackAt = player.getInventory().getItem(i);
				if (!stackAt.isEmpty() && SAPLING_PATTERN.matcher(stackAt.getItem().getDescriptionId()).find()) {
					ItemStack displayStack = stackAt.copy();
					if (PlayerHelper.substituteUse(ctx, stackAt).consumesAction()) {
						if (!ctx.getLevel().isClientSide) {
							ItemsRemainingRenderHandler.send(player, displayStack, SAPLING_PATTERN);
						}
						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return super.useOn(ctx);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
		if (!world.isClientSide && player instanceof Player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, (Player) player, getManaPerDamage() * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return ToolCommons.getToolPriority(stack);
	}
}
