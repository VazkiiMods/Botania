/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import java.util.List;

public class DummyMethodHandler implements IInternalMethodHandler {

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return DummyManaNetwork.instance;
	}

	@Override
	public void drawSimpleManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name) {}

	@Override
	public void drawComplexManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {}

	@Override
	public ItemStack getBindDisplayForFlowerType(TileEntitySpecialFlower e) {
		return ItemStack.EMPTY;
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {}

	@Override
	public IItemHandlerModifiable getAccessoriesInventory(PlayerEntity player) {
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
	public void breakOnAllCursors(PlayerEntity player, Item item, ItemStack stack, BlockPos pos, Direction side) {}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return false;
	}

	@Override
	public long getWorldElapsedTicks() {
		return 0;
	}

	@Override
	public List<IWrappedInventory> wrapInventory(List<InvWithLocation> inventories) {
		return null;
	}

}
