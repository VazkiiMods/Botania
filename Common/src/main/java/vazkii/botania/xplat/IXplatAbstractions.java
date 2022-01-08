package vazkii.botania.xplat;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.network.EffectType;

import javax.annotation.Nullable;

import java.util.ServiceLoader;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public interface IXplatAbstractions {
	// FML/Fabric Loader
	boolean isModLoaded(String modId);
	boolean isDevEnvironment();
	boolean isPhysicalClient();
	String getBotaniaVersion();

	// Capability access
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
