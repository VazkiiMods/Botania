/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.xplat;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.corporea.CorporeaIndexRequestEvent;
import vazkii.botania.api.corporea.CorporeaRequestEvent;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.neoforge.integration.curios.CurioIntegration;
import vazkii.botania.neoforge.internal_caps.ForgeInternalEntityCapabilities;
import vazkii.botania.neoforge.mixin.AbstractFurnaceBlockEntityForgeAccessor;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ForgeXplatImpl implements XplatAbstractions {

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
	public boolean isDataGen() {
		return DatagenModLoader.isRunningDataGen();
	}

	@Override
	public boolean isPhysicalClient() {
		return FMLLoader.getDist() == Dist.CLIENT;
	}

	@Override
	public String getBotaniaVersion() {
		return ModList.get().getModContainerById(BotaniaAPI.MODID).orElseThrow()
				.getModInfo().getVersion().toString();
	}

	// capability API lookup helper methods

	@Override
	public @Nullable <A> A findBlockApi(BlockApiNoContext<A> id, Level level, BlockPos pos, @Nullable BlockState state,
			@Nullable BlockEntity entity) {
		return level.getCapability(BotaniaForgeCapabilities.getBlockApiLookupById(id), pos, state, entity);
	}

	@Override
	public @Nullable <A, C> A findBlockApi(BlockApiWithContext<A, C> id, Level level, BlockPos pos,
			@Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C context) {
		return level.getCapability(BotaniaForgeCapabilities.getBlockApiLookupById(id), pos, state, entity, context);
	}

	@Override
	public @Nullable <A> A findEntityApi(EntityApiNoContext<A> id, Entity entity) {
		return entity.getCapability(BotaniaForgeCapabilities.getEntityApiLookupById(id));
	}

	@Override
	public @Nullable <A, C> A findEntityApi(EntityApiWithContext<A, C> id, Entity entity, @Nullable C context) {
		return entity.getCapability(BotaniaForgeCapabilities.getEntityApiLookupById(id), context);
	}

	@Override
	public @Nullable <A> A findItemApi(ItemApiNoContext<A> id, ItemStack stack) {
		return stack.getCapability(BotaniaForgeCapabilities.getItemApiLookupById(id));
	}

	@Override
	public @Nullable <A, C> A findItemApi(ItemApiWithContext<A, C> id, ItemStack stack, @Nullable C context) {
		return stack.getCapability(BotaniaForgeCapabilities.getItemApiLookupById(id), context);
	}

	// data attachment helper methods

	@Nullable
	@Override
	public <T> T getEntityData(DataIdBase<T> id, Entity entity) {
		return entity.getExistingDataOrNull(ForgeInternalEntityCapabilities.getAttachmentTypeById(id));
	}

	@Override
	public <T> void setEntityData(DataIdBase<T> id, Entity entity, T data) {
		entity.setData(ForgeInternalEntityCapabilities.getAttachmentTypeById(id), data);
	}

	@Override
	public void removeEntityData(DataIdBase<?> id, Entity entity) {
		entity.removeData(ForgeInternalEntityCapabilities.getAttachmentTypeById(id));
	}

	@Override
	public boolean isFluidContainer(ItemEntity item) {
		return item.getItem().getCapability(Capabilities.FluidHandler.ITEM) != null;
	}

	@Override
	public boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid) {
		IFluidHandlerItem h = item.getItem().getCapability(Capabilities.FluidHandler.ITEM);
		if (h == null) {
			return false;
		}
		var extracted = h.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
		boolean success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
		if (success) {
			h.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
			item.setItem(h.getContainer());
		}
		return success;
	}

	@Override
	public boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		IFluidHandlerItem h = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (h == null) {
			return false;
		}
		var extracted = h.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
		var success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
		if (success && !player.hasInfiniteMaterials()) {
			h.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
			player.setItemInHand(hand, h.getContainer());
		}
		return success;
	}

	@Override
	public boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		if (stack.isEmpty()) {
			return false;
		}

		// Have to copy and simulate on a stack of size 1, because buckets don't accept
		// fluid input if they're stacked (why Forge??)
		ItemStack toFill = stack.copy();
		toFill.setCount(1);

		var fluidHandler = toFill.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler != null) {
			var fluidToFill = new FluidStack(fluid, FluidType.BUCKET_VOLUME);
			int filled = fluidHandler.fill(fluidToFill, IFluidHandler.FluidAction.SIMULATE);

			if (filled == FluidType.BUCKET_VOLUME) {
				if (!player.hasInfiniteMaterials()) {
					fluidHandler.fill(fluidToFill, IFluidHandler.FluidAction.EXECUTE);
					stack.shrink(1);
					ItemStack result = fluidHandler.getContainer();
					if (stack.isEmpty()) {
						player.setItemInHand(hand, result);
					} else {
						player.getInventory().placeItemBackInInventory(result);
					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack fillItemWithWater(ItemStack stackToFill, Player player) {
		ItemStack split = stackToFill.copyWithCount(1);
		Optional<IFluidHandlerItem> optionalHandler = FluidUtil.getFluidHandler(split);
		if (optionalHandler.isPresent()) {
			var handler = optionalHandler.get();
			if (handler.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME),
					IFluidHandler.FluidAction.EXECUTE) > 0) {
				return handler.getContainer();
			}
		} else if (split.is(Items.GLASS_BOTTLE)) {
			return PotionContents.createItemStack(Items.POTION, Potions.WATER);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean hasInventory(Level level, BlockPos pos, Direction sideOfPos) {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, sideOfPos) != null;
	}

	@Override
	public ItemStack insertToInventory(Level level, BlockPos pos, Direction sideOfPos, ItemStack toInsert, boolean simulate) {
		BlockState state = level.getBlockState(pos);
		IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, null, sideOfPos);

		// can't do incremental simulations
		if (simulate || !state.is(BotaniaTags.Blocks.SINGLE_ITEM_INSERT)) {
			return ItemHandlerHelper.insertItemStacked(itemHandler, toInsert, simulate);
		}

		int maxInserts = toInsert.getCount();
		for (int i = 0; i < maxInserts; i++) {
			ItemStack single = toInsert.copyWithCount(1);
			if (!ItemHandlerHelper.insertItemStacked(itemHandler, single, false).isEmpty()) {
				break;
			}
			toInsert.setCount(toInsert.getCount() - 1);
		}
		return toInsert;
	}

	@Override
	public boolean fireCorporeaRequestEvent(CorporeaRequestMatcher matcher, int itemCount, CorporeaSpark spark, boolean dryRun) {
		return NeoForge.EVENT_BUS.post(new CorporeaRequestEvent(matcher, itemCount, spark, dryRun)).isCanceled();
	}

	@Override
	public boolean fireCorporeaIndexRequestEvent(ServerPlayer player, CorporeaRequestMatcher request, int count, CorporeaSpark spark) {
		return NeoForge.EVENT_BUS.post(new CorporeaIndexRequestEvent(player, request, count, spark)).isCanceled();
	}

	@Override
	public void fireManaItemEvent(Player player, List<ItemStack> toReturn) {
		NeoForge.EVENT_BUS.post(new ManaItemsEvent(player, toReturn));
	}

	@Override
	public float fireManaDiscountEvent(Player player, float discount, ItemStack tool) {
		var evt = new ManaDiscountEvent(player, discount, tool);
		NeoForge.EVENT_BUS.post(evt);
		return evt.getDiscount();
	}

	@Override
	public boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient) {
		var evt = new ManaProficiencyEvent(player, tool, proficient);
		NeoForge.EVENT_BUS.post(evt);
		return evt.isProficient();
	}

	@Override
	public void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside) {
		NeoForge.EVENT_BUS.post(new ElvenPortalUpdateEvent(portal, bounds, open, stacksInside));
	}

	@Override
	public void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		NeoForge.EVENT_BUS.post(new ManaNetworkEvent(thing, type, action));
	}

	@Override
	public void sendToPlayer(@Nullable Player player, CustomPacketPayload packet) {
		if (player instanceof ServerPlayer serverPlayer && !player.level().isClientSide()) {
			PacketDistributor.sendToPlayer(serverPlayer, packet);
		}
	}

	@Override
	public void sendToNear(Level level, BlockPos pos, CustomPacketPayload packet) {
		if (!level.isClientSide()) {
			PacketDistributor.sendToPlayersNear((ServerLevel) level, null, pos.getX(), pos.getY(), pos.getZ(), 64, packet);
		}
	}

	@Override
	public void sendToTracking(Entity e, CustomPacketPayload packet) {
		if (!e.level().isClientSide()) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(e, packet);
		}
	}

	@Override
	public void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener) {
		switch (type) {
			case CLIENT_RESOURCES -> NeoForge.EVENT_BUS.addListener(
					(RegisterClientReloadListenersEvent e) -> e.registerReloadListener(listener));
			case SERVER_DATA -> NeoForge.EVENT_BUS.addListener(
					(AddReloadListenerEvent e) -> e.addListener(listener));
		}
	}

	@Override
	public Item.Properties defaultItemBuilder() {
		return new Item.Properties();
	}

	@Override
	public Item.Properties noRepairOnForge(Item.Properties builder) {
		return builder.setNoRepair();
	}

	@Override
	public <T extends AbstractContainerMenu, D> MenuType<T> createMenuType(TriFunction<Integer, Inventory, D, T> constructor, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec) {
		return IMenuTypeExtension.create((windowId, inv, data) -> constructor.apply(windowId, inv, streamCodec.decode(data)));
	}

	@Nullable
	@Override
	public EquipmentHandler tryCreateEquipmentHandler() {
		if (XplatAbstractions.INSTANCE.isModLoaded("curios")) {
			CurioIntegration.init();
			return new CurioIntegration();
		}
		return null;
	}

	@Override
	public <D> void openMenu(ServerPlayer player, MenuProvider menu, D initialData, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec) {
		player.openMenu(menu, buf -> streamCodec.encode(buf, initialData));
	}

	@Override
	public boolean canFurnaceBurn(AbstractFurnaceBlockEntity furnace, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> items, int maxStackSize) {
		return AbstractFurnaceBlockEntityForgeAccessor.botania_canBurn(furnace.getLevel().registryAccess(), recipeHolder, items, maxStackSize, furnace);
	}

	@Override
	public Fluid getBucketFluid(BucketItem item) {
		return item.content;
	}

	@Override
	public int getSmeltingBurnTime(ItemStack stack) {
		return stack.getBurnTime(RecipeType.SMELTING);
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
	public Map<Block, Block> getCustomStrippables() {
		return Map.of();
	}

	@Override
	public int transferEnergyToNeighbors(Level level, BlockPos pos, int energy) {
		for (Direction e : Direction.values()) {
			BlockPos neighbor = pos.relative(e);
			if (!level.hasChunkAt(neighbor)) {
				continue;
			}

			IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, e.getOpposite());

			if (storage == null) {
				storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
			}

			if (storage != null) {
				energy -= storage.receiveEnergy(energy, false);

				if (energy <= 0) {
					return 0;
				}
			}
		}
		return energy;
	}

	@Nullable
	@Override
	public FoodProperties getFoodProperties(ItemStack stack) {
		return stack.getFoodProperties(null);
	}

	@Override
	public boolean canToolLightFire(ItemStack stack) {
		return stack.is(Items.FLINT_AND_STEEL) || stack.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT);
	}

	@Override
	public RailShape getRailDirection(BlockState state, BlockGetter level, BlockPos pos, @Nullable AbstractMinecart cart) {
		return state.getBlock() instanceof BaseRailBlock block
				? block.getRailDirection(state, level, pos, null)
				: RailShape.NORTH_SOUTH;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack inputStack) {
		return inputStack.getCraftingRemainingItem();
	}

	@Override
	public boolean requiresCustomTesting(Ingredient ingredient) {
		return !ingredient.isSimple();
	}

	@Override
	public boolean matchesWithCustomTestIngredients(List<ItemStack> nonEmptyStacks,
			List<Ingredient> ingredients) {
		return RecipeMatcher.findMatches(nonEmptyStacks, ingredients) != null;
	}

	@Override
	public boolean isRedStringContainerTarget(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		BlockEntity be = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		for (Direction dir : Direction.values()) {
			if (level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, be, dir) != null) {
				return true;
			}
		}
		return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, be, null) != null;
	}

	@Override
	public RedStringContainerBlockEntity newRedStringContainer(BlockPos pos, BlockState state) {
		return new RedStringContainerBlockEntity(pos, state);
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
		return BlockSetType.register(new BlockSetType(BotaniaAPI.MODID + ":" + name,
				canOpenByHand,
				canOpenByWindCharge,
				canButtonBeActivatedByArrows,
				pressurePlateSensitivity,
				soundType,
				doorClose,
				doorOpen,
				trapdoorClose,
				trapdoorOpen,
				pressurePlateClickOff,
				pressurePlateClickOn,
				buttonClickOff,
				buttonClickOn));
	}

	@Override
	public WoodType registerWoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
		return WoodType.register(new WoodType(BotaniaAPI.MODID + ":" + name, setType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen));
	}

	@Override
	public RecipeOutput createRecipeOutput(RecipeAcceptor recipeAcceptor, Supplier<Advancement.Builder> advancementBuilder) {
		return new RecipeOutput() {
			@Override
			public Advancement.Builder advancement() {
				return advancementBuilder.get();
			}

			@Override
			public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
				if (conditions.length > 0) {
					throw new UnsupportedOperationException("Conditions are not supported");
				}
				recipeAcceptor.accept(id, recipe, advancement);
			}
		};
	}

	@Override
	public boolean shouldShowExtendedItemTooltip(TooltipFlag flags) {
		return flags.hasShiftDown();
	}
}
