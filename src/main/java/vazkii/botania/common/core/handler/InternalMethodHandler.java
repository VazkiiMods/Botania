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

import baubles.api.BaublesApi;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandlerModifiable;
import vazkii.botania.api.BotaniaAPIClient;
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
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.integration.corporea.WrappedIInventory;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
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
	public ModelResourceLocation getSubTileBlockModelForName(String name) {
		return BotaniaAPIClient.getRegisteredSubtileBlockModels().get(name);
	}

	@Override
	public ModelResourceLocation getSubTileItemModelForName(String name) {
		return BotaniaAPIClient.getRegisteredSubtileItemModels().get(name);
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
	public ItemStack getSubTileAsStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(subTile);
	}

	@Override
	public ItemStack getSubTileAsFloatingFlowerStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), subTile);
	}

	@Override
	public String getStackSubTileKey(ItemStack stack) {
		return ItemBlockSpecialFlower.getType(stack);
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public IInventory getBaublesInventory(EntityPlayer player) {
		return BaublesApi.getBaubles(player);
	}

	@Override
	public IItemHandlerModifiable getBaublesInventoryWrapped(EntityPlayer player) {
		return BaublesApi.getBaublesHandler(player);
	}

	@Override
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		HUDHandler.drawSimpleManaHUD(color, mana, maxMana, name, res);
	}

	@Override
	public void drawComplexManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, mana, maxMana, name, res, bindDisplay, properlyBound);
	}

	@Override
	public ItemStack getBindDisplayForFlowerType(SubTileEntity e) {
		return e instanceof SubTileGenerating ? new ItemStack(ModBlocks.spreader) : e instanceof SubTileFunctional ? new ItemStack(ModBlocks.pool) : new ItemStack(ModItems.twigWand);
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
		return ConfigHandler.flowerForceCheck;
	}

	@Override
	public int getPassiveFlowerDecay() {
		return LibMisc.PASSIVE_FLOWER_DECAY;
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Transport")
	public boolean isBuildcraftPipe(TileEntity tile) {
		return false; // tile instanceof IPipeTile; todo buildcraft
	}

	@Override
	public void breakOnAllCursors(EntityPlayer player, Item item, ItemStack stack, BlockPos pos, EnumFacing side) {
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

	// todo use our own sync packet or use the autosync system
	@Override
	public void sendBaubleUpdatePacket(EntityPlayer player, int slot) {
		if(player instanceof EntityPlayerMP) {
			PacketHandler.INSTANCE.sendTo(new PacketSync(player, slot, BaublesApi.getBaublesHandler(player).getStackInSlot(slot)), (EntityPlayerMP) player);
		}
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
