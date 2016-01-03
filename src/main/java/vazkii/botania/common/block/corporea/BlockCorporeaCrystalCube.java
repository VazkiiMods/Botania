/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 3:56:19 PM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaCrystalCube extends BlockCorporeaBase implements ILexiconable {

	public BlockCorporeaCrystalCube() {
		super(Material.iron, LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		float f = (1F - 10F / 16F) / 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F, 1F - f);
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if(!world.isRemote) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.doRequest(player.isSneaking());
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		ItemStack stack = player.getCurrentEquippedItem();

		if(stack != null) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.setRequestTarget(stack);
			return true;
		}
		return false;
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
	public int getRenderType() {
		return 2;
	}

	@Override
	public TileCorporeaBase createNewTileEntity(World world, int meta) {
		return new TileCorporeaCrystalCube();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaCrystalCube;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getTileEntity(pos)).compValue;
	}

	@Override
	public boolean addLandingEffects(net.minecraft.world.WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles )
	{
		float f = (float) MathHelper.ceiling_float_int(entity.fallDistance - 3.0F);
		double d0 = (double)Math.min(0.2F + f / 15.0F, 10.0F);
		if (d0 > 2.5D) {
			d0 = 2.5D;
		}
		int i = (int)(150.0D * d0);
		worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.ELEMENTIUM)));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.EffectRenderer effectRenderer)
	{
		if (world.getBlockState(pos).getBlock() == this) {
			int i = 4;
			EntityDiggingFX.Factory factory = new EntityDiggingFX.Factory();
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					for (int l = 0; l < i; ++l) {
						double d0 = (double)pos.getX() + ((double)j + 0.5D) / (double)i;
						double d1 = (double)pos.getY() + ((double)k + 0.5D) / (double)i;
						double d2 = (double)pos.getZ() + ((double)l + 0.5D) / (double)i;
						effectRenderer.addEffect(factory.getEntityFX(-1, world, d0, d1, d2, d0 - (double)pos.getX() - 0.5D, d1 - (double)pos.getY() - 0.5D, d2 - (double)pos.getZ() - 0.5D, Block.getStateId(ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.ELEMENTIUM))));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
