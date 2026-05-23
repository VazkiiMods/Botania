package vazkii.botania.common.handler;

import com.google.common.base.Suppliers;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

// TODO: Merge with OrechidManager?
public class BotaniaRecipeIngredientsCache implements ResourceManagerReloadListener {
	private static final Supplier<BotaniaRecipeIngredientsCache> SERVER_INSTANCE = Suppliers.memoize(BotaniaRecipeIngredientsCache::new);
	private static final Supplier<BotaniaRecipeIngredientsCache> CLIENT_INSTANCE = Suppliers.memoize(BotaniaRecipeIngredientsCache::new);

	private final IntSet terraPlateInputItemIds = new IntOpenHashSet();
	private final IntSet elvenTradeInputItemIds = new IntOpenHashSet();

	public static boolean isTerraPlateInputItem(Level level, Item item) {
		return getInstance(level).isTerraPlateInputItemInternal(level, item);
	}

	public static boolean isElvenTradeInputItem(Level level, Item item) {
		return getInstance(level).isElvenTradeInputItemInternal(level, item);
	}

	/**
	 * Registers the server instance's cache reset.
	 */
	public static void registerListener() {
		XplatAbstractions.instance().registerReloadListener(PackType.SERVER_DATA, botaniaRL("ingredients_cache"),
				SERVER_INSTANCE.get());
	}

	/**
	 * Called to reset the client cache.
	 */
	public static void clearClientCache() {
		CLIENT_INSTANCE.get().clearCache();
	}

	private static BotaniaRecipeIngredientsCache getInstance(Level level) {
		return level.isClientSide() ? CLIENT_INSTANCE.get() : SERVER_INSTANCE.get();
	}

	@ApiStatus.Internal
	@Override
	public void onResourceManagerReload(@Nullable ResourceManager resourceManager) {
		clearCache();
	}

	private void clearCache() {
		terraPlateInputItemIds.clear();
		elvenTradeInputItemIds.clear();
	}

	private boolean isTerraPlateInputItemInternal(Level level, Item item) {
		return isMatchingInputItem(level, item, BotaniaRecipeTypes.TERRA_PLATE_TYPE, terraPlateInputItemIds);
	}

	private boolean isElvenTradeInputItemInternal(Level level, Item item) {
		return isMatchingInputItem(level, item, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, elvenTradeInputItemIds);
	}

	private static <I extends RecipeInput, T extends Recipe<I>> boolean isMatchingInputItem(Level level, Item item,
			RecipeType<T> recipeType, IntSet inputItemIds) {
		if (inputItemIds.isEmpty()) {
			// TODO: terra plate allows special ingredients, for which this caching approach is incorrect
			level.getRecipeManager().getAllRecipesFor(recipeType).stream()
					.flatMap(holder -> holder.value().getIngredients().stream())
					.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
					.map(ItemStack::getItem)
					.mapToInt(BuiltInRegistries.ITEM::getId)
					.forEach(inputItemIds::add);
		}
		return inputItemIds.contains(BuiltInRegistries.ITEM.getId(item));
	}

	private BotaniaRecipeIngredientsCache() {}
}
