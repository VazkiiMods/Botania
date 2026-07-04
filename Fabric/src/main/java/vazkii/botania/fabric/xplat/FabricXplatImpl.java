/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.fabric.xplat;

import com.redlimerl.speedrunigt.timer.InGameTimer;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.fabric.impl.recipe.ingredient.ShapelessMatch;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaFabricCapabilities;
import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.corporea.CorporeaIndexRequestCallback;
import vazkii.botania.api.corporea.CorporeaRequestCallback;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.recipe.ElvenPortalUpdateCallback;
import vazkii.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.CustomDamageItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.fabric.block_entity.FabricRedStringContainerBlockEntity;
import vazkii.botania.fabric.integration.speedrunigt.BotaniaSpeedrunIGTPlugin;
import vazkii.botania.fabric.integration.tr_energy.FluxfieldTRStorage;
import vazkii.botania.fabric.integration.trinkets.TrinketsIntegration;
import vazkii.botania.fabric.internal_caps.FabricInternalEntityAttachments;
import vazkii.botania.fabric.mixin.AbstractFurnaceBlockEntityFabricAccessor;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.integration.speedrunigt.BotaniaSpeedrunCategories.BotaniaSpeedrunCategory;

@SuppressWarnings("UnstableApiUsage")
public class FabricXplatImpl implements XplatAbstractions {
	@Override
	public boolean isFabric() {
		return true;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean isDataGen() {
		return FabricDataGenHelper.ENABLED;
	}

	@Override
	public boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public String getBotaniaVersion() {
		return FabricLoader.getInstance().getModContainer(BotaniaAPI.MODID).orElseThrow()
				.getMetadata().getVersion().getFriendlyString();
	}

	// capability API lookup helper methods

	@Override
	public @Nullable <A> A findBlockApi(BlockApiNoContext<A> id, Level level, BlockPos pos, @Nullable BlockState state,
			@Nullable BlockEntity entity) {
		return BotaniaFabricCapabilities.getBlockApiLookupById(id).find(level, pos, state, entity, Unit.INSTANCE);
	}

	@Override
	public @Nullable <A, C> A findBlockApi(BlockApiWithContext<A, C> id, Level level, BlockPos pos,
			@Nullable BlockState state, @Nullable BlockEntity entity, @UnknownNullability C context) {
		return BotaniaFabricCapabilities.getBlockApiLookupById(id).find(level, pos, state, entity, context);
	}

	@Override
	public @Nullable <A> A findEntityApi(EntityApiNoContext<A> id, Entity entity) {
		return BotaniaFabricCapabilities.getEntityApiLookupById(id).find(entity, Unit.INSTANCE);
	}

	@Override
	public @Nullable <A, C> A findEntityApi(EntityApiWithContext<A, C> id, Entity entity, @UnknownNullability C context) {
		return BotaniaFabricCapabilities.getEntityApiLookupById(id).find(entity, context);
	}

	@Override
	public @Nullable <A> A findItemApi(ItemApiNoContext<A> id, ItemStack stack) {
		return BotaniaFabricCapabilities.getItemApiLookupById(id).find(stack, Unit.INSTANCE);
	}

	@Override
	public @Nullable <A, C> A findItemApi(ItemApiWithContext<A, C> id, ItemStack stack, @UnknownNullability C context) {
		return BotaniaFabricCapabilities.getItemApiLookupById(id).find(stack, context);
	}

	// data attachment helper methods

	@Nullable
	@Override
	public <T> T getEntityData(DataIdBase<T> id, Entity entity) {
		return entity.getAttached(FabricInternalEntityAttachments.getAttachmentTypeById(id));
	}

	@Override
	public <T> void setEntityData(DataIdBase<T> id, Entity entity, T data) {
		entity.setAttached(FabricInternalEntityAttachments.getAttachmentTypeById(id), data);
	}

	@Override
	public void removeEntityData(DataIdBase<?> id, Entity entity) {
		entity.removeAttached(FabricInternalEntityAttachments.getAttachmentTypeById(id));
	}

	private static class SingleStackEntityStorage extends SingleStackStorage {
		private final ItemEntity entity;

		private SingleStackEntityStorage(ItemEntity entity) {
			this.entity = entity;
		}

		@Override
		protected ItemStack getStack() {
			return entity.getItem();
		}

		@Override
		protected void setStack(ItemStack stack) {
			entity.setItem(stack);
		}
	}

	@Override
	public boolean isFluidContainer(ItemEntity item) {
		return ContainerItemContext.ofSingleSlot(new SingleStackEntityStorage(item)).find(FluidStorage.ITEM) != null;
	}

	@Override
	public boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid) {
		var fluidStorage = ContainerItemContext.ofSingleSlot(new SingleStackEntityStorage(item)).find(FluidStorage.ITEM);
		if (fluidStorage == null) {
			return false;
		}
		try (Transaction txn = Transaction.openOuter()) {
			long extracted = fluidStorage.extract(FluidVariant.of(fluid), FluidConstants.BLOCK, txn);
			if (extracted == FluidConstants.BLOCK) {
				txn.commit();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var fluidStorage = ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM);
		if (fluidStorage == null) {
			return false;
		}
		try (Transaction txn = Transaction.openOuter()) {
			long extracted = fluidStorage.extract(FluidVariant.of(fluid), FluidConstants.BUCKET, txn);
			if (extracted == FluidConstants.BUCKET) {
				if (!player.hasInfiniteMaterials()) {
					// Only perform inventory side effects in survival
					txn.commit();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var fluidStorage = ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM);

		if (fluidStorage == null) {
			return false;
		}

		try (Transaction txn = Transaction.openOuter()) {
			long inserted = fluidStorage.insert(FluidVariant.of(fluid), FluidConstants.BUCKET, txn);
			if (inserted == FluidConstants.BUCKET) {
				if (!player.hasInfiniteMaterials()) {
					// Only perform inventory side effects in survival
					txn.commit();
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack fillItemWithWater(ItemStack stackToFill, Player player) {
		ItemStack split = stackToFill.copyWithCount(1);
		var context = new SingleStackStorage() {
			private ItemStack stack = split;

			@Override
			protected ItemStack getStack() {
				return stack;
			}

			@Override
			protected void setStack(ItemStack stack) {
				this.stack = stack;
			}
		};
		Storage<FluidVariant> storageViews = FluidStorage.ITEM.find(split, ContainerItemContext.ofSingleSlot(context));
		if (storageViews != null && storageViews.supportsInsertion()) {
			try (var tx = Transaction.openOuter()) {
				long inserted = storageViews.insert(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, tx);
				if (inserted > 0) {
					tx.commit();
					return context.getStack();
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean hasInventory(Level level, BlockPos pos, Direction sideOfPos) {
		var state = level.getBlockState(pos);
		var be = level.getBlockEntity(pos);
		return ItemStorage.SIDED.find(level, pos, state, be, sideOfPos) != null;
	}

	@Override
	public ItemStack insertToInventory(Level level, BlockPos pos, Direction sideOfPos, ItemStack toInsert, boolean simulate) {
		if (toInsert.isEmpty()) {
			//It is valid for Storage#insert implementations to crash when provided empty variants
			return toInsert;
		}

		var state = level.getBlockState(pos);
		var be = level.getBlockEntity(pos);
		var storage = ItemStorage.SIDED.find(level, pos, state, be, sideOfPos);
		if (storage == null) {
			return toInsert;
		}

		var itemVariant = ItemVariant.of(toInsert);
		try (Transaction txn = Transaction.openOuter()) {
			// Truncation to int ok since the value passed in was an int
			// and that value should only decrease or stay same
			int inserted;
			if (state.is(BotaniaTags.Blocks.SINGLE_ITEM_INSERT)) {
				int alreadyInserted = 0;
				for (int i = 0; i < toInsert.getCount(); i++) {
					alreadyInserted += (int) storage.insert(itemVariant, 1L, txn);
				}
				inserted = alreadyInserted;
			} else {
				inserted = (int) storage.insert(itemVariant, toInsert.getCount(), txn);
			}
			if (!simulate) {
				txn.commit();
			}

			if (inserted == toInsert.getCount()) {
				return ItemStack.EMPTY;
			} else {
				var ret = toInsert.copy();
				ret.setCount(toInsert.getCount() - inserted);
				return ret;
			}
		}
	}

	@Override
	public boolean fireCorporeaRequestEvent(CorporeaRequestMatcher matcher, int itemCount, CorporeaSpark spark, boolean dryRun) {
		return CorporeaRequestCallback.EVENT.invoker().onRequest(matcher, itemCount, spark, dryRun);
	}

	@Override
	public boolean fireCorporeaIndexRequestEvent(ServerPlayer player, CorporeaRequestMatcher request, int count, CorporeaSpark spark) {
		return CorporeaIndexRequestCallback.EVENT.invoker().onIndexRequest(player, request, count, spark);
	}

	@Override
	public void fireManaItemEvent(Player player, List<ItemStack> toReturn) {
		ManaItemsCallback.EVENT.invoker().getManaItems(player, toReturn);
	}

	@Override
	public float fireManaDiscountEvent(Player player, float discount, ItemStack tool) {
		return ManaDiscountCallback.EVENT.invoker().getManaDiscount(player, discount, tool);
	}

	@Override
	public boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient) {
		return ManaProficiencyCallback.EVENT.invoker().getProficient(player, tool, proficient);
	}

	@Override
	public void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside) {
		ElvenPortalUpdateCallback.EVENT.invoker().onElvenPortalTick(portal, bounds, open, stacksInside);
	}

	@Override
	public void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		ManaNetworkCallback.EVENT.invoker().onNetworkChange(thing, type, action);
	}

	@Override
	public void sendToPlayer(@Nullable Player player, CustomPacketPayload packet) {
		if (player instanceof ServerPlayer serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, packet);
		}
	}

	@Override
	public void sendToNear(Level level, BlockPos pos, CustomPacketPayload packet) {
		var pkt = ServerPlayNetworking.createS2CPacket(packet);
		for (var player : PlayerLookup.tracking((ServerLevel) level, pos)) {
			if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
				player.connection.send(pkt);
			}
		}
	}

	@Override
	public void sendToTracking(Entity e, CustomPacketPayload packet) {
		var pkt = ServerPlayNetworking.createS2CPacket(packet);
		PlayerLookup.tracking(e).forEach(p -> p.connection.send(pkt));
		if (e instanceof ServerPlayer) {
			((ServerPlayer) e).connection.send(pkt);
		}
	}

	@Override
	public void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener) {
		ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler,
					ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
				return listener.reload(barrier, manager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
			}

			@Override
			public ResourceLocation getFabricId() {
				return id;
			}
		});
	}

	@Override
	public Item.Properties defaultItemBuilder() {
		return new Item.Properties();
	}

	@Override
	public Item.Properties defaultItemBuilderWithCustomDamageOnFabric() {
		return defaultItemBuilder().customDamage((stack, amount, entity, slot, breakCallback) -> {
			var item = stack.getItem();
			if (item instanceof CustomDamageItem cd) {
				return cd.damageItem(stack, amount, entity, i -> breakCallback.run());
			}
			return amount;
		});
	}

	@Override
	public <T extends AbstractContainerMenu, D> MenuType<T> createMenuType(TriFunction<Integer, Inventory, D, T> constructor, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec) {
		return new ExtendedScreenHandlerType<>(constructor::apply, streamCodec);
	}

	@Nullable
	@Override
	public EquipmentHandler tryCreateEquipmentHandler() {
		if (isModLoaded("trinkets")) {
			TrinketsIntegration.init();
			return new TrinketsIntegration();
		}
		return null;
	}

	@Override
	public <D> void openMenu(ServerPlayer player, MenuProvider menu, D initialData, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec) {
		var menuProvider = new ExtendedScreenHandlerFactory<D>() {
			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
				return menu.createMenu(id, inventory, player);
			}

			@Override
			public Component getDisplayName() {
				return menu.getDisplayName();
			}

			@Override
			public D getScreenOpeningData(ServerPlayer player) {
				return initialData;
			}
		};
		player.openMenu(menuProvider);
	}

	@Override
	public boolean canFurnaceBurn(AbstractFurnaceBlockEntity furnace, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> items, int maxStackSize) {
		return AbstractFurnaceBlockEntityFabricAccessor.botania_canBurn(furnace.getLevel().registryAccess(), recipeHolder, items, maxStackSize);
	}

	@Override
	public Fluid getBucketFluid(BucketItem item) {
		return ((BucketItemAccessor) item).fabric_getFluid();
	}

	@Override
	public int getSmeltingBurnTime(ItemStack stack) {
		Integer v = FuelRegistry.INSTANCE.get(stack.getItem());
		return v == null ? 0 : v;
	}

	@Override
	public boolean preventsRemoteMovement(ItemEntity entity) {
		return false;
	}

	public static final Map<Block, Block> CUSTOM_STRIPPING = new LinkedHashMap<>();

	@Override
	public void addAxeStripping(Block input, Block output) {
		if (input.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS)
				&& output.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS)) {
			StrippableBlockRegistry.register(input, output);
		} else {
			CUSTOM_STRIPPING.put(input, output);
		}
	}

	@Override
	public Map<Block, Block> getCustomStrippables() {
		return Collections.unmodifiableMap(CUSTOM_STRIPPING);
	}

	@Override
	public int transferEnergyToNeighbors(Level level, BlockPos pos, int energy) {
		if (isModLoaded("team_reborn_energy")) {
			return FluxfieldTRStorage.transferEnergyToNeighbors(level, pos, energy);
		}
		return energy;
	}

	@Nullable
	@Override
	public FoodProperties getFoodProperties(ItemStack stack) {
		return stack.get(DataComponents.FOOD);
	}

	@Override
	public boolean canToolLightFire(ItemStack stack) {
		return stack.is(Items.FLINT_AND_STEEL);
	}

	@Override
	public RailShape getRailDirection(BlockState state, BlockGetter level, BlockPos pos, @Nullable AbstractMinecart cart) {
		return state.getBlock() instanceof BaseRailBlock block
				? state.getValue(block.getShapeProperty())
				: RailShape.NORTH_SOUTH;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack inputStack) {
		return inputStack.getRecipeRemainder();
	}

	@Override
	public boolean requiresCustomTesting(Ingredient ingredient) {
		return ingredient.requiresTesting();
	}

	@Override
	public boolean matchesWithCustomTestIngredients(List<ItemStack> nonEmptyStacks, List<Ingredient> ingredients) {
		return ShapelessMatch.isMatch(nonEmptyStacks, ingredients);
	}

	@Override
	public boolean isRedStringContainerTarget(Level level, BlockPos pos) {
		if (level.isClientSide()) {
			return false;
		}
		BlockState state = level.getBlockState(pos);
		BlockEntity be = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		for (Direction value : Direction.values()) {
			if (ItemStorage.SIDED.find(level, pos, state, be, value) != null) {
				return true;
			}
		}
		return ItemStorage.SIDED.find(level, pos, state, be, null) != null;
	}

	@Override
	public RedStringContainerBlockEntity newRedStringContainer(BlockPos pos, BlockState state) {
		return new FabricRedStringContainerBlockEntity(pos, state);
	}

	@Override
	public BlockSetType registerBlockSetType(
			String name,
			boolean canOpenByHand,
			boolean canOpenByWindCharge,
			boolean canButtonBeActivatedByArrows,
			BlockSetType.PressurePlateSensitivity pressurePlateSensitivity,
			SoundType soundType,
			SoundEvent doorClose,
			SoundEvent doorOpen,
			SoundEvent trapdoorClose,
			SoundEvent trapdoorOpen,
			SoundEvent pressurePlateClickOff,
			SoundEvent pressurePlateClickOn,
			SoundEvent buttonClickOff,
			SoundEvent buttonClickOn) {
		return BlockSetTypeBuilder.copyOf(BlockSetType.OAK)
				.openableByHand(canOpenByHand)
				.openableByWindCharge(canOpenByWindCharge)
				.buttonActivatedByArrows(canButtonBeActivatedByArrows)
				.pressurePlateActivationRule(pressurePlateSensitivity)
				.soundGroup(soundType)
				.doorCloseSound(doorClose)
				.doorOpenSound(doorOpen)
				.trapdoorCloseSound(trapdoorClose)
				.trapdoorOpenSound(trapdoorOpen)
				.pressurePlateClickOffSound(pressurePlateClickOff)
				.pressurePlateClickOnSound(pressurePlateClickOn)
				.buttonClickOffSound(buttonClickOff)
				.buttonClickOnSound(buttonClickOn)
				.register(botaniaRL(name));
	}

	@Override
	public WoodType registerWoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
		return WoodTypeBuilder.copyOf(WoodType.OAK)
				.soundGroup(soundType)
				.hangingSignSoundGroup(hangingSignSoundType)
				.fenceGateCloseSound(fenceGateClose)
				.fenceGateOpenSound(fenceGateOpen)
				.register(botaniaRL(name), setType);
	}

	@Override
	public RecipeOutput createRecipeOutput(RecipeAcceptor recipeAcceptor, Supplier<Advancement.Builder> advancementBuilder) {
		return new RecipeOutput() {
			@Override
			public void accept(ResourceLocation location, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
				recipeAcceptor.accept(location, recipe, advancement);
			}

			@Override
			public Advancement.Builder advancement() {
				return advancementBuilder.get();
			}
		};
	}

	@Override
	public boolean shouldShowExtendedItemTooltip(TooltipFlag flags) {
		return Proxy.INSTANCE.hasShiftDown();
	}

	private final boolean speedrunIGTLoaded = isModLoaded("speedrunigt");

	@Override
	public boolean isRunningCategory(BotaniaSpeedrunCategory category) {
		return speedrunIGTLoaded && BotaniaSpeedrunIGTPlugin.isRunningBotaniaCategory(category);
	}

	@Override
	public void completeSpeedrunTimer() {
		InGameTimer.complete();
	}
}
