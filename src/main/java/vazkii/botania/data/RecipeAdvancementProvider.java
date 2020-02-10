package vazkii.botania.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.RecipeProvider;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.ServerPackFinder;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.fml.packs.ResourcePackLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

public class RecipeAdvancementProvider extends RecipeProvider {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final ItemStack[] triggerItemBlacklist = new ItemStack[] { new ItemStack(ModItems.livingwoodTwig),
			new ItemStack(ModItems.dreamwoodTwig), new ItemStack(ModBlocks.dreamwood), new ItemStack(Blocks.DIRT),
			new ItemStack(ModItems.grassSeeds), new ItemStack(ModBlocks.defaultAltar),
			new ItemStack(ModFluffBlocks.livingwoodSlab), new ItemStack(ModFluffBlocks.dreamwoodSlab),
			new ItemStack(Blocks.FERN), new ItemStack(Blocks.GRASS), new ItemStack(Items.WHEAT_SEEDS),
			new ItemStack(ModItems.pestleAndMortar), new ItemStack(Items.FIRE_CHARGE),
			new ItemStack(ModItems.magnetRing), new ItemStack(ModItems.manaRing) };
	private static final Tag<?>[] triggerTagBlacklist = new Tag[] { ItemTags.LOGS, ItemTags.PLANKS,
			Tags.Items.RODS_WOODEN, ModTags.Items.LIVINGROCK, ModTags.Items.LIVINGWOOD, ItemTags.SAPLINGS,
			ModTags.Items.FLOATING_FLOWERS, Tags.Items.LEATHER, Tags.Items.DYES, Tags.Items.STONE,
			Tags.Items.COBBLESTONE, Tags.Items.GLASS };

	public RecipeAdvancementProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		RecipeManager recipeManager = loadRecipesAndTags();
		Predicate<Entry<ResourceLocation, IRecipe<CraftingInventory>>> botania = e -> LibMisc.MOD_ID
				.equals(e.getKey().getNamespace());
		Predicate<Entry<ResourceLocation, IRecipe<CraftingInventory>>> nonspecial = e -> !(e
				.getValue() instanceof SpecialRecipe);
		Predicate<Ingredient> blacklist = i -> !matchesBlacklist(i);
		Predicate<Ingredient> empty = i -> !i.hasNoMatchingItems();
		ResourceLocation parentAdvancement = new ResourceLocation("minecraft", "recipes/root");
		for (Object obj : recipeManager.getRecipes(IRecipeType.CRAFTING).entrySet().stream().filter(botania)
				.filter(nonspecial).toArray()) {
			@SuppressWarnings("unchecked")
			Entry<ResourceLocation, IRecipe<CraftingInventory>> entry = (Entry<ResourceLocation, IRecipe<CraftingInventory>>) obj;
			Ingredient[] ingredients = deduplicate(entry.getValue().getIngredients().stream().filter(empty).toArray());
			Ingredient trigger = null;
			if (ingredients.length == 0) {
				continue;
			} else if (ingredients.length == 1) {
				trigger = ingredients[0];
			} else {
				Object[] triggerIngredients = Arrays.asList(ingredients).stream().filter(blacklist).toArray();
				if (triggerIngredients.length > 1 || triggerIngredients.length == 0) {
					LOGGER.info(entry.getValue().getRecipeOutput().getItem().getRegistryName());
					for (Object o : triggerIngredients) {
						LOGGER.info("    " + ((Ingredient) o).serialize());
					}
					continue;
				} else {
					trigger = (Ingredient) triggerIngredients[0];
				}
			}
			JsonObject advancement = Advancement.Builder.builder().withParentId(parentAdvancement)
					.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(entry.getKey()))
					.withCriterion("has_required_item", hasItem(Items.COBBLESTONE))
					.withRewards(AdvancementRewards.Builder.recipe(entry.getKey()))
					.withRequirementsStrategy(IRequirementsStrategy.OR).serialize();
			JsonArray items = advancement.getAsJsonObject("criteria").getAsJsonObject("has_required_item")
					.getAsJsonObject("conditions").getAsJsonArray("items");
			items.remove(0);
			items.add(trigger.serialize());
			saveRecipeAdvancement(cache, advancement,
					generator.getOutputFolder()
							.resolve("data/" + entry.getValue().getId().getNamespace() + "/advancements/recipes/"
									+ getItemCategory(entry.getValue().getRecipeOutput().getItem()) + "/"
									+ entry.getValue().getId().getPath() + ".json"));
		}
	}

	@Override
	public String getName() {
		return "Botania recipe advancements";
	}

	private RecipeManager loadRecipesAndTags() {
		ResourcePackList<ResourcePackInfo> resourcePacks = new ResourcePackList<>(ResourcePackInfo::new);
		ResourcePackLoader.loadResourcePacks(resourcePacks, RecipeAdvancementProvider::buildPackFinder);
		SimpleReloadableResourceManager resourceManager = new SimpleReloadableResourceManager(
				ResourcePackType.SERVER_DATA, Thread.currentThread());
		resourceManager.addReloadListener(new NetworkTagManager());
		RecipeManager recipeManager = new RecipeManager();
		resourceManager.addReloadListener(recipeManager);
		resourcePacks.addPackFinder(new ServerPackFinder());
		resourcePacks.reloadPacksFromFinders();
		List<ResourcePackInfo> list = Lists.newArrayList(resourcePacks.getEnabledPacks());
		for (ResourcePackInfo resourcepackinfo : resourcePacks.getAllPacks()) {
			resourcepackinfo.getPriority().func_198993_a(list, resourcepackinfo, (p_200247_0_) -> {
				return p_200247_0_;
			}, false);
		}
		resourcePacks.setEnabledPacks(list);
		List<IResourcePack> list1 = Lists.newArrayList();
		resourcePacks.getEnabledPacks().forEach((p_200244_1_) -> {
			list1.add(p_200244_1_.getResourcePack());
		});
		CompletableFuture<Unit> completablefuture = resourceManager.reloadResourcesAndThen(Util.getServerExecutor(),
				Util.getServerExecutor(), list1, CompletableFuture.completedFuture(Unit.INSTANCE));
		BooleanSupplier isDone = completablefuture::isDone;
		while (!isDone.getAsBoolean()) {
			Thread.yield();
			LockSupport.parkNanos("waiting for tasks", 100000L);
		}
		return recipeManager;
	}

	/**
	 * copy of ServerLifecycleHooks.buildPackFinder
	 */
	private static <T extends ResourcePackInfo> ResourcePackLoader.IPackInfoFinder<T> buildPackFinder(
			Map<ModFile, ? extends ModFileResourcePack> modResourcePacks,
			BiConsumer<? super ModFileResourcePack, ? super T> packSetter) {
		return (packList, factory) -> serverPackFinder(modResourcePacks, packSetter, packList, factory);
	}

	/**
	 * edited copy of ServerLifecycleHooks.serverPackFinder
	 */
	private static <T extends ResourcePackInfo> void serverPackFinder(
			Map<ModFile, ? extends ModFileResourcePack> modResourcePacks,
			BiConsumer<? super ModFileResourcePack, ? super T> packSetter, Map<String, T> packList,
			ResourcePackInfo.IFactory<? extends T> factory) {
		for (Entry<ModFile, ? extends ModFileResourcePack> e : modResourcePacks.entrySet()) {
			IModInfo mod = e.getKey().getModInfos().get(0);
			if (Objects.equals(mod.getModId(), "minecraft"))
				continue; // skip the minecraft "mod"
			final String name = "mod:" + mod.getModId();
			final T packInfo = ResourcePackInfo.createResourcePack(name, true, e::getValue, factory,
					ResourcePackInfo.Priority.TOP);
			if (packInfo == null) {
				ModLoader.get().addWarning(new ModLoadingWarning(mod, ModLoadingStage.ERROR,
						"fml.modloading.brokenresources", e.getKey()));
				continue;
			}
			packSetter.accept(e.getValue(), packInfo);
			packList.put(name, packInfo);
		}
	}

	private static boolean matchesBlacklist(Ingredient ing) {
		for (ItemStack stack : triggerItemBlacklist) {
			if (ing.test(stack)) {
				return true;
			}
		}
		for (Tag<?> tag : triggerTagBlacklist) {
			for (Object item : tag.getAllElements()) {
				if (ing.test(new ItemStack((Item) item))) {
					return true;
				}
			}
		}
		return false;
	}

	private static Ingredient[] deduplicate(Object[] ingredients) {
		List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
		for (Object obj : ingredients) {
			// ingredients1.contains doesn't work here.
			boolean contains = false;
			for (Ingredient ing : ingredients1) {
				if (ing.equals(obj)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				ingredients1.add((Ingredient) obj);
			}
		}
		return ingredients1.toArray(new Ingredient[ingredients1.size()]);
	}

	private static String getItemCategory(Item item) {
		if (item instanceof BlockItem && ((BlockItem) item).getBlock().hasTileEntity()) {
			return "decorations";
		} else if (item instanceof BlockItem) {
			return "building_blocks";
		} else if (item instanceof SwordItem || item instanceof ArmorItem) {
			return "combat";
		} else if (item instanceof ToolItem || item instanceof HoeItem || item instanceof ShearsItem) {
			return "tools";
		} else if (item.isFood()) {
			return "food";
		} else {
			return "miscellaneous";
		}
	}

}
