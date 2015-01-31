/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 10, 2014, 11:48:12 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import java.awt.Color;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

public class ItemEnderDagger extends ItemManasteelSword {

	IIcon iconFront, iconOverlay;

	public ItemEnderDagger() {
		super(BotaniaAPI.manasteelToolMaterial, LibItemNames.ENDER_DAGGER);
		setMaxDamage(69); // What you looking at?
		setNoRepair();
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconFront = IconHelper.forItem(par1IconRegister, this, 0);
		iconOverlay = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 0 ? iconFront : iconOverlay;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		return Color.HSBtoRGB(0.75F, 1F, 1.5F - (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 100D) * 0.5 + 1.2F));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		if(par2EntityLivingBase instanceof EntityEnderman && par3EntityLivingBase instanceof EntityPlayer)
			par2EntityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) par3EntityLivingBase), 20);
		par1ItemStack.damageItem(1, par3EntityLivingBase);
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		// NO-OP
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return false;
	}

}
