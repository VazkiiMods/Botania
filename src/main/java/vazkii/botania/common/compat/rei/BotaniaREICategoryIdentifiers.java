/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import vazkii.botania.common.crafting.*;
import vazkii.botania.common.lib.ResourceLocationHelper;


import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class BotaniaREICategoryIdentifiers {
	public static final CategoryIdentifier<BreweryREIDisplay> BREWERY = CategoryIdentifier.of(RecipeBrew.TYPE_ID);
	public static final CategoryIdentifier<ElvenTradeREIDisplay> ELVEN_TRADE = CategoryIdentifier.of(RecipeElvenTrade.TYPE_ID);
	public static final CategoryIdentifier<ManaPoolREIDisplay> MANA_INFUSION = CategoryIdentifier.of(RecipeManaInfusion.TYPE_ID);
	public static final CategoryIdentifier<OrechidREIDisplay> ORECHID = CategoryIdentifier.of(ResourceLocationHelper.prefix("orechid"));
	public static final CategoryIdentifier<OrechidIgnemREIDisplay> ORECHID_IGNEM = CategoryIdentifier.of(ResourceLocationHelper.prefix("orechid_ignem"));
	public static final CategoryIdentifier<PetalApothecaryREIDisplay> PETAL_APOTHECARY = CategoryIdentifier.of(RecipePetals.TYPE_ID);
	public static final CategoryIdentifier<PureDaisyREIDisplay> PURE_DAISY = CategoryIdentifier.of(RecipePureDaisy.TYPE_ID);
	public static final CategoryIdentifier<RunicAltarREIDisplay> RUNE_ALTAR = CategoryIdentifier.of(RecipeRuneAltar.TYPE_ID);

	private BotaniaREICategoryIdentifiers() {

	}
}
