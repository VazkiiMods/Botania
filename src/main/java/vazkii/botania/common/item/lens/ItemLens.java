/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemLens extends Item implements ILensControl, ICompositableLens, ITinyPlanetExcempt {
	public static final int PROP_NONE = 0,
			PROP_POWER = 1,
			PROP_ORIENTATION = 1 << 1,
			PROP_TOUCH = 1 << 2,
			PROP_INTERACTION = 1 << 3,
			PROP_DAMAGE = 1 << 4,
			PROP_CONTROL = 1 << 5;

	private static final String TAG_COLOR = "color";
	private static final String TAG_COMPOSITE_LENS = "compositeLens";

	private final Lens lens;
	private final int props;

	public ItemLens(Item.Properties builder, Lens lens, int props) {
		super(builder);
		this.lens = lens;
		this.props = props;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		int storedColor = getStoredColor(stack);
		if (storedColor != -1) {
			TranslationTextComponent colorName = new TranslationTextComponent(storedColor == 16 ? "botania.color.rainbow" : "color.minecraft." + DyeColor.byId(storedColor));
			stacks.add(new TranslationTextComponent("botaniamisc.color", colorName).func_240699_a_(TextFormatting.GRAY));
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		ItemStack compositeLens = getCompositeLens(stack);
		if (compositeLens.isEmpty()) {
			return super.getDisplayName(stack);
		}
		String shortKeyA = stack.getTranslationKey() + ".short";
		String shortKeyB = compositeLens.getTranslationKey() + ".short";
		return new TranslationTextComponent("item.botania.composite_lens", new TranslationTextComponent(shortKeyA), new TranslationTextComponent(shortKeyB));
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		int storedColor = getStoredColor(stack);
		if (storedColor != -1) {
			props.color = getLensColor(stack);
		}

		getLens(stack).apply(stack, props);

		ItemStack compositeLens = getCompositeLens(stack);
		if (!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens) {
			((ILens) compositeLens.getItem()).apply(compositeLens, props);
		}
	}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableEntity entity = (ThrowableEntity) burst;

		dead = getLens(stack).collideBurst(burst, entity, pos, isManaBlock, dead, stack);

		ItemStack compositeLens = getCompositeLens(stack);
		if (!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens) {
			dead = ((ILens) compositeLens.getItem()).collideBurst(burst, pos, isManaBlock, dead, compositeLens);
		}

		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		ThrowableEntity entity = (ThrowableEntity) burst;
		int storedColor = getStoredColor(stack);

		if (storedColor == 16 && entity.world.isRemote) {
			burst.setColor(getLensColor(stack));
		}

		getLens(stack).updateBurst(burst, entity, stack);

		ItemStack compositeLens = getCompositeLens(stack);
		if (!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens) {
			((ILens) compositeLens.getItem()).updateBurst(burst, compositeLens);
		}
	}

	@Override
	public int getLensColor(ItemStack stack) {
		int storedColor = getStoredColor(stack);

		if (storedColor == -1) {
			return 0xFFFFFF;
		}

		if (storedColor == 16) {
			return MathHelper.hsvToRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 1F, 1F);
		}

		return DyeColor.byId(storedColor).getColorValue();
	}

	public static int getStoredColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, -1);
	}

	public static void setLensColor(ItemStack stack, int color) {
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}

	public static boolean isBlacklisted(ItemStack lens1, ItemStack lens2) {
		ICompositableLens item1 = (ICompositableLens) lens1.getItem();
		ICompositableLens item2 = (ICompositableLens) lens2.getItem();
		return (item1.getProps(lens1) & item2.getProps(lens2)) != 0;
	}

	public static Lens getLens(ItemStack stack) {
		if (stack.getItem() instanceof ItemLens) {
			return ((ItemLens) stack.getItem()).lens;
		} else {
			return new Lens();
		}
	}

	@Override
	public boolean canCombineLenses(ItemStack sourceLens, ItemStack compositeLens) {
		ICompositableLens sourceItem = (ICompositableLens) sourceLens.getItem();
		ICompositableLens compositeItem = (ICompositableLens) compositeLens.getItem();
		if (sourceItem == compositeItem) {
			return false;
		}

		if (!sourceItem.isCombinable(sourceLens) || !compositeItem.isCombinable(compositeLens)) {
			return false;
		}

		if (isBlacklisted(sourceLens, compositeLens)) {
			return false;
		}

		return true;
	}

	@Override
	public ItemStack getCompositeLens(ItemStack stack) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_COMPOSITE_LENS, true);
		if (cmp == null) {
			return ItemStack.EMPTY;
		} else {
			return ItemStack.read(cmp);
		}
	}

	@Override
	public ItemStack setCompositeLens(ItemStack sourceLens, ItemStack compositeLens) {
		if (!compositeLens.isEmpty()) {
			CompoundNBT cmp = compositeLens.write(new CompoundNBT());
			ItemNBTHelper.setCompound(sourceLens, TAG_COMPOSITE_LENS, cmp);
		}
		return sourceLens;
	}

	@Override
	public int getManaToTransfer(IManaBurst burst, ThrowableEntity entity, ItemStack stack, IManaReceiver receiver) {
		return getLens(stack).getManaToTransfer(burst, entity, stack, receiver);
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return stack.getItem() != ModItems.lensStorm;
	}

	@Override
	public boolean isControlLens(ItemStack stack) {
		return (getProps(stack) & PROP_CONTROL) != 0;
	}

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		return getLens(stack).allowBurstShooting(stack, spreader, redstone);
	}

	@Override
	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		getLens(stack).onControlledSpreaderTick(stack, spreader, redstone);
	}

	@Override
	public void onControlledSpreaderPulse(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		getLens(stack).onControlledSpreaderPulse(stack, spreader, redstone);
	}

	@Override
	public int getProps(ItemStack stack) {
		return props;
	}

	@Override
	public boolean isCombinable(ItemStack stack) {
		return stack.getItem() != ModItems.lensNormal;
	}

}
