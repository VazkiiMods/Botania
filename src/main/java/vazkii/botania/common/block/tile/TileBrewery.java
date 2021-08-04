/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

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

import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public class TileBrewery extends TileSimpleInventory implements IManaReceiver, TickableBlockEntity {
	private static final String TAG_MANA = "mana";
	private static final int CRAFT_EFFECT_EVENT = 0;

	public IBrewRecipe recipe;
	private int mana = 0;
	private int manaLastTick = 0;
	public int signal = 0;

	public TileBrewery() {
		super(ModTiles.BREWERY);
	}

	public boolean addItem(@Nullable Player player, ItemStack stack, @Nullable InteractionHand hand) {
		if (recipe != null || stack.isEmpty() || stack.getItem() instanceof IBrewItem && ((IBrewItem) stack.getItem()).getBrew(stack) != null && ((IBrewItem) stack.getItem()).getBrew(stack) != ModBrews.fallbackBrew || getItemHandler().getItem(0).isEmpty() != stack.getItem() instanceof IBrewContainer) {
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
					if (stack.isEmpty() && player != null) {
						player.setItemInHand(hand, ItemStack.EMPTY);
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
		Optional<IBrewRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.BREW_TYPE, getItemHandler(), level);
		maybeRecipe.ifPresent(recipeBrew -> {
			this.recipe = recipeBrew;
			level.setBlockAndUpdate(worldPosition, ModBlocks.brewery.defaultBlockState().setValue(BlockStateProperties.POWERED, true));
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

		if (!level.isClientSide && recipe == null) {
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1));
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getItem().isEmpty()) {
					ItemStack stack = item.getItem();
					addItem(null, stack, null);
				}
			}
		}

		if (recipe != null) {
			if (!recipe.matches(getItemHandler(), level)) {
				recipe = null;
				level.setBlockAndUpdate(worldPosition, ModBlocks.brewery.defaultBlockState());
			}

			if (recipe != null) {
				if (mana != manaLastTick) {
					int color = recipe.getBrew().getColor(getItemHandler().getItem(0));
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;
					for (int i = 0; i < 5; i++) {
						WispParticleData data1 = WispParticleData.wisp(0.1F + (float) Math.random() * 0.05F, r, g, b);
						level.addParticle(data1, worldPosition.getX() + 0.7 - Math.random() * 0.4, worldPosition.getY() + 0.9 - Math.random() * 0.2, worldPosition.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						for (int j = 0; j < 2; j++) {
							WispParticleData data = WispParticleData.wisp(0.1F + (float) Math.random() * 0.2F, 0.2F, 0.2F, 0.2F);
							level.addParticle(data, worldPosition.getX() + 0.7 - Math.random() * 0.4, worldPosition.getY() + 0.9 - Math.random() * 0.2, worldPosition.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						}
					}
				}

				if (mana >= getManaCost() && !level.isClientSide) {
					int mana = getManaCost();
					receiveMana(-mana);

					ItemStack output = recipe.getOutput(getItemHandler().getItem(0));
					ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
					level.addFreshEntity(outputItem);
					level.blockEvent(getBlockPos(), ModBlocks.brewery, CRAFT_EFFECT_EVENT, recipe.getBrew().getColor(output));

					for (int i = 0; i < inventorySize(); i++) {
						getItemHandler().setItem(i, ItemStack.EMPTY);
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
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}

		manaLastTick = mana;
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == CRAFT_EFFECT_EVENT) {
			if (level.isClientSide) {
				for (int i = 0; i < 25; i++) {
					float r = (param >> 16 & 0xFF) / 255F;
					float g = (param >> 8 & 0xFF) / 255F;
					float b = (param & 0xFF) / 255F;
					SparkleParticleData data1 = SparkleParticleData.sparkle((float) Math.random() * 2F + 0.5F, r, g, b, 10);
					level.addParticle(data1, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 1, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
					for (int j = 0; j < 2; j++) {
						WispParticleData data = WispParticleData.wisp(0.1F + (float) Math.random() * 0.2F, 0.2F, 0.2F, 0.2F);
						level.addParticle(data, worldPosition.getX() + 0.7 - Math.random() * 0.4, worldPosition.getY() + 0.9 - Math.random() * 0.2, worldPosition.getZ() + 0.7 - Math.random() * 0.4, 0.05F - (float) Math.random() * 0.1F, 0.05F + (float) Math.random() * 0.03F, 0.05F - (float) Math.random() * 0.1F);
					}
				}
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ModSounds.potionCreate, SoundSource.BLOCKS, 1F, 1.5F + (float) Math.random() * 0.25F, false);
			}
			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	public int getManaCost() {
		ItemStack stack = getItemHandler().getItem(0);
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
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(7) {
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

	public void renderHUD(PoseStack ms, Minecraft mc) {
		int manaToGet = getManaCost();
		if (manaToGet > 0) {
			int x = mc.getWindow().getGuiScaledWidth() / 2 + 20;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 8;

			if (recipe == null) {
				return;
			}

			RenderHelper.renderProgressPie(ms, x, y, (float) mana / (float) manaToGet, recipe.getOutput(getItemHandler().getItem(0)));
		}
	}

}
