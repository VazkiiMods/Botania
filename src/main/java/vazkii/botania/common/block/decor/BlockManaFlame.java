/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 21, 2014, 12:28:06 AM (GMT)]
 */
package vazkii.botania.common.block.decor;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.world.WorldTypeSkyblock;

import java.util.List;

public class BlockManaFlame extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

	public BlockManaFlame() {
		super(Material.cloth, LibBlockNames.MANA_FLAME);
		setSoundType(SoundType.CLOTH);
		setLightLevel(1F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((TileManaFlame) world.getTileEntity(pos)).getLightColor();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess p_149655_1_, BlockPos pos) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing s, float xs, float ys, float zs) {
		if(WorldTypeSkyblock.isWorldSkyblock(world)) {
			if(stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.sapling) && !player.inventory.hasItemStack(new ItemStack(ModItems.lexicon))) {
				if(!world.isRemote)
					stack.stackSize--;
				if(!player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon)))
					player.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.lexicon), false);
				return true;
			}

		}
		return false;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return ImmutableList.of();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileManaFlame();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.lenses;
	}

}
