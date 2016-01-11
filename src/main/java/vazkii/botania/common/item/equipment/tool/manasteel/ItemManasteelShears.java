/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 13, 2014, 7:28:35 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.lib.LibObfuscation;

public class ItemManasteelShears extends ItemShears implements IManaUsingItem {

	public static final int MANA_PER_DAMAGE = 30;

	public ItemManasteelShears() {
		this(LibItemNames.MANASTEEL_SHEARS);
	}

	public ItemManasteelShears(String name) {
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setUnlocalizedName(name);
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
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
		if(entity.worldObj.isRemote)
			return false;

		if(entity instanceof IShearable) {
			IShearable target = (IShearable)entity;
			if(target.isShearable(itemstack, entity.worldObj, new BlockPos(entity))) {
				List<ItemStack> drops = target.onSheared(itemstack, entity.worldObj, new BlockPos(entity), EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));

				Random rand = new Random();
				for(ItemStack stack : drops) {
					EntityItem ent = entity.entityDropItem(stack, 1.0F);
					ent.motionY += rand.nextFloat() * 0.05F;
					ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				}

				ToolCommons.damageItem(itemstack, 1, player, MANA_PER_DAMAGE);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		if (player.worldObj.isRemote)
			return false;

		Block block = player.worldObj.getBlockState(pos).getBlock();
		if(block instanceof IShearable) {
			IShearable target = (IShearable)block;
			if(target.isShearable(itemstack, player.worldObj, pos)) {
				List<ItemStack> drops = target.onSheared(itemstack, player.worldObj, pos, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
				Random rand = new Random();

				for(ItemStack stack : drops) {
					float f = 0.7F;
					double d  = rand.nextFloat() * f + (1D - f) * 0.5;
					double d1 = rand.nextFloat() * f + (1D - f) * 0.5;
					double d2 = rand.nextFloat() * f + (1D - f) * 0.5;

					EntityItem entityitem = new EntityItem(player.worldObj, pos.getX() + d, pos.getY() + d1, pos.getZ() + d2, stack);
					ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, entityitem, 10, LibObfuscation.PICKUP_DELAY);
					player.worldObj.spawnEntityInWorld(entityitem);
				}

				ToolCommons.damageItem(itemstack, 1, player, MANA_PER_DAMAGE);
				player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
			}
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 0 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
