package vazkii.botania.common.block.decor.slabs;

import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockModSlab;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockModSlab extends BlockSlab implements ILexiconable {

	String name;

	public BlockModSlab(boolean full, Material mat, String name) {
		super(mat);
		this.name = name;
		setUnlocalizedName(name);
		if(!full) {
			setCreativeTab(BotaniaCreativeTab.INSTANCE);
			useNeighborBrightness = true;
		}
	}

	public abstract BlockSlab getFullBlock();

	public abstract BlockSlab getSingleBlock();

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(getSingleBlock());
	}

	@Override
	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(getSingleBlock());
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return super.quantityDropped(state, fortune, random);
	}

	@Override
	public ItemStack createStackedBlock(IBlockState par1) {
		return new ItemStack(getSingleBlock());
	}

	public void register() {
		GameRegistry.registerBlock(this, ItemBlockModSlab.class, name);
	}

	@Override
	public String getUnlocalizedName(int i) {
		return name;
	}

	@Override
	public abstract boolean isDouble();

	@Override
	public abstract IProperty getVariantProperty();

	@Override
	public abstract Object getVariant(ItemStack stack);

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}
