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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TileAltar extends TileSimpleInventory implements IPetalApothecary, Tickable {

	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
	private static final int SET_KEEP_TICKS_EVENT = 0;
	private static final int CRAFT_EFFECT_EVENT = 1;

	private static final String ITEM_TAG_APOTHECARY_SPAWNED = "ApothecarySpawned";

	private List<ItemStack> lastRecipe = null;
	private int recipeKeepTicks = 0;

	public TileAltar() {
		super(ModTiles.ALTAR);
	}

	public boolean collideEntityItem(ItemEntity item) {
		ItemStack stack = item.getStack();
		if (world.isClient || stack.isEmpty() || !item.isAlive()) {
			return false;
		}

		if (getCachedState().getBlock() == ModBlocks.defaultAltar && stack.getItem() == Blocks.VINE.asItem()) {
			CompoundTag tmp = new CompoundTag();
			writePacketNBT(tmp);

			stack.decrement(1);
			world.setBlockState(getPos(), ModBlocks.mossyAltar.getDefaultState());

			BlockEntity newAltar = world.getBlockEntity(getPos());
			if (newAltar instanceof TileAltar) {
				((TileAltar) newAltar).readPacketNBT(tmp);
			}

			return true;
		}

		boolean hasFluidCapability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();

		if (getFluid() == State.EMPTY) {
			// XXX: special handling for now since fish buckets don't have fluid cap, may need to be changed later
			if (stack.getItem() instanceof FishBucketItem && ((FishBucketItem) stack.getItem()).getFluid() == Fluids.WATER) {
				setFluid(State.WATER);
				((FishBucketItem) stack.getItem()).onEmptied(world, stack, getPos().up()); // Spawns the fish
				item.setStack(new ItemStack(Items.BUCKET));
				return true;
			}

			if (hasFluidCapability) {
				IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(NullPointerException::new);

				FluidStack drainWater = fluidHandler.drain(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
				FluidStack drainLava = fluidHandler.drain(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);

				if (!drainWater.isEmpty() && drainWater.getFluid() == Fluids.WATER && drainWater.getAmount() == FluidAttributes.BUCKET_VOLUME) {
					setFluid(State.WATER);
					fluidHandler.drain(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
					item.setStack(fluidHandler.getContainer());
					return true;
				} else if (!drainLava.isEmpty() && drainLava.getFluid() == Fluids.LAVA && drainLava.getAmount() == FluidAttributes.BUCKET_VOLUME) {
					setFluid(State.LAVA);
					fluidHandler.drain(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
					item.setStack(fluidHandler.getContainer());
					return true;
				}
			}

			return false;
		}

		if (getFluid() == State.LAVA) {
			item.setOnFireFor(100);
			return true;
		}

		if (SEED_PATTERN.matcher(stack.getTranslationKey()).find()) {
			Optional<IPetalRecipe> maybeRecipe = world.getRecipeManager().getFirstMatch(ModRecipeTypes.PETAL_TYPE, getItemHandler(), world);
			maybeRecipe.ifPresent(recipe -> {
				saveLastRecipe();
				ItemStack output = recipe.craft(getItemHandler());

				for (int i = 0; i < inventorySize(); i++) {
					getItemHandler().setStack(i, ItemStack.EMPTY);
				}

				stack.decrement(1);

				ItemEntity outputItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
				outputItem.addScoreboardTag(ITEM_TAG_APOTHECARY_SPAWNED);
				world.spawnEntity(outputItem);

				setFluid(State.EMPTY);

				world.addSyncedBlockEvent(getPos(), getCachedState().getBlock(), CRAFT_EFFECT_EVENT, 0);
			});
			return maybeRecipe.isPresent();
		} else if (!hasFluidCapability && !item.getScoreboardTags().contains(ITEM_TAG_APOTHECARY_SPAWNED)) {
			if (!getItemHandler().getStack(inventorySize() - 1).isEmpty()) {
				return false;
			}

			for (int i = 0; i < inventorySize(); i++) {
				if (getItemHandler().getStack(i).isEmpty()) {
					getItemHandler().setStack(i, stack.split(1));
					world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.1F, 10F);
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
			ItemStack stack = getItemHandler().getStack(i);
			if (stack.isEmpty()) {
				break;
			}
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
		world.addSyncedBlockEvent(getPos(), getCachedState().getBlock(), SET_KEEP_TICKS_EVENT, 400);
	}

	public void trySetLastRecipe(PlayerEntity player) {
		tryToSetLastRecipe(player, getItemHandler(), lastRecipe);
		if (!isEmpty()) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public static void tryToSetLastRecipe(PlayerEntity player, Inventory inv, List<ItemStack> lastRecipe) {
		if (lastRecipe == null || lastRecipe.isEmpty() || player.world.isClient) {
			return;
		}

		int index = 0;
		boolean didAny = false;
		for (ItemStack stack : lastRecipe) {
			if (stack.isEmpty()) {
				continue;
			}

			for (int i = 0; i < player.inventory.size(); i++) {
				ItemStack pstack = player.inventory.getStack(i);
				if (player.isCreative() || (!pstack.isEmpty() && pstack.isItemEqualIgnoreDamage(stack) && ItemStack.areTagsEqual(stack, pstack))) {
					inv.setStack(index, player.isCreative() ? stack.copy() : pstack.split(1));
					didAny = true;
					index++;
					break;
				}
			}
		}

		if (didAny) {
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.1F, 10F);
			ServerPlayerEntity mp = (ServerPlayerEntity) player;
			mp.playerScreenHandler.sendContentUpdates();
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
	public void tick() {
		if (!world.isClient) {
			List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, new Box(pos.add(0, 1D / 16D * 20D, 0), pos.add(1, 1D / 16D * 32D, 1)));

			boolean didChange = false;
			for (ItemEntity item : items) {
				didChange = collideEntityItem(item) || didChange;
			}

			if (didChange) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		} else {
			for (int i = 0; i < inventorySize(); i++) {
				ItemStack stackAt = getItemHandler().getStack(i);
				if (stackAt.isEmpty()) {
					break;
				}

				if (Math.random() >= 0.97) {
					ICustomApothecaryColor comp = getFlowerComponent(stackAt);

					int color = comp == null ? 0x888888 : comp.getParticleColor(stackAt);
					float red = (color >> 16 & 0xFF) / 255F;
					float green = (color >> 8 & 0xFF) / 255F;
					float blue = (color & 0xFF) / 255F;
					if (Math.random() >= 0.75F) {
						world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.1F, 10F);
					}
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
					world.addParticle(data, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1.2, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0, 0);
				}
			}

			if (getFluid() == State.LAVA) {
				world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.05, 0);
				if (Math.random() > 0.9) {
					world.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.01, 0);
				}
			}
		}

		if (recipeKeepTicks > 0) {
			--recipeKeepTicks;
		} else {
			lastRecipe = null;
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int id, int param) {
		switch (id) {
		case SET_KEEP_TICKS_EVENT:
			recipeKeepTicks = param;
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
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.altarCraft, SoundCategory.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		default:
			return super.onSyncedBlockEvent(id, param);
		}
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
	public void setFluid(State fluid) {
		world.setBlockState(getPos(), getCachedState().with(BlockAltar.FLUID, fluid));
	}

	@Override
	public State getFluid() {
		return getCachedState().get(BlockAltar.FLUID);
	}

	@Environment(EnvType.CLIENT)
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

			Optional<IPetalRecipe> maybeRecipe = world.getRecipeManager().getFirstMatch(ModRecipeTypes.PETAL_TYPE, getItemHandler(), world);
			maybeRecipe.ifPresent(recipe -> {
				RenderSystem.color4f(1F, 1F, 1F, 1F);
				mc.textureManager.bindTexture(HUDHandler.manaBar);
				RenderHelper.drawTexturedModalRect(ms, xc + radius + 9, yc - 8, 0, 8, 22, 15);

				ItemStack stack = recipe.craft(getItemHandler());

				mc.getItemRenderer().renderGuiItemIcon(stack, xc + radius + 32, yc - 8);
				mc.getItemRenderer().renderGuiItemIcon(new ItemStack(Items.WHEAT_SEEDS), xc + radius + 16, yc + 6);
				mc.textRenderer.draw(ms, "+", xc + radius + 14, yc + 10, 0xFFFFFF);
			});

			for (int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				RenderSystem.translated(xPos, yPos, 0);
				mc.getItemRenderer().renderGuiItemIcon(getItemHandler().getStack(i), 0, 0);
				RenderSystem.translated(-xPos, -yPos, 0);

				angle += anglePer;
			}
		} else if (recipeKeepTicks > 0 && getFluid() == State.WATER) {
			String s = I18n.translate("botaniamisc.altarRefill0");
			mc.textRenderer.draw(ms, s, xc - mc.textRenderer.getWidth(s) / 2, yc + 10, 0xFFFFFF);
			s = I18n.translate("botaniamisc.altarRefill1");
			mc.textRenderer.draw(ms, s, xc - mc.textRenderer.getWidth(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

}
