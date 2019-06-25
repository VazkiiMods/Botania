/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:44:59 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.integration.corporea.WrappedIInventory;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageLoreText;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class InternalMethodHandler extends DummyMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return new PageText(key);
	}

	@Override
	public LexiconPage elfPaperTextPage(String key) {
		return new PageLoreText(key);
	}

	@Override
	public LexiconPage imagePage(String key, String resource) {
		return new PageImage(key, resource);
	}

	@Override
	public LexiconPage craftingRecipesPage(String key, List<ResourceLocation> recipes) {
		return new PageCraftingRecipe(key, recipes);
	}

	@Override
	public LexiconPage craftingRecipePage(String key, ResourceLocation recipe) {
		return new PageCraftingRecipe(key, recipe);
	}

	@Override
	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes) {
		return new PagePetalRecipe<>(key, recipes);
	}

	@Override
	public LexiconPage petalRecipePage(String key, RecipePetals recipe) {
		return new PagePetalRecipe<>(key, recipe);
	}

	@Override
	public LexiconPage runeRecipesPage(String key, List<RecipeRuneAltar> recipes) {
		return new PageRuneRecipe(key, recipes);
	}

	@Override
	public LexiconPage runeRecipePage(String key, RecipeRuneAltar recipe) {
		return new PageRuneRecipe(key, recipe);
	}

	@Override
	public LexiconPage manaInfusionRecipesPage(String key, List<RecipeManaInfusion> recipes) {
		return new PageManaInfusionRecipe(key, recipes);
	}

	@Override
	public LexiconPage manaInfusionRecipePage(String key, RecipeManaInfusion recipe) {
		return new PageManaInfusionRecipe(key, recipe);
	}

	@Override
	public LexiconPage elvenTradePage(String key, List<RecipeElvenTrade> recipes) {
		return new PageElvenRecipe(key, recipes);
	}

	@Override
	public LexiconPage elvenTradesPage(String key, RecipeElvenTrade recipe) {
		return new PageElvenRecipe(key, recipe);
	}

	@Override
	public LexiconPage brewPage(String key, String bottomText, RecipeBrew recipe) {
		return new PageBrew(recipe, key, bottomText);
	}

	@Override
	public LexiconPage multiblockPage(String key, MultiblockSet mb) {
		return new PageMultiblock(key, mb);
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public IItemHandlerModifiable getAccessoriesInventory(PlayerEntity player) {
		if(Botania.curiosLoaded) {
			LazyOptional<IItemHandlerModifiable> cap = EquipmentHandler.getAllWorn(player);
			return cap.orElseGet(EmptyHandler::new);
		}
		return new EmptyHandler();
	}

	@Override
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name) {
		HUDHandler.drawSimpleManaHUD(color, mana, maxMana, name);
	}

	@Override
	public void drawComplexManaHUD(int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, mana, maxMana, name, bindDisplay, properlyBound);
	}

	@Override
	public ItemStack getBindDisplayForFlowerType(TileEntitySpecialFlower e) {
		return e instanceof TileEntityGeneratingFlower ? new ItemStack(ModBlocks.manaSpreader) : e instanceof TileEntityFunctionalFlower ? new ItemStack(ModBlocks.manaPool) : new ItemStack(ModItems.twigWand);
	}

	@Override
	public void renderLexiconText(int x, int y, int width, int height, String unlocalizedText) {
		PageText.renderText(x, y, width, height, unlocalizedText);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		Botania.proxy.sparkleFX(x, y, z, r, g, b, size, m);
	}

	@Override
	public ResourceLocation getDefaultBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}

	@Override
	public boolean shouldForceCheck() {
		return ConfigHandler.COMMON.flowerForceCheck.get();
	}

	@Override
	public int getPassiveFlowerDecay() {
		return LibMisc.PASSIVE_FLOWER_DECAY;
	}

	@Override
	public boolean isBuildcraftPipe(TileEntity tile) {
		return false; // tile instanceof IPipeTile; todo buildcraft
	}

	@Override
	public void breakOnAllCursors(PlayerEntity player, Item item, ItemStack stack, BlockPos pos, Direction side) {
		ItemLokiRing.breakOnAllCursors(player, item, stack, pos, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SubTileSolegnolia.hasSolegnoliaAround(e);
	}

	@Override
	public long getWorldElapsedTicks() {
		return Botania.proxy.getWorldElapsedTicks();
	}

	@Override
	public boolean isBotaniaFlower(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block instanceof BlockModFlower || block instanceof BlockFloatingFlower || block instanceof ISpecialFlower;
	}

	@Override
	public List<IWrappedInventory> wrapInventory(List<InvWithLocation> inventories) {
		List<IWrappedInventory> arrayList = new ArrayList<IWrappedInventory>();
		for(InvWithLocation inv : inventories) {
			ICorporeaSpark spark = CorporeaHelper.getSparkForInventory(inv);
			IWrappedInventory wrapped = null;
			// try integrations

			// last chance - this will always work
			if(wrapped == null) {
				wrapped = WrappedIInventory.wrap(inv, spark);
			}
			arrayList.add(wrapped);
		}
		return arrayList;
	}
}
