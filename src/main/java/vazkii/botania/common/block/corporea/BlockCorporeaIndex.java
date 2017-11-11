/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2015, 12:49:58 AM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockCorporeaIndex extends BlockCorporeaBase implements ILexiconable {

	public BlockCorporeaIndex() {
		super(Material.IRON, LibBlockNames.CORPOREA_INDEX);
		setHardness(5.5F);
		setSoundType(SoundType.METAL);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileCorporeaIndex();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaIndex;
	}

	@Override
	public boolean addLandingEffects(IBlockState state, net.minecraft.world.WorldServer world, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles )
	{
		float f = MathHelper.ceil(entity.fallDistance - 3.0F);
		double d0 = Math.min(0.2F + f / 15.0F, 10.0F);
		if (d0 > 2.5D) {
			d0 = 2.5D;
		}
		int i = (int)(150.0D * d0);
		world.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.ELEMENTIUM)));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager effectRenderer)
	{
		if (world.getBlockState(pos).getBlock() == this) {
			int i = 4;
			ParticleDigging.Factory factory = new ParticleDigging.Factory();
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					for (int l = 0; l < i; ++l) {
						double d0 = pos.getX() + (j + 0.5D) / i;
						double d1 = pos.getY() + (k + 0.5D) / i;
						double d2 = pos.getZ() + (l + 0.5D) / i;
						effectRenderer.addEffect(factory.createParticle(-1, world, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, Block.getStateId(ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.ELEMENTIUM))));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, "corporea_index");
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileCorporeaIndex.class);
	}

}
