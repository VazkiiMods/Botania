/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.generating;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class SpectrolusBlockEntity extends GeneratingFlowerBlockEntity {
	public static final String TAG_NEXT_COLOR = "nextColor";
	public static final String TAG_COLORS = "colors";
	private static final int WOOL_GEN = 1200;
	private static final int SHEEP_GEN = 5000;
	private static final int BABY_SHEEP_GEN = 1; // you are a monster

	private static final int RANGE = 1;

	private DyeColor nextColor = DyeColor.WHITE;
	@UnknownNullability
	private List<DyeColor> colors;

	public SpectrolusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.SPECTROLUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide()) {
			return;
		}

		// sheep need to enter the actual block space
		for (Sheep sheep : getLevel().getEntitiesOfClass(Sheep.class, new AABB(getEffectivePos()), Entity::isAlive)) {
			if (!sheep.isSheared() && sheep.getColor() == nextColor) {
				addManaAndCycle(sheep.isBaby() ? BABY_SHEEP_GEN : SHEEP_GEN);
				float pitch = sheep.isBaby()
						? (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.5F
						: (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F;
				//Usage of vanilla sound event: this sheep do be dying though. And generic sounds are meant to be reused.
				sheep.playSound(SoundEvents.SHEEP_DEATH, 0.9F, pitch);
				sheep.playSound(SoundEvents.GENERIC_EAT, 1, 1);

				ItemStack morbid = new ItemStack(sheep.isOnFire() ? Items.COOKED_MUTTON : Items.MUTTON);
				((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, morbid),
						sheep.getX(), sheep.getY() + sheep.getEyeHeight(), sheep.getZ(),
						20, 0.1, 0.1, 0.1, 0.05);

				ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(sheep.getColor()));
				((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, wool),
						sheep.getX(), sheep.getY() + sheep.getEyeHeight(), sheep.getZ(),
						20, 0.1, 0.1, 0.1, 0.05);
			}
			sheep.setHealth(0);
		}

		AABB itemAABB = MathHelper.inflateBoxAround(getEffectivePos(), RANGE);
		Predicate<ItemEntity> selector = e -> DelayHelper.canInteractWithImmediate(this, e)
				&& ColorHelper.isWool(e.getItem().getItem());
		for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, itemAABB, selector)) {
			ItemStack stack = item.getItem();
			Block expected = ColorHelper.WOOL_MAP.apply(nextColor);

			if (expected.asItem() == stack.getItem()) {
				addManaAndCycle(WOOL_GEN);
				((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), item.getX(), item.getY(), item.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
			}

			item.discard();
		}
	}

	private void addManaAndCycle(int toAdd) {
		addMana(toAdd);
		int colorIndex = colors.indexOf(nextColor) + 1;
		nextColor = colorIndex >= colors.size() ? colors.getFirst() : colors.get(colorIndex);
		markForImmediateSync();
		markForPersisting();
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public int getColor() {
		return ColorHelper.getColorLegibleOnGrayBackground(nextColor);
	}

	public static class WandHud extends BindableFlowerWandHud<SpectrolusBlockEntity> {
		public WandHud(SpectrolusBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			ItemStack stack = new ItemStack(ColorHelper.WOOL_MAP.apply(flower.nextColor));

			if (stack.isEmpty()) {
				super.renderHUD(gui, window, font, partialTick);
			} else {
				int halfWidth = RenderHelper.itemWithNameWidth(stack, font) / 2;
				int centerY = window.getGuiScaledHeight() / 2;

				super.renderHUD(gui, window, font, halfWidth + 2, halfWidth + 2, 48);
				RenderHelper.renderItemWithNameCentered(gui, window, font, stack, centerY + 30,
						ColorHelper.getColorLegibleOnGrayBackground(flower.nextColor));
			}
		}
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		if (colors == null && level instanceof ServerLevel serverLevel) {
			List<DyeColor> dyeColors = new ArrayList<>(ColorHelper.supportedColors().toList());
			Collections.shuffle(dyeColors, new Random(serverLevel.getSeed()));
			colors = dyeColors;
			nextColor = colors.getFirst();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putByte(TAG_NEXT_COLOR, (byte) nextColor.getId());
		cmp.putByteArray(TAG_COLORS, colors.stream().map(color -> (byte) color.getId()).toList());
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		nextColor = DyeColor.byId(cmp.getInt(TAG_NEXT_COLOR));
		byte[] colorIds = cmp.getByteArray(TAG_COLORS);
		colors = (colorIds.length == 0
				? IntStream.range(0, 16)
				: IntStream.range(0, colorIds.length).map(i -> colorIds[i])).mapToObj(DyeColor::byId).toList();
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		tag.putByte(TAG_NEXT_COLOR, (byte) nextColor.getId());
		return tag;
	}
}
