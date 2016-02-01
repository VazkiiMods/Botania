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

import java.util.List;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.world.WorldTypeSkyblock;
import net.minecraftforge.fml.common.Optional;

public class BlockManaFlame extends BlockModContainer implements ILexiconable {

	public BlockManaFlame() {
		super(Material.cloth);
		setUnlocalizedName(LibBlockNames.MANA_FLAME);
		float f = 0.25F;
		setStepSound(soundTypeCloth);
		setBlockBounds(f, f, f, 1F - f, 1F - f, 1F - f);
		setLightLevel(1F);
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		return ((TileManaFlame) world.getTileEntity(pos)).getLightColor();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess p_149655_1_, BlockPos pos) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World p_149668_1_, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		if(WorldTypeSkyblock.isWorldSkyblock(world)) {
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.sapling) && !player.inventory.hasItem(ModItems.lexicon)) {
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaFlame();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.lenses;
	}

}
