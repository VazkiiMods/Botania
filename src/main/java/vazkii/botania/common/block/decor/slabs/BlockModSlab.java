package vazkii.botania.common.block.decor.slabs;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockModSlab;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public abstract class BlockModSlab extends BlockSlab implements ILexiconable {

	final String name;
	protected final boolean doubleSlab;
	public static final PropertyEnum<DummyEnum> DUMMY = PropertyEnum.create("dummy", DummyEnum.class);

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

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HALF, getVariantProperty());
	}

	@Nonnull
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

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		return new ItemStack(getSingleBlock());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(getSingleBlock());
	}

	@Override
	public ItemStack createStackedBlock(@Nonnull IBlockState par1) {
		return new ItemStack(getSingleBlock());
	}

	public void register() {
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, name));
		if (!isDouble())
			GameRegistry.register(new ItemBlockModSlab(this), new ResourceLocation(LibMisc.MOD_ID, name));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(int i) {
		return name;
	}

	@Override
	public final boolean isDouble() {
		return doubleSlab;
	}

	@Nonnull
	@Override
	public final IProperty getVariantProperty() {
		return DUMMY; // Vanilla expects us to store different kinds of slabs into one block ID. Except we don't. We need this dummy property and dummy value to satisfy it.
	}

	@Nonnull
	@Override
	public final Comparable<?> getTypeForItem(@Nonnull ItemStack stack) {
		return DummyEnum.SINGLETON;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

	public enum DummyEnum implements IStringSerializable {
		SINGLETON {
			@Nonnull
			@Override
			public String getName() {
				return name().toLowerCase(Locale.ROOT);
			}
		}
	}

}
