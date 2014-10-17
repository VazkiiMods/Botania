/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:44:59 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import baubles.common.lib.PlayerHandler;

public class InternalMethodHandler extends DummyMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return new PageText(key);
	}

	@Override
	public LexiconPage imagePage(String key, String resource) {
		return new PageImage(key, resource);
	}

	@Override
	public LexiconPage craftingRecipesPage(String key, List<IRecipe> recipes) {
		return new PageCraftingRecipe(key, recipes);
	}

	@Override
	public LexiconPage craftingRecipePage(String key, IRecipe recipe) {
		return new PageCraftingRecipe(key, recipe);
	}

	@Override
	public IIcon getSubTileIconForName(String name) {
		IIcon icon = (ConfigHandler.altFlowerTextures ? BlockSpecialFlower.iconsAlt : BlockSpecialFlower.icons).get(name);
		return icon == null ? Blocks.red_flower.getIcon(0, 0) : icon;
	}

	@Override
	public void registerBasicSignatureIcons(String name, IIconRegister register) {
		IIcon normal = IconHelper.forName(register, name);
		IIcon alt = IconHelper.forName(register, BlockModFlower.ALT_DIR + "/" + name);
		BlockSpecialFlower.icons.put(name, normal);
		BlockSpecialFlower.iconsAlt.put(name, alt == null ? normal : alt);	
	}
	
	@Override
	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes) {
		return new PagePetalRecipe(key, recipes);
	}

	@Override
	public LexiconPage petalRecipePage(String key, RecipePetals recipe) {
		return new PagePetalRecipe(key, recipe);
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
	public ItemStack getSubTileAsStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(subTile);
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public IInventory getBaublesInventory(EntityPlayer player) {
		return PlayerHandler.getPlayerBaubles(player);
	}

	@Override
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		HUDHandler.drawSimpleManaHUD(color, mana, maxMana, name, res);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		Botania.proxy.sparkleFX(world, x, y, z, r, g, b, size, m);
	}

	@Override
	public boolean shouldForceCheck() {
		return ConfigHandler.flowerForceCheck;
	}
	
	@Override
	public int getPassiveFlowerDecay() {
		return ConfigHandler.hardcorePassiveGeneration;
	}

}
