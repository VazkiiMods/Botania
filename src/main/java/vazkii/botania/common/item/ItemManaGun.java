/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 13, 2014, 4:30:27 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.recipe.ManaGunClipRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunLensRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunRemoveLensRecipe;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemManaGun extends ItemMod implements IManaUsingItem {

	private static final String TAG_LENS = "lens";
	private static final String TAG_CLIP = "clip";
	private static final String TAG_CLIP_POS = "clipPos";

	private static final int CLIP_SLOTS = 6;
	private static final int COOLDOWN = 30;

	IIcon[] icons;

	public ItemManaGun() {
		super();
		setMaxDamage(COOLDOWN);
		setMaxStackSize(1);
		setNoRepair();
		setUnlocalizedName(LibItemNames.MANA_GUN);

		GameRegistry.addRecipe(new ManaGunLensRecipe());
		GameRegistry.addRecipe(new ManaGunRemoveLensRecipe());
		GameRegistry.addRecipe(new ManaGunClipRecipe());
		RecipeSorter.register("botania:manaGunLens", ManaGunLensRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register("botania:manaGunRemoveLens", ManaGunRemoveLensRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register("botania:manaGunClip", ManaGunClipRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int effCd = COOLDOWN;
		PotionEffect effect = par3EntityPlayer.getActivePotionEffect(Potion.digSpeed);
		if(effect != null)
			effCd -= (effect.getAmplifier() + 1) * 8;

		if(par3EntityPlayer.isSneaking() && hasClip(par1ItemStack)) {
			rotatePos(par1ItemStack);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.click", 0.6F, (1.0F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.2F) * 0.7F);
			if(par2World.isRemote)
				par3EntityPlayer.swingItem();
			ItemStack lens = getLens(par1ItemStack);
			ItemsRemainingRenderHandler.set(lens, -2);
			par1ItemStack.setItemDamage(effCd);
		} else if(par1ItemStack.getItemDamage() == 0) {
			EntityManaBurst burst = getBurst(par3EntityPlayer, par1ItemStack, true);
			if(burst != null && ManaItemHandler.requestManaExact(par1ItemStack, par3EntityPlayer, burst.getMana(), true)) {
				if(!par2World.isRemote) {
					par2World.playSoundAtEntity(par3EntityPlayer, "botania:manaBlaster", 0.6F, 1F);
					par3EntityPlayer.addStat(ModAchievements.manaBlasterShoot, 1);
					if(isSugoiKawaiiDesuNe(par1ItemStack))
						par3EntityPlayer.addStat(ModAchievements.desuGun, 1);
					par2World.spawnEntityInWorld(burst);
				} else {
					par3EntityPlayer.swingItem();
					par3EntityPlayer.motionX -= burst.motionX * 0.1;
					par3EntityPlayer.motionY -= burst.motionY * 0.3;
					par3EntityPlayer.motionZ -= burst.motionZ * 0.1;
				}
				par1ItemStack.setItemDamage(effCd);
			} else if(!par2World.isRemote)
				par2World.playSoundAtEntity(par3EntityPlayer, "random.click", 0.6F, (1.0F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.2F) * 0.7F);
		}

		return par1ItemStack;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		int states = 3;
		icons = new IIcon[states * 2];

		for(int i = 0; i < states; i++) {
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
			icons[states + i] = IconHelper.forName(par1IconRegister, "desuGun" + i);
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		boolean desu = isSugoiKawaiiDesuNe(stack);
		int index = pass;
		if(index == 0 && hasClip(stack))
			index = 2;

		return icons[Math.min(2, index) + (desu ? 3 : 0)];
	}

	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	private boolean isSugoiKawaiiDesuNe(ItemStack stack) {
		return stack.getDisplayName().equalsIgnoreCase("desu gun");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		EntityManaBurst burst = getBurst(Minecraft.getMinecraft().thePlayer, par1ItemStack, false);
		Color color = new Color(burst == null ? 0x20FF20 : burst.getColor());

		float mul = (float) (Math.sin((double) ClientTickHandler.ticksInGame / 5) * 0.15F);
		int c = (int) (255 * mul);

		return new Color(Math.max(0, Math.min(255, color.getRed() + c)), Math.max(0, Math.min(255, color.getGreen() + c)), Math.max(0, Math.min(255, color.getBlue() + c))).getRGB();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getLens(stack) != null;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return getLens(itemStack);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
		return false;
	}

	public EntityManaBurst getBurst(EntityPlayer player, ItemStack stack, boolean request) {
		EntityManaBurst burst = new EntityManaBurst(player);

		int maxMana = 120;
		int color = 0x20FF20;
		int ticksBeforeManaLoss = 60;
		float manaLossPerTick = 4F;
		float motionModifier = 5F;
		float gravity = 0F;
		BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

		ItemStack lens = getLens(stack);
		if(lens != null)
			((ILens) lens.getItem()).apply(lens, props);


		burst.setSourceLens(lens);
		if(!request || ManaItemHandler.requestManaExact(stack, player, props.maxMana, false)) {
			burst.setColor(props.color);
			burst.setMana(props.maxMana);
			burst.setStartingMana(props.maxMana);
			burst.setMinManaLoss(props.ticksBeforeManaLoss);
			burst.setManaLossPerTick(props.manaLossPerTick);
			burst.setGravity(props.gravity);
			burst.setMotion(burst.motionX * props.motionModifier, burst.motionY * props.motionModifier, burst.motionZ * props.motionModifier);

			return burst;
		}
		return null;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		boolean clip = hasClip(par1ItemStack);
		if(clip && !GuiScreen.isShiftKeyDown()) {
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), par3List);
			return;
		}

		ItemStack lens = getLens(par1ItemStack);
		if(lens != null) {
			List<String> tooltip = lens.getTooltip(par2EntityPlayer, false);
			if(tooltip.size() > 1)
				par3List.addAll(tooltip.subList(1, tooltip.size()));
		}

		if(clip) {
			int pos = getClipPos(par1ItemStack);
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasClip"), par3List);
			for(int i = 0; i < CLIP_SLOTS; i++) {
				String name = "";
				EnumChatFormatting formatting = i == pos ? EnumChatFormatting.GREEN : EnumChatFormatting.GRAY;
				ItemStack lensAt = getLensAtPos(par1ItemStack, i);
				if(lensAt == null)
					name = StatCollector.translateToLocal("botaniamisc.clipEmpty");
				else name = lensAt.getDisplayName();
				addStringToTooltip(formatting + " - " + name, par3List);
			}
		}
	}

	private void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		ItemStack lens = getLens(par1ItemStack);
		return super.getItemStackDisplayName(par1ItemStack) + (lens == null ? "" : " (" + EnumChatFormatting.GREEN + lens.getDisplayName() + EnumChatFormatting.RESET + ")");
	}

	public static boolean hasClip(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_CLIP, false);
	}

	public static void setClip(ItemStack stack, boolean clip) {
		ItemNBTHelper.setBoolean(stack, TAG_CLIP, clip);
	}

	public static int getClipPos(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_CLIP_POS, 0);
	}

	public static void setClipPos(ItemStack stack, int pos) {
		ItemNBTHelper.setInt(stack, TAG_CLIP_POS, pos);
	}

	public static void rotatePos(ItemStack stack) {
		int currPos = getClipPos(stack);
		boolean acceptEmpty = getLensAtPos(stack, currPos) != null;
		int[] slots = new int[CLIP_SLOTS - 1];

		int index = 0;
		for(int i = currPos + 1; i < CLIP_SLOTS; i++, index++)
			slots[index] = i;
		for(int i = 0; i < currPos; i++, index++)
			slots[index] = i;

		for(int i : slots) {
			ItemStack lensAt = getLensAtPos(stack, i);
			if(acceptEmpty || lensAt != null) {
				setClipPos(stack, i);
				return;
			}
		}
	}

	public static ItemStack getLensAtPos(ItemStack stack, int pos) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_LENS + pos, true);
		if(cmp != null) {
			ItemStack lens = ItemStack.loadItemStackFromNBT(cmp);
			return lens;
		}
		return null;
	}

	public static void setLensAtPos(ItemStack stack, ItemStack lens, int pos) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(lens != null)
			lens.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_LENS + pos, cmp);
	}

	public static void setLens(ItemStack stack, ItemStack lens) {
		if(hasClip(stack))
			setLensAtPos(stack, lens, getClipPos(stack));

		NBTTagCompound cmp = new NBTTagCompound();
		if(lens != null)
			lens.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_LENS, cmp);
	}

	public static ItemStack getLens(ItemStack stack) {
		if(hasClip(stack))
			return getLensAtPos(stack, getClipPos(stack));

		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_LENS, true);
		if(cmp != null) {
			ItemStack lens = ItemStack.loadItemStackFromNBT(cmp);
			return lens;
		}
		return null;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
