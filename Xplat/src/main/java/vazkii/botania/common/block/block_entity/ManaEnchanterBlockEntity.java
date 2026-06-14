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
import com.mojang.blaze3d.platform.Window;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.network.clientbound.EnchanterDestroyEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaEnchanterBlockEntity extends BlockEntity implements ManaReceiver, SparkAttachable, Wandable, Clearable {
	private static final String TAG_STAGE = "stage";
	private static final String TAG_STAGE_TICKS = "stageTicks";
	private static final String TAG_STAGE_3_END_TICKS = "stage3EndTicks";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private static final String TAG_MANA = "mana";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ENCHANTS = "enchantsToApply";
	private static final int CRAFT_EFFECT_EVENT = 0;
	private static final int IDLE_CHECK_INTERVAL_TICKS = 10;

	private static final String[][] PATTERN = new String[][] {
			{
					"_P_______P_",
					"___________",
					"___________",
					"P_________P",
					"___________",
					"___________",
					"_P_______P_",
			},
			{
					"_F_______F_",
					"___________",
					"____F_F____",
					"F____L____F",
					"____F_F____",
					"___________",
					"_F_______F_",
			},
			{
					"___________",
					"____BBB____",
					"___B_B_B___",
					"___BB0BB___",
					"___B_B_B___",
					"____BBB____",
					"___________",
			}
	};

	private static final Supplier<IStateMatcher> OBSIDIAN_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			Blocks.OBSIDIAN,
			state -> state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)
	));

	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			PATTERN,
			'P', BotaniaBlocks.MANA_PYLON,
			'L', Blocks.LAPIS_BLOCK,
			'B', OBSIDIAN_MATCHER.get(),
			'0', OBSIDIAN_MATCHER.get(),
			'F', PatchouliAPI.get().tagMatcher(BotaniaTags.Blocks.ENCHANTER_FLOWERS)
	));

	private static final Supplier<IMultiblock> FORMED_MULTIBLOCK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			PATTERN,
			'P', BotaniaBlocks.MANA_PYLON,
			'L', BotaniaBlocks.MANA_ENCHANTER,
			'B', OBSIDIAN_MATCHER.get(),
			'0', OBSIDIAN_MATCHER.get(),
			'F', PatchouliAPI.get().predicateMatcher(BotaniaBlocks.WHITE_MYSTICAL_FLOWER, state -> state.is(BotaniaTags.Blocks.ENCHANTER_FLOWERS))
	));

	public State stage = State.IDLE;
	public int stageTicks = 0;

	public int stage3EndTicks = 0;

	private int idleTicks = 0;

	private int manaRequired = -1;
	private int mana = 0;

	public ItemStack itemToEnchant = ItemStack.EMPTY;
	private final List<EnchantmentInstance> enchants = new ArrayList<>();

	private static final Map<Direction.Axis, BlockPos[]> PYLON_LOCATIONS = new EnumMap<>(Direction.Axis.class);

	static {
		PYLON_LOCATIONS.put(Direction.Axis.X, new BlockPos[] { new BlockPos(-5, 1, 0), new BlockPos(5, 1, 0), new BlockPos(-4, 1, 3), new BlockPos(4, 1, 3), new BlockPos(-4, 1, -3), new BlockPos(4, 1, -3) });
		PYLON_LOCATIONS.put(Direction.Axis.Z, new BlockPos[] { new BlockPos(0, 1, -5), new BlockPos(0, 1, 5), new BlockPos(3, 1, -4), new BlockPos(3, 1, 4), new BlockPos(-3, 1, -4), new BlockPos(-3, 1, 4) });
	}

	public ManaEnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.MANA_ENCHANTER, pos, state);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if (stage != State.IDLE || itemToEnchant.isEmpty() || !itemToEnchant.isEnchantable()) {
			return false;
		}

		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX() - 2, worldPosition.getY(), worldPosition.getZ() - 2, worldPosition.getX() + 3, worldPosition.getY() + 1, worldPosition.getZ() + 3));
		int count = items.size();

		if (count > 0 && !level.isClientSide()) {
			for (ItemEntity entity : items) {
				ItemStack item = entity.getItem();
				if (item.is(Items.ENCHANTED_BOOK)) {
					ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);
					if (!itemenchantments.isEmpty()) {
						for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
							if (isEnchantmentValid(entry.getKey())) {
								advanceStage();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void gatherEnchants() {
		if (!level.isClientSide() && stageTicks % 20 == 0) {
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX() - 2, worldPosition.getY(), worldPosition.getZ() - 2, worldPosition.getX() + 3, worldPosition.getY() + 1, worldPosition.getZ() + 3));
			boolean addedEnch = false;

			for (ItemEntity entity : items) {
				ItemStack item = entity.getItem();
				if (item.is(Items.ENCHANTED_BOOK)) {
					ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);
					if (!itemenchantments.isEmpty()) {
						for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
							Holder<Enchantment> ench = entry.getKey();
							int enchantLvl = entry.getIntValue();
							if (!hasEnchantAlready(ench) && isEnchantmentValid(ench)) {
								this.enchants.add(new EnchantmentInstance(ench, enchantLvl));
								level.playSound(null, worldPosition, BotaniaSounds.ding, SoundSource.BLOCKS, 1F, 1F);
								addedEnch = true;
								break;
							}
						}
					}
				}
			}

			if (!addedEnch) {
				if (enchants.isEmpty()) {
					stage = State.IDLE;
				} else {
					advanceStage();
				}
			}
		}
	}

	private void gatherMana(Direction.Axis axis) {
		if (manaRequired == -1) {
			manaRequired = 0;
			for (EnchantmentInstance data : enchants) {
				manaRequired += (int) (5000F * ((15 - Math.min(15, data.enchantment.value().getWeight()))
						* 1.05F)
						* ((3F + data.level * data.level)
								* 0.25F)
						* (0.9F + enchants.size() * 0.05F)
						* (data.enchantment.is(EnchantmentTags.DOUBLE_TRADE_PRICE) ? 1.25F : 1F));
			}
		} else if (mana >= manaRequired) {
			manaRequired = 0;
			for (BlockPos offset : PYLON_LOCATIONS.get(axis)) {
				BlockEntity te = level.getBlockEntity(worldPosition.offset(offset));
				if (te instanceof PylonBlockEntity pylon) {
					pylon.activated = false;
				}
			}

			advanceStage();
		} else {
			SparkHelper.registerTransferFromSparksAround(SparkAttachable.getAttachedSpark(level, getBlockPos()), level, worldPosition);
			if (stageTicks % 5 == 0) {
				sync();
			}
		}
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, ManaEnchanterBlockEntity self) {
		Direction.Axis axis = state.getValue(BotaniaStateProperties.ENCHANTER_DIRECTION);

		for (BlockPos offset : PYLON_LOCATIONS.get(axis)) {
			BlockEntity tile = level.getBlockEntity(worldPosition.offset(offset));
			if (tile instanceof PylonBlockEntity pylon) {
				boolean gatheringMana = self.stage == State.GATHER_MANA;
				pylon.activated = gatheringMana;
				if (gatheringMana) {
					pylon.centerPos = worldPosition;
				}
			}
		}

		if (self.stage != State.IDLE) {
			self.stageTicks++;
		} else {
			self.idleTicks++;
		}

		if (level.isClientSide() || self.stage == State.IDLE && self.idleTicks % IDLE_CHECK_INTERVAL_TICKS != 0) {
			return;
		}

		Rotation rot = getAxisRotation(axis);
		if (!FORMED_MULTIBLOCK.get().validate(level, worldPosition.below(), rot)) {
			level.setBlockAndUpdate(worldPosition, Blocks.LAPIS_BLOCK.defaultBlockState());
			XplatAbstractions.INSTANCE.sendToNear(level, worldPosition, new EnchanterDestroyEffectPacket(worldPosition));
			level.playSound(null, worldPosition, BotaniaSounds.enchanterFade, SoundSource.BLOCKS, 1F, 1F);
			return;
		}

		switch (self.stage) {
			case GATHER_ENCHANTS -> self.gatherEnchants();
			case GATHER_MANA -> self.gatherMana(axis);
			case DO_ENCHANT -> { // Enchant
				if (self.stageTicks >= 100) {
					for (EnchantmentInstance data : self.enchants) {
						if (EnchantmentHelper.getItemEnchantmentLevel(data.enchantment, self.itemToEnchant) == 0) {
							self.itemToEnchant.enchant(data.enchantment, data.level);
						}
					}

					self.enchants.clear();
					self.manaRequired = -1;
					self.mana = 0;

					level.blockEvent(worldPosition, BotaniaBlocks.MANA_ENCHANTER, CRAFT_EFFECT_EVENT, 0);
					self.advanceStage();
				}
			}
			case RESET -> { // Reset
				if (self.stageTicks >= 20) {
					self.advanceStage();
				}

			}
			default -> {
			}
		}
	}

	private void advanceStage() {
		switch (stage) {
			case IDLE -> stage = State.GATHER_ENCHANTS;
			case GATHER_ENCHANTS -> stage = State.GATHER_MANA;
			case GATHER_MANA -> stage = State.DO_ENCHANT;
			case DO_ENCHANT -> {
				stage = State.RESET;
				stage3EndTicks = stageTicks;
			}
			case RESET -> {
				stage = State.IDLE;
				stage3EndTicks = 0;
			}
		}

		stageTicks = 0;
		sync();
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == CRAFT_EFFECT_EVENT) {
			if (level.isClientSide()) {
				for (int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					level.addParticle(data, getBlockPos().getX() + Math.random() * 0.4 - 0.2, getBlockPos().getY(),
							getBlockPos().getZ() + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
						BotaniaSounds.enchanterEnchant, SoundSource.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		return super.triggerEvent(event, param);
	}

	@Override
	@UnknownNullability
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaRequired;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(manaRequired, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return manaRequired > 0;
	}

	public void sync() {
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_MANA, mana);
		cmp.putInt(TAG_MANA_REQUIRED, manaRequired);
		cmp.putInt(TAG_STAGE, stage.ordinal());
		cmp.putInt(TAG_STAGE_TICKS, stageTicks);
		cmp.putInt(TAG_STAGE_3_END_TICKS, stage3EndTicks);

		if (!itemToEnchant.isEmpty()) {
			cmp.put(TAG_ITEM, itemToEnchant.save(registries));
		}

		if (!enchants.isEmpty()) {
			String enchStr = enchants.stream()
					.map(e -> e.enchantment.unwrapKey().map(ResourceKey::location).orElseThrow() + "=" + e.level)
					.collect(Collectors.joining(","));

			cmp.putString(TAG_ENCHANTS, enchStr);
		}
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		mana = cmp.getInt(TAG_MANA);
		manaRequired = cmp.getInt(TAG_MANA_REQUIRED);
		stage = State.values()[cmp.getInt(TAG_STAGE)];
		stageTicks = cmp.getInt(TAG_STAGE_TICKS);
		stage3EndTicks = cmp.getInt(TAG_STAGE_3_END_TICKS);

		CompoundTag itemCmp = cmp.getCompound(TAG_ITEM);
		itemToEnchant = itemCmp.isEmpty() ? ItemStack.EMPTY : ItemStack.parseOptional(registries, itemCmp);

		enchants.clear();
		String enchStr = cmp.getString(TAG_ENCHANTS);
		if (!enchStr.isEmpty()) {
			String[] enchTokens = enchStr.split(",");
			for (String token : enchTokens) {
				String[] entryTokens = token.split("=");
				ResourceLocation enchantmentId = ResourceLocation.parse(entryTokens[0]);
				ResourceKey<Enchantment> enchantmentKey = ResourceKey.create(Registries.ENCHANTMENT, enchantmentId);
				int lvl = Integer.parseInt(entryTokens[1]);
				registries.lookupOrThrow(Registries.ENCHANTMENT).get(enchantmentKey)
						.ifPresent(ench -> enchants.add(new EnchantmentInstance(ench, lvl)));
			}
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		// TODO: adjust implementation to simplify updates
		saveAdditional(tag, registries);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	private boolean hasEnchantAlready(Holder<Enchantment> enchant) {
		for (EnchantmentInstance data : enchants) {
			if (data.enchantment == enchant) {
				return true;
			}
		}

		return false;
	}

	private boolean isEnchantmentValid(@Nullable Holder<Enchantment> ench) {
		if (ench == null || !ench.value().canEnchant(itemToEnchant)) {
			return false;
		}

		for (EnchantmentInstance data : enchants) {
			Holder<Enchantment> otherEnch = data.enchantment;
			if (!Enchantment.areCompatible(ench, otherEnch)) {
				return false;
			}
		}

		return true;
	}

	@Nullable
	public static Direction.Axis canEnchanterExist(Level world, BlockPos pos) {
		Rotation rot = MULTIBLOCK.get().validate(world, pos.below());
		if (rot == null) {
			return null;
		}

		return switch (rot) {
			case NONE, CLOCKWISE_180 -> Direction.Axis.Z;
			case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> Direction.Axis.X;
		};
	}

	private static Rotation getAxisRotation(Direction.Axis axis) {
		return switch (axis) {
			case Z -> Rotation.NONE;
			case X -> Rotation.CLOCKWISE_90;
			default -> throw new IllegalStateException("Enchanter should only ever be facing in X or Z direction");
		};
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public boolean areIncomingTransfersDone() {
		return stage == State.DO_ENCHANT;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, manaRequired - getCurrentMana());
	}

	@Override
	public void clearContent() {
		this.itemToEnchant = ItemStack.EMPTY;
		this.stage = State.IDLE;
	}

	public static Wandable createLapisBlockWandable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
		return (player, stack, side) -> {
			Direction.Axis axis = ManaEnchanterBlockEntity.canEnchanterExist(level, pos);
			if (axis == null) {
				return false;
			}

			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, BotaniaBlocks.MANA_ENCHANTER.defaultBlockState().setValue(BotaniaStateProperties.ENCHANTER_DIRECTION, axis));
				level.playSound(null, pos, BotaniaSounds.enchanterForm, SoundSource.BLOCKS, 1F, 1F);
				PlayerHelper.grantCriterion((ServerPlayer) player, botaniaRL("main/enchanter_make"), "code_triggered");
			} else {
				RandomSource rng = level.getRandom();
				for (int i = 0; i < 50; i++) {
					float red = rng.nextFloat();
					float green = rng.nextFloat();
					float blue = rng.nextFloat();

					double x = (rng.nextDouble() - 0.5) * 6;
					double y = (rng.nextDouble() - 0.5) * 6;
					double z = (rng.nextDouble() - 0.5) * 6;

					float velMul = -0.07F;

					float motionX = (float) x * velMul;
					float motionY = (float) y * velMul;
					float motionZ = (float) z * velMul;
					WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.15F + 0.15F, red, green, blue);
					level.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, motionX, motionY, motionZ);
				}
			}

			return true;
		};
	}

	public static class WandHud implements WandHUD {
		private final ManaEnchanterBlockEntity enchanter;

		public WandHud(ManaEnchanterBlockEntity enchanter) {
			this.enchanter = enchanter;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			if (enchanter.manaRequired > 0 && !enchanter.itemToEnchant.isEmpty()) {
				int x = window.getGuiScaledWidth() / 2 + 8;
				int y = window.getGuiScaledHeight() / 2 - 12;

				RenderHelper.renderHUDBox(gui, x, y, x + 24, y + 24);
				RenderHelper.renderProgressPie(gui, partialTick, x + 4, y + 4,
						(float) enchanter.mana / (float) enchanter.manaRequired,
						enchanter.itemToEnchant
				);
			}
		}
	}

	public enum State {
		IDLE,
		GATHER_ENCHANTS,
		GATHER_MANA,
		DO_ENCHANT,
		RESET
	}

}
