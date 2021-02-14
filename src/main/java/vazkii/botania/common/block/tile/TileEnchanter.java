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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileEnchanter extends TileMod implements ISparkAttachable, Tickable {
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

	public static final Lazy<IMultiblock> MULTIBLOCK = new Lazy<>(() -> PatchouliAPI.get().makeMultiblock(
			PATTERN,
			'P', ModBlocks.manaPylon,
			'L', Blocks.LAPIS_BLOCK,
			'B', Blocks.OBSIDIAN,
			'0', Blocks.OBSIDIAN,
			'F', PatchouliAPI.get().predicateMatcher(ModBlocks.whiteFlower, state -> state.getBlock().isIn(ModTags.Blocks.ENCHANTER_FLOWERS))
	));

	private static final Lazy<IMultiblock> FORMED_MULTIBLOCK = new Lazy<>(() -> PatchouliAPI.get().makeMultiblock(
			PATTERN,
			'P', ModBlocks.manaPylon,
			'L', ModBlocks.enchanter,
			'B', Blocks.OBSIDIAN,
			'0', Blocks.OBSIDIAN,
			'F', PatchouliAPI.get().predicateMatcher(ModBlocks.whiteFlower, state -> state.getBlock().isIn(ModTags.Blocks.ENCHANTER_FLOWERS))
	));

	public State stage = State.IDLE;
	public int stageTicks = 0;

	public int stage3EndTicks = 0;

	private int manaRequired = -1;
	private int mana = 0;

	public ItemStack itemToEnchant = ItemStack.EMPTY;
	private final List<EnchantmentLevelEntry> enchants = new ArrayList<>();

	private static final Map<Direction.Axis, BlockPos[]> PYLON_LOCATIONS = new EnumMap<>(Direction.Axis.class);

	static {
		PYLON_LOCATIONS.put(Direction.Axis.X, new BlockPos[] { new BlockPos(-5, 1, 0), new BlockPos(5, 1, 0), new BlockPos(-4, 1, 3), new BlockPos(4, 1, 3), new BlockPos(-4, 1, -3), new BlockPos(4, 1, -3) });
		PYLON_LOCATIONS.put(Direction.Axis.Z, new BlockPos[] { new BlockPos(0, 1, -5), new BlockPos(0, 1, 5), new BlockPos(3, 1, -4), new BlockPos(3, 1, 4), new BlockPos(-3, 1, -4), new BlockPos(-3, 1, 4) });
	}

	public TileEnchanter() {
		super(ModTiles.ENCHANTER);
	}

	public void onWanded(PlayerEntity player, ItemStack wand) {
		if (stage != State.IDLE || itemToEnchant.isEmpty() || !itemToEnchant.isEnchantable()) {
			return;
		}

		List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3));
		int count = items.size();

		if (count > 0 && !world.isClient) {
			for (ItemEntity entity : items) {
				ItemStack item = entity.getStack();
				if (item.getItem() == Items.ENCHANTED_BOOK) {
					Map<Enchantment, Integer> enchants = EnchantmentHelper.get(item);
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
		if (!world.isClient && stageTicks % 20 == 0) {
			List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3));
			boolean addedEnch = false;

			for (ItemEntity entity : items) {
				ItemStack item = entity.getStack();
				if (item.getItem() == Items.ENCHANTED_BOOK) {
					Map<Enchantment, Integer> enchants = EnchantmentHelper.get(item);
					if (enchants.size() > 0) {
						Map.Entry<Enchantment, Integer> e = enchants.entrySet().iterator().next();
						Enchantment ench = e.getKey();
						int enchantLvl = e.getValue();
						if (!hasEnchantAlready(ench) && isEnchantmentValid(ench)) {
							this.enchants.add(new EnchantmentLevelEntry(ench, enchantLvl));
							world.playSound(null, pos, ModSounds.ding, SoundCategory.BLOCKS, 1F, 1F);
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
			for (EnchantmentLevelEntry data : enchants) {
				manaRequired += (int) (5000F * ((15 - Math.min(15, data.enchantment.getRarity().getWeight()))
						* 1.05F)
						* ((3F + data.level * data.level)
								* 0.25F)
						* (0.9F + enchants.size() * 0.05F)
						* (data.enchantment.isTreasure() ? 1.25F : 1F));
			}
		} else if (mana >= manaRequired) {
			manaRequired = 0;
			for (BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
				BlockEntity te = world.getBlockEntity(pos.add(pylon));
				if (te instanceof TilePylon) {
					((TilePylon) te).activated = false;
				}
			}

			advanceStage();
		} else {
			ISparkEntity spark = getAttachedSpark();
			if (spark != null) {
				SparkHelper.getSparksAround(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, spark.getNetwork())
						.filter(otherSpark -> spark != otherSpark && otherSpark.getAttachedTile() instanceof IManaPool)
						.forEach(os -> os.registerTransfer(spark));
			}
			if (stageTicks % 5 == 0) {
				sync();
			}
		}
	}

	@Override
	public void tick() {
		BlockState state = getCachedState();
		Direction.Axis axis = state.get(BotaniaStateProps.ENCHANTER_DIRECTION);

		for (BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
			BlockEntity tile = world.getBlockEntity(pos.add(pylon));
			if (tile instanceof TilePylon) {
				((TilePylon) tile).activated = stage == State.GATHER_MANA;
				if (stage == State.GATHER_MANA) {
					((TilePylon) tile).centerPos = pos;
				}
			}
		}

		if (stage != State.IDLE) {
			stageTicks++;
		}

		if (world.isClient) {
			return;
		}

		if (FORMED_MULTIBLOCK.get().validate(world, pos.down()) == null) {
			world.setBlockState(pos, Blocks.LAPIS_BLOCK.getDefaultState());
			PacketBotaniaEffect.sendNearby(world, pos, PacketBotaniaEffect.EffectType.ENCHANTER_DESTROY,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			world.playSound(null, pos, ModSounds.enchanterFade, SoundCategory.BLOCKS, 0.5F, 10F);
		}

		switch (stage) {
		case GATHER_ENCHANTS:
			gatherEnchants();
			break;
		case GATHER_MANA:
			gatherMana(axis);
			break;
		case DO_ENCHANT: { // Enchant
			if (stageTicks >= 100) {
				for (EnchantmentLevelEntry data : enchants) {
					if (EnchantmentHelper.getLevel(data.enchantment, itemToEnchant) == 0) {
						itemToEnchant.addEnchantment(data.enchantment, data.level);
					}
				}

				enchants.clear();
				manaRequired = -1;
				mana = 0;

				world.addSyncedBlockEvent(getPos(), ModBlocks.enchanter, CRAFT_EFFECT_EVENT, 0);
				advanceStage();
			}
			break;
		}
		case RESET: { // Reset
			if (stageTicks >= 20) {
				advanceStage();
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
	public boolean onSyncedBlockEvent(int event, int param) {
		switch (event) {
		case CRAFT_EFFECT_EVENT: {
			if (world.isClient) {
				for (int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					world.addParticle(data, getPos().getX() + Math.random() * 0.4 - 0.2, getPos().getY(), getPos().getZ() + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.enchanterEnchant, SoundCategory.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		default:
			return super.onSyncedBlockEvent(event, param);
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
			cmp.put(TAG_ITEM, itemToEnchant.toTag(itemCmp));
		}

		String enchStr = enchants.stream()
				.map(e -> Registry.ENCHANTMENT.getId(e.enchantment) + "=" + e.level)
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
		itemToEnchant = ItemStack.fromTag(itemCmp);

		enchants.clear();
		String enchStr = cmp.getString(TAG_ENCHANTS);
		if (!enchStr.isEmpty()) {
			String[] enchTokens = enchStr.split(",");
			for (String token : enchTokens) {
				try {
					String[] entryTokens = token.split("=");
					int lvl = Integer.parseInt(entryTokens[1]);
					Registry.ENCHANTMENT.getOrEmpty(new Identifier(entryTokens[0])).ifPresent(ench -> {
						enchants.add(new EnchantmentLevelEntry(ench, lvl));
					});
				} catch (InvalidIdentifierException ignored) {}
			}
		}
	}

	private boolean hasEnchantAlready(Enchantment enchant) {
		for (EnchantmentLevelEntry data : enchants) {
			if (data.enchantment == enchant) {
				return true;
			}
		}

		return false;
	}

	private boolean isEnchantmentValid(@Nullable Enchantment ench) {
		if (ench == null || !ench.isAcceptableItem(itemToEnchant)) {
			return false;
		}

		for (EnchantmentLevelEntry data : enchants) {
			Enchantment otherEnch = data.enchantment;
			if (!ench.canCombine(otherEnch)) {
				return false;
			}
		}

		return true;
	}

	@Nullable
	public static Direction.Axis canEnchanterExist(World world, BlockPos pos) {
		BlockRotation rot = MULTIBLOCK.get().validate(world, pos.down());
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
	public void attachSpark(ISparkEntity entity) {}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = world.getEntitiesByClass(Entity.class, new Box(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1), Predicates.instanceOf(ISparkEntity.class));
		if (sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (ISparkEntity) e;
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
	public void renderHUD(MatrixStack ms) {
		if (manaRequired > 0 && !itemToEnchant.isEmpty()) {
			int x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 + 20;
			int y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - 8;

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
