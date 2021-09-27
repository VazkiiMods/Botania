/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.google.common.base.Predicates;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileEnchanter extends TileMod implements ISparkAttachable {
	private static final String TAG_STAGE = "stage";
	private static final String TAG_STAGE_TICKS = "stageTicks";
	private static final String TAG_STAGE_3_END_TICKS = "stage3EndTicks";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private static final String TAG_MANA = "mana";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ENCHANTS = "enchantsToApply";
	private static final int CRAFT_EFFECT_EVENT = 0;

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
			'P', ModBlocks.manaPylon,
			'L', Blocks.LAPIS_BLOCK,
			'B', OBSIDIAN_MATCHER.get(),
			'0', OBSIDIAN_MATCHER.get(),
			'F', PatchouliAPI.get().predicateMatcher(ModBlocks.whiteFlower, state -> state.is(ModTags.Blocks.ENCHANTER_FLOWERS))
	));

	private static final Supplier<IMultiblock> FORMED_MULTIBLOCK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			PATTERN,
			'P', ModBlocks.manaPylon,
			'L', ModBlocks.enchanter,
			'B', OBSIDIAN_MATCHER.get(),
			'0', OBSIDIAN_MATCHER.get(),
			'F', PatchouliAPI.get().predicateMatcher(ModBlocks.whiteFlower, state -> state.is(ModTags.Blocks.ENCHANTER_FLOWERS))
	));

	public State stage = State.IDLE;
	public int stageTicks = 0;

	public int stage3EndTicks = 0;

	private int manaRequired = -1;
	private int mana = 0;

	public ItemStack itemToEnchant = ItemStack.EMPTY;
	private final List<EnchantmentInstance> enchants = new ArrayList<>();

	private static final Map<Direction.Axis, BlockPos[]> PYLON_LOCATIONS = new EnumMap<>(Direction.Axis.class);

	static {
		PYLON_LOCATIONS.put(Direction.Axis.X, new BlockPos[] { new BlockPos(-5, 1, 0), new BlockPos(5, 1, 0), new BlockPos(-4, 1, 3), new BlockPos(4, 1, 3), new BlockPos(-4, 1, -3), new BlockPos(4, 1, -3) });
		PYLON_LOCATIONS.put(Direction.Axis.Z, new BlockPos[] { new BlockPos(0, 1, -5), new BlockPos(0, 1, 5), new BlockPos(3, 1, -4), new BlockPos(3, 1, 4), new BlockPos(-3, 1, -4), new BlockPos(-3, 1, 4) });
	}

	public TileEnchanter(BlockPos pos, BlockState state) {
		super(ModTiles.ENCHANTER, pos, state);
	}

	public void onWanded(Player player, ItemStack wand) {
		if (stage != State.IDLE || itemToEnchant.isEmpty() || !itemToEnchant.isEnchantable()) {
			return;
		}

		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX() - 2, worldPosition.getY(), worldPosition.getZ() - 2, worldPosition.getX() + 3, worldPosition.getY() + 1, worldPosition.getZ() + 3));
		int count = items.size();

		if (count > 0 && !level.isClientSide) {
			for (ItemEntity entity : items) {
				ItemStack item = entity.getItem();
				if (item.is(Items.ENCHANTED_BOOK)) {
					Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);
					if (enchants.size() > 0) {
						Enchantment enchant = enchants.keySet().iterator().next();
						if (isEnchantmentValid(enchant)) {
							advanceStage();
							return;
						}
					}
				}
			}
		}
	}

	private void gatherEnchants() {
		if (!level.isClientSide && stageTicks % 20 == 0) {
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX() - 2, worldPosition.getY(), worldPosition.getZ() - 2, worldPosition.getX() + 3, worldPosition.getY() + 1, worldPosition.getZ() + 3));
			boolean addedEnch = false;

			for (ItemEntity entity : items) {
				ItemStack item = entity.getItem();
				if (item.is(Items.ENCHANTED_BOOK)) {
					Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);
					if (enchants.size() > 0) {
						Map.Entry<Enchantment, Integer> e = enchants.entrySet().iterator().next();
						Enchantment ench = e.getKey();
						int enchantLvl = e.getValue();
						if (!hasEnchantAlready(ench) && isEnchantmentValid(ench)) {
							this.enchants.add(new EnchantmentInstance(ench, enchantLvl));
							level.playSound(null, worldPosition, ModSounds.ding, SoundSource.BLOCKS, 1F, 1F);
							addedEnch = true;
							break;
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
				manaRequired += (int) (5000F * ((15 - Math.min(15, data.enchantment.getRarity().getWeight()))
						* 1.05F)
						* ((3F + data.level * data.level)
								* 0.25F)
						* (0.9F + enchants.size() * 0.05F)
						* (data.enchantment.isTreasureOnly() ? 1.25F : 1F));
			}
		} else if (mana >= manaRequired) {
			manaRequired = 0;
			for (BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
				BlockEntity te = level.getBlockEntity(worldPosition.offset(pylon));
				if (te instanceof TilePylon) {
					((TilePylon) te).activated = false;
				}
			}

			advanceStage();
		} else {
			IManaSpark spark = getAttachedSpark();
			if (spark != null) {
				SparkHelper.getSparksAround(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, spark.getNetwork())
						.filter(otherSpark -> spark != otherSpark && otherSpark.getAttachedTile() instanceof IManaPool)
						.forEach(os -> os.registerTransfer(spark));
			}
			if (stageTicks % 5 == 0) {
				sync();
			}
		}
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileEnchanter self) {
		Direction.Axis axis = state.getValue(BotaniaStateProps.ENCHANTER_DIRECTION);

		for (BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
			BlockEntity tile = level.getBlockEntity(worldPosition.offset(pylon));
			if (tile instanceof TilePylon) {
				((TilePylon) tile).activated = self.stage == State.GATHER_MANA;
				if (self.stage == State.GATHER_MANA) {
					((TilePylon) tile).centerPos = worldPosition;
				}
			}
		}

		if (self.stage != State.IDLE) {
			self.stageTicks++;
		}

		if (level.isClientSide) {
			return;
		}

		if (FORMED_MULTIBLOCK.get().validate(level, worldPosition.below()) == null) {
			level.setBlockAndUpdate(worldPosition, Blocks.LAPIS_BLOCK.defaultBlockState());
			PacketBotaniaEffect.sendNearby(level, worldPosition, PacketBotaniaEffect.EffectType.ENCHANTER_DESTROY,
					worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
			level.playSound(null, worldPosition, ModSounds.enchanterFade, SoundSource.BLOCKS, 0.5F, 10F);
		}

		switch (self.stage) {
		case GATHER_ENCHANTS:
			self.gatherEnchants();
			break;
		case GATHER_MANA:
			self.gatherMana(axis);
			break;
		case DO_ENCHANT: { // Enchant
			if (self.stageTicks >= 100) {
				for (EnchantmentInstance data : self.enchants) {
					if (EnchantmentHelper.getItemEnchantmentLevel(data.enchantment, self.itemToEnchant) == 0) {
						self.itemToEnchant.enchant(data.enchantment, data.level);
					}
				}

				self.enchants.clear();
				self.manaRequired = -1;
				self.mana = 0;

				level.blockEvent(worldPosition, ModBlocks.enchanter, CRAFT_EFFECT_EVENT, 0);
				self.advanceStage();
			}
			break;
		}
		case RESET: { // Reset
			if (self.stageTicks >= 20) {
				self.advanceStage();
			}

			break;
		}
		default:
			break;
		}
	}

	private void advanceStage() {
		switch (stage) {
		case IDLE:
			stage = State.GATHER_ENCHANTS;
			break;
		case GATHER_ENCHANTS:
			stage = State.GATHER_MANA;
			break;
		case GATHER_MANA:
			stage = State.DO_ENCHANT;
			break;
		case DO_ENCHANT: {
			stage = State.RESET;
			stage3EndTicks = stageTicks;
			break;
		}
		case RESET: {
			stage = State.IDLE;
			stage3EndTicks = 0;
			break;
		}
		}

		stageTicks = 0;
		sync();
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		switch (event) {
		case CRAFT_EFFECT_EVENT: {
			if (level.isClientSide) {
				for (int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					level.addParticle(data, getBlockPos().getX() + Math.random() * 0.4 - 0.2, getBlockPos().getY(), getBlockPos().getZ() + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ModSounds.enchanterEnchant, SoundSource.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		default:
			return super.triggerEvent(event, param);
		}
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

	@Override
	public void sync() {
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, mana);
		cmp.putInt(TAG_MANA_REQUIRED, manaRequired);
		cmp.putInt(TAG_STAGE, stage.ordinal());
		cmp.putInt(TAG_STAGE_TICKS, stageTicks);
		cmp.putInt(TAG_STAGE_3_END_TICKS, stage3EndTicks);

		CompoundTag itemCmp = new CompoundTag();
		if (!itemToEnchant.isEmpty()) {
			cmp.put(TAG_ITEM, itemToEnchant.save(itemCmp));
		}

		String enchStr = enchants.stream()
				.map(e -> Registry.ENCHANTMENT.getKey(e.enchantment) + "=" + e.level)
				.collect(Collectors.joining(","));
		cmp.putString(TAG_ENCHANTS, enchStr);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		mana = cmp.getInt(TAG_MANA);
		manaRequired = cmp.getInt(TAG_MANA_REQUIRED);
		stage = State.values()[cmp.getInt(TAG_STAGE)];
		stageTicks = cmp.getInt(TAG_STAGE_TICKS);
		stage3EndTicks = cmp.getInt(TAG_STAGE_3_END_TICKS);

		CompoundTag itemCmp = cmp.getCompound(TAG_ITEM);
		itemToEnchant = ItemStack.of(itemCmp);

		enchants.clear();
		String enchStr = cmp.getString(TAG_ENCHANTS);
		if (!enchStr.isEmpty()) {
			String[] enchTokens = enchStr.split(",");
			for (String token : enchTokens) {
				try {
					String[] entryTokens = token.split("=");
					int lvl = Integer.parseInt(entryTokens[1]);
					Registry.ENCHANTMENT.getOptional(new ResourceLocation(entryTokens[0])).ifPresent(ench -> {
						enchants.add(new EnchantmentInstance(ench, lvl));
					});
				} catch (ResourceLocationException ignored) {}
			}
		}
	}

	private boolean hasEnchantAlready(Enchantment enchant) {
		for (EnchantmentInstance data : enchants) {
			if (data.enchantment == enchant) {
				return true;
			}
		}

		return false;
	}

	private boolean isEnchantmentValid(@Nullable Enchantment ench) {
		if (ench == null || !ench.canEnchant(itemToEnchant)) {
			return false;
		}

		for (EnchantmentInstance data : enchants) {
			Enchantment otherEnch = data.enchantment;
			if (!ench.isCompatibleWith(otherEnch)) {
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

		switch (rot) {
		default:
		case NONE:
		case CLOCKWISE_180:
			return Direction.Axis.Z;
		case CLOCKWISE_90:
		case COUNTERCLOCKWISE_90:
			return Direction.Axis.X;
		}
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public IManaSpark getAttachedSpark() {
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 2, worldPosition.getZ() + 1), Predicates.instanceOf(IManaSpark.class));
		if (sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (IManaSpark) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return stage == State.DO_ENCHANT;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, manaRequired - getCurrentMana());
	}

	@Environment(EnvType.CLIENT)
	public void renderHUD(PoseStack ms) {
		if (manaRequired > 0 && !itemToEnchant.isEmpty()) {
			int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 20;
			int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 8;

			RenderHelper.renderProgressPie(ms, x, y, (float) mana / (float) manaRequired, itemToEnchant);
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
