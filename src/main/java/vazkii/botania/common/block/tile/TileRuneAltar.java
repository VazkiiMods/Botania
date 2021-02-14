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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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

public class TileRuneAltar extends TileSimpleInventory implements IManaReceiver, Tickable {
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

	public TileRuneAltar() {
		super(ModTiles.RUNE_ALTAR);
	}

	public boolean addItem(@Nullable PlayerEntity player, ItemStack stack, @Nullable Hand hand) {
		if (cooldown > 0 || stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.lexicon) {
			return false;
		}

		if (stack.getItem() == ModBlocks.livingrock.asItem()) {
			if (!world.isClient) {
				ItemStack toSpawn = player != null && player.abilities.creativeMode ? stack.copy().split(1) : stack.split(1);
				ItemEntity item = new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, toSpawn);
				item.setPickupDelay(40);
				item.setVelocity(Vec3d.ZERO);
				world.spawnEntity(item);
			}

			return true;
		}

		if (manaToGet != 0) {
			return false;
		}

		boolean did = false;

		for (int i = 0; i < inventorySize(); i++) {
			if (getItemHandler().getStack(i).isEmpty()) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.setCount(1);
				getItemHandler().setStack(i, stackToAdd);

				if (player == null || !player.abilities.creativeMode) {
					stack.decrement(1);
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
	public boolean onSyncedBlockEvent(int id, int param) {
		switch (id) {
		case SET_KEEP_TICKS_EVENT:
			recipeKeepTicks = param;
			return true;
		case SET_COOLDOWN_EVENT:
			cooldown = param;
			return true;
		case CRAFT_EFFECT_EVENT: {
			if (world.isClient) {
				for (int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					world.addParticle(data, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.runeAltarCraft, SoundCategory.BLOCKS, 1, 1, false);
			}
			return true;
		}
		default:
			return super.onSyncedBlockEvent(id, param);
		}
	}

	@Override
	public void tick() {

		// Update every tick.
		receiveMana(0);

		if (!world.isClient) {
			if (manaToGet == 0) {
				List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos, pos.add(1, 1, 1)));
				for (ItemEntity item : items) {
					if (item.isAlive() && !item.getStack().isEmpty() && item.getStack().getItem() != ModBlocks.livingrock.asItem()) {
						ItemStack stack = item.getStack();
						addItem(null, stack, null);
					}
				}
			}

			int newSignal = 0;
			if (manaToGet > 0) {
				newSignal++;
				if (mana >= manaToGet) {
					newSignal++;
				}
			}

			if (newSignal != signal) {
				signal = newSignal;
				world.updateComparators(pos, getCachedState().getBlock());
			}

			updateRecipe();
		} else {
			if (manaToGet > 0 && mana >= manaToGet && world.random.nextInt(20) == 0) {
				Vector3 vec = Vector3.fromTileEntityCenter(this);
				Vector3 endVec = vec.add(0, 2.5, 0);
				Botania.proxy.lightningFX(vec, endVec, 2F, 0x00948B, 0x00E4D7);
			}

			if (cooldown > 0) {
				WispParticleData data = WispParticleData.wisp(0.2F, 0.2F, 0.2F, 0.2F, 1);
				world.addParticle(data, pos.getX() + Math.random(), pos.getY() + 0.8, pos.getZ() + Math.random(), 0, - -0.025F, 0);
			}
		}

		if (cooldown > 0) {
			cooldown--;
		}

		if (recipeKeepTicks > 0) {
			--recipeKeepTicks;
		} else {
			lastRecipe = null;
		}
	}

	private void updateRecipe() {
		int manaToGet = this.manaToGet;

		if (currentRecipe != null) {
			this.manaToGet = currentRecipe.getManaUsage();
		} else {
			this.manaToGet = world.getRecipeManager().getFirstMatch(ModRecipeTypes.RUNE_TYPE, getItemHandler(), world)
					.map(IRuneAltarRecipe::getManaUsage)
					.orElse(0);
		}

		if (manaToGet != this.manaToGet) {
			world.playSound(null, pos, ModSounds.runeAltarStart, SoundCategory.BLOCKS, 1, 1);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	private void saveLastRecipe() {
		lastRecipe = new ArrayList<>();
		for (int i = 0; i < inventorySize(); i++) {
			ItemStack stack = getItemHandler().getStack(i);
			if (stack.isEmpty()) {
				break;
			}
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
		world.addSyncedBlockEvent(getPos(), ModBlocks.runeAltar, SET_KEEP_TICKS_EVENT, 400);
	}

	public void trySetLastRecipe(PlayerEntity player) {
		TileAltar.tryToSetLastRecipe(player, getItemHandler(), lastRecipe);
		if (!isEmpty()) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public void onWanded(PlayerEntity player, ItemStack wand) {
		if (world.isClient) {
			return;
		}

		IRuneAltarRecipe recipe = null;

		if (currentRecipe != null) {
			recipe = currentRecipe;
		} else {
			Optional<IRuneAltarRecipe> maybeRecipe = world.getRecipeManager().getFirstMatch(ModRecipeTypes.RUNE_TYPE, getItemHandler(), world);
			if (maybeRecipe.isPresent()) {
				recipe = maybeRecipe.get();
			}
		}

		if (recipe != null && manaToGet > 0 && mana >= manaToGet) {
			List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos, pos.add(1, 1, 1)));
			ItemEntity livingrock = null;
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getStack().isEmpty() && item.getStack().getItem() == ModBlocks.livingrock.asItem()) {
					livingrock = item;
					break;
				}
			}

			if (livingrock != null) {
				int mana = recipe.getManaUsage();
				receiveMana(-mana);
				ItemStack output = recipe.craft(getItemHandler());
				ItemEntity outputItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
				world.spawnEntity(outputItem);
				currentRecipe = null;
				world.addSyncedBlockEvent(getPos(), ModBlocks.runeAltar, SET_COOLDOWN_EVENT, 60);
				world.addSyncedBlockEvent(getPos(), ModBlocks.runeAltar, CRAFT_EFFECT_EVENT, 0);

				saveLastRecipe();
				for (int i = 0; i < inventorySize(); i++) {
					ItemStack stack = getItemHandler().getStack(i);
					if (!stack.isEmpty()) {
						if (stack.getItem() instanceof ItemRune && (player == null || !player.abilities.creativeMode)) {
							ItemEntity outputRune = new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, stack.copy());
							world.spawnEntity(outputRune);
						}

						getItemHandler().setStack(i, ItemStack.EMPTY);
					}
				}

				livingrock.getStack().decrement(1);
			}
		}
	}

	public boolean isEmpty() {
		for (int i = 0; i < inventorySize(); i++) {
			if (!getItemHandler().getStack(i).isEmpty()) {
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
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(16) {
			@Override
			public int getMaxCountPerStack() {
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

	public void renderHUD(MatrixStack ms, MinecraftClient mc) {
		int xc = mc.getWindow().getScaledWidth() / 2;
		int yc = mc.getWindow().getScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;
		for (int i = 0; i < inventorySize(); i++) {
			if (getItemHandler().getStack(i).isEmpty()) {
				break;
			}
			amt++;
		}

		if (amt > 0) {
			float anglePer = 360F / amt;
			world.getRecipeManager().getFirstMatch(ModRecipeTypes.RUNE_TYPE, getItemHandler(), world).ifPresent(recipe -> {
				RenderSystem.enableBlend();
				RenderSystem.enableRescaleNormal();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				float progress = (float) mana / (float) manaToGet;

				mc.getTextureManager().bindTexture(HUDHandler.manaBar);
				RenderSystem.color4f(1F, 1F, 1F, 1F);
				RenderHelper.drawTexturedModalRect(ms, xc + radius + 9, yc - 8, progress == 1F ? 0 : 22, 8, 22, 15);

				if (progress == 1F) {
					mc.getItemRenderer().renderGuiItemIcon(new ItemStack(ModBlocks.livingrock), xc + radius + 16, yc + 8);
					// change to MatrixStack ops when renderItemIntoGUI starts taking MatrixStack
					RenderSystem.translated(0, 0, 100);
					mc.getItemRenderer().renderGuiItemIcon(new ItemStack(ModItems.twigWand), xc + radius + 24, yc + 8);
					RenderSystem.translated(0, 0, -100);
				}

				RenderHelper.renderProgressPie(ms, xc + radius + 32, yc - 8, progress, recipe.craft(getItemHandler()));

				if (progress == 1F) {
					mc.textRenderer.draw(ms, "+", xc + radius + 14, yc + 12, 0xFFFFFF);
				}
			});

			for (int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				// change to MatrixStack ops when renderItemIntoGUI starts taking MatrixStack
				RenderSystem.translated(xPos, yPos, 0);
				mc.getItemRenderer().renderGuiItemIcon(getItemHandler().getStack(i), 0, 0);
				RenderSystem.translated(-xPos, -yPos, 0);

				angle += anglePer;
			}
		} else if (recipeKeepTicks > 0) {
			String s = I18n.translate("botaniamisc.altarRefill0");
			mc.textRenderer.drawWithShadow(ms, s, xc - mc.textRenderer.getWidth(s) / 2, yc + 10, 0xFFFFFF);
			s = I18n.translate("botaniamisc.altarRefill1");
			mc.textRenderer.drawWithShadow(ms, s, xc - mc.textRenderer.getWidth(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

	public int getTargetMana() {
		return manaToGet;
	}

}
