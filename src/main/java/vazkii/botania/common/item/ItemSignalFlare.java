/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 6:49:15 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemSignalFlare extends ItemMod implements IColorable {

	private static final String TAG_COLOR = "color";

	public ItemSignalFlare() {
		super(LibItemNames.SIGNAL_FLARE);
		setMaxStackSize(1);
		setNoRepair();
		setMaxDamage(200);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand) {
		if(par1ItemStack.getItemDamage() == 0) {
			if(par2World.isRemote)
				par3EntityPlayer.swingArm(hand);
			else {
				EntitySignalFlare flare = new EntitySignalFlare(par2World);
				flare.setPosition(par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ);
				flare.setColor(getColor(par1ItemStack));
				par2World.playSound(null, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, SoundEvents.entity_generic_explode, SoundCategory.PLAYERS, 40F, (1.0F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.2F) * 0.7F);

				par2World.spawnEntityInWorld(flare);

				int stunned = 0;
				int range = 5;
				List<EntityLivingBase> entities = par2World.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(par3EntityPlayer.posX - range, par3EntityPlayer.posY - range, par3EntityPlayer.posZ - range, par3EntityPlayer.posX + range, par3EntityPlayer.posY + range, par3EntityPlayer.posZ + range));
				for(EntityLivingBase entity : entities)
					if(entity != par3EntityPlayer && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
						entity.addPotionEffect(new PotionEffect(MobEffects.moveSlowdown, 50, 5));
						stunned++;
					}

				if(stunned >= 100)
					par3EntityPlayer.addStat(ModAchievements.signalFlareStun, 1);
			}
			par1ItemStack.damageItem(200, par3EntityPlayer);
			return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		int colorv = getColor(par1ItemStack);
		if(colorv >= EnumDyeColor.values().length || colorv < 0)
			return 0xFFFFFF;

		return EnumDyeColor.byMetadata(getColor(par1ItemStack)).getMapColor().colorValue;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(forColor(i));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		int storedColor = getColor(par1ItemStack);
		par3List.add(String.format(I18n.translateToLocal("botaniamisc.flareColor"), I18n.translateToLocal("botania.color" + storedColor)));
	}

	public static ItemStack forColor(int color) {
		ItemStack stack = new ItemStack(ModItems.signalFlare);
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);

		return stack;
	}

	public static int getColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, 0xFFFFFF);
	}
}