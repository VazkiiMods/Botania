/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.configdata.ConfigDataManager;
import vazkii.botania.api.corporea.CorporeaNodeDetector;
import vazkii.botania.api.internal.ManaNetwork;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.block_entity.flower.misc.SolegnoliaBlockEntity;
import vazkii.botania.common.config.ConfigDataManagerImpl;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.handler.ManaNetworkHandler;
import vazkii.botania.common.helper.RegistryHelper;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.item.BotaniaArmorMaterials;
import vazkii.botania.common.item.BotaniaItemTiers;
import vazkii.botania.common.item.relic.RingOfLokiItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BotaniaAPIImpl implements BotaniaAPI {

	private ConfigDataManager configDataManager = new ConfigDataManagerImpl();

	@Override
	public int apiVersion() {
		return 4;
	}

	@Override
	public DefaultedRegistry<Brew> getBrewRegistry() {
		return (DefaultedRegistry<Brew>) RegistryHelper.getRegistry(BotaniaRegistries.BREWS);
	}

	@Override
	public DefaultedRegistry<IslandType> getIslandTypeRegistry() {
		return (DefaultedRegistry<IslandType>) RegistryHelper.getRegistry(BotaniaRegistries.ISLAND_TYPES);
	}

	@Override
	public Holder<ArmorMaterial> getManasteelArmorMaterial() {
		return BotaniaArmorMaterials.MANASTEEL;
	}

	@Override
	public Holder<ArmorMaterial> getElementiumArmorMaterial() {
		return BotaniaArmorMaterials.ELEMENTIUM;
	}

	@Override
	public Holder<ArmorMaterial> getManaweaveArmorMaterial() {
		return BotaniaArmorMaterials.MANAWEAVE;
	}

	@Override
	public Holder<ArmorMaterial> getTerrasteelArmorMaterial() {
		return BotaniaArmorMaterials.TERRASTEEL;
	}

	@Override
	public Tier getManasteelItemTier() {
		return BotaniaItemTiers.MANASTEEL;
	}

	@Override
	public Tier getElementiumItemTier() {
		return BotaniaItemTiers.ELEMENTIUM;
	}

	@Override
	public Tier getTerrasteelItemTier() {
		return BotaniaItemTiers.TERRASTEEL;
	}

	@Override
	public ManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public Container getAccessoriesInventory(Player player) {
		return EquipmentHandler.getAllWorn(player);
	}

	@Override
	public void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side) {
		RingOfLokiItem.breakOnAllCursors(player, stack, pos, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SolegnoliaBlockEntity.hasSolegnoliaAround(e);
	}

	@Override
	public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float size, int m) {
		SparkleParticleData data = SparkleParticleData.sparkle(size, r, g, b, m);
		world.addParticle(data, x, y, z, 0, 0, 0);
	}

	private final Map<ResourceLocation, Function<DyeColor, Block>> paintableBlocks = new ConcurrentHashMap<>();

	@Override
	public Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.unmodifiableMap(paintableBlocks);
	}

	@Override
	public void registerPaintableBlock(ResourceLocation block, Function<DyeColor, Block> transformer) {
		paintableBlocks.put(block, transformer);
	}

	@Override
	public void registerCorporeaNodeDetector(CorporeaNodeDetector detector) {
		CorporeaNodeDetectors.register(detector);
	}

	@Override
	public ConfigDataManager getConfigData() {
		return configDataManager;
	}

	@Override
	public void setConfigData(ConfigDataManager configDataManager) {
		this.configDataManager = configDataManager;
	}

	// API lookup helper methods, used by the API IDs

	@Nullable
	@Override
	public <A> A findBlockApi(BlockApiNoContext<A> id, Level level, BlockPos pos, @Nullable BlockState state,
			@Nullable BlockEntity blockEntity) {
		return XplatAbstractions.INSTANCE.findBlockApi(id, level, pos, state, blockEntity);
	}

	@Nullable
	@Override
	public <A, C> A findBlockApi(BlockApiWithContext<A, C> id, Level level, BlockPos pos,
			@Nullable BlockState state, @Nullable BlockEntity blockEntity, @Nullable C context) {
		return XplatAbstractions.INSTANCE.findBlockApi(id, level, pos, state, blockEntity, context);
	}

	@Nullable
	@Override
	public <A> A findItemApi(ItemApiNoContext<A> id, ItemStack stack) {
		return XplatAbstractions.INSTANCE.findItemApi(id, stack);
	}

	@Nullable
	@Override
	public <A, C> A findItemApi(ItemApiWithContext<A, C> id, ItemStack stack, @Nullable C context) {
		return XplatAbstractions.INSTANCE.findItemApi(id, stack, context);
	}

	@Nullable
	@Override
	public <A> A findEntityApi(EntityApiNoContext<A> id, Entity entity) {
		return XplatAbstractions.INSTANCE.findEntityApi(id, entity);
	}

	@Nullable
	@Override
	public <A, C> A findEntityApi(EntityApiWithContext<A, C> id, Entity entity, @Nullable C context) {
		return XplatAbstractions.INSTANCE.findEntityApi(id, entity, context);
	}

	// entity data attachment helper methods, used by the data IDs

	@Nullable
	@Override
	public <T> T getEntityData(DataIdBase<T> id, Entity entity) {
		return XplatAbstractions.INSTANCE.getEntityData(id, entity);
	}

	@Override
	public <T> void setEntityData(DataIdBase<T> id, Entity entity, T data) {
		XplatAbstractions.INSTANCE.setEntityData(id, entity, data);
	}

	@Override
	public void removeEntityData(DataIdBase<?> id, Entity entity) {
		XplatAbstractions.INSTANCE.removeEntityData(id, entity);
	}
}
