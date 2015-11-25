/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 2:42:32 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A component of a multiblock, the normal one
 * is just a block.
 */
public class MultiblockComponent {

	public BlockPos relPos;
	public final Block block;
	public final int meta;
	public final TileEntity tileEntity;
	public boolean doFancyRender;

	public MultiblockComponent(BlockPos relPos, Block block, int meta) {
		this(relPos, block, meta, null);
	}

	public MultiblockComponent(BlockPos relPos, Block block, int meta, boolean doFancyRender) {
		this(relPos, block, meta, doFancyRender, null);
	}

	public MultiblockComponent(BlockPos relPos, Block block, int meta, TileEntity tileEntity) {
		this(relPos, block, meta, block.hasTileEntity(block.getStateFromMeta(meta)) == (tileEntity != null), tileEntity);
	}

	public MultiblockComponent(BlockPos relPos, Block block, int meta, boolean doFancyRender, TileEntity tileEntity) {
		this.relPos = relPos;
		this.block = block;
		this.meta = meta;
		this.tileEntity = tileEntity;
		this.doFancyRender = doFancyRender;
	}

	public BlockPos getRelativePosition() {
		return relPos;
	}

	public Block getBlock() {
		return block;
	}

	public int getMeta() {
		return meta;
	}

	public boolean matches(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == getBlock() && (meta == -1 || world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) == meta);
	}

	public ItemStack[] getMaterials() {
		return new ItemStack[] { new ItemStack(block, 1, meta) };
	}

	public void rotate(double angle) {
		double x = relPos.getX();
		double z = relPos.getZ();
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		double xn = x * cos - z * sin;
		double zn = x * sin + z * cos;
		relPos = new BlockPos((int) Math.round(xn), relPos.getY(), (int) Math.round(zn));
	}

	public MultiblockComponent copy() {
		return new MultiblockComponent(relPos, block, meta, tileEntity);
	}

	public TileEntity getTileEntity() {
		return tileEntity;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldDoFancyRender() {
		return doFancyRender;
	}
}
