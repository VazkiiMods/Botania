package vazkii.botania.forge.xplat;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block.HornHarvestable;
import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.CorporeaIndexRequestEvent;
import vazkii.botania.api.corporea.CorporeaRequestEvent;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.internal_caps.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.forge.ForgeBotaniaCreativeTab;
import vazkii.botania.forge.integration.curios.CurioIntegration;
import vazkii.botania.forge.internal_caps.ForgeInternalEntityCapabilities;
import vazkii.botania.forge.mixin.AbstractFurnaceBlockEntityForgeAccessor;
import vazkii.botania.forge.mixin.RecipeProviderForgeAccessor;
import vazkii.botania.forge.mixin.RegistryForgeAccessor;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.network.IPacket;
import vazkii.botania.xplat.IXplatAbstractions;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ForgeXplatImpl implements IXplatAbstractions {
	@Override
	public boolean isForge() {
		return true;
	}

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
	public IManaItem findManaItem(ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.MANA_ITEM).orElse(null);
	}

	@Nullable
	@Override
	public IRelic findRelic(ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.RELIC).orElse(null);
	}

	@Nullable
	@Override
	public ExoflameHeatable findExoflameHeatable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.EXOFLAME_HEATABLE, level, pos, state, be);
	}

	@Nullable
	@Override
	public HornHarvestable findHornHarvestable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.HORN_HARVEST, level, pos, state, be);
	}

	@Nullable
	@Override
	public HourglassTrigger findHourglassTrigger(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.HOURGLASS_TRIGGER, level, pos, state, be);
	}

	@Nullable
	@Override
	public IManaCollisionGhost findManaGhost(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.MANA_GHOST, level, pos, state, be);
	}

	@Nullable
	@Override
	public IManaReceiver findManaReceiver(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, @Nullable Direction direction) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.MANA_RECEIVER, level, pos, state, be, direction);
	}

	@Nullable
	@Override
	public ISparkAttachable findSparkAttachable(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity be, Direction direction) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.SPARK_ATTACHABLE, level, pos, blockState, be, direction);
	}

	@Nullable
	@Override
	public IManaTrigger findManaTrigger(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.MANA_TRIGGER, level, pos, state, be);
	}

	@Nullable
	@Override
	public Wandable findWandable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaForgeCapabilities.WANDABLE, level, pos, state, be);
	}

	@Override
	public boolean isFluidContainer(ItemEntity item) {
		return item.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
	}

	@Override
	public boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid) {
		return item.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var extracted = h.drain(new FluidStack(fluid, FluidType.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					var success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
					if (success) {
						item.setItem(h.getContainer());
					}
					return success;
				})
				.orElse(false);
	}

	@Override
	public boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var extracted = h.drain(new FluidStack(fluid, FluidType.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					var success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
					if (success && !player.getAbilities().instabuild) {
						player.setItemInHand(hand, h.getContainer());
					}
					return success;
				})
				.orElse(false);
	}

	@Override
	public boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(h -> {
					var filled = h.fill(new FluidStack(fluid, FluidType.BUCKET_VOLUME),
							IFluidHandler.FluidAction.EXECUTE);
					var success = filled == FluidType.BUCKET_VOLUME;
					if (success && !player.getAbilities().instabuild) {
						player.setItemInHand(hand, h.getContainer());
					}
					return success;
				})
				.orElse(false);
	}

	@Override
	public boolean hasInventory(Level level, BlockPos pos, Direction sideOfPos) {
		var be = level.getBlockEntity(pos);
		return be != null
				&& be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOfPos).isPresent();
	}

	@Override
	public ItemStack insertToInventory(Level level, BlockPos pos, Direction sideOfPos, ItemStack toInsert, boolean simulate) {
		var be = level.getBlockEntity(pos);
		if (be == null) {
			return toInsert;
		}

		var cap = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOfPos);
		return cap.map(handler -> ItemHandlerHelper.insertItemStacked(handler, toInsert, simulate))
				.orElse(toInsert);
	}

	@Override
	public EthicalComponent ethicalComponent(PrimedTnt tnt) {
		return tnt.getCapability(ForgeInternalEntityCapabilities.TNT_ETHICAL).orElseThrow(IllegalStateException::new);
	}

	@Override
	public GhostRailComponent ghostRailComponent(AbstractMinecart cart) {
		return cart.getCapability(ForgeInternalEntityCapabilities.GHOST_RAIL).orElseThrow(IllegalStateException::new);
	}

	@Override
	public ItemFlagsComponent itemFlagsComponent(ItemEntity item) {
		// If missing, just give a fresh instance - works around Create's Ponder fake world
		return item.getCapability(ForgeInternalEntityCapabilities.INTERNAL_ITEM).orElseGet(ItemFlagsComponent::new);
	}

	@Override
	public KeptItemsComponent keptItemsComponent(Player player, boolean reviveCaps) {
		if (reviveCaps) {
			// See the javadoc on reviveCaps for why this is necessary
			player.reviveCaps();
		}
		var ret = player.getCapability(ForgeInternalEntityCapabilities.KEPT_ITEMS).orElseThrow(IllegalStateException::new);
		if (reviveCaps) {
			player.invalidateCaps();
		}
		return ret;
	}

	@Nullable
	@Override
	public LooniumComponent looniumComponent(LivingEntity entity) {
		return entity.getCapability(ForgeInternalEntityCapabilities.LOONIUM_DROP).orElse(null);
	}

	@Override
	public NarslimmusComponent narslimmusComponent(Slime slime) {
		return slime.getCapability(ForgeInternalEntityCapabilities.NARSLIMMUS).orElseThrow(IllegalStateException::new);
	}

	@Override
	public TigerseyeComponent tigersEyeComponent(Creeper creeper) {
		return creeper.getCapability(ForgeInternalEntityCapabilities.TIGERSEYE).orElseThrow(IllegalStateException::new);
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
	public void fireManaNetworkEvent(IManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		MinecraftForge.EVENT_BUS.post(new ManaNetworkEvent(thing, type, action));
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
		return RegistryForgeAccessor.callRegisterDefaulted(ResourceKey.createRegistryKey(prefix("brews")),
				LibMisc.MOD_ID + ":fallback", registry -> ModBrews.fallbackBrew);
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
		NetworkHooks.openScreen(player, menu, writeInitialData);
	}

	@Override
	public Attribute getReachDistanceAttribute() {
		return ForgeMod.REACH_DISTANCE.get();
	}

	@Override
	public Attribute getStepHeightAttribute() {
		return ForgeMod.STEP_HEIGHT_ADDITION.get();
	}

	@Override
	public TagKey<Block> getOreTag() {
		return Tags.Blocks.ORES;
	}

	@Override
	public boolean isInGlassTag(BlockState state) {
		return state.is(Tags.Blocks.GLASS);
	}

	@Override
	public boolean canFurnaceBurn(AbstractFurnaceBlockEntity furnace, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize) {
		return ((AbstractFurnaceBlockEntityForgeAccessor) furnace).callCanBurn(recipe, items, maxStackSize);
	}

	@Override
	public void saveRecipeAdvancement(DataGenerator generator, CachedOutput cache, JsonObject json, Path path) {
		// this is dumb
		((RecipeProviderForgeAccessor) new RecipeProvider(generator)).callSaveRecipeAdvancement(cache, json, path);
	}

	@Override
	public Fluid getBucketFluid(BucketItem item) {
		return item.getFluid();
	}

	@Override
	public int getSmeltingBurnTime(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
	}

	@Override
	public boolean preventsRemoteMovement(ItemEntity entity) {
		return entity.getPersistentData().getBoolean("PreventRemoteMovement");
	}

	public static final Map<Block, Block> CUSTOM_STRIPPABLES = new HashMap<>();

	@Override
	public void addAxeStripping(Block input, Block output) {
		CUSTOM_STRIPPABLES.put(input, output);
	}

	@Override
	public int transferEnergyToNeighbors(Level level, BlockPos pos, int energy) {
		for (Direction e : Direction.values()) {
			BlockPos neighbor = pos.relative(e);
			if (!level.hasChunkAt(neighbor)) {
				continue;
			}

			BlockEntity be = level.getBlockEntity(neighbor);
			if (be == null) {
				continue;
			}

			LazyOptional<IEnergyStorage> storage = LazyOptional.empty();

			if (be.getCapability(CapabilityEnergy.ENERGY, e.getOpposite()).isPresent()) {
				storage = be.getCapability(CapabilityEnergy.ENERGY, e.getOpposite());
			} else if (be.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {
				storage = be.getCapability(CapabilityEnergy.ENERGY, null);
			}

			if (storage.isPresent()) {
				energy -= storage.orElseThrow(NullPointerException::new).receiveEnergy(energy, false);

				if (energy <= 0) {
					return 0;
				}
			}
		}
		return energy;
	}

	@Override
	public boolean isRedStringContainerTarget(BlockEntity be) {
		for (Direction dir : Direction.values()) {
			if (be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir).isPresent()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public TileRedStringContainer newRedStringContainer(BlockPos pos, BlockState state) {
		return new TileRedStringContainer(pos, state);
	}
}
