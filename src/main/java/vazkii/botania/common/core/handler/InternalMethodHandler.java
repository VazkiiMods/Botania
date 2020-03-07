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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.integration.corporea.WrappedIInventory;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class InternalMethodHandler extends DummyMethodHandler {
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
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
        SparkleParticleData data = SparkleParticleData.sparkle(size, r, g, b, m);
        world.addParticle(data, x, y, z, 0, 0, 0);
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
