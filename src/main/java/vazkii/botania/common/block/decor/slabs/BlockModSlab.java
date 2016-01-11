package vazkii.botania.common.block.decor.slabs;

import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockModSlab;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockModSlab extends BlockSlab implements ILexiconable {

	String name;
	protected final boolean doubleSlab;
	public static final PropertyEnum DUMMY = PropertyEnum.create("dummy", DummyEnum.class);

	public BlockModSlab(boolean full, Material mat, String name) {
		super(mat);
		this.name = name;
		setUnlocalizedName(name);
		doubleSlab = full;
		if(!full) {
			setCreativeTab(BotaniaCreativeTab.INSTANCE);
			useNeighborBrightness = true;
		}
		setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(DUMMY, DummyEnum.SINGLETON));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, HALF, getVariantProperty());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (doubleSlab) {
			return getDefaultState();
		} else {
			return getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (doubleSlab) {
			return 0;
		} else {
			return state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;
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
		GameRegistry.registerBlock(this, isDouble() ? null : ItemBlockModSlab.class, name);
	}

	@Override
	public String getUnlocalizedName(int i) {
		return name;
	}

	@Override
	public final boolean isDouble() {
		return doubleSlab;
	}

	@Override
	public final IProperty getVariantProperty() {
		return DUMMY; // Vanilla expects us to store different kinds of slabs into one block ID. Except we don't. We need this dummy property and dummy value to satisfy it.
	}

	@Override
	public final Object getVariant(ItemStack stack) {
		return DummyEnum.SINGLETON;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

	public enum DummyEnum implements IStringSerializable {
		SINGLETON {
			@Override
			public String getName() {
				return name();
			}
		}
	}

}
