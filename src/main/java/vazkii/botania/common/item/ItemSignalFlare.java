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

import java.awt.Color;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSignalFlare extends ItemMod {

	IIcon[] icons;

	private static final String TAG_COLOR = "color";

	public ItemSignalFlare() {
		super();
		setMaxStackSize(1);
		setNoRepair();
		setMaxDamage(200);
		setUnlocalizedName(LibItemNames.SIGNAL_FLARE);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(par1ItemStack.getItemDamage() == 0) {
			if(par2World.isRemote)
				par3EntityPlayer.swingItem();
			else {
				EntitySignalFlare flare = new EntitySignalFlare(par2World);
				flare.setPosition(par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ);
				flare.setColor(getColor(par1ItemStack));
				par2World.playSoundAtEntity(par3EntityPlayer, "random.explode", 40F, (1.0F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.2F) * 0.7F);

				par2World.spawnEntityInWorld(flare);

				int stunned = 0;
				int range = 5;
				List<EntityLivingBase> entities = par2World.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(par3EntityPlayer.posX - range, par3EntityPlayer.posY - range, par3EntityPlayer.posZ - range, par3EntityPlayer.posX + range, par3EntityPlayer.posY + range, par3EntityPlayer.posZ + range));
				for(EntityLivingBase entity : entities)
					if(entity != par3EntityPlayer && (!(entity instanceof EntityPlayer) || MinecraftServer.getServer() == null || MinecraftServer.getServer().isPVPEnabled())) {
						entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 50, 5));
						stunned++;
					}

				if(stunned >= 100)
					par3EntityPlayer.addStat(ModAchievements.signalFlareStun, 1);
			}
			par1ItemStack.damageItem(200, par3EntityPlayer);
		}

		return par1ItemStack;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return icons[Math.min(1, pass)];
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		int colorv = getColor(par1ItemStack);
		if(colorv >= EntitySheep.fleeceColorTable.length || colorv < 0)
			return 0xFFFFFF;

		float[] color = EntitySheep.fleeceColorTable[getColor(par1ItemStack)];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(forColor(i));
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		int storedColor = getColor(par1ItemStack);
		par3List.add(String.format(StatCollector.translateToLocal("botaniamisc.flareColor"), StatCollector.translateToLocal("botania.color" + storedColor)));
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