package vazkii.botania.common.block.decor.stairs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockModStairs extends BlockStairs implements ILexiconable {

	public BlockModStairs(Block source, int meta, String name) {
		super(source, meta);
		setBlockName(name);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		useNeighborBrightness = true;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}
