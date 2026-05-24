package vazkii.botania.common.handler;

import com.google.common.base.Suppliers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.ElvenTradeRecipe;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

// TODO: Merge with OrechidManager?
public class BotaniaRecipeIngredientsCache implements ResourceManagerReloadListener {
	private static final Supplier<BotaniaRecipeIngredientsCache> SERVER_INSTANCE = Suppliers.memoize(BotaniaRecipeIngredientsCache::new);
	private static final Supplier<BotaniaRecipeIngredientsCache> CLIENT_INSTANCE = Suppliers.memoize(BotaniaRecipeIngredientsCache::new);

	private final IntSet terraPlateInputItemIds = new IntOpenHashSet();
	private final Int2ObjectMap<Set<RecipeHolder<ElvenTradeRecipe>>> elvenTradeRecipeInputCache = new Int2ObjectOpenHashMap<>();

	public static boolean isTerraPlateInputItem(Level level, Item item) {
		return getInstance(level).isTerraPlateInputItemInternal(level, item);
	}

	public static boolean isElvenTradeInputItem(Level level, Item item) {
		return getInstance(level).isElvenTradeInputItemInternal(level, item);
	}

	public static Collection<RecipeHolder<ElvenTradeRecipe>> getElvenTradeCandidates(Level level, Item item) {
		return getInstance(level).getElvenTradeCandidatesInternal(level, item);
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
		elvenTradeRecipeInputCache.clear();
	}

	private boolean isTerraPlateInputItemInternal(Level level, Item item) {
		if (terraPlateInputItemIds.isEmpty()) {
			// TODO: terra plate allows special ingredients, for which this caching approach is incorrect
			level.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.TERRA_PLATE_TYPE).stream()
					.flatMap(holder -> holder.value().getIngredients().stream())
					.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
					.map(ItemStack::getItem)
					.mapToInt(BuiltInRegistries.ITEM::getId)
					.forEach(terraPlateInputItemIds::add);
		}
		return terraPlateInputItemIds.contains(BuiltInRegistries.ITEM.getId(item));
	}

	private boolean isElvenTradeInputItemInternal(Level level, Item item) {
		initializeElvenTradeCacheIfNecessary(level);
		return elvenTradeRecipeInputCache.containsKey(BuiltInRegistries.ITEM.getId(item));
	}

	private Collection<RecipeHolder<ElvenTradeRecipe>> getElvenTradeCandidatesInternal(Level level, Item item) {
		initializeElvenTradeCacheIfNecessary(level);
		return elvenTradeRecipeInputCache.getOrDefault(BuiltInRegistries.ITEM.getId(item), Set.of());
	}

	private void initializeElvenTradeCacheIfNecessary(Level level) {
		if (!elvenTradeRecipeInputCache.isEmpty()) {
			return;
		}
		Int2ObjectMap<Set<RecipeHolder<ElvenTradeRecipe>>> mutableCacheMap = new Int2ObjectOpenHashMap<>();
		for (RecipeHolder<ElvenTradeRecipe> holder : level.getRecipeManager()
				.getAllRecipesFor(BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
			holder.value().getIngredients().stream()
					.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
					.map(ItemStack::getItem)
					.mapToInt(BuiltInRegistries.ITEM::getId)
					.forEach(itemId -> mutableCacheMap
							.computeIfAbsent(itemId, id -> new HashSet<>())
							.add(holder));
		}
		for (Int2ObjectMap.Entry<Set<RecipeHolder<ElvenTradeRecipe>>> entry : mutableCacheMap.int2ObjectEntrySet()) {
			elvenTradeRecipeInputCache.put(entry.getIntKey(), Set.copyOf(entry.getValue()));
		}
	}

	private BotaniaRecipeIngredientsCache() {}
}
