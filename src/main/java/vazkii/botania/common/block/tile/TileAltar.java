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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.mixin.AccessorBucketItem;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.Ref;
import alexiil.mc.lib.attributes.misc.Reference;

public class TileAltar extends TileSimpleInventory implements IPetalApothecary {

	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
	private static final int SET_KEEP_TICKS_EVENT = 0;
	private static final int CRAFT_EFFECT_EVENT = 1;

	public static final String ITEM_TAG_APOTHECARY_SPAWNED = "ApothecarySpawned";

	private List<ItemStack> lastRecipe = null;
	private int recipeKeepTicks = 0;

	public TileAltar(BlockPos pos, BlockState state) {
		super(ModTiles.ALTAR, pos, state);
	}

	public boolean collideEntityItem(ItemEntity item) {
		ItemStack stack = item.getItem();
		if (level.isClientSide || stack.isEmpty() || !item.isAlive()) {
			return false;
		}

		if (getBlockState().getBlock() == ModBlocks.defaultAltar && stack.getItem() == Blocks.VINE.asItem()) {
			CompoundTag tmp = new CompoundTag();
			writePacketNBT(tmp);

			stack.shrink(1);
			level.setBlockAndUpdate(getBlockPos(), ModBlocks.mossyAltar.defaultBlockState());

			BlockEntity newAltar = level.getBlockEntity(getBlockPos());
			if (newAltar instanceof TileAltar) {
				((TileAltar) newAltar).readPacketNBT(tmp);
			}

			return true;
		}

		boolean hasFluidCapability = FluidAttributes.EXTRACTABLE.getAll(stack).getCount() > 0;

		if (getFluid() == State.EMPTY) {
			// XXX: special handling for now since fish buckets don't have fluid cap, may need to be changed later
			// todo fabric: check if LBA gives fish buckets this
			if (stack.getItem() instanceof MobBucketItem && ((AccessorBucketItem) stack.getItem()).getFluid() == Fluids.WATER) {
				setFluid(State.WATER);
				((MobBucketItem) stack.getItem()).checkExtraContent(null, level, stack, getBlockPos().above()); // Spawns the fish
				item.setItem(new ItemStack(Items.BUCKET));
				return true;
			}

			Reference<ItemStack> ref = new Ref<>(stack);
			ItemAttributeList<FluidExtractable> extrs = FluidAttributes.EXTRACTABLE.getAll(ref);

			for (int i = 0; i < extrs.getCount(); i++) {
				FluidExtractable extr = extrs.get(i);

				ExactFluidFilter waterFilt = new ExactFluidFilter(FluidKeys.WATER);
				FluidVolume waterExtracted = extr.attemptExtraction(waterFilt, FluidAmount.BUCKET, Simulation.SIMULATE);
				if (waterExtracted.getAmount_F().equals(FluidAmount.BUCKET)) {
					extr.attemptExtraction(waterFilt, FluidAmount.BUCKET, Simulation.ACTION);
					setFluid(State.WATER);
					item.setItem(ref.get());
					return true;
				}

				ExactFluidFilter lavaFilt = new ExactFluidFilter(FluidKeys.LAVA);
				FluidVolume lavaExtracted = extr.attemptExtraction(lavaFilt, FluidAmount.BUCKET, Simulation.SIMULATE);
				if (lavaExtracted.getAmount_F().equals(FluidAmount.BUCKET)) {
					extr.attemptExtraction(lavaFilt, FluidAmount.BUCKET, Simulation.ACTION);
					setFluid(State.LAVA);
					item.setItem(ref.get());
					return true;
				}

			}

			return false;
		}

		if (getFluid() == State.LAVA) {
			item.setSecondsOnFire(100);
			return true;
		}

		if (SEED_PATTERN.matcher(stack.getDescriptionId()).find()) {
			Optional<IPetalRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.PETAL_TYPE, getItemHandler(), level);
			maybeRecipe.ifPresent(recipe -> {
				saveLastRecipe();
				ItemStack output = recipe.assemble(getItemHandler());

				for (int i = 0; i < inventorySize(); i++) {
					getItemHandler().setItem(i, ItemStack.EMPTY);
				}

				stack.shrink(1);

				ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
				EntityComponents.INTERNAL_ITEM.get(outputItem).apothecarySpawned = true;
				level.addFreshEntity(outputItem);

				setFluid(State.EMPTY);

				level.blockEvent(getBlockPos(), getBlockState().getBlock(), CRAFT_EFFECT_EVENT, 0);
			});
			return maybeRecipe.isPresent();
		} else if (!hasFluidCapability && !EntityComponents.INTERNAL_ITEM.get(item).apothecarySpawned) {
			if (!getItemHandler().getItem(inventorySize() - 1).isEmpty()) {
				return false;
			}

			for (int i = 0; i < inventorySize(); i++) {
				if (getItemHandler().getItem(i).isEmpty()) {
					getItemHandler().setItem(i, stack.split(1));
					level.playSound(null, worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 10F);
					return true;
				}
			}
		}

		return false;
	}

	@Nullable
	private ICustomApothecaryColor getFlowerComponent(ItemStack stack) {
		ICustomApothecaryColor c = null;
		if (stack.getItem() instanceof ICustomApothecaryColor) {
			c = (ICustomApothecaryColor) stack.getItem();
		}
		return c;
	}

	public void saveLastRecipe() {
		lastRecipe = new ArrayList<>();
		for (int i = 0; i < inventorySize(); i++) {
			ItemStack stack = getItemHandler().getItem(i);
			if (stack.isEmpty()) {
				break;
			}
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
		level.blockEvent(getBlockPos(), getBlockState().getBlock(), SET_KEEP_TICKS_EVENT, 400);
	}

	public void trySetLastRecipe(Player player) {
		tryToSetLastRecipe(player, getItemHandler(), lastRecipe);
		if (!isEmpty()) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public static void tryToSetLastRecipe(Player player, Container inv, List<ItemStack> lastRecipe) {
		if (lastRecipe == null || lastRecipe.isEmpty() || player.level.isClientSide) {
			return;
		}

		int index = 0;
		boolean didAny = false;
		for (ItemStack stack : lastRecipe) {
			if (stack.isEmpty()) {
				continue;
			}

			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack pstack = player.getInventory().getItem(i);
				if (player.isCreative() || (!pstack.isEmpty() && pstack.sameItem(stack) && ItemStack.tagMatches(stack, pstack))) {
					inv.setItem(index, player.isCreative() ? stack.copy() : pstack.split(1));
					didAny = true;
					index++;
					break;
				}
			}
		}

		if (didAny) {
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 10F);
			ServerPlayer mp = (ServerPlayer) player;
			mp.inventoryMenu.broadcastChanges();
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

	private void tickRecipeKeep() {
		if (recipeKeepTicks > 0) {
			--recipeKeepTicks;
		} else {
			lastRecipe = null;
		}
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TileAltar self) {
		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.offset(0, 1D / 16D * 20D, 0), worldPosition.offset(1, 1D / 16D * 32D, 1)));

		boolean didChange = false;
		for (ItemEntity item : items) {
			didChange = self.collideEntityItem(item) || didChange;
		}

		if (didChange) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
		}

		self.tickRecipeKeep();
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, TileAltar self) {
		for (int i = 0; i < self.inventorySize(); i++) {
			ItemStack stackAt = self.getItemHandler().getItem(i);
			if (stackAt.isEmpty()) {
				break;
			}

			if (Math.random() >= 0.97) {
				ICustomApothecaryColor comp = self.getFlowerComponent(stackAt);

				int color = comp == null ? 0x888888 : comp.getParticleColor(stackAt);
				float red = (color >> 16 & 0xFF) / 255F;
				float green = (color >> 8 & 0xFF) / 255F;
				float blue = (color & 0xFF) / 255F;
				if (Math.random() >= 0.75F) {
					level.playSound(null, worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 10F);
				}
				SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
				level.addParticle(data, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
			}
		}

		if (self.getFluid() == State.LAVA) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 1, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.05, 0);
			if (Math.random() > 0.9) {
				level.addParticle(ParticleTypes.LAVA, worldPosition.getX() + 0.5 + Math.random() * 0.4 - 0.2, worldPosition.getY() + 1, worldPosition.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.01, 0);
			}
		}

		self.tickRecipeKeep();
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		switch (id) {
		case SET_KEEP_TICKS_EVENT:
			recipeKeepTicks = param;
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
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ModSounds.altarCraft, SoundSource.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		default:
			return super.triggerEvent(id, param);
		}
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
	public void setFluid(State fluid) {
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockAltar.FLUID, fluid));
	}

	@Override
	public State getFluid() {
		return getBlockState().getValue(BlockAltar.FLUID);
	}

	@Environment(EnvType.CLIENT)
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

			Optional<IPetalRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.PETAL_TYPE, getItemHandler(), level);
			maybeRecipe.ifPresent(recipe -> {
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.setShaderTexture(0, HUDHandler.manaBar);
				RenderHelper.drawTexturedModalRect(ms, xc + radius + 9, yc - 8, 0, 8, 22, 15);

				ItemStack stack = recipe.assemble(getItemHandler());

				mc.getItemRenderer().renderGuiItem(stack, xc + radius + 32, yc - 8);
				mc.getItemRenderer().renderGuiItem(new ItemStack(Items.WHEAT_SEEDS), xc + radius + 16, yc + 6);
				mc.font.draw(ms, "+", xc + radius + 14, yc + 10, 0xFFFFFF);
			});

			for (int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				PoseStack pose = RenderSystem.getModelViewStack();
				pose.pushPose();
				pose.translate(xPos, yPos, 0);
				RenderSystem.applyModelViewMatrix();
				mc.getItemRenderer().renderGuiItem(getItemHandler().getItem(i), 0, 0);
				pose.popPose();
				RenderSystem.applyModelViewMatrix();

				angle += anglePer;
			}
		} else if (recipeKeepTicks > 0 && getFluid() == State.WATER) {
			String s = I18n.get("botaniamisc.altarRefill0");
			mc.font.draw(ms, s, xc - mc.font.width(s) / 2, yc + 10, 0xFFFFFF);
			s = I18n.get("botaniamisc.altarRefill1");
			mc.font.draw(ms, s, xc - mc.font.width(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

}
