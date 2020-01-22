/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 13, 2014, 7:21:32 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ItemManasteelSword extends SwordItem implements IManaUsingItem {

	public static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelSword(Properties props) {
		this(BotaniaAPI.MANASTEEL_ITEM_TIER, props);
	}

	public ItemManasteelSword(IItemTier mat, Properties props) {
		super(mat, 3, -2.4F, props);
		addPropertyOverride(new ResourceLocation("botania", "elucidator"),
				(stack, worldIn, entityIn) -> "the elucidator".equals(stack.getDisplayName().getString().toLowerCase().trim()) ? 1 : 0);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, getManaPerDamage());
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof PlayerEntity && stack.getDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (PlayerEntity) player, getManaPerDamage() * 2, true))
			stack.setDamage(stack.getDamage() - 1);
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
