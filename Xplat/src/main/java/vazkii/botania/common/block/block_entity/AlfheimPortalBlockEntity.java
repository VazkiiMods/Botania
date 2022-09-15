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
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.recipe.ElvenTradeRecipe;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.advancements.AlfheimPortalBreadTrigger;
import vazkii.botania.common.advancements.AlfheimPortalTrigger;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.TriPredicate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AlfheimPortalBlockEntity extends BotaniaBlockEntity implements Wandable {
	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> {
		record Matcher(TagKey<Block> tag, Direction.Axis displayedRotation, Block defaultBlock) implements IStateMatcher {
			@Override
			public BlockState getDisplayedState(long ticks) {
				var blocks = StreamSupport.stream(Registry.BLOCK.getTagOrEmpty(this.tag).spliterator(), false)
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

	public static final int MANA_COST = 500;
	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_TICKS_SINCE_LAST_ITEM = "ticksSinceLastItem";
	private static final String TAG_STACK_COUNT = "stackCount";
	private static final String TAG_STACK = "portalStack";
	public static final String TAG_PORTAL_FLAG = "_elvenPortal";

	private final List<ItemStack> stacksIn = new ArrayList<>();

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
		if (blockState.getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.OFF) {
			self.ticksOpen = 0;
			return;
		}
		AlfheimPortalState state = blockState.getValue(BotaniaStateProperties.ALFPORTAL_STATE);
		AlfheimPortalState newState = self.getValidState();

		self.ticksOpen++;

		AABB aabb = self.getPortalAABB();
		boolean open = self.ticksOpen > 60;
		XplatAbstractions.INSTANCE.fireElvenPortalUpdateEvent(self, aabb, open, self.stacksIn);

		if (self.ticksOpen > 60) {
			self.ticksSinceLastItem++;
			if (level.isClientSide && BotaniaConfig.client().elfPortalParticlesEnabled()) {
				self.blockParticle(state);
			}

			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, aabb);
			if (!level.isClientSide) {
				for (ItemEntity item : items) {
					if (!item.isAlive()) {
						continue;
					}

					ItemStack stack = item.getItem();
					if (XplatAbstractions.INSTANCE.itemFlagsComponent(item).alfPortalSpawned) {
						continue;
					}

					item.discard();
					if (self.validateItemUsage(item)) {
						self.addItem(stack);
					}
					self.ticksSinceLastItem = 0;
				}
			}

			if (!level.isClientSide && !self.stacksIn.isEmpty() && self.ticksSinceLastItem >= 4) {
				self.resolveRecipes();
			}
		}

		if (self.closeNow) {
			if (!level.isClientSide) {
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

			if (!level.isClientSide) {
				level.setBlockAndUpdate(worldPosition, blockState.setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
			}
		} else if (self.explode) {
			level.explode(null, worldPosition.getX() + .5, worldPosition.getY() + 2.0, worldPosition.getZ() + .5,
					3f, Explosion.BlockInteraction.BREAK);
			self.explode = false;

			if (!level.isClientSide && self.breadPlayer != null) {
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
		for (Recipe<?> recipe : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE).values()) {
			if (recipe instanceof ElvenTradeRecipe tradeRecipe && tradeRecipe.containsItem(inputStack)) {
				return true;
			}
		}
		if (inputStack.is(Items.BREAD)) {
			//Don't teleport bread. (See also: #2403)
			explode = true;
			breadPlayer = entity.getThrower();
		}

		return false;
	}

	private void blockParticle(AlfheimPortalState state) {
		double dh, dy;

		// Pick one of the inner positions
		switch (level.random.nextInt(9)) {
			case 0 -> {
				dh = 0;
				dy = 1;
			}
			case 1 -> {
				dh = 0;
				dy = 2;
			}
			case 2 -> {
				dh = 0;
				dy = 3;
			}
			case 3 -> {
				dh = -1;
				dy = 1;
			}
			case 4 -> {
				dh = -1;
				dy = 2;
			}
			case 5 -> {
				dh = -1;
				dy = 3;
			}
			case 6 -> {
				dh = 1;
				dy = 1;
			}
			case 7 -> {
				dh = 1;
				dy = 2;
			}
			case 8 -> {
				dh = 1;
				dy = 3;
			}
			default -> throw new AssertionError();
		}
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
			AlfheimPortalState newState = getValidState();
			if (newState != AlfheimPortalState.OFF) {
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
				if (player instanceof ServerPlayer serverPlayer) {
					AlfheimPortalTrigger.INSTANCE.trigger(serverPlayer, serverPlayer.getLevel(), getBlockPos(), stack);
				}
				return true;
			}
		}

		return false;
	}

	private AABB getPortalAABB() {
		AABB aabb = new AABB(worldPosition.offset(-1, 1, 0), worldPosition.offset(2, 4, 1));
		if (getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.ON_X) {
			aabb = new AABB(worldPosition.offset(0, 1, -1), worldPosition.offset(1, 4, 2));
		}

		return aabb;
	}

	private void addItem(ItemStack stack) {
		int size = stack.getCount();
		stack.setCount(1);
		for (int i = 0; i < size; i++) {
			stacksIn.add(stack.copy());
		}
	}

	@SuppressWarnings("unchecked")
	public static Collection<ElvenTradeRecipe> elvenTradeRecipes(Level world) {
		// By virtue of IRecipeType's type parameter,
		// we know all the recipes in the map must be ElvenTradeRecipe.
		// However, vanilla's signature on this method is dumb (should be Map<ResourceLocation, T>)
		return (Collection<ElvenTradeRecipe>) (Collection<?>) BotaniaRecipeTypes.getRecipes(world, BotaniaRecipeTypes.ELVEN_TRADE_TYPE).values();
	}

	private void resolveRecipes() {
		List<BlockPos> pylons = locatePylons();
		for (Recipe<?> r : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE).values()) {
			if (!(r instanceof ElvenTradeRecipe recipe)) {
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
				}
				break;
			}
		}
	}

	private void spawnItem(ItemStack stack) {
		ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, stack);
		XplatAbstractions.INSTANCE.itemFlagsComponent(item).alfPortalSpawned = true;
		level.addFreshEntity(item);
		ticksSinceLastItem = 0;
	}

	@Override
	public void saveAdditional(CompoundTag cmp) {
		super.saveAdditional(cmp);

		cmp.putInt(TAG_STACK_COUNT, stacksIn.size());
		int i = 0;
		for (ItemStack stack : stacksIn) {
			CompoundTag stackcmp = stack.save(new CompoundTag());
			cmp.put(TAG_STACK + i, stackcmp);
			i++;
		}
	}

	@Override
	public void load(@NotNull CompoundTag cmp) {
		super.load(cmp);

		int count = cmp.getInt(TAG_STACK_COUNT);
		stacksIn.clear();
		for (int i = 0; i < count; i++) {
			CompoundTag stackcmp = cmp.getCompound(TAG_STACK + i);
			ItemStack stack = ItemStack.of(stackcmp);
			stacksIn.add(stack);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_TICKS_OPEN, ticksOpen);
		cmp.putInt(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		ticksOpen = cmp.getInt(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInt(TAG_TICKS_SINCE_LAST_ITEM);
	}

	private AlfheimPortalState getValidState() {
		Rotation rot = MULTIBLOCK.get().validate(level, getBlockPos());
		if (rot == null) {
			return AlfheimPortalState.OFF;
		}

		lightPylons();
		return switch (rot) {
			case NONE, CLOCKWISE_180 -> AlfheimPortalState.ON_Z;
			case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> AlfheimPortalState.ON_X;
		};
	}

	public List<BlockPos> locatePylons() {
		int range = 5;

		return BlockPos.betweenClosedStream(getBlockPos().offset(-range, -range, -range), getBlockPos().offset(range, range, range))
				.filter(level::hasChunkAt)
				.filter(p -> level.getBlockState(p).is(BotaniaBlocks.naturaPylon) && level.getBlockState(p.below()).getBlock() instanceof ManaPoolBlock)
				.map(BlockPos::immutable)
				.collect(Collectors.toList());
	}

	public void lightPylons() {
		if (ticksOpen < 50) {
			return;
		}

		List<BlockPos> pylons = locatePylons();
		for (BlockPos pos : pylons) {
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof PylonBlockEntity pylon) {
				pylon.activated = true;
				pylon.centerPos = getBlockPos();
			}
		}

		if (ticksOpen == 50) {
			consumeMana(pylons, 200000, true);
		}
	}

	public boolean consumeMana(List<BlockPos> pylons, int totalCost, boolean close) {
		List<ManaPoolBlockEntity> consumePools = new ArrayList<>();
		int consumed = 0;

		if (pylons.size() < 2) {
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
				} else if (!level.isClientSide) {
					consumePools.add(pool);
					consumed += costPer;
				}
			}
		}

		if (consumed >= expectedConsumption) {
			for (ManaPoolBlockEntity pool : consumePools) {
				pool.receiveMana(-costPer);
			}
			return true;
		}

		return false;
	}
}
