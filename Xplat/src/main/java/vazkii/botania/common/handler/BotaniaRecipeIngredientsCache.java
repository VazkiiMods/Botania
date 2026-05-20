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
		return level.isClientSide ? CLIENT_INSTANCE.get() : SERVER_INSTANCE.get();
	}

	@ApiStatus.Internal
	@Override
	public void onResourceManagerReload(@Nullable ResourceManager resourceManager) {
		clearCache();
	}

	private void clearCache() {
		terraPlateInputItemIds.clear();
	}

	public static boolean isTerraPlateInputItem(Level level, Item item) {
		return getInstance(level).isTerraPlateInputItemInternal(level, item);
	}

	private boolean isTerraPlateInputItemInternal(Level level, Item item) {
		if (terraPlateInputItemIds.isEmpty()) {
			scanTerraPlateIngredients(level);
		}
		return terraPlateInputItemIds.contains(BuiltInRegistries.ITEM.getId(item));
	}

	private void scanTerraPlateIngredients(Level level) {
		level.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.TERRA_PLATE_TYPE).stream()
				.flatMap(holder -> holder.value().getIngredients().stream())
				.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
				.map(ItemStack::getItem)
				.mapToInt(BuiltInRegistries.ITEM::getId)
				.forEach(terraPlateInputItemIds::add);
	}

	private BotaniaRecipeIngredientsCache() {}
}
