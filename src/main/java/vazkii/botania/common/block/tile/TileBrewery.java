/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ObjectHolder;

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
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public class TileBrewery extends TileSimpleInventory implements IManaReceiver, ITickableTileEntity {

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.BREWERY) public static TileEntityType<TileBrewery> TYPE;
	private static final String TAG_MANA = "mana";
	private static final int CRAFT_EFFECT_EVENT = 0;

	public IBrewRecipe recipe;
	private int mana = 0;
	private int manaLastTick = 0;
	public int signal = 0;

	public TileBrewery() {
		super(TYPE);
	}

	public boolean addItem(@Nullable PlayerEntity player, ItemStack stack, @Nullable Hand hand) {
		if (recipe != null || stack.isEmpty() || stack.getItem() instanceof IBrewItem && ((IBrewItem) stack.getItem()).getBrew(stack) != null && ((IBrewItem) stack.getItem()).getBrew(stack) != ModBrews.fallbackBrew || itemHandler.getStackInSlot(0).isEmpty() != stack.getItem() instanceof IBrewContainer) {
			return false;
		}

		boolean did = false;

		for (int i = 0; i < getSizeInventory(); i++) {
			if (itemHandler.getStackInSlot(i).isEmpty()) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.setCount(1);
				itemHandler.setStackInSlot(i, stackToAdd);

				if (player == null || !player.abilities.isCreativeMode) {
					stack.shrink(1);
					if (stack.isEmpty() && player != null) {
						player.setHeldItem(hand, ItemStack.EMPTY);
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
		Optional<IBrewRecipe> maybeRecipe = world.getRecipeManager().getRecipe(ModRecipeTypes.BREW_TYPE, getRecipeWrapper(), world);
		maybeRecipe.ifPresent(recipeBrew -> {
			this.recipe = recipeBrew;
			world.setBlockState(pos, ModBlocks.brewery.getDefaultState().with(BlockStateProperties.POWERED, true));
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

		if (!world.isRemote && recipe == null) {
			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
			for (ItemEntity item : items) {
				if (item.isAlive() && !item.getItem().isEmpty()) {
					ItemStack stack = item.getItem();
					addItem(null, stack, null);
				}
			}
		}

		if (recipe != null) {
			if (!recipe.matches(getRecipeWrapper(), world)) {
				recipe = null;
				world.setBlockState(pos, ModBlocks.brewery.getDefaultState());
			}

			if (recipe != null) {
				if (mana != manaLastTick) {
					int color = recipe.getBrew().getColor(itemHandler.getStackInSlot(0));
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

				if (mana >= getManaCost() && !world.isRemote) {
					int mana = getManaCost();
					receiveMana(-mana);

					ItemStack output = recipe.getOutput(itemHandler.getStackInSlot(0));
					ItemEntity outputItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
					world.addEntity(outputItem);
					world.addBlockEvent(getPos(), ModBlocks.brewery, CRAFT_EFFECT_EVENT, recipe.getBrew().getColor(output));

					for (int i = 0; i < getSizeInventory(); i++) {
						itemHandler.setStackInSlot(i, ItemStack.EMPTY);
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
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}

		manaLastTick = mana;
	}

	@Override
	public boolean receiveClientEvent(int event, int param) {
		if (event == CRAFT_EFFECT_EVENT) {
			if (world.isRemote) {
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
			return super.receiveClientEvent(event, param);
		}
	}

	public int getManaCost() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (recipe == null || stack.isEmpty() || !(stack.getItem() instanceof IBrewContainer)) {
			return 0;
		}
		IBrewContainer container = (IBrewContainer) stack.getItem();
		return container.getManaCost(recipe.getBrew(), stack);
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		super.writePacketNBT(tag);

		tag.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		super.readPacketNBT(tag);

		mana = tag.getInt(TAG_MANA);
	}

	@Override
	public int getSizeInventory() {
		return 7;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
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

	public void renderHUD(Minecraft mc) {
		int manaToGet = getManaCost();
		if (manaToGet > 0) {
			int x = mc.getMainWindow().getScaledWidth() / 2 + 20;
			int y = mc.getMainWindow().getScaledHeight() / 2 - 8;

			if (recipe == null) {
				return;
			}

			RenderHelper.renderProgressPie(x, y, (float) mana / (float) manaToGet, recipe.getOutput(itemHandler.getStackInSlot(0)));
		}
	}

}
