/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 6:43:03 PM (GMT)]
 */
package vazkii.botania.api.internal;

import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public class DummyMethodHandler implements IInternalMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage elfPaperTextPage(String key) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage imagePage(String key, String resource) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage craftingRecipesPage(String key, List<IRecipe> recipes) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage craftingRecipePage(String key, IRecipe recipe) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage petalRecipePage(String key, RecipePetals recipe) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage runeRecipesPage(String key, List<RecipeRuneAltar> recipes) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage runeRecipePage(String key, RecipeRuneAltar recipe) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage manaInfusionRecipesPage(String key, List<RecipeManaInfusion> recipes) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage manaInfusionRecipePage(String key, RecipeManaInfusion recipe) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage elvenTradePage(String key, List<RecipeElvenTrade> recipes) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage elvenTradesPage(String key, RecipeElvenTrade recipe) {
		return dummyPage(key);
	}

	@Override
	public LexiconPage brewPage(String key, String bottomText, RecipeBrew recipe) {
		return dummyPage(key);
	}

	private LexiconPage dummyPage(String key) {
		return new DummyPage(key);
	}

	@Override
	public ItemStack getSubTileAsStack(String subTile) {
		return new ItemStack(Blocks.stone, 0, 0);
	}

	@Override
	public ItemStack getSubTileAsFloatingFlowerStack(String subTile) {
		return getSubTileAsStack(subTile);
	}

	@Override
	public String getStackSubTileKey(ItemStack stack) {
		return null;
	}

	@Override
	public IIcon getSubTileIconForName(String name) {
		return Blocks.red_flower.getIcon(0, 0);
	}

	@Override
	public void registerBasicSignatureIcons(String name, IIconRegister register) {
		// NO-OP
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return DummyManaNetwork.instance;
	}

	@Override
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		// NO-OP
	}

	@Override
	public void renderLexiconText(int x, int y, int width, int height, String unlocalizedText) {
		// NO-OP
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		// NO-OP
	}

	@Override
	public IInventory getBaublesInventory(EntityPlayer player) {
		return null;
	}

	@Override
	public boolean shouldForceCheck() {
		return true;
	}

	@Override
	public int getPassiveFlowerDecay() {
		return 0;
	}

	@Override
	public ResourceLocation getDefaultBossBarTexture() {
		return null;
	}

	@Override
	public void setBossStatus(IBotaniaBoss status) {
		// NO-OP
	}

	@Override
	public boolean isBuildcraftPipe(TileEntity tile) {
		return false;
	}


	@Override
	public void breakOnAllCursors(EntityPlayer player, Item item, ItemStack stack, int x, int y, int z, int side) {
		// NO-OP
	}


}
