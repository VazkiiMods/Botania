/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:34:34 PM (GMT)]
 */
package vazkii.botania.api.internal;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;

import java.util.List;

/**
 * Any methods that refer to internal methods in Botania are here.
 * This is defaulted to a dummy handler, whose methods do nothing.
 * This handler is set to a proper one on PreInit. Make sure to
 * make your mod load after Botania if you have any intention of
 * doing anythign with this on PreInit.
 */
public interface IInternalMethodHandler {

	public LexiconPage textPage(String key);

	public LexiconPage elfPaperTextPage(String key);

	public LexiconPage imagePage(String key, String resource);

	public LexiconPage craftingRecipesPage(String key, List<ResourceLocation> recipes);

	public LexiconPage craftingRecipePage(String key, ResourceLocation recipe);

	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes);

	public LexiconPage petalRecipePage(String key, RecipePetals recipe);

	public LexiconPage runeRecipesPage(String key, List<RecipeRuneAltar> recipes);

	public LexiconPage runeRecipePage(String key, RecipeRuneAltar recipe);

	public LexiconPage manaInfusionRecipesPage(String key, List<RecipeManaInfusion> recipes);

	public LexiconPage manaInfusionRecipePage(String key, RecipeManaInfusion recipe);

	public LexiconPage elvenTradePage(String key, List<RecipeElvenTrade> recipes);

	public LexiconPage elvenTradesPage(String key, RecipeElvenTrade recipe);

	public LexiconPage brewPage(String key, String bottomText, RecipeBrew recipe);

	public LexiconPage multiblockPage(String key, MultiblockSet mb);

	public IManaNetwork getManaNetworkInstance();

	public ItemStack getSubTileAsStack(String subTile);

	public ItemStack getSubTileAsFloatingFlowerStack(String subTile);

	public String getStackSubTileKey(ItemStack stack);

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getSubTileBlockModelForName(String name);

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getSubTileItemModelForName(String name);

	public boolean shouldForceCheck();

	public int getPassiveFlowerDecay();

	public IInventory getBaublesInventory(EntityPlayer player);

	public IItemHandlerModifiable getBaublesInventoryWrapped(EntityPlayer player);

	public void breakOnAllCursors(EntityPlayer player, Item item, ItemStack stack, BlockPos pos, EnumFacing side);

	public boolean hasSolegnoliaAround(Entity e);

	@SideOnly(Side.CLIENT)
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res);

	@SideOnly(Side.CLIENT)
	public void drawComplexManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res, ItemStack bindDisplay, boolean properlyBound);

	public ItemStack getBindDisplayForFlowerType(SubTileEntity e);

	public void renderLexiconText(int x, int y, int width, int height, String unlocalizedText);

	public ResourceLocation getDefaultBossBarTexture();

	public boolean isBuildcraftPipe(TileEntity tile);

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m);

	public long getWorldElapsedTicks();

	public boolean isBotaniaFlower(World world, BlockPos pos);

	public void sendBaubleUpdatePacket(EntityPlayer player, int slot);

	/**
	 * Wrap inventories in the network into wrappers providing compatibility for storage mods.
	 */
	List<IWrappedInventory> wrapInventory(List<InvWithLocation> inventories);

}
