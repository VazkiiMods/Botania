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

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ItemManasteelShears extends ItemShears implements IManaUsingItem, IModelRegister {

	public static final int MANA_PER_DAMAGE = 30;

	public ItemManasteelShears() {
		this(LibItemNames.MANASTEEL_SHEARS);
	}

	public ItemManasteelShears(String name) {
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
		setTranslationKey(name);
	}

	@Nonnull
	@Override
	public String getUnlocalizedNameInefficiently(@Nonnull ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Override
	public boolean itemInteractionForEntity(@Nonnull ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if(entity.world.isRemote)
			return false;

		if(entity instanceof IShearable) {
			IShearable target = (IShearable)entity;
			if(target.isShearable(itemstack, entity.world, new BlockPos(entity))) {
				List<ItemStack> drops = target.onSheared(itemstack, entity.world, new BlockPos(entity), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

				for(ItemStack stack : drops) {
					entity.entityDropItem(stack, 1.0F);
				}

				ToolCommons.damageItem(itemstack, 1, player, MANA_PER_DAMAGE);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean onBlockStartBreak(@Nonnull ItemStack itemstack, @Nonnull BlockPos pos, EntityPlayer player) {
		if (player.world.isRemote)
			return false;

		Block block = player.world.getBlockState(pos).getBlock();
		if(block instanceof IShearable) {
			IShearable target = (IShearable)block;
			if(target.isShearable(itemstack, player.world, pos)) {
				List<ItemStack> drops = target.onSheared(itemstack, player.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
				Random rand = new Random();

				for(ItemStack stack : drops) {
					float f = 0.7F;
					double d  = rand.nextFloat() * f + (1D - f) * 0.5;
					double d1 = rand.nextFloat() * f + (1D - f) * 0.5;
					double d2 = rand.nextFloat() * f + (1D - f) * 0.5;

					EntityItem entityitem = new EntityItem(player.world, pos.getX() + d, pos.getY() + d1, pos.getZ() + d2, stack);
					entityitem.setPickupDelay(10);
					player.world.spawnEntity(entityitem);
				}

				ToolCommons.damageItem(itemstack, 1, player, MANA_PER_DAMAGE);
				player.addStat(StatList.getBlockStats(block), 1);
				player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
				return true;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
