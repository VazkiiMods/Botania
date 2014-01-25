/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 2:04:15 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockSpecialFlower extends ItemBlock {

	private static String TAG_TYPE = "type";
	
	@SideOnly(Side.CLIENT)
	private static Map<String, Icon> iconsForType = new HashMap();
	
	public ItemBlockSpecialFlower(int par1) {
		super(par1);
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		String type = getType(stack);
		if(!iconsForType.containsKey(type)) {
			try {
				SubTileEntity tile = BotaniaAPI.getSubTileMapping(type).newInstance();
				iconsForType.put(type, tile.getIcon());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return iconsForType.get(type);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return LibBlockNames.SPECIAL_FLOWER_PREFIX + getType(stack);
	}
	
	public static String getType(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_TYPE, "");
	}

	public static ItemStack ofType(String type) {
		ItemStack stack = new ItemStack(ModBlocks.specialFlower);
		ItemNBTHelper.setString(null, TAG_TYPE, type);
		return stack;
	}
	
}
