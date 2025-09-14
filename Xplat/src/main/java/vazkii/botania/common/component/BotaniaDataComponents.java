package vazkii.botania.common.component;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.block.flower.generating.GourmaryllisBlockEntity;
import vazkii.botania.common.block.flower.generating.RafflowsiaBlockEntity;
import vazkii.botania.common.item.LaputaShardItem;
import vazkii.botania.common.item.ManaBlasterItem;
import vazkii.botania.common.item.relic.RingOfLokiItem;
import vazkii.botania.common.lib.LibComponentNames;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaDataComponents {
	private static final Map<String, DataComponentType<?>> ALL = new HashMap<>();

	// general flags
	/**
	 * General persisted "active" flag for items that can be toggled and need to remember their state later.
	 * (examples: stone of temperance, manufactory halo's auto-crafting, terra shatterer)
	 */
	public static final DataComponentType<Unit> ACTIVE = makeUnit(LibComponentNames.ACTIVE);
	/**
	 * Transient "active" flag for items with an active state that does not need to be persisted.
	 * (examples: slime in a bottle being happy, assembly halo being held, bauble box being open)
	 */
	public static final DataComponentType<Unit> ACTIVE_TRANSIENT = makeTransientUnit(LibComponentNames.ACTIVE_TRANSIENT);

	public static final DataComponentType<Unit> PHANTOM_INKED = makeUnit(LibComponentNames.PHANTOM_INKED);
	public static final DataComponentType<Unit> RESOLUTE_IVY = makeUnit(LibComponentNames.RESOLUTE_IVY);

	// various specific flags
	public static final DataComponentType<Unit> ELEMENTIUM_TIPPED = makeUnit(LibComponentNames.ELEMENTIUM_TIPPED);
	public static final DataComponentType<Unit> ELVEN_UNLOCK = makeUnit(LibComponentNames.ELVEN_UNLOCK);
	/**
	 * Helper component that causes mana pool items to be rendered with mana content.
	 */
	public static final DataComponentType<Unit> RENDER_FULL = makeTransientUnit(LibComponentNames.RENDER_FULL);
	public static final DataComponentType<SingleItem> COSMETIC_OVERRIDE = make(LibComponentNames.COSMETIC_OVERRIDE,
			builder -> builder.persistent(SingleItem.CODEC).cacheEncoding()
					.networkSynchronized(SingleItem.STREAM_CODEC));

	// wand properties
	public static final DataComponentType<DyeColor> WAND_COLOR1 = make(LibComponentNames.WAND_COLOR1,
			builder -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
	public static final DataComponentType<DyeColor> WAND_COLOR2 = make(LibComponentNames.WAND_COLOR2,
			builder -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
	public static final DataComponentType<Unit> WAND_BIND_MODE = makeUnit(LibComponentNames.WAND_BIND_MODE);
	public static final DataComponentType<GlobalPos> BINDING_POS = make(LibComponentNames.BINDING_POS,
			builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));
	public static final DataComponentType<Direction> BINDING_SIDE = make(LibComponentNames.BINDING_SIDE,
			builder -> builder.persistent(Direction.CODEC).networkSynchronized(Direction.STREAM_CODEC));

	// mana item properties
	/**
	 * Current amount of mana in the item.
	 */
	public static final DataComponentType<Integer> MANA = make(LibComponentNames.MANA,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	/**
	 * Maximum amount of mana in the item. Not fixed and may become zero if the item can temporarily not store any mana.
	 */
	public static final DataComponentType<Integer> MAX_MANA = make(LibComponentNames.MAX_MANA,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	/**
	 * Mana backlog to be processed later through some other means.
	 * If this component exists, it receives the same delta amounts as the {@link #MANA} component.
	 */
	public static final DataComponentType<Integer> MANA_BACKLOG = make(LibComponentNames.MANA_BACKLOG,
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	/**
	 * Defines that the item can receive mana while in the player's inventory,
	 * e.g. from mana producers or nearby dispersive sparks.
	 */
	public static final DataComponentType<Unit> CAN_ACCEPT_MANA_FROM_ITEMS = makeUnit(LibComponentNames.CAN_ACCEPT_MANA_FROM_ITEMS);
	/**
	 * Defines that the item can be filled in a mana pool.
	 */
	public static final DataComponentType<Unit> CAN_RECEIVE_MANA_FROM_POOL = makeUnit(LibComponentNames.CAN_RECEIVE_MANA_FROM_POOL);
	/**
	 * Defines that the item can provide mana to mana-consuming items in the player's inventory.
	 */
	public static final DataComponentType<Unit> CAN_PROVIDE_MANA_TO_ITEMS = makeUnit(LibComponentNames.CAN_PROVIDE_MANA_TO_ITEMS);
	/**
	 * Defines that the item can be drained into a mana pool.
	 */
	public static final DataComponentType<Unit> CAN_DRAIN_MANA_TO_POOL = makeUnit(LibComponentNames.CAN_DRAIN_MANA_TO_POOL);
	/**
	 * Defined that the item can provide infinite mana without draining itself.
	 */
	public static final DataComponentType<Unit> CREATIVE_MANA = makeUnit(LibComponentNames.CREATIVE_MANA);
	/**
	 * Bound pool of a mana mirror. (Not part of the default mana item implementation.)
	 */
	public static final DataComponentType<GlobalPos> MANA_POOL_POS = make(LibComponentNames.MANA_POOL_POS,
			builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));

	// crafting halo data
	public static final DataComponentType<Float> HALO_ROTATION_BASE = make(LibComponentNames.HALO_ROTATION_BASE,
			builder -> builder.networkSynchronized(ByteBufCodecs.FLOAT));
	public static final DataComponentType<ResourceLocation> LAST_RECIPE_ID = make(LibComponentNames.LAST_RECIPE_ID,
			builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC)
	);
	public static final DataComponentType<StoredIds> STORED_RECIPES = make(LibComponentNames.STORED_RECIPES,
			builder -> builder.persistent(StoredIds.CODEC).cacheEncoding()
					.networkSynchronized(StoredIds.STREAM_CODEC));

	public static final DataComponentType<Long> LAST_TRIGGER_TIME = make(LibComponentNames.LAST_TRIGGER_TIME,
			builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

	/**
	 * Defines the dyed lens color value. Should not be used together with {@link #LENS_RAINBOW_TINT}.
	 */
	public static final DataComponentType<DyeColor> LENS_TINT = make(LibComponentNames.LENS_TINT,
			builder -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
	/**
	 * Defines that the lens is rainbow-tinted. Takes precedence over {@link #LENS_TINT}.
	 */
	public static final DataComponentType<Unit> LENS_RAINBOW_TINT = makeUnit(LibComponentNames.LENS_RAINBOW_TINT);
	public static final DataComponentType<SingleItem> ATTACHED_LENS = make(LibComponentNames.ATTACHED_LENS,
			builder -> builder.persistent(SingleItem.CODEC).cacheEncoding()
					.networkSynchronized(SingleItem.STREAM_CODEC));

	// brews and similar consumables
	public static final DataComponentType<ResourceLocation> BREW = make(LibComponentNames.BREW,
			builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final DataComponentType<Integer> MAX_USES = make(LibComponentNames.MAX_USES,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> REMAINING_USES = make(LibComponentNames.REMAINING_USES,
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Long> RANDOM_SEED = make(LibComponentNames.RANDOM_SEED,
			builder -> builder.persistent(Codec.LONG).networkSynchronized(new StreamCodec<>() {
				// There doesn't seem to be a built-in non-var stream codec for Long values.
				@Override
				public Long decode(RegistryFriendlyByteBuf buffer) {
					return buffer.readLong();
				}

				@Override
				public void encode(RegistryFriendlyByteBuf buffer, Long value) {
					buffer.writeLong(value);
				}
			}));

	public static final DataComponentType<List<Integer>> SPECTATOR_HIGHLIGHT_ENTITIES = make(LibComponentNames.SPECTATOR_HIGHLIGHT_ENTITIES,
			builder -> builder.networkSynchronized(ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())));
	public static final DataComponentType<List<BlockPos>> SPECTATOR_HIGHLIGHT_BLOCKS = make(LibComponentNames.SPECTATOR_HIGHLIGHT_BLOCKS,
			builder -> builder.networkSynchronized(BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list())));

	// black hole talisman
	public static final DataComponentType<ResourceLocation> BLOCK_TYPE = make(LibComponentNames.BLOCK_TYPE,
			builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final DataComponentType<Integer> BLOCK_COUNT = make(LibComponentNames.BLOCK_COUNT,
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

	public static final DataComponentType<Integer> RANGE = make(LibComponentNames.RANGE,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> SIZE = make(LibComponentNames.SIZE,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<ResourceLocation> MOB_TYPE = make(LibComponentNames.MOB_TYPE,
			builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final DataComponentType<Integer> NOT_MY_NAME_STEP = make(LibComponentNames.NOT_MY_NAME_STEP,
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<String> SEXTANT_MODE = make(LibComponentNames.SEXTANT_MODE,
			builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
	public static final DataComponentType<List<BlockPos>> LOKI_RING_OFFSET_LIST = make(LibComponentNames.LOKI_RING_OFFSET_LIST,
			builder -> builder.persistent(BlockPos.CODEC.sizeLimitedListOf(RingOfLokiItem.MAX_NUM_CURSORS)).cacheEncoding()
					.networkSynchronized(BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list(RingOfLokiItem.MAX_NUM_CURSORS))));
	public static final DataComponentType<List<String>> ANCIENT_WILLS = make(LibComponentNames.ANCIENT_WILLS,
			builder -> builder.persistent(Codec.STRING.sizeLimitedListOf(16)).cacheEncoding()
					.networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list(16))));
	public static final DataComponentType<UUID> SOULBOUND = make(LibComponentNames.SOULBOUND,
			builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));
	public static final DataComponentType<Map<ResourceLocation, BlockPos>> BOUND_POSITIONS = make(LibComponentNames.BOUND_POSITIONS,
			builder -> builder.persistent(Codec.unboundedMap(ResourceLocation.CODEC, BlockPos.CODEC)).cacheEncoding()
					.networkSynchronized(ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, BlockPos.STREAM_CODEC)));

	// Flügel Tiara components, TODO: subject to future reorganization
	public static final DataComponentType<Integer> TIARA_VARIANT = make(LibComponentNames.TIARA_VARIANT,
			builder -> builder.persistent(ExtraCodecs.intRange(0, 9)).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> REMAINING_TICKS = make(LibComponentNames.REMAINING_TICKS,
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> MAX_USE_TICKS = make(LibComponentNames.MAX_USE_TICKS,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Unit> FLYING = makeTransientUnit(LibComponentNames.FLYING);
	public static final DataComponentType<Unit> IS_SPRINTING = makeTransientUnit(LibComponentNames.IS_SPRINTING);
	public static final DataComponentType<Unit> CREATIVE_FLIGHT = makeUnit(LibComponentNames.CREATIVE_FLIGHT);
	public static final DataComponentType<Unit> BOOST_PENDING = makeTransientUnit(LibComponentNames.BOOST_PENDING);
	public static final DataComponentType<Unit> IS_GLIDING = makeTransientUnit(LibComponentNames.IS_GLIDING);

	// Key of the King's Law
	public static final DataComponentType<Unit> CHARGING = makeTransientUnit(LibComponentNames.CHARGING);
	public static final DataComponentType<Integer> WEAPONS_SPAWNED = make(LibComponentNames.WEAPONS_SPAWNED,
			builder -> builder.networkSynchronized(ByteBufCodecs.VAR_INT));

	// Laputa Shard
	public static final DataComponentType<Integer> SHARD_LEVEL = make(LibComponentNames.SHARD_LEVEL,
			builder -> builder.persistent(ExtraCodecs.intRange(1, LaputaShardItem.MAX_LEVEL)).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Boolean> SHARD_POINTY = make(LibComponentNames.SHARD_POINTY,
			builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
	public static final DataComponentType<Float> SHARD_HEIGHT_SCALE = make(LibComponentNames.SHARD_HEIGHT_SCALE,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
	public static final DataComponentType<LaputaState> LAPUTA_STATE = make(LibComponentNames.LAPUTA_STATE,
			builder -> builder.persistent(LaputaState.CODEC).cacheEncoding()
					.networkSynchronized(LaputaState.STREAM_CODEC));

	// Mana Blaster
	public static final DataComponentType<Unit> CLIP = makeUnit(LibComponentNames.CLIP);
	public static final DataComponentType<Integer> CLIP_POS = make(LibComponentNames.CLIP_POS,
			builder -> builder.persistent(ExtraCodecs.intRange(0, ManaBlasterItem.CLIP_SLOTS - 1))
					.networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<List<ItemStack>> ATTACHED_LENSES = make(LibComponentNames.ATTACHED_LENSES,
			builder -> builder.persistent(ExtraCodecs.nonEmptyList(ItemStack.CODEC.sizeLimitedListOf(ManaBlasterItem.CLIP_SLOTS)))
					.cacheEncoding().networkSynchronized(ItemStack.LIST_STREAM_CODEC));
	public static final DataComponentType<Integer> COOLDOWN = make(LibComponentNames.COOLDOWN,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

	// Vitreous Pickaxe silktouch hack (not transient as it must be cleared manually)
	public static final DataComponentType<Unit> SILK_HACK = makeUnit(LibComponentNames.SILK_HACK);

	// Rod of the Shifting Crust - TODO: lots of questionable transient stuff here
	public static final DataComponentType<Unit> TEMPERANCE_ACTIVE = makeTransientUnit(LibComponentNames.TEMPERANCE_ACTIVE);
	public static final DataComponentType<Unit> SWAPPING = makeTransientUnit(LibComponentNames.SWAPPING);
	public static final DataComponentType<Integer> EXTRA_RANGE = make(LibComponentNames.EXTRA_RANGE,
			builder -> builder.networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<BlockPos> SELECTED_POS = make(LibComponentNames.SELECTED_POS,
			builder -> builder.networkSynchronized(BlockPos.STREAM_CODEC));
	public static final DataComponentType<Direction> SWAP_CLICK_AXIS = make(LibComponentNames.SWAP_CLICK_AXIS,
			builder -> builder.networkSynchronized(Direction.STREAM_CODEC));
	public static final DataComponentType<ResourceLocation> TARGET_BLOCK = make(LibComponentNames.TARGET_BLOCK,
			builder -> builder.networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final DataComponentType<ResourceLocation> PLACED_ITEM = make(LibComponentNames.PLACED_ITEM,
			builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final DataComponentType<Direction> SWAP_DIRECTION = make(LibComponentNames.SWAP_DIRECTION,
			builder -> builder.persistent(Direction.CODEC).networkSynchronized(Direction.STREAM_CODEC));
	public static final DataComponentType<Vec3> SWAP_HIT_VEC = make(LibComponentNames.SWAP_HIT_VEC,
			builder -> builder.persistent(Vec3.CODEC).networkSynchronized(ByteBufCodecs.VECTOR3F.map(Vec3::new, Vec3::toVector3f)));

	// Planestrider's Sash
	public static final DataComponentType<Float> SPEED = make(LibComponentNames.SPEED,
			builder -> builder.networkSynchronized(ByteBufCodecs.FLOAT));
	public static final DataComponentType<Vec3> OLD_POS = make(LibComponentNames.OLD_POS,
			// TODO: must be double precision to work further from origin, but does client need it at all?
			builder -> builder.networkSynchronized(ByteBufCodecs.fromCodec(Vec3.CODEC)));

	public static final DataComponentType<Integer> TARGET_ENTITY = make(LibComponentNames.TARGET_ENTITY,
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Float> TARGET_DIST = make(LibComponentNames.TARGET_DIST,
			builder -> builder.persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

	// Flower/Petal Pouch
	public static final DataComponentType<List<TagKey<Item>>> ITEM_TAGS = make(LibComponentNames.ITEM_TAGS,
			builder -> builder.persistent(TagKey.codec(Registries.ITEM).listOf(1, 2)).cacheEncoding());
	public static final DataComponentType<List<TagKey<Item>>> CRAFTABLE_ITEM_TAGS = make(LibComponentNames.CRAFTABLE_ITEM_TAGS,
			builder -> builder.persistent(TagKey.codec(Registries.ITEM).listOf(1, 10)).cacheEncoding());

	// Various block entity data
	public static final DataComponentType<Integer> DECAY_TICKS = make("decay_ticks",
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> STREAK_LENGTH = make("streak_length",
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<Integer> LAST_REPEATS = make("last_repeats",
			builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DataComponentType<List<ItemStack>> LAST_FOODS = make("last_foods",
			builder -> builder.persistent(ExtraCodecs.nonEmptyList(ItemStack.SINGLE_ITEM_CODEC.sizeLimitedListOf(GourmaryllisBlockEntity.getMaxStreak())))
					.cacheEncoding().networkSynchronized(ItemStack.LIST_STREAM_CODEC));
	public static final DataComponentType<List<ResourceLocation>> LAST_FLOWERS = make("last_flowers",
			builder -> builder.persistent(ExtraCodecs.nonEmptyList(ResourceLocation.CODEC.sizeLimitedListOf(RafflowsiaBlockEntity.getMaxStreak())))
					.cacheEncoding().networkSynchronized(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list(RafflowsiaBlockEntity.getMaxStreak()))));
	public static final DataComponentType<DyeColor> NEXT_COLOR = make("next_color",
			builder -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));

	public static void registerComponents(BiConsumer<DataComponentType<?>, ResourceLocation> biConsumer) {
		for (Map.Entry<String, DataComponentType<?>> entry : ALL.entrySet()) {
			biConsumer.accept(entry.getValue(), botaniaRL(entry.getKey()));
		}
	}

	private static <T> DataComponentType<T> make(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		if (!name.matches("[a-z]+(?:_[a-z0-9]+)*")) {
			throw new IllegalArgumentException("Typo? Name should be in snake_case: " + name);
		}
		DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
		var old = ALL.put(name, type);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name " + name);
		}
		return type;
	}

	private static DataComponentType<Unit> makeUnit(String name) {
		return make(name, builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	}

	private static DataComponentType<Unit> makeTransientUnit(String name) {
		return make(name, builder -> builder.networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	}

}
