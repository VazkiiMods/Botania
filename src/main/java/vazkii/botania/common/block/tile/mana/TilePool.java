/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TilePool extends TileMod implements IManaPool, IKeyLocked, ISparkAttachable, IThrottledPacket {
	public static final int PARTICLE_COLOR = 0x00C6FF;
	public static final int MAX_MANA = 1000000;
	private static final int MAX_MANA_DILLUTED = 10000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_OUTPUTTING = "outputting";
	private static final String TAG_COLOR = "color";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_CAN_ACCEPT = "canAccept";
	private static final String TAG_CAN_SPARE = "canSpare";
	private static final String TAG_FRAGILE = "fragile";
	private static final String TAG_INPUT_KEY = "inputKey";
	private static final String TAG_OUTPUT_KEY = "outputKey";
	private static final int CRAFT_EFFECT_EVENT = 0;
	private static final int CHARGE_EFFECT_EVENT = 1;

	private boolean outputting = false;

	public DyeColor color = DyeColor.WHITE;
	private int mana;

	public int manaCap = -1;
	private int soundTicks = 0;
	private boolean canAccept = true;
	private boolean canSpare = true;
	public boolean fragile = false;
	boolean isDoingTransfer = false;
	int ticksDoingTransfer = 0;

	private String inputKey = "";
	private final String outputKey = "";

	private int ticks = 0;
	private boolean sendPacket = false;

	public TilePool(BlockPos pos, BlockState state) {
		super(ModTiles.POOL, pos, state);
	}

	@Override
	public boolean isFull() {
		Block blockBelow = level.getBlockState(worldPosition.below()).getBlock();
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= manaCap;
	}

	@Override
	public void receiveMana(int mana) {
		int old = this.mana;
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, manaCap));
		if (old != this.mana) {
			setChanged();
			markDispatchable();
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		ManaNetworkCallback.removePool(this);
	}

	public static int calculateComparatorLevel(int mana, int max) {
		int val = (int) ((double) mana / (double) max * 15.0);
		if (mana > 0) {
			val = Math.max(val, 1);
		}
		return val;
	}

	public static List<IManaInfusionRecipe> manaInfusionRecipes(Level world) {
		return ModRecipeTypes.getRecipes(world, ModRecipeTypes.MANA_INFUSION_TYPE).values().stream()
				.filter(r -> r instanceof IManaInfusionRecipe)
				.map(r -> (IManaInfusionRecipe) r)
				.collect(Collectors.toList());
	}

	public IManaInfusionRecipe getMatchingRecipe(@Nonnull ItemStack stack, @Nonnull BlockState state) {
		List<IManaInfusionRecipe> matchingNonCatRecipes = new ArrayList<>();
		List<IManaInfusionRecipe> matchingCatRecipes = new ArrayList<>();

		for (IManaInfusionRecipe recipe : manaInfusionRecipes(level)) {
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

		if (stack.getItem() instanceof IManaDissolvable) {
			((IManaDissolvable) stack.getItem()).onDissolveTick(this, stack, item);
		}

		int age = ((AccessorItemEntity) item).getAge();
		if (age > 100 && age < 130) {
			return false;
		}

		IManaInfusionRecipe recipe = getMatchingRecipe(stack, level.getBlockState(worldPosition.below()));

		if (recipe != null) {
			int mana = recipe.getManaToConsume();
			if (getCurrentMana() >= mana) {
				receiveMana(-mana);

				ItemStack output = recipe.getRecipeOutput(stack);
				stack.shrink(1);
				item.setOnGround(false); //Force entity collision update to run every tick if crafting is in progress

				ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
				((AccessorItemEntity) outputItem).setAge(105);
				level.addFreshEntity(outputItem);

				craftingFanciness();
				return true;
			}
		}

		return false;
	}

	private void craftingFanciness() {
		if (soundTicks == 0) {
			level.playSound(null, worldPosition, ModSounds.manaPoolCraft, SoundSource.BLOCKS, 0.4F, 4F);
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
				if (ConfigHandler.COMMON.chargingAnimationEnabled.getValue()) {
					boolean outputting = param == 1;
					Vector3 itemVec = Vector3.fromBlockPos(worldPosition).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
					Vector3 tileVec = Vector3.fromBlockPos(worldPosition).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
					Botania.proxy.lightningFX(outputting ? tileVec : itemVec,
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
		if (manaCap == -1) {
			manaCap = ((BlockPool) getBlockState().getBlock()).variant == BlockPool.Variant.DILUTED ? MAX_MANA_DILLUTED : MAX_MANA;
		}
		if (!ManaNetworkHandler.instance.isPoolIn(this) && !isRemoved()) {
			ManaNetworkCallback.addPool(this);
		}
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, TilePool self) {
		self.initManaCapAndNetwork();
		double particleChance = 1F - (double) self.getCurrentMana() / (double) self.manaCap * 0.1;
		if (Math.random() > particleChance) {
			float red = (PARTICLE_COLOR >> 16 & 0xFF) / 255F;
			float green = (PARTICLE_COLOR >> 8 & 0xFF) / 255F;
			float blue = (PARTICLE_COLOR & 0xFF) / 255F;
			WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, red, green, blue, 2F);
			level.addParticle(data, worldPosition.getX() + 0.3 + Math.random() * 0.5, worldPosition.getY() + 0.6 + Math.random() * 0.25, worldPosition.getZ() + Math.random(), 0, (float) Math.random() / 25F, 0);
		}
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TilePool self) {
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
			if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem) stack.getItem();
				if (self.outputting && mana.canReceiveManaFromPool(stack, self) || !self.outputting && mana.canExportManaToPool(stack, self)) {
					boolean didSomething = false;

					int bellowCount = 0;
					if (self.outputting) {
						for (Direction dir : Direction.Plane.HORIZONTAL) {
							BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
							if (tile instanceof TileBellows && ((TileBellows) tile).getLinkedTile() == self) {
								bellowCount++;
							}
						}
					}
					int transfRate = 1000 * (bellowCount + 1);

					if (self.outputting) {
						if (self.canSpare) {
							if (self.getCurrentMana() > 0 && mana.getMana(stack) < mana.getMaxMana(stack)) {
								didSomething = true;
							}

							int manaVal = Math.min(transfRate, Math.min(self.getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
							mana.addMana(stack, manaVal);
							self.receiveMana(-manaVal);
						}
					} else {
						if (self.canAccept) {
							if (mana.getMana(stack) > 0 && !self.isFull()) {
								didSomething = true;
							}

							int manaVal = Math.min(transfRate, Math.min(self.manaCap - self.getCurrentMana(), mana.getMana(stack)));
							mana.addMana(stack, -manaVal);
							self.receiveMana(manaVal);
						}
					}

					if (didSomething) {
						if (ConfigHandler.COMMON.chargingAnimationEnabled.getValue() && level.random.nextInt(20) == 0) {
							level.blockEvent(worldPosition, state.getBlock(), CHARGE_EFFECT_EVENT, self.outputting ? 1 : 0);
						}
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
		cmp.putInt(TAG_MANA, mana);
		cmp.putBoolean(TAG_OUTPUTTING, outputting);
		cmp.putInt(TAG_COLOR, color.getId());

		cmp.putInt(TAG_MANA_CAP, manaCap);
		cmp.putBoolean(TAG_CAN_ACCEPT, canAccept);
		cmp.putBoolean(TAG_CAN_SPARE, canSpare);
		cmp.putBoolean(TAG_FRAGILE, fragile);

		cmp.putString(TAG_INPUT_KEY, inputKey);
		cmp.putString(TAG_OUTPUT_KEY, outputKey);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		mana = cmp.getInt(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);
		color = DyeColor.byId(cmp.getInt(TAG_COLOR));

		if (cmp.contains(TAG_MANA_CAP)) {
			manaCap = cmp.getInt(TAG_MANA_CAP);
		}
		if (cmp.contains(TAG_CAN_ACCEPT)) {
			canAccept = cmp.getBoolean(TAG_CAN_ACCEPT);
		}
		if (cmp.contains(TAG_CAN_SPARE)) {
			canSpare = cmp.getBoolean(TAG_CAN_SPARE);
		}
		fragile = cmp.getBoolean(TAG_FRAGILE);

		if (cmp.contains(TAG_INPUT_KEY)) {
			inputKey = cmp.getString(TAG_INPUT_KEY);
		}
		if (cmp.contains(TAG_OUTPUT_KEY)) {
			inputKey = cmp.getString(TAG_OUTPUT_KEY);
		}

	}

	public void onWanded(Player player) {
		if (player == null || player.isShiftKeyDown()) {
			outputting = !outputting;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Environment(EnvType.CLIENT)
	public void renderHUD(PoseStack ms, Minecraft mc) {
		ItemStack pool = new ItemStack(getBlockState().getBlock());
		String name = pool.getHoverName().getString();
		int color = 0x4444FF;
		BotaniaAPIClient.instance().drawSimpleManaHUD(ms, color, getCurrentMana(), manaCap, name);

		int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 11;
		int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + 30;

		int u = outputting ? 22 : 0;
		int v = 38;

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		mc.getTextureManager().bind(HUDHandler.manaBar);
		RenderHelper.drawTexturedModalRect(ms, x, y, u, v, 22, 15);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		ItemStack tablet = new ItemStack(ModItems.manaTablet);
		ItemManaTablet.setStackCreative(tablet);

		mc.getItemRenderer().renderAndDecorateItem(tablet, x - 20, y);
		mc.getItemRenderer().renderAndDecorateItem(pool, x + 26, y);

		RenderSystem.disableLighting();
		RenderSystem.disableBlend();
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
	public int getCurrentMana() {
		if (getBlockState().getBlock() instanceof BlockPool) {
			return ((BlockPool) getBlockState().getBlock()).variant == BlockPool.Variant.CREATIVE ? MAX_MANA : mana;
		}
		return 0;
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
	public void attachSpark(ISparkEntity entity) {}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.above(), worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if (sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return false;
	}

	@Override
	public int getAvailableSpaceForMana() {
		int space = Math.max(0, manaCap - getCurrentMana());
		if (space > 0) {
			return space;
		} else if (level.getBlockState(worldPosition.below()).getBlock() == ModBlocks.manaVoid) {
			return manaCap;
		} else {
			return 0;
		}
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public void setColor(DyeColor color) {
		this.color = color;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	@Override
	public void markDispatchable() {
		sendPacket = true;
	}
}
