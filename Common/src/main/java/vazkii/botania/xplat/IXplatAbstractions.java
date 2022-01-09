package vazkii.botania.xplat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;

import org.apache.commons.lang3.function.TriFunction;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.internal_caps.*;
import vazkii.botania.common.network.EffectType;

import javax.annotation.Nullable;

import java.util.List;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface IXplatAbstractions {
	// FML/Fabric Loader
	default boolean gogLoaded() {
		return isModLoaded(BotaniaAPI.GOG_MODID);
	}

	boolean isModLoaded(String modId);
	boolean isDevEnvironment();
	boolean isPhysicalClient();
	String getBotaniaVersion();

	// Capability access (API-facing caps)
	@Nullable
	IAvatarWieldable findAvatarWieldable(ItemStack stack);
	@Nullable
	IBlockProvider findBlockProvider(ItemStack stack);
	@Nullable
	ICoordBoundItem findCoordBoundItem(ItemStack stack);
	@Nullable
	IExoflameHeatable findExoflameHeatable(Level level, BlockPos pos, BlockState state, BlockEntity be);
	@Nullable
	IHornHarvestable findHornHarvestable(Level level, BlockPos pos, BlockState state, BlockEntity be);
	@Nullable
	IHourglassTrigger findHourglassTrigger(Level level, BlockPos pos, BlockState state, BlockEntity be);
	@Nullable
	IWandable findWandable(Level level, BlockPos pos, BlockState state, BlockEntity be);
	boolean isFluidContainer(ItemEntity item);
	boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid);
	boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid);
	boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid);

	// Capability access (internal caps)
	EthicalComponent ethicalComponent(PrimedTnt tnt);
	GhostRailComponent ghostRailComponent(AbstractMinecart cart);
	ItemFlagsComponent itemFlagsComponent(ItemEntity item);
	KeptItemsComponent keptItemsComponent(Player player);
	LooniumComponent looniumComponent(LivingEntity entity);
	NarslimmusComponent narslimmusComponent(Slime slime);
	TigerseyeComponent tigersEyeComponent(Creeper creeper);

	// Events
	boolean fireCorporeaRequestEvent(ICorporeaRequestMatcher matcher, int itemCount, ICorporeaSpark spark, boolean dryRun);
	boolean fireCorporeaIndexRequestEvent(ServerPlayer player, ICorporeaRequestMatcher request, int count, ICorporeaSpark spark);
	void fireManaItemEvent(Player player, List<ItemStack> toReturn);
	float fireManaDiscountEvent(Player player, float discount, ItemStack tool);
	boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient);
	void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside);
	void fireManaNetworkEvent(BlockEntity be, ManaBlockType type, ManaNetworkAction action);

	// Clientbound packets
	void sendEffectPacket(Player player, EffectType type, double x, double y, double z, int... args);
	void sendEffectPacketNear(Entity e, EffectType type, double x, double y, double z, int... args);
	void sendEffectPacketNear(Level level, BlockPos pos, EffectType type, double x, double y, double z, int... args);
	Packet<?> makeSpawnDopplegangerPacket(EntityDoppleganger boss, int playerCount, boolean hardMode, BlockPos source, UUID bossInfoUuid);
	void sendItemsRemainingPacket(Player player, ItemStack stack, int count, @Nullable Component message);
	void sendGogWorldPacket(Player player);
	void sendItemTimeCounterPacket(Player player, int entityId, int timeCounter);
	void sendAvatarTornadoRodPacket(Player player, boolean elytra);

	// Serverbound packets
	void sendIndexKeybindRequestPacket(ItemStack stack);
	void sendJumpPacket();
	void sendDodgePacket();
	void sendLeftClickPacket();

	// Registrations
	<T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks);
	void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener);
	Item.Properties defaultItemBuilder();
	<T extends AbstractContainerMenu> MenuType<T> createMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor);
	Registry<Brew> createBrewRegistry();

	// Misc
	void openMenu(ServerPlayer player, MenuProvider menu, Consumer<FriendlyByteBuf> buf);
	Attribute getReachDistanceAttribute();
	Attribute getStepHeightAttribute();
	Tag.Named<Block> blockTag(ResourceLocation id);
	Tag.Named<Item> itemTag(ResourceLocation id);
	Tag.Named<EntityType<?>> entityTag(ResourceLocation id);

	IXplatAbstractions INSTANCE = find();

	private static IXplatAbstractions find() {
		var providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
			throw new IllegalStateException("There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			BotaniaAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
