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

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
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

public class ItemBlockSpecialFlower extends ItemBlockMod implements IRecipeKeyProvider {

	public ItemBlockSpecialFlower(Block block1) {
		super(block1);
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return BotaniaAPI.getSignatureForName(getType(stack)).getIconForStack(stack);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		if(placed) {
			String type = getType(stack);
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileSpecialFlower) {
				TileSpecialFlower tile = (TileSpecialFlower) te;
				tile.setSubTile(type);
				tile.onBlockAdded(world, x, y, z);
				tile.onBlockPlacedBy(world, x, y, z, player, stack);
				if(!world.isRemote)
					world.markBlockForUpdate(x, y, z);
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
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String type = getType(par1ItemStack);
		SubTileSignature sig = BotaniaAPI.getSignatureForName(type);

		sig.addTooltip(par1ItemStack, par2EntityPlayer, par3List);

		if(ConfigHandler.referencesEnabled) {
			String refUnlocalized = sig.getUnlocalizedLoreTextForStack(par1ItemStack);
			String refLocalized = StatCollector.translateToLocal(refUnlocalized);
			if(!refLocalized.equals(refUnlocalized))
				par3List.add(EnumChatFormatting.ITALIC + refLocalized);
		}

		String mod = BotaniaAPI.subTileMods.get(type);
		if(!mod.equals(LibMisc.MOD_ID))
			par3List.add(EnumChatFormatting.ITALIC + "[" + mod + "]");
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
		if(type.equals(LibBlockNames.SUBTILE_DAYBLOOM))
			return ModAchievements.daybloomPickup;
		else if(type.equals(LibBlockNames.SUBTILE_ENDOFLAME))
			return ModAchievements.endoflamePickup;
		else if(type.equals(LibBlockNames.SUBTILE_KEKIMURUS))
			return ModAchievements.kekimurusPickup;
		else if(type.equals(LibBlockNames.SUBTILE_HEISEI_DREAM))
			return ModAchievements.heiseiDreamPickup;
		else if(type.equals(LibBlockNames.SUBTILE_POLLIDISIAC))
			return ModAchievements.pollidisiacPickup;
		else if(type.equals(LibBlockNames.SUBTILE_BUBBELL))
			return ModAchievements.bubbellPickup;
		else if(type.equals(LibBlockNames.SUBTILE_DANDELIFEON))
			return ModAchievements.dandelifeonPickup;
		else if(type.equals(""))
			return ModAchievements.nullFlower;
		return null;
	}

}

