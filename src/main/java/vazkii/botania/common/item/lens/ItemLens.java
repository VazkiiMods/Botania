/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 31, 2014, 3:02:58 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ICompositableLens;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ILensControl;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class ItemLens extends ItemMod implements ILensControl, ICompositableLens, ITinyPlanetExcempt {
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

	public ItemLens(String name, Lens lens, int props) {
		super(name);
		setMaxStackSize(1);
		this.lens = lens;
		this.props = props;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, World world, List<String> stacks, ITooltipFlag flags) {
		int storedColor = getStoredColor(par1ItemStack);
		if(storedColor != -1)
			stacks.add(I18n.format("botaniamisc.color", I18n.format("botania.color" + storedColor)));
	}


	private String getItemShortTermName(ItemStack stack) {
		return net.minecraft.util.text.translation.I18n.translateToLocal(stack.getTranslationKey().replaceAll("item.", "item.botania:") + ".short");
	}

	@Nonnull
	@Override
	public String getItemStackDisplayName(@Nonnull ItemStack stack) {
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens.isEmpty())
			return super.getItemStackDisplayName(stack);
		return String.format(net.minecraft.util.text.translation.I18n.translateToLocal("item.botania:compositeLens.name"), getItemShortTermName(stack), getItemShortTermName(compositeLens));
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		int storedColor = getStoredColor(stack);
		if(storedColor != -1)
			props.color = getLensColor(stack);

		getLens(stack).apply(stack, props);

		ItemStack compositeLens = getCompositeLens(stack);
		if(!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).apply(compositeLens, props);
	}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;

		dead = getLens(stack).collideBurst(burst, entity, pos, isManaBlock, dead, stack);

		ItemStack compositeLens = getCompositeLens(stack);
		if(!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens)
			dead = ((ILens) compositeLens.getItem()).collideBurst(burst, pos, isManaBlock, dead, compositeLens);

		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;
		int storedColor = getStoredColor(stack);

		if(storedColor == 16 && entity.world.isRemote)
			burst.setColor(getLensColor(stack));

		getLens(stack).updateBurst(burst, entity, stack);

		ItemStack compositeLens = getCompositeLens(stack);
		if(!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).updateBurst(burst, compositeLens);
	}

	@Override
	public int getLensColor(ItemStack stack) {
		int storedColor = getStoredColor(stack);

		if(storedColor == -1)
			return 0xFFFFFF;

		if(storedColor == 16)
			return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 1F, 1F);

		return EnumDyeColor.byMetadata(storedColor).colorValue;
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
		if(stack.getItem() instanceof ItemLens)
			return ((ItemLens) stack.getItem()).lens;
		else return new Lens();
	}

	@Override
	public boolean canCombineLenses(ItemStack sourceLens, ItemStack compositeLens) {
		ICompositableLens sourceItem = (ICompositableLens) sourceLens.getItem();
		ICompositableLens compositeItem = (ICompositableLens) compositeLens.getItem();
		if(sourceItem == compositeItem && sourceLens.getItemDamage() == compositeLens.getItemDamage())
			return false;

		if(!sourceItem.isCombinable(sourceLens) || !compositeItem.isCombinable(compositeLens))
			return false;

		if(isBlacklisted(sourceLens, compositeLens))
			return false;

		return true;
	}

	@Override
	public ItemStack getCompositeLens(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COMPOSITE_LENS, true);
		if(cmp == null)
			return ItemStack.EMPTY;
		else return new ItemStack(cmp);
	}

	@Override
	public ItemStack setCompositeLens(ItemStack sourceLens, ItemStack compositeLens) {
		if(!compositeLens.isEmpty()) {
			NBTTagCompound cmp = compositeLens.writeToNBT(new NBTTagCompound());
			ItemNBTHelper.setCompound(sourceLens, TAG_COMPOSITE_LENS, cmp);
		}
		return sourceLens;
	}

	@Override
	public int getManaToTransfer(IManaBurst burst, EntityThrowable entity, ItemStack stack, IManaReceiver receiver) {
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
