/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemRune;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileRuneAltar extends TileSimpleInventory implements IManaReceiver {
	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_TO_GET = "manaToGet";
	private static final int SET_KEEP_TICKS_EVENT = 0;
	private static final int SET_COOLDOWN_EVENT = 1;
	private static final int CRAFT_EFFECT_EVENT = 2;

	private IRuneAltarRecipe currentRecipe;

	public int manaToGet = 0;
	private int mana = 0;
	private int cooldown = 0;
	public int signal = 0;

	private List<ItemStack> lastRecipe = null;
	private int recipeKeepTicks = 0;

	public TileRuneAltar(BlockPos pos, BlockState state) {
		super(ModTiles.RUNE_ALTAR, pos, state);
	}

	public boolean addItem(@Nullable Player player, ItemStack stack, @Nullable InteractionHand hand) {
		if (cooldown > 0 || stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.lexicon) {
			return false;
		}

		if (stack.getItem() == ModBlocks.livingrock.asItem()) {
			if (!level.isClientSide) {
				ItemStack toSpawn = player != null && player.getAbilities().instabuild ? stack.copy().split(1) : stack.split(1);
				ItemEntity item = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5, toSpawn);
				item.setPickUpDelay(40);
				item.setDeltaMovement(Vec3.ZERO);
				level.addFreshEntity(item);
			}

			return true;
		}

		if (manaToGet != 0) {
			return false;
		}

		boolean did = false;

		for (int i = 0; i < inventorySize(); i++) {
			if (getItemHandler().getItem(i).isEmpty()) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.setCount(1);
				getItemHandler().setItem(i, stackToAdd);

				if (player == null || !player.getAbilities().instabuild) {
					stack.shrink(1);
				}

				break;
			}
		}

		if (did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}

		return true;
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		switch (id) {
		case SET_KEEP_TICKS_EVENT:
			recipeKeepTicks = param;
			return true;
		case SET_COOLDOWN_EVENT:
			cooldown = param;
			return true;
		case CRAFT_EFFECT_EVENT: {
			if (level.isClientSide) {
				for (int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					level.addParticle(data, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 1, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ModSounds.runeAltarCraft, SoundSource.BLOCKS, 1, 1, false);
			}
			return true;
		}
		default:
			return super.triggerEvent(id, param);
		}
	}

	private void tickCooldown() {
		if (cooldown > 0) {
			cooldown--;
		}

		if (recipeKeepTicks > 0) {
			--recipeKeepTicks;
		} else {
			lastRecipe = null;
		}
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TileRuneAltar self) {
		if (self.manaToGet == 0) {
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)));
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getItem().isEmpty() && item.getItem().getItem() != ModBlocks.livingrock.asItem()) {
					ItemStack stack = item.getItem();
					self.addItem(null, stack, null);
				}
			}
		}

		int newSignal = 0;
		if (self.manaToGet > 0) {
			newSignal++;
			if (self.mana >= self.manaToGet) {
				newSignal++;
			}
		}

		if (newSignal != self.signal) {
			self.signal = newSignal;
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}

		self.updateRecipe();
		self.tickCooldown();
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, TileRuneAltar self) {
		if (self.manaToGet > 0 && self.mana >= self.manaToGet && level.random.nextInt(20) == 0) {
			Vector3 vec = Vector3.fromTileEntityCenter(self);
			Vector3 endVec = vec.add(0, 2.5, 0);
			Botania.proxy.lightningFX(vec, endVec, 2F, 0x00948B, 0x00E4D7);
		}

		if (self.cooldown > 0) {
			WispParticleData data = WispParticleData.wisp(0.2F, 0.2F, 0.2F, 0.2F, 1);
			level.addParticle(data, worldPosition.getX() + Math.random(), worldPosition.getY() + 0.8, worldPosition.getZ() + Math.random(), 0, - -0.025F, 0);
		}
		self.tickCooldown();
	}

	private void updateRecipe() {
		int manaToGet = this.manaToGet;

		if (currentRecipe != null) {
			this.manaToGet = currentRecipe.getManaUsage();
		} else {
			this.manaToGet = level.getRecipeManager().getRecipeFor(ModRecipeTypes.RUNE_TYPE, getItemHandler(), level)
					.map(IRuneAltarRecipe::getManaUsage)
					.orElse(0);
		}

		if (manaToGet != this.manaToGet) {
			level.playSound(null, worldPosition, ModSounds.runeAltarStart, SoundSource.BLOCKS, 1, 1);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	private void saveLastRecipe() {
		lastRecipe = new ArrayList<>();
		for (int i = 0; i < inventorySize(); i++) {
			ItemStack stack = getItemHandler().getItem(i);
			if (stack.isEmpty()) {
				break;
			}
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
		level.blockEvent(getBlockPos(), ModBlocks.runeAltar, SET_KEEP_TICKS_EVENT, 400);
	}

	public void trySetLastRecipe(Player player) {
		TileAltar.tryToSetLastRecipe(player, getItemHandler(), lastRecipe);
		if (!isEmpty()) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public void onWanded(Player player, ItemStack wand) {
		if (level.isClientSide) {
			return;
		}

		IRuneAltarRecipe recipe = null;

		if (currentRecipe != null) {
			recipe = currentRecipe;
		} else {
			Optional<IRuneAltarRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.RUNE_TYPE, getItemHandler(), level);
			if (maybeRecipe.isPresent()) {
				recipe = maybeRecipe.get();
			}
		}

		if (recipe != null && manaToGet > 0 && mana >= manaToGet) {
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)));
			ItemEntity livingrock = null;
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getItem().isEmpty() && item.getItem().getItem() == ModBlocks.livingrock.asItem()) {
					livingrock = item;
					break;
				}
			}

			if (livingrock != null) {
				int mana = recipe.getManaUsage();
				receiveMana(-mana);
				ItemStack output = recipe.assemble(getItemHandler());
				ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
				level.addFreshEntity(outputItem);
				currentRecipe = null;
				level.blockEvent(getBlockPos(), ModBlocks.runeAltar, SET_COOLDOWN_EVENT, 60);
				level.blockEvent(getBlockPos(), ModBlocks.runeAltar, CRAFT_EFFECT_EVENT, 0);

				saveLastRecipe();
				for (int i = 0; i < inventorySize(); i++) {
					ItemStack stack = getItemHandler().getItem(i);
					if (!stack.isEmpty()) {
						if (stack.getItem() instanceof ItemRune && (player == null || !player.getAbilities().instabuild)) {
							ItemEntity outputRune = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, stack.copy());
							level.addFreshEntity(outputRune);
						}

						getItemHandler().setItem(i, ItemStack.EMPTY);
					}
				}

				livingrock.getItem().shrink(1);
			}
		}
	}

	public boolean isEmpty() {
		for (int i = 0; i < inventorySize(); i++) {
			if (!getItemHandler().getItem(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);

		tag.putInt(TAG_MANA, mana);
		tag.putInt(TAG_MANA_TO_GET, manaToGet);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);

		mana = tag.getInt(TAG_MANA);
		manaToGet = tag.getInt(TAG_MANA_TO_GET);
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(16) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaToGet;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(this.mana + mana, manaToGet);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}

	public void renderHUD(PoseStack ms, Minecraft mc) {
		int xc = mc.getWindow().getGuiScaledWidth() / 2;
		int yc = mc.getWindow().getGuiScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;
		for (int i = 0; i < inventorySize(); i++) {
			if (getItemHandler().getItem(i).isEmpty()) {
				break;
			}
			amt++;
		}

		if (amt > 0) {
			float anglePer = 360F / amt;
			level.getRecipeManager().getRecipeFor(ModRecipeTypes.RUNE_TYPE, getItemHandler(), level).ifPresent(recipe -> {
				RenderSystem.enableBlend();
				RenderSystem.enableRescaleNormal();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				float progress = (float) mana / (float) manaToGet;

				mc.getTextureManager().bind(HUDHandler.manaBar);
				RenderSystem.color4f(1F, 1F, 1F, 1F);
				RenderHelper.drawTexturedModalRect(ms, xc + radius + 9, yc - 8, progress == 1F ? 0 : 22, 8, 22, 15);

				if (progress == 1F) {
					mc.getItemRenderer().renderGuiItem(new ItemStack(ModBlocks.livingrock), xc + radius + 16, yc + 8);
					// change to MatrixStack ops when renderItemIntoGUI starts taking MatrixStack
					RenderSystem.translated(0, 0, 100);
					mc.getItemRenderer().renderGuiItem(new ItemStack(ModItems.twigWand), xc + radius + 24, yc + 8);
					RenderSystem.translated(0, 0, -100);
				}

				RenderHelper.renderProgressPie(ms, xc + radius + 32, yc - 8, progress, recipe.assemble(getItemHandler()));

				if (progress == 1F) {
					mc.font.draw(ms, "+", xc + radius + 14, yc + 12, 0xFFFFFF);
				}
			});

			for (int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				// change to MatrixStack ops when renderItemIntoGUI starts taking MatrixStack
				RenderSystem.translated(xPos, yPos, 0);
				mc.getItemRenderer().renderGuiItem(getItemHandler().getItem(i), 0, 0);
				RenderSystem.translated(-xPos, -yPos, 0);

				angle += anglePer;
			}
		} else if (recipeKeepTicks > 0) {
			String s = I18n.get("botaniamisc.altarRefill0");
			mc.font.drawShadow(ms, s, xc - mc.font.width(s) / 2, yc + 10, 0xFFFFFF);
			s = I18n.get("botaniamisc.altarRefill1");
			mc.font.drawShadow(ms, s, xc - mc.font.width(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

	public int getTargetMana() {
		return manaToGet;
	}

}
