/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;
import java.util.function.Predicate;

public class SubTileSpectrolus extends TileEntityGeneratingFlower {
	public static final String TAG_NEXT_COLOR = "nextColor";
	private static final int WOOL_GEN = 1200;
	private static final int SHEEP_GEN = 5000;
	private static final int BABY_SHEEP_GEN = 1; // you are a monster

	private static final int RANGE = 1;

	private DyeColor nextColor = DyeColor.WHITE;

	public SubTileSpectrolus() {
		super(ModSubtiles.SPECTROLUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			return;
		}

		// sheep need to enter the actual block space
		List<Entity> targets = getWorld().getEntities(SheepEntity.class, new Box(getEffectivePos()), Entity::isAlive);

		Box itemAABB = new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1));
		int slowdown = getSlowdownFactor();
		Predicate<Entity> selector = e -> (e instanceof ItemEntity && e.isAlive() && ((AccessorItemEntity) e).getAge() >= slowdown);
		targets.addAll(getWorld().getEntities(Entity.class, itemAABB, selector));

		for (Entity target : targets) {
			if (target instanceof SheepEntity) {
				SheepEntity sheep = (SheepEntity) target;
				if (!sheep.isSheared() && sheep.getColor() == nextColor) {
					addManaAndCycle(sheep.isBaby() ? BABY_SHEEP_GEN : SHEEP_GEN);
					float pitch = sheep.isBaby() ? (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.5F : (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F;
					sheep.playSound(SoundEvents.ENTITY_SHEEP_DEATH, 0.9F, pitch);
					sheep.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);

					ItemStack morbid = new ItemStack(sheep.isOnFire() ? Items.COOKED_MUTTON : Items.MUTTON);
					((ServerWorld) getWorld()).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, morbid), target.getX(), target.getY() + target.getStandingEyeHeight(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);

					ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(sheep.getColor()));
					((ServerWorld) getWorld()).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, wool), target.getX(), target.getY() + target.getStandingEyeHeight(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
				}
				sheep.setHealth(0);
			} else if (target instanceof ItemEntity) {
				ItemStack stack = ((ItemEntity) target).getStack();

				if (!stack.isEmpty() && ColorHelper.isWool(Block.getBlockFromItem(stack.getItem()))) {
					Block expected = ColorHelper.WOOL_MAP.apply(nextColor);

					if (expected.asItem() == stack.getItem()) {
						addManaAndCycle(WOOL_GEN);
						((ServerWorld) getWorld()).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), target.getX(), target.getY(), target.getZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
					}

					target.remove();
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
		return MathHelper.hsvToRgb(ticksExisted / 100F, 1F, 1F);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc) {
		super.renderHUD(ms, mc);

		ItemStack stack = new ItemStack(ColorHelper.WOOL_MAP.apply(nextColor));
		int color = getColor();

		if (!stack.isEmpty()) {
			Text stackName = stack.getName();
			int width = 16 + mc.textRenderer.getWidth(stackName) / 2;
			int x = mc.getWindow().getScaledWidth() / 2 - width;
			int y = mc.getWindow().getScaledHeight() / 2 + 30;

			mc.textRenderer.drawWithShadow(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderInGuiWithOverrides(stack, x, y);
		}

		RenderSystem.disableLighting();
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
