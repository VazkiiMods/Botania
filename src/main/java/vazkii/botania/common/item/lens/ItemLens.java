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
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class ItemLens extends ItemMod implements ILensControl, ICompositableLens, ITinyPlanetExcempt {

	public static final int SUBTYPES = 24;

	public static final int NORMAL = 0,
			SPEED = 1,
			POWER = 2,
			TIME = 3,
			EFFICIENCY = 4,
			BOUNCE = 5,
			GRAVITY = 6,
			MINE = 7,
			DAMAGE = 8,
			PHANTOM = 9,
			MAGNET = 10,
			EXPLOSIVE = 11,
			INFLUENCE = 12,
			WEIGHT = 13,
			PAINT = 14,
			FIRE = 15,
			PISTON = 16,
			LIGHT = 17,
			WARP = 18,
			REDIRECT = 19,
			FIREWORK = 20,
			FLARE = 21,
			MESSENGER = 22,
			TRIPWIRE = 23;

	public static final int STORM = 5000;

	private static final int PROP_NONE = 0,
			PROP_POWER = 1,
			PROP_ORIENTATION = 1 << 1,
			PROP_TOUCH = 1 << 2,
			PROP_INTERACTION = 1 << 3,
			PROP_DAMAGE = 1 << 4,
			PROP_CONTROL = 1 << 5;

	private static final int[] props = new int[SUBTYPES];
	private static final Lens[] lenses = new Lens[SUBTYPES];
	private static final Lens fallbackLens = new Lens();
	private static final Lens stormLens = new LensStorm();

	static {
		setProps(NORMAL, PROP_NONE);
		setProps(SPEED, PROP_NONE);
		setProps(POWER, PROP_POWER);
		setProps(TIME, PROP_NONE);
		setProps(EFFICIENCY, PROP_NONE);
		setProps(BOUNCE, PROP_TOUCH);
		setProps(GRAVITY, PROP_ORIENTATION);
		setProps(MINE, PROP_TOUCH | PROP_INTERACTION);
		setProps(DAMAGE, PROP_DAMAGE);
		setProps(PHANTOM, PROP_TOUCH);
		setProps(MAGNET, PROP_ORIENTATION);
		setProps(EXPLOSIVE, PROP_DAMAGE | PROP_TOUCH | PROP_INTERACTION);
		setProps(INFLUENCE, PROP_NONE);
		setProps(WEIGHT, PROP_TOUCH | PROP_INTERACTION);
		setProps(PAINT, PROP_TOUCH | PROP_INTERACTION);
		setProps(FIRE, PROP_DAMAGE | PROP_TOUCH | PROP_INTERACTION);
		setProps(PISTON, PROP_TOUCH | PROP_INTERACTION);
		setProps(LIGHT, PROP_TOUCH | PROP_INTERACTION);
		setProps(WARP, PROP_NONE);
		setProps(REDIRECT, PROP_TOUCH | PROP_INTERACTION);
		setProps(FIREWORK, PROP_TOUCH);
		setProps(FLARE, PROP_CONTROL);
		setProps(MESSENGER, PROP_POWER);
		setProps(TRIPWIRE, PROP_CONTROL);

		setLens(NORMAL, fallbackLens);
		setLens(SPEED, new LensSpeed());
		setLens(POWER, new LensPower());
		setLens(TIME, new LensTime());
		setLens(EFFICIENCY, new LensEfficiency());
		setLens(BOUNCE, new LensBounce());
		setLens(GRAVITY, new LensGravity());
		setLens(MINE, new LensMine());
		setLens(DAMAGE, new LensDamage());
		setLens(PHANTOM, new LensPhantom());
		setLens(MAGNET, new LensMagnet());
		setLens(EXPLOSIVE, new LensExplosive());
		setLens(INFLUENCE, new LensInfluence());
		setLens(WEIGHT, new LensWeight());
		setLens(PAINT, new LensPaint());
		setLens(FIRE, new LensFire());
		setLens(PISTON, new LensPiston());
		setLens(LIGHT, new LensLight());
		setLens(WARP, new LensWarp());
		setLens(REDIRECT, new LensRedirect());
		setLens(FIREWORK, new LensFirework());
		setLens(FLARE, new LensFlare());
		setLens(MESSENGER, new LensMessenger());
		setLens(TRIPWIRE, new LensTripwire());
	}

	private static final String TAG_COLOR = "color";
	private static final String TAG_COMPOSITE_LENS = "compositeLens";

	public ItemLens() {
		super(LibItemNames.LENS);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < SUBTYPES; i++)
				stacks.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return "item." + LibItemNames.LENS_NAMES[Math.min(SUBTYPES - 1, stack.getItemDamage())];
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

		getLens(stack.getItemDamage()).apply(stack, props);

		ItemStack compositeLens = getCompositeLens(stack);
		if(!compositeLens.isEmpty() && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).apply(compositeLens, props);
	}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;

		dead = getLens(stack.getItemDamage()).collideBurst(burst, entity, pos, isManaBlock, dead, stack);

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

		getLens(stack.getItemDamage()).updateBurst(burst, entity, stack);

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

	public static void setProps(int lens, int props_) {
		props[lens] = props_;
	}

	public static void setLens(int index, Lens lens) {
		lenses[index] = lens;
	}

	public static boolean isBlacklisted(ItemStack lens1, ItemStack lens2) {
		ICompositableLens item1 = (ICompositableLens) lens1.getItem();
		ICompositableLens item2 = (ICompositableLens) lens2.getItem();
		return (item1.getProps(lens1) & item2.getProps(lens2)) != 0;
	}

	public static Lens getLens(int index) {
		if(index == STORM)
			return stormLens;

		if(index < 0 || index >= lenses.length)
			return fallbackLens;

		Lens lens = lenses[index];
		return lens == null ? fallbackLens : lens;
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
		return getLens(stack.getItemDamage()).getManaToTransfer(burst, entity, stack, receiver);
	}
	
	@Override
	public boolean shouldPull(ItemStack stack) {
		return stack.getItemDamage() != STORM;
	}

	@Override
	public boolean isControlLens(ItemStack stack) {
		return (getProps(stack) & PROP_CONTROL) != 0;
	}

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		return getLens(stack.getItemDamage()).allowBurstShooting(stack, spreader, redstone);
	}

	@Override
	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		getLens(stack.getItemDamage()).onControlledSpreaderTick(stack, spreader, redstone);
	}

	@Override
	public void onControlledSpreaderPulse(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		getLens(stack.getItemDamage()).onControlledSpreaderPulse(stack, spreader, redstone);
	}

	@Override
	public int getProps(ItemStack stack) {
		return props[stack.getItemDamage()];
	}

	@Override
	public boolean isCombinable(ItemStack stack) {
		return stack.getItemDamage() != NORMAL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemMetas(this, LibItemNames.LENS_NAMES.length, i -> LibItemNames.LENS_NAMES[i]);
	}

}
