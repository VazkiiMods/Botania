/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.xplat;

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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.ServiceUtil;
import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface XplatAbstractions {
	// FML/Fabric Loader
	default boolean gogLoaded() {
		return isModLoaded(BotaniaAPI.GOG_MODID);
	}

	// Yes, this forms a loop by default. Each loader overrides their own to break the loop
	default boolean isFabric() {
		return !isForge();
	}

	default boolean isForge() {
		return !isFabric();
	}

	boolean isModLoaded(String modId);
	boolean isDevEnvironment();
	boolean isDataGen();
	boolean isPhysicalClient();
	String getBotaniaVersion();

	// capability API lookup helper methods
	@Nullable
	<A> A findBlockApi(BlockApiNoContext<A> id, Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity);
	@Nullable
	<A, C> A findBlockApi(BlockApiWithContext<A, C> id, Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, @Nullable C context);
	@Nullable
	<A> A findEntityApi(EntityApiNoContext<A> id, Entity entity);
	@Nullable
	<A, C> A findEntityApi(EntityApiWithContext<A, C> id, Entity entity, @Nullable C context);
	@Nullable
	<A> A findItemApi(ItemApiNoContext<A> id, ItemStack stack);
	@Nullable
	<A, C> A findItemApi(ItemApiWithContext<A, C> id, ItemStack stack, @Nullable C context);

	// data attachment helper methods
	@Nullable
	<T> T getEntityData(DataIdBase<T> id, Entity entity);
	<T> void setEntityData(DataIdBase<T> id, Entity entity, T data);
	void removeEntityData(DataIdBase<?> id, Entity entity);

	boolean isFluidContainer(ItemEntity item);
	boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid);
	boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid);
	boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid);
	ItemStack fillItemWithWater(ItemStack stackToFill, Player player);
	boolean hasInventory(Level level, BlockPos pos, Direction sideOfPos);
	ItemStack insertToInventory(Level level, BlockPos pos, Direction sideOfPos, ItemStack toInsert, boolean simulate);

	// Events
	boolean fireCorporeaRequestEvent(CorporeaRequestMatcher matcher, int itemCount, CorporeaSpark spark, boolean dryRun);
	boolean fireCorporeaIndexRequestEvent(ServerPlayer player, CorporeaRequestMatcher request, int count, CorporeaSpark spark);
	void fireManaItemEvent(Player player, List<ItemStack> toReturn);
	float fireManaDiscountEvent(Player player, float discount, ItemStack tool);
	boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient);
	void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside);
	void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action);

	// Networking
	void sendToPlayer(@Nullable Player player, CustomPacketPayload packet);
	void sendToNear(Level level, BlockPos pos, CustomPacketPayload packet);
	void sendToTracking(Entity e, CustomPacketPayload packet);

	// Registrations
	void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener);
	Item.Properties defaultItemBuilder();

	default Item.Properties defaultItemBuilderWithCustomDamageOnFabric() {
		return defaultItemBuilder();
	}

	/**
	 * Forge allows items to opt out of craft-repairing using the builder.
	 * Fabric we handle it manually in RepairItemRecipeFabricMixin
	 */
	default Item.Properties noRepairOnForge(Item.Properties builder) {
		return builder;
	}

	<T extends AbstractContainerMenu, D> MenuType<T> createMenuType(TriFunction<Integer, Inventory, D, T> constructor, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec);
	@Nullable
	EquipmentHandler tryCreateEquipmentHandler();

	// Misc
	<D> void openMenu(ServerPlayer player, MenuProvider menu, D initialData, StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec);

	TagKey<Block> getOreTag();

	// Forge patches AbstractFurnaceBlockEntity.canBurn to be an instance method, so we gotta abstract it
	boolean canFurnaceBurn(AbstractFurnaceBlockEntity furnace, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> items, int maxStackSize);
	// Forge patches BucketItem to use a supplier for the fluid, and exposes it, while Fabric needs an accessor
	Fluid getBucketFluid(BucketItem item);
	int getSmeltingBurnTime(ItemStack stack);
	boolean preventsRemoteMovement(ItemEntity entity);
	void addAxeStripping(Block input, Block output);
	Map<Block, Block> getCustomStrippables();
	int transferEnergyToNeighbors(Level level, BlockPos pos, int energy);
	@Nullable
	FoodProperties getFoodProperties(ItemStack stack);
	boolean canToolLightFire(ItemStack stack);
	// Forge rails can define the shape through other means than the vanilla block state property
	RailShape getRailDirection(BlockState state, BlockGetter level, BlockPos pos, @Nullable AbstractMinecart cart);
	// both loaders provide stack-sensitive craft remainder implementations
	ItemStack getCraftingRemainingItem(ItemStack inputStack);
	boolean requiresCustomTesting(Ingredient ingredient);
	boolean matchesWithCustomTestIngredients(List<ItemStack> nonEmptyStacks, List<Ingredient> ingredients);

	// Red string container
	boolean isRedStringContainerTarget(Level level, BlockPos pos);
	RedStringContainerBlockEntity newRedStringContainer(BlockPos pos, BlockState state);

	BlockSetType registerBlockSetType(
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
			SoundEvent buttonClickOn);

	WoodType registerWoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen);

	// (hopefully temporary) workaround for NeoForge changing RecipeOutput::accept signature
	RecipeOutput createRecipeOutput(RecipeAcceptor recipeAcceptor, Supplier<Advancement.Builder> advancementBuilder);

	@FunctionalInterface
	interface RecipeAcceptor {
		void accept(ResourceLocation location, Recipe<?> recipe, @Nullable AdvancementHolder advancement);
	}

	/**
	 * @deprecated Use {@link #instance()}
	 */
	@Deprecated
	XplatAbstractions INSTANCE = ServiceUtil.findService(XplatAbstractions.class, null);

	static XplatAbstractions instance() {
		return INSTANCE;
	}
}
