/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.ManaDissolvable;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.ManaNetworkHandler;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.ManaTabletItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static vazkii.botania.api.state.BotaniaStateProperties.OPTIONAL_DYE_COLOR;

public class ManaPoolBlockEntity extends BotaniaBlockEntity implements ManaPool, KeyLocked, SparkAttachable,
		ThrottledPacket, Wandable {
	public static final int PARTICLE_COLOR = 0x00C6FF;
	public static final int MAX_MANA = 1000000;
	private static final int MAX_MANA_DILLUTED = 10000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_OUTPUTTING = "outputting";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_CAN_ACCEPT = "canAccept";
	private static final String TAG_CAN_SPARE = "canSpare";
	private static final String TAG_INPUT_KEY = "inputKey";
	private static final String TAG_OUTPUT_KEY = "outputKey";
	private static final int CRAFT_EFFECT_EVENT = 0;
	private static final int CHARGE_EFFECT_EVENT = 1;

	private boolean outputting = false;

	private Optional<DyeColor> legacyColor = Optional.empty();
	private int mana;

	private int manaCap = -1;
	private int soundTicks = 0;
	private boolean canAccept = true;
	private boolean canSpare = true;
	boolean isDoingTransfer = false;
	int ticksDoingTransfer = 0;

	private String inputKey = "";
	private final String outputKey = "";

	private int ticks = 0;
	private boolean sendPacket = false;

	public ManaPoolBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.POOL, pos, state);
	}

	@Override
	public boolean isFull() {
		BlockState stateBelow = level.getBlockState(worldPosition.below());
		return !stateBelow.is(BotaniaBlocks.manaVoid) && getCurrentMana() >= getMaxMana();
	}

	@Override
	public void receiveMana(int mana) {
		int old = this.mana;
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, getMaxMana()));
		if (old != this.mana) {
			setChanged();
			markDispatchable();
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		BotaniaAPI.instance().getManaNetworkInstance().fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.REMOVE);
	}

	public static int calculateComparatorLevel(int mana, int max) {
		int val = (int) ((double) mana / (double) max * 15.0);
		if (mana > 0) {
			val = Math.max(val, 1);
		}
		return val;
	}

	public ManaInfusionRecipe getMatchingRecipe(@NotNull ItemStack stack, @NotNull BlockState state) {
		List<ManaInfusionRecipe> matchingNonCatRecipes = new ArrayList<>();
		List<ManaInfusionRecipe> matchingCatRecipes = new ArrayList<>();

		for (var recipe : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.MANA_INFUSION_TYPE).values()) {
			if (recipe.matches(stack)) {
				if (recipe.getRecipeCatalyst() == null) {
					matchingNonCatRecipes.add(recipe);
				} else if (recipe.getRecipeCatalyst().test(state)) {
					matchingCatRecipes.add(recipe);
				}
			}
		}

		// Recipes with matching catalyst take priority above recipes with no catalyst specified
		return !matchingCatRecipes.isEmpty() ? matchingCatRecipes.get(0) : !matchingNonCatRecipes.isEmpty() ? matchingNonCatRecipes.get(0) : null;
	}

	public boolean collideEntityItem(ItemEntity item) {
		if (level.isClientSide || !item.isAlive() || item.getItem().isEmpty()) {
			return false;
		}

		ItemStack stack = item.getItem();

		if (stack.getItem() instanceof ManaDissolvable dissolvable) {
			dissolvable.onDissolveTick(this, item);
		}

		if (XplatAbstractions.INSTANCE.itemFlagsComponent(item).manaInfusionSpawned) {
			return false;
		}

		ManaInfusionRecipe recipe = getMatchingRecipe(stack, level.getBlockState(worldPosition.below()));

		if (recipe != null) {
			int mana = recipe.getManaToConsume();
			if (getCurrentMana() >= mana) {
				receiveMana(-mana);

				ItemStack output = recipe.getRecipeOutput(level.registryAccess(), stack);
				EntityHelper.shrinkItem(item);
				item.setOnGround(false); //Force entity collision update to run every tick if crafting is in progress

				ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
				XplatAbstractions.INSTANCE.itemFlagsComponent(outputItem).manaInfusionSpawned = true;
				level.addFreshEntity(outputItem);

				craftingEffect(true);
				return true;
			}
		}

		return false;
	}

	public void craftingEffect(boolean playSound) {
		if (playSound && soundTicks == 0) {
			level.playSound(null, worldPosition, BotaniaSounds.manaPoolCraft, SoundSource.BLOCKS, 1F, 1F);
			soundTicks = 6;
		}

		level.blockEvent(getBlockPos(), getBlockState().getBlock(), CRAFT_EFFECT_EVENT, 0);
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
						level.addParticle(data, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 0.75, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
					}
				}

				return true;
			}
			case CHARGE_EFFECT_EVENT: {
				if (level.isClientSide) {
					if (BotaniaConfig.common().chargingAnimationEnabled()) {
						boolean outputting = param == 1;
						Vec3 itemVec = Vec3.atLowerCornerOf(worldPosition).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
						Vec3 tileVec = Vec3.atLowerCornerOf(worldPosition).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
						Proxy.INSTANCE.lightningFX(level, outputting ? tileVec : itemVec,
								outputting ? itemVec : tileVec, 80, level.random.nextLong(), 0x4400799c, 0x4400C6FF);
					}
				}
				return true;
			}
			default:
				return super.triggerEvent(event, param);
		}
	}

	private void initManaCapAndNetwork() {
		if (getMaxMana() == -1) {
			manaCap = ((ManaPoolBlock) getBlockState().getBlock()).variant == ManaPoolBlock.Variant.DILUTED ? MAX_MANA_DILLUTED : MAX_MANA;
		}
		if (!ManaNetworkHandler.instance.isPoolIn(level, this) && !isRemoved()) {
			BotaniaAPI.instance().getManaNetworkInstance().fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.ADD);
		}
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, ManaPoolBlockEntity self) {
		self.initManaCapAndNetwork();
		double particleChance = 1F - (double) self.getCurrentMana() / (double) self.getMaxMana() * 0.1;
		if (Math.random() > particleChance) {
			float red = (PARTICLE_COLOR >> 16 & 0xFF) / 255F;
			float green = (PARTICLE_COLOR >> 8 & 0xFF) / 255F;
			float blue = (PARTICLE_COLOR & 0xFF) / 255F;
			WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, red, green, blue, 2F);
			level.addParticle(data, worldPosition.getX() + 0.3 + Math.random() * 0.5, worldPosition.getY() + 0.6 + Math.random() * 0.25, worldPosition.getZ() + Math.random(), 0, (float) Math.random() / 25F, 0);
		}
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, ManaPoolBlockEntity self) {

		// Legacy color format
		if (self.legacyColor.isPresent()) {
			self.setColor(self.legacyColor);
			self.legacyColor = Optional.empty();
		}

		self.initManaCapAndNetwork();
		boolean wasDoingTransfer = self.isDoingTransfer;
		self.isDoingTransfer = false;

		if (self.soundTicks > 0) {
			self.soundTicks--;
		}

		if (self.sendPacket && self.ticks % 10 == 0) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
			self.sendPacket = false;
		}

		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)));
		for (ItemEntity item : items) {
			if (!item.isAlive()) {
				continue;
			}

			ItemStack stack = item.getItem();
			var mana = XplatAbstractions.INSTANCE.findManaItem(stack);
			if (!stack.isEmpty() && mana != null) {
				if (self.outputting && mana.canReceiveManaFromPool(self) || !self.outputting && mana.canExportManaToPool(self)) {
					boolean didSomething = false;

					int bellowCount = 0;
					if (self.outputting) {
						for (Direction dir : Direction.Plane.HORIZONTAL) {
							BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
							if (tile instanceof BellowsBlockEntity bellows && bellows.getLinkedTile() == self) {
								bellowCount++;
							}
						}
					}
					int transfRate = 1000 * (bellowCount + 1);

					if (self.outputting) {
						if (self.canSpare) {
							if (self.getCurrentMana() > 0 && mana.getMana() < mana.getMaxMana()) {
								didSomething = true;
							}

							int manaVal = Math.min(transfRate, Math.min(self.getCurrentMana(), mana.getMaxMana() - mana.getMana()));
							mana.addMana(manaVal);
							self.receiveMana(-manaVal);
						}
					} else {
						if (self.canAccept) {
							if (mana.getMana() > 0 && !self.isFull()) {
								didSomething = true;
							}

							int manaVal = Math.min(transfRate, Math.min(self.getMaxMana() - self.getCurrentMana(), mana.getMana()));
							if (manaVal == 0 && self.level.getBlockState(worldPosition.below()).is(BotaniaBlocks.manaVoid)) {
								manaVal = Math.min(transfRate, mana.getMana());
							}
							mana.addMana(-manaVal);
							self.receiveMana(manaVal);
						}
					}

					if (didSomething) {
						if (BotaniaConfig.common().chargingAnimationEnabled() && level.random.nextInt(20) == 0) {
							level.blockEvent(worldPosition, state.getBlock(), CHARGE_EFFECT_EVENT, self.outputting ? 1 : 0);
						}
						EntityHelper.syncItem(item);
						self.isDoingTransfer = self.outputting;
					}
				}
			}
		}

		if (self.isDoingTransfer) {
			self.ticksDoingTransfer++;
		} else {
			self.ticksDoingTransfer = 0;
			if (wasDoingTransfer) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
			}
		}

		self.ticks++;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, getCurrentMana());
		cmp.putBoolean(TAG_OUTPUTTING, outputting);

		cmp.putInt(TAG_MANA_CAP, getMaxMana());
		cmp.putBoolean(TAG_CAN_ACCEPT, canAccept);
		cmp.putBoolean(TAG_CAN_SPARE, canSpare);

		cmp.putString(TAG_INPUT_KEY, inputKey);
		cmp.putString(TAG_OUTPUT_KEY, outputKey);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		mana = cmp.getInt(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);

		// Legacy color format
		if (cmp.contains("color")) {
			DyeColor color = DyeColor.byId(cmp.getInt("color"));
			// White was previously used as "no color"
			if (color != DyeColor.WHITE) {
				legacyColor = Optional.of(color);
			} else {
				legacyColor = Optional.empty();
			}
		}
		if (cmp.contains(TAG_MANA_CAP)) {
			manaCap = cmp.getInt(TAG_MANA_CAP);
		}
		if (cmp.contains(TAG_CAN_ACCEPT)) {
			canAccept = cmp.getBoolean(TAG_CAN_ACCEPT);
		}
		if (cmp.contains(TAG_CAN_SPARE)) {
			canSpare = cmp.getBoolean(TAG_CAN_SPARE);
		}

		if (cmp.contains(TAG_INPUT_KEY)) {
			inputKey = cmp.getString(TAG_INPUT_KEY);
		}
		if (cmp.contains(TAG_OUTPUT_KEY)) {
			inputKey = cmp.getString(TAG_OUTPUT_KEY);
		}

	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			outputting = !outputting;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}

	public static class WandHud implements WandHUD {
		private final ManaPoolBlockEntity pool;

		public WandHud(ManaPoolBlockEntity pool) {
			this.pool = pool;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			ItemStack poolStack = new ItemStack(pool.getBlockState().getBlock());
			String name = poolStack.getHoverName().getString();

			int centerX = mc.getWindow().getGuiScaledWidth() / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;

			int width = Math.max(102, mc.font.width(name)) + 4;

			RenderHelper.renderHUDBox(gui, centerX - width / 2, centerY + 8, centerX + width / 2, centerY + 48);

			BotaniaAPIClient.instance().drawSimpleManaHUD(gui, 0x0095FF, pool.getCurrentMana(), pool.getMaxMana(), name);

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			int arrowU = pool.outputting ? 22 : 0;
			int arrowV = 38;
			RenderHelper.drawTexturedModalRect(gui, HUDHandler.manaBar, centerX - 11, centerY + 30, arrowU, arrowV, 22, 15);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			ItemStack tablet = new ItemStack(BotaniaItems.manaTablet);
			ManaTabletItem.setStackCreative(tablet);

			gui.renderItem(tablet, centerX - 31, centerY + 30);
			gui.renderItem(poolStack, centerX + 15, centerY + 30);

			RenderSystem.disableBlend();
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

	@Override
	public boolean isOutputtingPower() {
		return outputting;
	}

	@Override
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		if (getBlockState().getBlock() instanceof ManaPoolBlock pool) {
			return pool.variant == ManaPoolBlock.Variant.CREATIVE ? MAX_MANA : mana;
		}
		return 0;
	}

	@Override
	public int getMaxMana() {
		return manaCap;
	}

	@Override
	public String getInputKey() {
		return inputKey;
	}

	@Override
	public String getOutputKey() {
		return outputKey;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public ManaSpark getAttachedSpark() {
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.above(), worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
		if (sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (ManaSpark) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return false;
	}

	@Override
	public int getAvailableSpaceForMana() {
		int space = Math.max(0, getMaxMana() - getCurrentMana());
		if (space > 0) {
			return space;
		} else if (level.getBlockState(worldPosition.below()).is(BotaniaBlocks.manaVoid)) {
			return getMaxMana();
		} else {
			return 0;
		}
	}

	@Override
	public Optional<DyeColor> getColor() {
		return getBlockState().getValue(OPTIONAL_DYE_COLOR).toDyeColor();
	}

	@Override
	public void setColor(Optional<DyeColor> color) {
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(OPTIONAL_DYE_COLOR, BotaniaStateProperties.OptionalDyeColor.fromOptionalDyeColor(color)));
	}

	@Override
	public void markDispatchable() {
		sendPacket = true;
	}
}
