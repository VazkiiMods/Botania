/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 13, 2014, 7:14:54 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public class ItemManasteelShovel extends ItemSpade implements IManaUsingItem, ISortableTool {

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelShovel() {
		this(BotaniaAPI.manasteelToolMaterial, LibItemNames.MANASTEEL_SHOVEL);
	}

	public ItemManasteelShovel(ToolMaterial mat, String name) {
		super(mat);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, name));
		setUnlocalizedName(name);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, MANA_PER_DAMAGE);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
		if (state.getBlockHardness(world, pos) != 0F)
			ToolCommons.damageItem(stack, 1, entity, MANA_PER_DAMAGE);

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, BlockPos pos, EnumHand hand, EnumFacing side, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if(!p_77648_2_.canPlayerEdit(pos, side, p_77648_1_))
			return EnumActionResult.PASS;
		else {
			UseHoeEvent event = new UseHoeEvent(p_77648_2_, p_77648_1_, p_77648_3_, pos);
			if(MinecraftForge.EVENT_BUS.post(event))
				return EnumActionResult.FAIL;

			if(event.getResult() == Result.ALLOW) {
				ToolCommons.damageItem(p_77648_1_, 1, p_77648_2_, MANA_PER_DAMAGE);
				return EnumActionResult.SUCCESS;
			}

			Block block = p_77648_3_.getBlockState(pos).getBlock();

			if(side != EnumFacing.DOWN && p_77648_3_.getBlockState(pos.up()).getBlock().isAir(p_77648_3_.getBlockState(pos.up()), p_77648_3_, pos.up()) && (block == Blocks.grass || block == Blocks.dirt)) {
				Block block1 = Blocks.farmland;
				p_77648_3_.playSound(null, pos, block1.getSoundType().getStepSound(), SoundCategory.BLOCKS, (block1.getSoundType().getVolume() + 1.0F) / 2.0F, block1.getSoundType().getPitch() * 0.8F);

				if (p_77648_3_.isRemote)
					return EnumActionResult.SUCCESS;
				else {
					p_77648_3_.setBlockState(pos, block1.getDefaultState());
					ToolCommons.damageItem(p_77648_1_, 1, p_77648_2_, MANA_PER_DAMAGE);
					return EnumActionResult.SUCCESS;
				}
			}

			return EnumActionResult.PASS;
		}
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

	@Override
	public ToolType getSortingType(ItemStack stack) {
		return ToolType.SHOVEL;
	}

	@Override
	public int getSortingPriority(ItemStack stack) {
		return ToolCommons.getToolPriority(stack);
	}

}
