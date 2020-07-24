/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.mana;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Collection;
import java.util.Collections;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaIngredientHelper implements IIngredientHelper<IManaIngredient> {

	@Override
	public Iterable<Integer> getColors(IManaIngredient ingredient) {
		return Collections.singletonList(TilePool.PARTICLE_COLOR);
	}

	@Override
	public ItemStack getCheatItemStack(IManaIngredient ingredient) {
		int amount = ingredient.getAmount();
		ItemStack stack = new ItemStack(amount > ItemManaTablet.MAX_MANA && !ingredient.isCreative() ? ModItems.manaRingGreater : ModItems.manaTablet);

		((IManaItem) stack.getItem()).addMana(stack, ingredient.getAmount());
		if (ingredient.isCreative()) {
			ItemManaTablet.setStackCreative(stack);
		}
		return stack;
	}

	@Override
	public Collection<String> getCreativeTabNames(IManaIngredient ingredient) {
		return Collections.singletonList(BotaniaCreativeTab.INSTANCE.getTabLabel());
	}

	@Nullable
	@Override
	public IManaIngredient getMatch(Iterable<IManaIngredient> ingredients, IManaIngredient ingredientToMatch) {
		return null;
	}

	@Override
	public String getDisplayName(IManaIngredient manaIngredient) {
		return I18n.format("botania.ingredient.mana");
	}

	@Override
	public String getUniqueId(IManaIngredient manaIngredient) {
		return LibMisc.MOD_ID + ":mana";
	}

	@Override
	public String getWildcardId(IManaIngredient manaIngredient) {
		return LibMisc.MOD_ID + ":mana";
	}

	@Override
	public String getModId(IManaIngredient manaIngredient) {
		return LibMisc.MOD_ID;
	}

	@Override
	public String getResourceId(IManaIngredient manaIngredient) {
		return LibMisc.MOD_ID + ":mana";
	}

	@Override
	public IManaIngredient copyIngredient(IManaIngredient manaIngredient) {
		return manaIngredient;
	}

	@Override
	public String getErrorInfo(@Nullable IManaIngredient manaIngredient) {
		if (manaIngredient == null) {
			return "This mana pool has... 'null' mana... Not really a valid ingredient.";
		}
		if (manaIngredient.getAmount() < 0) {
			return "Mana amount was negative, which makes no sense.";
		}
		return getDisplayName(manaIngredient);
	}

}
