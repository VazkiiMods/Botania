/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.mana;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;

import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.lib.LibMisc;

public class ManaIngredient implements IManaIngredient {

	public static final IIngredientType<IManaIngredient> TYPE = () -> IManaIngredient.class;
	public static final ManaIngredientHelper HELPER = new ManaIngredientHelper();

	private final int amount;
	private final boolean isCreative;

	public ManaIngredient(int amount, boolean isCreative) {
		if (amount < 0) {
			throw new IllegalArgumentException("negative mana amount");
		}
		this.amount = amount;
		this.isCreative = isCreative;
	}

	public static ManaIngredient getCreative() {
		return new ManaIngredient(ItemManaTablet.MAX_MANA, true);
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public boolean isCreative() {
		return isCreative;
	}

	@Override
	public IIngredientRenderer<IManaIngredient> getSquareRenderer() {
		return ManaIngredientRenderer.Square.INSTANCE;
	}

	@Override
	public IIngredientRenderer<IManaIngredient> getBarRenderer() {
		return ManaIngredientRenderer.Bar.INSTANCE;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ManaIngredient other = (ManaIngredient) obj;
		return amount == other.amount;
	}

	@Override
	public String toString() {
		return LibMisc.MOD_ID + ":mana[" + amount + (isCreative ? " (Creative)" : "") + "]";
	}
}
