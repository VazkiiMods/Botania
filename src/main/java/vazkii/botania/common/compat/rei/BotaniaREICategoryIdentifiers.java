/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import vazkii.botania.common.crafting.*;

public class BotaniaREICategoryIdentifiers {
	public static final CategoryIdentifier<BreweryREIDisplay> BREWERY = CategoryIdentifier.of(RecipeBrew.TYPE_ID);
	public static final CategoryIdentifier<ElvenTradeREIDisplay> ELVEN_TRADE = CategoryIdentifier.of(RecipeElvenTrade.TYPE_ID);
	public static final CategoryIdentifier<ManaPoolREIDisplay> MANA_INFUSION = CategoryIdentifier.of(RecipeManaInfusion.TYPE_ID);
	public static final CategoryIdentifier<OrechidREIDisplay> MARIMORPHOSIS = CategoryIdentifier.of(RecipeOrechid.MARIMORPHOSIS_TYPE_ID);
	public static final CategoryIdentifier<OrechidREIDisplay> ORECHID = CategoryIdentifier.of(RecipeOrechid.TYPE_ID);
	public static final CategoryIdentifier<OrechidIgnemREIDisplay> ORECHID_IGNEM = CategoryIdentifier.of(RecipeOrechid.IGNEM_TYPE_ID);
	public static final CategoryIdentifier<PetalApothecaryREIDisplay> PETAL_APOTHECARY = CategoryIdentifier.of(RecipePetals.TYPE_ID);
	public static final CategoryIdentifier<PureDaisyREIDisplay> PURE_DAISY = CategoryIdentifier.of(RecipePureDaisy.TYPE_ID);
	public static final CategoryIdentifier<RunicAltarREIDisplay> RUNE_ALTAR = CategoryIdentifier.of(RecipeRuneAltar.TYPE_ID);
	public static final CategoryIdentifier<TerraPlateREIDisplay> TERRA_PLATE = CategoryIdentifier.of(RecipeTerraPlate.TYPE_ID);

	private BotaniaREICategoryIdentifiers() {

	}
}
