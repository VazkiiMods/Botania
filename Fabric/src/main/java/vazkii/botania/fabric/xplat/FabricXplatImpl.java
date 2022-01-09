package vazkii.botania.fabric.xplat;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaFabricCapabilities;
import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.corporea.CorporeaIndexRequestCallback;
import vazkii.botania.api.corporea.CorporeaRequestCallback;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.recipe.ElvenPortalUpdateCallback;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.*;
import vazkii.botania.fabric.FabricBotaniaCreativeTab;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FabricXplatImpl implements IXplatAbstractions {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public String getBotaniaVersion() {
		return FabricLoader.getInstance().getModContainer(LibMisc.MOD_ID).get()
				.getMetadata().getVersion().getFriendlyString();
	}

	@Nullable
	@Override
	public IAvatarWieldable findAvatarWieldable(ItemStack stack) {
		return BotaniaFabricCapabilities.AVATAR_WIELDABLE.find(stack, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public IBlockProvider findBlockProvider(ItemStack stack) {
		return BotaniaFabricCapabilities.BLOCK_PROVIDER.find(stack, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public ICoordBoundItem findCoordBoundItem(ItemStack stack) {
		return BotaniaFabricCapabilities.COORD_BOUND_ITEM.find(stack, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public IExoflameHeatable findExoflameHeatable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return BotaniaFabricCapabilities.EXOFLAME_HEATABLE.find(level, pos, state, be, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public IHornHarvestable findHornHarvestable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return BotaniaFabricCapabilities.HORN_HARVEST.find(level, pos, state, be, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public IHourglassTrigger findHourglassTrigger(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return BotaniaFabricCapabilities.HOURGLASS_TRIGGER.find(level, pos, state, be, Unit.INSTANCE);
	}

	@Nullable
	@Override
	public IWandable findWandable(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return BotaniaFabricCapabilities.WANDABLE.find(level, pos, state, be, Unit.INSTANCE);
	}

	@Override
	public boolean fireCorporeaRequestEvent(ICorporeaRequestMatcher matcher, int itemCount, ICorporeaSpark spark, boolean dryRun) {
		return CorporeaRequestCallback.EVENT.invoker().onRequest(matcher, itemCount, spark, dryRun);
	}

	@Override
	public boolean fireCorporeaIndexRequestEvent(ServerPlayer player, ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
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
	public void fireManaNetworkEvent(BlockEntity be, ManaBlockType type, ManaNetworkAction action) {
		ManaNetworkCallback.EVENT.invoker().onNetworkChange(be, type, action);

	}

	@Override
	public void sendEffectPacket(Player player, EffectType type, double x, double y, double z, int... args) {
		PacketBotaniaEffect.send(player, type, x, y, z, args);
	}

	@Override
	public void sendEffectPacketNear(Entity e, EffectType type, double x, double y, double z, int... args) {
		PacketBotaniaEffect.sendNearby(e, type, x, y, z, args);
	}

	@Override
	public void sendEffectPacketNear(Level level, BlockPos pos, EffectType type, double x, double y, double z, int... args) {
		PacketBotaniaEffect.sendNearby(level, pos, type, x, y, z, args);
	}

	@Override
	public Packet<?> makeSpawnDopplegangerPacket(EntityDoppleganger boss, int playerCount, boolean hardMode, BlockPos source, UUID bossInfoUuid) {
		return PacketSpawnDoppleganger.make(boss, playerCount, hardMode, source, bossInfoUuid);
	}

	@Override
	public void sendItemsRemainingPacket(Player player, ItemStack stack, int count, @Nullable Component message) {
		PacketUpdateItemsRemaining.send(player, stack, count, message);
	}

	@Override
	public void sendGogWorldPacket(Player player) {
		PacketGogWorld.send((ServerPlayer) player);
	}

	@Override
	public void sendItemTimeCounterPacket(Player player, int entityId, int timeCounter) {
		PacketItemAge.send((ServerPlayer) player, entityId, timeCounter);
	}

	@Override
	public void sendAvatarTornadoRodPacket(Player player, boolean elytra) {
		PacketAvatarTornadoRod.sendTo((ServerPlayer) player, elytra);
	}

	@Override
	public void sendIndexKeybindRequestPacket(ItemStack stack) {
		PacketIndexKeybindRequest.send(stack);
	}

	@Override
	public void sendJumpPacket() {
		PacketJump.send();
	}

	@Override
	public void sendDodgePacket() {
		PacketDodge.send();
	}

	@Override
	public void sendLeftClickPacket() {
		PacketLeftClick.send();
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
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
		return new FabricItemSettings().group(FabricBotaniaCreativeTab.INSTANCE);
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {
		return new ExtendedScreenHandlerType<>(constructor::apply);
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menu, Consumer<FriendlyByteBuf> writeInitialData) {
		var menuProvider = new ExtendedScreenHandlerFactory() {
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
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				writeInitialData.accept(buf);
			}
		};
		player.openMenu(menuProvider);
	}
}
