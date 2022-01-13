package vazkii.botania.forge.xplat;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.CorporeaIndexRequestEvent;
import vazkii.botania.api.corporea.CorporeaRequestEvent;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.forge.ForgeBotaniaCreativeTab;
import vazkii.botania.forge.integration.CurioIntegration;
import vazkii.botania.forge.mixin.AccessorRegistry;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.network.IPacket;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ForgeXplatImpl implements IXplatAbstractions {
	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public boolean isPhysicalClient() {
		return FMLLoader.getDist() == Dist.CLIENT;
	}

	@Override
	public String getBotaniaVersion() {
		return ModList.get().getModContainerById(LibMisc.MOD_ID).get()
				.getModInfo().getVersion().toString();
	}

	@Nullable
	@Override
	public IAvatarWieldable findAvatarWieldable(ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.AVATAR_WIELDABLE).orElse(null);
	}

	@Nullable
	@Override
	public IBlockProvider findBlockProvider(ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.BLOCK_PROVIDER).orElse(null);
	}

	@Nullable
	@Override
	public ICoordBoundItem findCoordBoundItem(ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.COORD_BOUND_ITEM).orElse(null);
	}

	@Nullable
	@Override
	public IExoflameHeatable findExoflameHeatable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return be.getCapability(BotaniaForgeCapabilities.EXOFLAME_HEATABLE).orElse(null);
	}

	@Nullable
	@Override
	public IHornHarvestable findHornHarvestable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		// todo non-be's need some sort of lookaside registry
		return be.getCapability(BotaniaForgeCapabilities.HORN_HARVEST).orElse(null);
	}

	@Nullable
	@Override
	public IHourglassTrigger findHourglassTrigger(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		// todo non-be's need some sort of lookaside registry
		return be.getCapability(BotaniaForgeCapabilities.HOURGLASS_TRIGGER).orElse(null);
	}

	@Nullable
	@Override
	public IWandable findWandable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		// todo non-be's need some sort of lookaside registry
		return be.getCapability(BotaniaForgeCapabilities.WANDABLE).orElse(null);
	}

	@Override
	public boolean isFluidContainer(ItemEntity item) {
		return item.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
	}

	@Override
	public boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid) {
		return item.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var result = h.drain(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					return result.getFluid() == fluid && result.getAmount() == FluidAttributes.BUCKET_VOLUME;
				})
				.orElse(false);
	}

	@Override
	public boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		return player.getItemInHand(hand).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var result = h.drain(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					return result.getFluid() == fluid && result.getAmount() == FluidAttributes.BUCKET_VOLUME;
				})
				.orElse(false);
	}

	@Override
	public boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		return player.getItemInHand(hand).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var result = h.fill(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					return result == FluidAttributes.BUCKET_VOLUME;
				})
				.orElse(false);
	}

	@Override
	public boolean fireCorporeaRequestEvent(ICorporeaRequestMatcher matcher, int itemCount, ICorporeaSpark spark, boolean dryRun) {
		return MinecraftForge.EVENT_BUS.post(new CorporeaRequestEvent(matcher, itemCount, spark, dryRun));
	}

	@Override
	public boolean fireCorporeaIndexRequestEvent(ServerPlayer player, ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		return MinecraftForge.EVENT_BUS.post(new CorporeaIndexRequestEvent(player, request, count, spark));
	}

	@Override
	public void fireManaItemEvent(Player player, List<ItemStack> toReturn) {
		MinecraftForge.EVENT_BUS.post(new ManaItemsEvent(player, toReturn));
	}

	@Override
	public float fireManaDiscountEvent(Player player, float discount, ItemStack tool) {
		var evt = new ManaDiscountEvent(player, discount, tool);
		MinecraftForge.EVENT_BUS.post(evt);
		return evt.getDiscount();
	}

	@Override
	public boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient) {
		var evt = new ManaProficiencyEvent(player, tool, proficient);
		MinecraftForge.EVENT_BUS.post(evt);
		return evt.isProficient();
	}

	@Override
	public void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside) {
		MinecraftForge.EVENT_BUS.post(new ElvenPortalUpdateEvent(portal, bounds, open, stacksInside));
	}

	@Override
	public void fireManaNetworkEvent(BlockEntity be, ManaBlockType type, ManaNetworkAction action) {
		MinecraftForge.EVENT_BUS.post(new ManaNetworkEvent(be, type, action));
	}

	@Override
	public Packet<?> toVanillaClientboundPacket(IPacket packet) {
		return ForgePacketHandler.CHANNEL.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT);
	}

	@Override
	public void sendToPlayer(Player player, IPacket packet) {
		if (!player.level.isClientSide) {
			ForgePacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					packet);
		}
	}

	private static final PacketDistributor<Pair<Level, BlockPos>> TRACKING_CHUNK_AND_NEAR = new PacketDistributor<>(
			(_d, pairSupplier) -> {
				var pair = pairSupplier.get();
				var level = pair.getFirst();
				var blockpos = pair.getSecond();
				var chunkpos = new ChunkPos(blockpos);
				return packet -> {
					var players = ((ServerChunkCache) level.getChunkSource()).chunkMap
							.getPlayers(chunkpos, false);
					for (var player : players) {
						if (player.distanceToSqr(blockpos.getX(), blockpos.getY(), blockpos.getZ()) < 64 * 64) {
							player.connection.send(packet);
						}
					}
				};
			},
			NetworkDirection.PLAY_TO_CLIENT
	);

	@Override
	public void sendToNear(Level level, BlockPos pos, IPacket packet) {
		if (!level.isClientSide) {
			ForgePacketHandler.CHANNEL.send(TRACKING_CHUNK_AND_NEAR.with(() -> Pair.of(level, pos)), packet);
		}
	}

	@Override
	public void sendToTracking(Entity e, IPacket packet) {
		if (!e.level.isClientSide) {
			ForgePacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e), packet);
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
		return BlockEntityType.Builder.of(func::apply, blocks).build(null);
	}

	@Override
	public void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener) {
		switch (type) {
			case CLIENT_RESOURCES -> MinecraftForge.EVENT_BUS.addListener(
					(RegisterClientReloadListenersEvent e) -> e.registerReloadListener(listener));
			case SERVER_DATA -> MinecraftForge.EVENT_BUS.addListener(
					(AddReloadListenerEvent e) -> e.addListener(listener));
		}
	}

	@Override
	public Item.Properties defaultItemBuilder() {
		return new Item.Properties().tab(ForgeBotaniaCreativeTab.INSTANCE);
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {
		return IForgeMenuType.create(constructor::apply);
	}

	@Override
	public Registry<Brew> createBrewRegistry() {
		// The registryKey really belongs on ModBrews, but this method is called from there,
		// so we'd like to avoid the circular dependency.
		return AccessorRegistry.callRegisterDefaulted(ResourceKey.createRegistryKey(prefix("brews")),
				LibMisc.MOD_ID + ":fallback", () -> ModBrews.fallbackBrew);
	}

	@Nullable
	@Override
	public EquipmentHandler tryCreateEquipmentHandler() {
		if (IXplatAbstractions.INSTANCE.isModLoaded("curios")) {
			CurioIntegration.init();
			return new CurioIntegration();
		}
		return null;
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menu, Consumer<FriendlyByteBuf> writeInitialData) {
		NetworkHooks.openGui(player, menu, writeInitialData);
	}

	@Override
	public Attribute getReachDistanceAttribute() {
		return ForgeMod.REACH_DISTANCE.get();
	}

	@Override
	public Attribute getStepHeightAttribute() {
		return null;
	}

	@Override
	public Tag.Named<Block> blockTag(ResourceLocation id) {
		return BlockTags.createOptional(id);
	}

	@Override
	public Tag.Named<Item> itemTag(ResourceLocation id) {
		return ItemTags.createOptional(id);
	}

	@Override
	public Tag.Named<EntityType<?>> entityTag(ResourceLocation id) {
		return EntityTypeTags.createOptional(id);
	}
}
