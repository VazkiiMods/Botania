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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import java.util.List;

/**
 * Any methods that refer to internal methods in Botania are here.
 * This is defaulted to a dummy handler, whose methods do nothing.
 * This handler is set to a proper one on PreInit. Make sure to
 * make your mod load after Botania if you have any intention of
 * doing anythign with this on PreInit.
 */
public interface IInternalMethodHandler {

	public IManaNetwork getManaNetworkInstance();

	public boolean shouldForceCheck();

	public int getPassiveFlowerDecay();

	public IItemHandlerModifiable getAccessoriesInventory(PlayerEntity player);

	public void breakOnAllCursors(PlayerEntity player, Item item, ItemStack stack, BlockPos pos, Direction side);

	public boolean hasSolegnoliaAround(Entity e);

	@OnlyIn(Dist.CLIENT)
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name);

	@OnlyIn(Dist.CLIENT)
	public void drawComplexManaHUD(int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound);

	public ItemStack getBindDisplayForFlowerType(TileEntitySpecialFlower e);

	public void renderLexiconText(int x, int y, int width, int height, String unlocalizedText);

	public ResourceLocation getDefaultBossBarTexture();

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m);

	public long getWorldElapsedTicks();

	public boolean isBotaniaFlower(World world, BlockPos pos);

	/**
	 * Wrap inventories in the network into wrappers providing compatibility for storage mods.
	 */
	List<IWrappedInventory> wrapInventory(List<InvWithLocation> inventories);

}
