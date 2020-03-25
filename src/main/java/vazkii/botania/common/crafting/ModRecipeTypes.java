package vazkii.botania.common.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {
	public static final IRecipeType<RecipeManaInfusion> MANA_INFUSION_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();
	public static final IRecipeType<AbstractElvenTradeRecipe> ELVEN_TRADE_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SpecialRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SpecialRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	@SubscribeEvent
	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
		ResourceLocation id = prefix("elven_trade");
		Registry.register(Registry.RECIPE_TYPE, id, ELVEN_TRADE_TYPE);
		evt.getRegistry().register(ELVEN_TRADE_SERIALIZER.setRegistryName(id));
		evt.getRegistry().register(LEXICON_ELVEN_TRADE_SERIALIZER.setRegistryName(prefix("elven_trade_lexicon")));

		id = prefix("mana_infusion");
		Registry.register(Registry.RECIPE_TYPE, id, MANA_INFUSION_TYPE);
		evt.getRegistry().register(MANA_INFUSION_SERIALIZER.setRegistryName(id));
	}

	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
