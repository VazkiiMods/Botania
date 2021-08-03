/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ItemManasteelHoe extends HoeItem implements IManaUsingItem, ISortableTool {
	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelHoe(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props, -1f);
	}

	public ItemManasteelHoe(Tier mat, Properties properties, float attackSpeed) {
		super(mat, (int) -mat.getAttackDamageBonus(), attackSpeed, properties);
	}

	public static int damageItem(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> onBroken) {
		int manaPer = ((ItemManasteelHoe) stack.getItem()).getManaPerDamage();
		return ToolCommons.damageItemIfPossible(stack, amount, entity, manaPer);
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
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
