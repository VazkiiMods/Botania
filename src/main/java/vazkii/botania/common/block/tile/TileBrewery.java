/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public class TileBrewery extends TileSimpleInventory implements IManaReceiver, Tickable {
	private static final String TAG_MANA = "mana";
	private static final int CRAFT_EFFECT_EVENT = 0;

	public IBrewRecipe recipe;
	private int mana = 0;
	private int manaLastTick = 0;
	public int signal = 0;

	public TileBrewery() {
		super(ModTiles.BREWERY);
	}

	public boolean addItem(@Nullable PlayerEntity player, ItemStack stack, @Nullable Hand hand) {
		if (recipe != null || stack.isEmpty() || stack.getItem() instanceof IBrewItem && ((IBrewItem) stack.getItem()).getBrew(stack) != null && ((IBrewItem) stack.getItem()).getBrew(stack) != ModBrews.fallbackBrew || getItemHandler().getStack(0).isEmpty() != stack.getItem() instanceof IBrewContainer) {
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
					if (stack.isEmpty() && player != null) {
						player.setStackInHand(hand, ItemStack.EMPTY);
					}
				}

				break;
			}
		}

		if (did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			findRecipe();
		}

		return true;
	}

	private void findRecipe() {
		Optional<IBrewRecipe> maybeRecipe = world.getRecipeManager().getFirstMatch(ModRecipeTypes.BREW_TYPE, getItemHandler(), world);
		maybeRecipe.ifPresent(recipeBrew -> {
			this.recipe = recipeBrew;
			world.setBlockState(pos, ModBlocks.brewery.getDefaultState().with(Properties.POWERED, true));
		});
	}

	@Override
	public void tick() {
		if (mana > 0 && recipe == null) {
			findRecipe();

			if (recipe == null) {
				mana = 0;
			}
		}

		// Update every tick.
		receiveMana(0);

		if (!world.isClient && recipe == null) {
			List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getStack().isEmpty()) {
					ItemStack stack = item.getStack();
					addItem(null, stack, null);
				}
			}
		}

		if (recipe != null) {
			if (!recipe.matches(getItemHandler(), world)) {
				recipe = null;
				world.setBlockState(pos, ModBlocks.brewery.getDefaultState());
			}

			if (recipe != null) {
				if (mana != manaLastTick) {
					int color = recipe.getBrew().getColor(getItemHandler().getStack(0));
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;
					for (int i = 0; i < 5; i++) {
						WispParticleData data1 = WispParticleData.wisp(0.1F + (float) Math.random() * 0.05F, r, g, b);
						world.addParticle(data1, pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						for (int j = 0; j < 2; j++) {
							WispParticleData data = WispParticleData.wisp(0.1F + (float) Math.random() * 0.2F, 0.2F, 0.2F, 0.2F);
							world.addParticle(data, pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						}
					}
				}

				if (mana >= getManaCost() && !world.isClient) {
					int mana = getManaCost();
					receiveMana(-mana);

					ItemStack output = recipe.getOutput(getItemHandler().getStack(0));
					ItemEntity outputItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
					world.spawnEntity(outputItem);
					world.addSyncedBlockEvent(getPos(), ModBlocks.brewery, CRAFT_EFFECT_EVENT, recipe.getBrew().getColor(output));

					for (int i = 0; i < inventorySize(); i++) {
						getItemHandler().setStack(i, ItemStack.EMPTY);
					}
				}
			}
		}

		int newSignal = 0;
		if (recipe != null) {
			newSignal++;
		}

		if (newSignal != signal) {
			signal = newSignal;
			world.updateComparators(pos, getCachedState().getBlock());
		}

		manaLastTick = mana;
	}

	@Override
	public boolean onSyncedBlockEvent(int event, int param) {
		if (event == CRAFT_EFFECT_EVENT) {
			if (world.isClient) {
				for (int i = 0; i < 25; i++) {
					float r = (param >> 16 & 0xFF) / 255F;
					float g = (param >> 8 & 0xFF) / 255F;
					float b = (param & 0xFF) / 255F;
					SparkleParticleData data1 = SparkleParticleData.sparkle((float) Math.random() * 2F + 0.5F, r, g, b, 10);
					world.addParticle(data1, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
					for (int j = 0; j < 2; j++) {
						WispParticleData data = WispParticleData.wisp(0.1F + (float) Math.random() * 0.2F, 0.2F, 0.2F, 0.2F);
						world.addParticle(data, pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, 0.05F - (float) Math.random() * 0.1F, 0.05F + (float) Math.random() * 0.03F, 0.05F - (float) Math.random() * 0.1F);
					}
				}
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.potionCreate, SoundCategory.BLOCKS, 1F, 1.5F + (float) Math.random() * 0.25F, false);
			}
			return true;
		} else {
			return super.onSyncedBlockEvent(event, param);
		}
	}

	public int getManaCost() {
		ItemStack stack = getItemHandler().getStack(0);
		if (recipe == null || stack.isEmpty() || !(stack.getItem() instanceof IBrewContainer)) {
			return 0;
		}
		IBrewContainer container = (IBrewContainer) stack.getItem();
		return container.getManaCost(recipe.getBrew(), stack);
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);

		tag.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);

		mana = tag.getInt(TAG_MANA);
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(7) {
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
		return mana >= getManaCost();
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(this.mana + mana, getManaCost());
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}

	public void renderHUD(MatrixStack ms, MinecraftClient mc) {
		int manaToGet = getManaCost();
		if (manaToGet > 0) {
			int x = mc.getWindow().getScaledWidth() / 2 + 20;
			int y = mc.getWindow().getScaledHeight() / 2 - 8;

			if (recipe == null) {
				return;
			}

			RenderHelper.renderProgressPie(ms, x, y, (float) mana / (float) manaToGet, recipe.getOutput(getItemHandler().getStack(0)));
		}
	}

}
