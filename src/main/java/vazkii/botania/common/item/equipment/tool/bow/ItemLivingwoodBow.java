/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 21, 2015, 4:58:45 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLivingwoodBow extends ItemBow implements IManaUsingItem {

	public static final int MANA_PER_DAMAGE = 40;
	IIcon[] pullIcons = new IIcon[3];

	public ItemLivingwoodBow() {
		this(LibItemNames.LIVINGWOOD_BOW);
	}

	public ItemLivingwoodBow(String name) {
		super();
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setUnlocalizedName(name);
		setMaxDamage(500);
		setFull3D();
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		ArrowNockEvent event = new ArrowNockEvent(p_77659_3_, p_77659_1_);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled())
			return event.result;

		if(canFire(p_77659_1_, p_77659_2_, p_77659_3_, 0))
			p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));

		return p_77659_1_;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
		int j = (int) ((getMaxItemUseDuration(p_77615_1_) - p_77615_4_) * chargeVelocityMultiplier());

		ArrowLooseEvent event = new ArrowLooseEvent(p_77615_3_, p_77615_1_, j);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled())
			return;
		j = event.charge;

		boolean flag = canFire(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_);
		boolean infinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_) > 0;

		if(flag) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if(f < 0.1D)
				return;

			if(f > 1.0F)
				f = 1.0F;

			EntityArrow entityarrow = makeArrow(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_, f);

			if(f == 1.0F)
				entityarrow.setIsCritical(true);

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, p_77615_1_);

			if(k > 0)
				entityarrow.setDamage(entityarrow.getDamage() + k * 0.5D + 0.5D);

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, p_77615_1_);

			if(l > 0)
				entityarrow.setKnockbackStrength(l);

			if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, p_77615_1_) > 0)
				entityarrow.setFire(100);

			ToolCommons.damageItem(p_77615_1_, 1, p_77615_3_, MANA_PER_DAMAGE);
			p_77615_2_.playSoundAtEntity(p_77615_3_, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			onFire(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_, infinity, entityarrow);

			if(!p_77615_2_.isRemote)
				p_77615_2_.spawnEntityInWorld(entityarrow);
		}
	}

	float chargeVelocityMultiplier() {
		return 1F;
	}

	boolean postsEvent() {
		return true;
	}

	EntityArrow makeArrow(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_, float f) {
		return new EntityArrow(p_77615_2_, p_77615_3_, f * 2.0F);
	}

	boolean canFire(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
		return p_77615_3_.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_) > 0 || p_77615_3_.inventory.hasItem(Items.arrow);
	}

	void onFire(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_, boolean infinity, EntityArrow arrow) {
		if(infinity)
			arrow.canBePickedUp = 2;
		else p_77615_3_.inventory.consumeInventoryItem(Items.arrow);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this, 0);
		for(int i = 0; i < 3; i++)
			pullIcons[i] = IconHelper.forItem(par1IconRegister, this, i + 1);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 3 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if(stack != usingItem)
			return itemIcon;

		int j = (int) ((getMaxItemUseDuration(stack) - useRemaining) * chargeVelocityMultiplier());

		if(j >= 18)
			return pullIcons[2];
		if(j > 13)
			return pullIcons[1];
		if(j > 0)
			return pullIcons[0];

		return itemIcon;
	}

}
