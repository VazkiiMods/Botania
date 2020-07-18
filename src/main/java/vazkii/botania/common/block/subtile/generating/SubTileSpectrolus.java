/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

		if (getWorld().isRemote) {
			return;
		}

		// sheep need to enter the actual block space
		List<Entity> targets = getWorld().getEntitiesWithinAABB(SheepEntity.class, new AxisAlignedBB(getEffectivePos()), Entity::isAlive);

		AxisAlignedBB itemAABB = new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1));
		int slowdown = getSlowdownFactor();
		Predicate<Entity> selector = e -> (e instanceof ItemEntity && e.isAlive() && ((AccessorItemEntity) e).getAge() >= slowdown);
		targets.addAll(getWorld().getEntitiesWithinAABB(Entity.class, itemAABB, selector));

		for (Entity target : targets) {
			if (target instanceof SheepEntity) {
				SheepEntity sheep = (SheepEntity) target;
				if (!sheep.getSheared() && sheep.getFleeceColor() == nextColor) {
					addManaAndCycle(sheep.isChild() ? BABY_SHEEP_GEN : SHEEP_GEN);
					float pitch = sheep.isChild() ? (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.5F : (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F;
					sheep.playSound(SoundEvents.ENTITY_SHEEP_DEATH, 0.9F, pitch);
					sheep.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);

					ItemStack morbid = new ItemStack(sheep.isBurning() ? Items.COOKED_MUTTON : Items.MUTTON);
					((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, morbid), target.getPosX(), target.getPosY() + target.getEyeHeight(), target.getPosZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);

					ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.get(sheep.getFleeceColor()).get());
					((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, wool), target.getPosX(), target.getPosY() + target.getEyeHeight(), target.getPosZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
				}
				sheep.setHealth(0);
			} else if (target instanceof ItemEntity) {
				ItemStack stack = ((ItemEntity) target).getItem();

				if (!stack.isEmpty() && ColorHelper.WOOL_MAP.containsValue(Block.getBlockFromItem(stack.getItem()).delegate)) {
					Block expected = ColorHelper.WOOL_MAP.get(nextColor).get();

					if (expected.asItem() == stack.getItem()) {
						addManaAndCycle(WOOL_GEN);
						((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), target.getPosX(), target.getPosY(), target.getPosZ(), 20, 0.1D, 0.1D, 0.1D, 0.05D);
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
		return MathHelper.hsvToRGB(ticksExisted / 100F, 1F, 1F);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		ItemStack stack = new ItemStack(ColorHelper.WOOL_MAP.get(nextColor).get());
		int color = getColor();

		if (!stack.isEmpty()) {
			ITextComponent stackName = stack.getDisplayName();
			int width = 16 + mc.fontRenderer.func_238414_a_(stackName) / 2;
			int x = mc.getMainWindow().getScaledWidth() / 2 - width;
			int y = mc.getMainWindow().getScaledHeight() / 2 + 30;

			mc.fontRenderer.func_238407_a_(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
		}

		RenderSystem.disableLighting();
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_NEXT_COLOR, nextColor.ordinal());
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = DyeColor.byId(cmp.getInt(TAG_NEXT_COLOR));
	}
}
