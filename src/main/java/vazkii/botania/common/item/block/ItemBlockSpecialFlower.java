/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 2:04:15 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IRecipeKeyProvider;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class ItemBlockSpecialFlower extends ItemBlockMod implements IRecipeKeyProvider {

	public ItemBlockSpecialFlower(Block block1) {
		super(block1);
		setHasSubtypes(true);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		boolean placed = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if(placed) {
			String type = getType(stack);
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileSpecialFlower) {
				TileSpecialFlower tile = (TileSpecialFlower) te;
				tile.setSubTile(type);
				tile.onBlockAdded(world, pos, newState);
				tile.onBlockPlacedBy(world, pos, newState, player, stack);
				if(!world.isRemote)
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
			}
		}

		return placed;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return BotaniaAPI.getSignatureForName(getType(stack)).getUnlocalizedNameForStack(stack);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return getUnlocalizedNameInefficiently_(par1ItemStack);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		String type = getType(par1ItemStack);
		SubTileSignature sig = BotaniaAPI.getSignatureForName(type);

		sig.addTooltip(par1ItemStack, par2EntityPlayer, par3List);

		if(ConfigHandler.referencesEnabled) {
			String refUnlocalized = sig.getUnlocalizedLoreTextForStack(par1ItemStack);
			String refLocalized = I18n.translateToLocal(refUnlocalized);
			if(!refLocalized.equals(refUnlocalized))
				par3List.add(TextFormatting.ITALIC + refLocalized);
		}

		String mod = BotaniaAPI.subTileMods.get(type);
		if(!mod.equals(LibMisc.MOD_ID))
			par3List.add(TextFormatting.ITALIC + "[" + mod + "]");
	}

	public static String getType(ItemStack stack) {
		return ItemNBTHelper.detectNBT(stack) ? ItemNBTHelper.getString(stack, SubTileEntity.TAG_TYPE, "") : "";
	}

	public static ItemStack ofType(String type) {
		return ofType(new ItemStack(ModBlocks.specialFlower), type);
	}

	public static ItemStack ofType(ItemStack stack, String type) {
		ItemNBTHelper.setString(stack, SubTileEntity.TAG_TYPE, type);
		return stack;
	}

	@Override
	public String getKey(ItemStack stack) {
		return "flower." + getType(stack);
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		String type = getType(stack);
		switch (type) {
			case LibBlockNames.SUBTILE_DAYBLOOM:
				return ModAchievements.daybloomPickup;
			case LibBlockNames.SUBTILE_ENDOFLAME:
				return ModAchievements.endoflamePickup;
			case LibBlockNames.SUBTILE_KEKIMURUS:
				return ModAchievements.kekimurusPickup;
			case LibBlockNames.SUBTILE_HEISEI_DREAM:
				return ModAchievements.heiseiDreamPickup;
			case LibBlockNames.SUBTILE_POLLIDISIAC:
				return ModAchievements.pollidisiacPickup;
			case LibBlockNames.SUBTILE_BUBBELL:
				return ModAchievements.bubbellPickup;
			case LibBlockNames.SUBTILE_DANDELIFEON:
				return ModAchievements.dandelifeonPickup;
			case "":
				return ModAchievements.nullFlower;
		}
		return null;
	}

}

