/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.core.helper.DelayHelper;

import java.util.function.Predicate;

public class SubTileSpectrolus extends TileEntityGeneratingFlower {
	public static final String TAG_NEXT_COLOR = "nextColor";
	private static final int WOOL_GEN = 1200;
	private static final int SHEEP_GEN = 5000;
	private static final int BABY_SHEEP_GEN = 1; // you are a monster

	private static final int RANGE = 1;

	private DyeColor nextColor = DyeColor.WHITE;

	public SubTileSpectrolus(BlockPos pos, BlockState state) {
		super(ModSubtiles.SPECTROLUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		// sheep need to enter the actual block space
		var sheeps = getLevel().getEntitiesOfClass(Sheep.class, new AABB(getEffectivePos()), Entity::isAlive);

		AABB itemAABB = new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1));
		Predicate<ItemEntity> selector = e -> DelayHelper.canInteractWithImmediate(this, e);
		var items = getLevel().getEntitiesOfClass(ItemEntity.class, itemAABB, selector);

		for (Entity target : Iterables.concat(sheeps, items)) {
			if (target instanceof Sheep) {
				Sheep sheep = (Sheep) target;
				if (!sheep.isSheared() && sheep.getColor() == nextColor) {
					addManaAndCycle(sheep.isBaby() ? BABY_SHEEP_GEN : SHEEP_GEN);
					float pitch = sheep.isBaby() ? (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.5F : (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F;
					sheep.playSound(SoundEvents.SHEEP_DEATH, 0.9F, pitch);
					sheep.playSound(SoundEvents.GENERIC_EAT, 1, 1);

					ItemStack morbid = new ItemStack(sheep.isOnFire() ? Items.COOKED_MUTTON : Items.MUTTON);
					((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, morbid), target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);

					ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(sheep.getColor()));
					((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, wool), target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
				}
				sheep.setHealth(0);
			} else if (target instanceof ItemEntity) {
				ItemStack stack = ((ItemEntity) target).getItem();

				if (!stack.isEmpty() && ColorHelper.isWool(Block.byItem(stack.getItem()))) {
					Block expected = ColorHelper.WOOL_MAP.apply(nextColor);

					if (expected.asItem() == stack.getItem()) {
						addManaAndCycle(WOOL_GEN);
						((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), target.getX(), target.getY(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
					}

					target.discard();
				}
			}
		}
	}

	private void addManaAndCycle(int toAdd) {
		addMana(toAdd);
		nextColor = nextColor == DyeColor.BLACK ? DyeColor.WHITE : DyeColor.values()[nextColor.ordinal() + 1];
		sync();
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public int getColor() {
		return Mth.hsvToRgb(ticksExisted * 2 % 360 / 360F, 1F, 1F);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		ItemStack stack = new ItemStack(ColorHelper.WOOL_MAP.apply(nextColor));
		int color = getColor();

		if (!stack.isEmpty()) {
			Component stackName = stack.getHoverName();
			int width = 16 + mc.font.width(stackName) / 2;
			int x = mc.getWindow().getGuiScaledWidth() / 2 - width;
			int y = mc.getWindow().getGuiScaledHeight() / 2 + 30;

			mc.font.drawShadow(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderAndDecorateItem(stack, x, y);
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_NEXT_COLOR, nextColor.ordinal());
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = DyeColor.byId(cmp.getInt(TAG_NEXT_COLOR));
	}
}
