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

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
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
import cpw.mods.fml.common.Optional;

public class BlockManaFlame extends BlockModContainer implements ILexiconable {

	public BlockManaFlame() {
		super(Material.cloth);
		setBlockName(LibBlockNames.MANA_FLAME);
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
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return ((TileManaFlame) world.getTileEntity(x, y, z)).getLightColor();
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
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
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
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
	public IIcon getIcon(int side, int meta) {
		return Blocks.fire.getIcon(side, meta);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaFlame();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.lenses;
	}

}
