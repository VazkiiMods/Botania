/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import com.google.common.base.Suppliers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.recipe.ElvenTradeRecipe;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.advancements.AlfheimPortalBreadTrigger;
import vazkii.botania.common.advancements.AlfheimPortalTrigger;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.NbtHelper;
import vazkii.botania.common.internal_caps.ItemSources;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.world.BotaniaExplosionDamageCalculator;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.TriPredicate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class AlfheimPortalBlockEntity extends BlockEntity implements Wandable {
	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> {
		record Matcher(TagKey<Block> tag, Direction.Axis displayedRotation, Block defaultBlock) implements IStateMatcher {
			@Override
			public BlockState getDisplayedState(long ticks) {
				var blocks = StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(this.tag).spliterator(), false)
						.map(Holder::value)
						.toList();
				if (blocks.isEmpty()) {
					return Blocks.BEDROCK.defaultBlockState();
				}

				BlockState block = blocks.contains(defaultBlock)
						? defaultBlock.defaultBlockState()
						: blocks.get((int) ((ticks / 20) % blocks.size())).defaultBlockState();

				return block.hasProperty(BlockStateProperties.AXIS)
						? block.setValue(BlockStateProperties.AXIS, displayedRotation())
						: block;
			}

			@Override
			public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
				return (blockGetter, pos, state) -> state.is(tag());
			}
		}
		var horizontal = new Matcher(BotaniaTags.Blocks.LIVINGWOOD_LOGS, Direction.Axis.X, BotaniaBlocks.livingwoodLog);
		var vertical = new Matcher(BotaniaTags.Blocks.LIVINGWOOD_LOGS, Direction.Axis.Y, BotaniaBlocks.livingwoodLog);
		var horizontalGlimmer = new Matcher(BotaniaTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING, Direction.Axis.X, BotaniaBlocks.livingwoodLogGlimmering);
		var verticalGlimmer = new Matcher(BotaniaTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING, Direction.Axis.Y, BotaniaBlocks.livingwoodLogGlimmering);

		return PatchouliAPI.get().makeMultiblock(
				new String[][] {
						{ "_", "w", "g", "w", "_" },
						{ "W", " ", " ", " ", "W" },
						{ "G", " ", " ", " ", "G" },
						{ "W", " ", " ", " ", "W" },
						{ "_", "w", "0", "w", "_" }
				},
				'W', vertical,
				'w', horizontal,
				'G', verticalGlimmer,
				'g', horizontalGlimmer,
				'0', BotaniaBlocks.alfPortal
		);
	});

	private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new BotaniaExplosionDamageCalculator(true, BotaniaTags.Entities.PORTAL_BREAD_IMMUNE);
	private static final float EXPLOSION_RADIUS = 3f;
	public static final Style PORTAL_EXPLOSION_EXPLAINER_STYLE = Style.EMPTY
			.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/VazkiiMods/Botania/issues/2403"))
			.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("VazkiiMods/Botania#2403")));

	public static final int MANA_COST = 500;
	public static final int MANA_COST_OPENING = 200000;
	public static final int MIN_REQUIRED_PYLONS = 2;
	public static final int PYLON_SEARCH_RANGE = 5;
	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_TICKS_SINCE_LAST_ITEM = "ticksSinceLastItem";
	private static final String TAG_STACK_COUNT = "stackCount";
	private static final String TAG_STACK = "portalStack";
	private static final int TICKS_TO_LIGHT_PYLONS = 50;
	public static final int TICKS_UNTIL_FULLY_OPENED = 60;
	private static final int TICKS_BETWEEN_ITEMS = 4;

	private final List<ItemStack> stacksIn = new ArrayList<>();
	private final List<BlockPos> cachedPylonPositions = new ArrayList<>();

	public int ticksOpen = 0;
	private int ticksSinceLastItem = 0;
	private boolean closeNow = false;
	private boolean explode = false;
	@Nullable
	private UUID breadPlayer = null;

	public AlfheimPortalBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.ALF_PORTAL, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState blockState, AlfheimPortalBlockEntity self) {
		AlfheimPortalState state = blockState.getValue(BotaniaStateProperties.ALFPORTAL_STATE);
		if (state == AlfheimPortalState.OFF) {
			if (self.ticksOpen != 0) {
				self.ticksOpen = 0;
				self.ticksSinceLastItem = 0;
				self.setChanged();
			}
			return;
		}
		AlfheimPortalState newState = self.getValidState(state);

		if (self.ticksOpen <= TICKS_UNTIL_FULLY_OPENED) {
			self.ticksOpen++;
			if (self.ticksOpen > TICKS_UNTIL_FULLY_OPENED) {
				self.setChanged();
			} else {
				level.blockEntityChanged(worldPosition);
			}
		}

		AABB aabb = self.getPortalAABB(state);
		boolean open = self.ticksOpen > TICKS_UNTIL_FULLY_OPENED;
		XplatAbstractions.INSTANCE.fireElvenPortalUpdateEvent(self, aabb, open, self.stacksIn);

		if (self.ticksOpen > TICKS_UNTIL_FULLY_OPENED && !self.closeNow && newState != AlfheimPortalState.OFF) {
			if (self.ticksSinceLastItem <= TICKS_BETWEEN_ITEMS) {
				self.ticksSinceLastItem++;
				level.blockEntityChanged(worldPosition);
			}
			if (level.isClientSide() && BotaniaConfig.client().elfPortalParticlesEnabled()) {
				self.blockParticle(state);
			}

			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, aabb);
			if (!level.isClientSide()) {
				for (ItemEntity item : items) {
					if (!item.isAlive()) {
						continue;
					}

					ItemStack stack = item.getItem();
					if (XplatAbstractions.instance().isItemSource(item, ItemSources.ALFHEIM_PORTAL)) {
						continue;
					}

					item.discard();
					if (self.validateItemUsage(item)) {
						self.addItem(stack);
					}
					self.ticksSinceLastItem = 0;
					self.setChanged();
				}
			}

			if (!level.isClientSide() && !self.stacksIn.isEmpty() && self.ticksSinceLastItem >= TICKS_BETWEEN_ITEMS) {
				self.resolveRecipes();
			}
		}

		if (self.closeNow) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(worldPosition, BotaniaBlocks.alfPortal.defaultBlockState());
			}
			for (int i = 0; i < 36; i++) {
				self.blockParticle(state);
			}
			self.closeNow = false;
		} else if (newState != state) {
			if (newState == AlfheimPortalState.OFF) {
				for (int i = 0; i < 36; i++) {
					self.blockParticle(state);
				}
			}

			if (!level.isClientSide()) {
				level.setBlockAndUpdate(worldPosition, blockState.setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
			}
		} else if (self.explode) {
			Holder<DamageType> type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
					.getHolderOrThrow(BotaniaDamageTypes.PORTAL_BREAD_EXPLOSION);
			Vec3 sourcePosition = worldPosition.getCenter().add(0, 2, 0);
			level.explode(null, new DamageSource(type, sourcePosition), EXPLOSION_DAMAGE_CALCULATOR,
					sourcePosition, EXPLOSION_RADIUS, false, Level.ExplosionInteraction.TNT);
			self.explode = false;

			if (!level.isClientSide() && self.breadPlayer != null) {
				Player entity = level.getPlayerByUUID(self.breadPlayer);
				if (entity instanceof ServerPlayer serverPlayer) {
					AlfheimPortalBreadTrigger.INSTANCE.trigger(serverPlayer, worldPosition);
				}
			}
			self.breadPlayer = null;
		}
	}

	private boolean validateItemUsage(ItemEntity entity) {
		ItemStack inputStack = entity.getItem();
		for (RecipeHolder<ElvenTradeRecipe> recipe : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
			if (recipe.value() instanceof ElvenTradeRecipe tradeRecipe && tradeRecipe.containsItem(inputStack)) {
				return true;
			}
		}
		if (inputStack.is(Items.BREAD)) {
			//Don't teleport bread. (See also: #2403)
			explode = true;
			if (entity.getOwner() != null) {
				breadPlayer = entity.getOwner().getUUID();
			}
		}

		return false;
	}

	private void blockParticle(AlfheimPortalState state) {
		// Pick one of the inner positions, offsets [-1,+1] and [+1,+3]
		int rnd = level.getRandom().nextInt(9);
		double dh = (rnd / 3) - 1;
		double dy = (rnd % 3) + 1;
		double dx = state == AlfheimPortalState.ON_X ? 0 : dh;
		double dz = state == AlfheimPortalState.ON_Z ? 0 : dh;

		float motionMul = 0.2F;
		WispParticleData data = WispParticleData.wisp((float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() * 0.25F), (float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F));
		level.addParticle(data, getBlockPos().getX() + dx, getBlockPos().getY() + dy, getBlockPos().getZ() + dz, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		AlfheimPortalState state = getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
		if (state == AlfheimPortalState.OFF) {
			AlfheimPortalState newState = getValidState(state);
			if (newState != AlfheimPortalState.OFF) {
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
				if (player instanceof ServerPlayer serverPlayer) {
					AlfheimPortalTrigger.INSTANCE.trigger(serverPlayer, serverPlayer.serverLevel(), getBlockPos(), stack);
				}
				return true;
			}
		}

		return false;
	}

	private AABB getPortalAABB(AlfheimPortalState state) {
		return state == AlfheimPortalState.ON_X
				? MathHelper.inflateBoxAround(worldPosition, 0, 1, -1, 4)
				: MathHelper.inflateBoxAround(worldPosition, 1, 0, -1, 4);
	}

	private void addItem(ItemStack stack) {
		int size = stack.getCount();
		stack.setCount(1);
		for (int i = 0; i < size; i++) {
			stacksIn.add(stack.copy());
		}
	}

	public static Collection<RecipeHolder<ElvenTradeRecipe>> elvenTradeRecipes(Level world) {
		// By virtue of IRecipeType's type parameter,
		// we know all the recipes in the map must be ElvenTradeRecipe.
		// However, vanilla's signature on this method is dumb (should be Map<ResourceLocation, T>)
		return BotaniaRecipeTypes.getRecipes(world, BotaniaRecipeTypes.ELVEN_TRADE_TYPE);
	}

	private void resolveRecipes() {
		List<BlockPos> pylons = locatePylons(true);
		for (RecipeHolder<ElvenTradeRecipe> r : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
			if (!(r.value() instanceof ElvenTradeRecipe recipe)) {
				continue;
			}
			Optional<List<ItemStack>> match = recipe.match(stacksIn);
			if (match.isPresent()) {
				if (consumeMana(pylons, MANA_COST, false)) {
					List<ItemStack> inputs = match.get();
					for (ItemStack stack : inputs) {
						stacksIn.remove(stack);
					}
					for (ItemStack output : recipe.getOutputs(inputs)) {
						spawnItem(output.copy());
					}
					setChanged();
				}
				break;
			}
		}
	}

	private void spawnItem(ItemStack stack) {
		ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, stack);
		XplatAbstractions.instance().setItemSource(item, ItemSources.ALFHEIM_PORTAL);
		// probably can't easily associate this with a player for tracking stats
		stack.onCraftedBySystem(level);
		level.addFreshEntity(item);
		ticksSinceLastItem = 0;
	}

	@Override
	public void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_TICKS_OPEN, ticksOpen);
		cmp.putInt(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);

		cmp.putInt(TAG_STACK_COUNT, stacksIn.size());
		int i = 0;
		for (ItemStack stack : stacksIn) {
			cmp.put(TAG_STACK + i, stack.save(registries));
			i++;
		}
	}

	@Override
	public void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		ticksOpen = cmp.getInt(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInt(TAG_TICKS_SINCE_LAST_ITEM);

		int count = cmp.getInt(TAG_STACK_COUNT);
		stacksIn.clear();
		for (int i = 0; i < count; i++) {
			CompoundTag stackcmp = cmp.getCompound(TAG_STACK + i);
			ItemStack stack = ItemStack.parse(registries, stackcmp).orElse(ItemStack.EMPTY);
			stacksIn.add(stack);
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		NbtHelper.putVarInt(tag, TAG_TICKS_OPEN, ticksOpen);
		return tag;
	}

	@Nullable
	private static Rotation getStateRotation(AlfheimPortalState state) {
		return switch (state) {
			case ON_X -> Rotation.CLOCKWISE_90;
			case ON_Z -> Rotation.NONE;
			default -> null;
		};
	}

	private AlfheimPortalState getValidState(AlfheimPortalState oldState) {
		Rotation rot;
		if (oldState != AlfheimPortalState.OFF) {
			Rotation oldRot = getStateRotation(oldState);
			if (!MULTIBLOCK.get().validate(level, getBlockPos(), oldRot)) {
				return AlfheimPortalState.OFF;
			}
			rot = oldRot;
		} else {
			rot = MULTIBLOCK.get().validate(level, getBlockPos());
		}
		if (rot == null) {
			return AlfheimPortalState.OFF;
		}

		lightPylons();
		return switch (rot) {
			case NONE, CLOCKWISE_180 -> AlfheimPortalState.ON_Z;
			case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> AlfheimPortalState.ON_X;
		};
	}

	public List<BlockPos> locatePylons(boolean rescanNow) {
		if (!rescanNow && cachedPylonPositions.size() >= MIN_REQUIRED_PYLONS) {
			List<BlockPos> cachedResult = new ArrayList<>();
			for (BlockPos pos : cachedPylonPositions) {
				if (isValidPylonPosition(pos)) {
					cachedResult.add(pos);
				}
			}
			if (cachedResult.size() >= MIN_REQUIRED_PYLONS) {
				return cachedResult;
			}

			// not enough valid cached pylons, scan again
		}

		List<BlockPos> result = new ArrayList<>();

		for (BlockPos pos : MathHelper.aroundPosClosed(getBlockPos(), PYLON_SEARCH_RANGE)) {
			if (isValidPylonPosition(pos)) {
				result.add(pos.immutable());
			}
		}

		cachedPylonPositions.clear();
		cachedPylonPositions.addAll(result);

		return result;
	}

	private boolean isValidPylonPosition(BlockPos pos) {
		return getLevel().hasChunkAt(pos)
				&& getLevel().getBlockState(pos).is(BotaniaBlocks.naturaPylon)
				&& getLevel().getBlockState(pos.below()).getBlock() instanceof ManaPoolBlock;
	}

	public void lightPylons() {
		if (ticksOpen < TICKS_TO_LIGHT_PYLONS) {
			return;
		}

		boolean finishOpening = ticksOpen == TICKS_TO_LIGHT_PYLONS;
		List<BlockPos> pylons = locatePylons(finishOpening);
		for (BlockPos pos : pylons) {
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof PylonBlockEntity pylon) {
				pylon.activated = true;
				pylon.centerPos = getBlockPos();
			}
		}

		if (finishOpening) {
			consumeMana(pylons, MANA_COST_OPENING, true);
		}
	}

	public boolean consumeMana(List<BlockPos> pylons, int totalCost, boolean close) {
		List<ManaPoolBlockEntity> consumePools = new ArrayList<>();
		int consumed = 0;

		if (pylons.size() < MIN_REQUIRED_PYLONS) {
			closeNow = true;
			return false;
		}

		int costPer = Math.max(1, totalCost / pylons.size());
		int expectedConsumption = costPer * pylons.size();

		for (BlockPos pos : pylons) {
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof PylonBlockEntity pylon) {
				pylon.activated = true;
				pylon.centerPos = getBlockPos();
			}

			tile = level.getBlockEntity(pos.below());
			if (tile instanceof ManaPoolBlockEntity pool) {
				if (pool.getCurrentMana() < costPer) {
					closeNow = closeNow || close;
					return false;
				} else if (!level.isClientSide()) {
					consumePools.add(pool);
					consumed += costPer;
				}
			}
		}

		if (consumed >= expectedConsumption) {
			for (ManaPoolBlockEntity pool : consumePools) {
				pool.receiveMana(-costPer);
				pool.craftingEffect(false);
			}
			return true;
		}

		return false;
	}
}
