/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 7, 2015, 7:59:10 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockFelPumpkin extends BlockMod implements ILexiconable {

	private static final String TAG_FEL_SPAWNED = "Botania-FelSpawned";

	public BlockFelPumpkin() {
		super(Material.gourd, LibBlockNames.FEL_PUMPKIN);
		setHardness(1F);
		setSoundType(SoundType.WOOD);
		MinecraftForge.EVENT_BUS.register(this);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.CARDINALS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.getHorizontal(meta));
	}

	@Override
	public void onBlockAdded(World p_149726_1_, BlockPos pos, IBlockState state) {
		super.onBlockAdded(p_149726_1_, pos, state);

		if(!p_149726_1_.isRemote && p_149726_1_.getBlockState(pos.down()).getBlock() == Blocks.iron_bars && p_149726_1_.getBlockState(pos.down(2)).getBlock() == Blocks.iron_bars) {
			p_149726_1_.setBlockState(pos, Blocks.air.getDefaultState(), 2);
			p_149726_1_.setBlockState(pos.down(), Blocks.air.getDefaultState(), 2);
			p_149726_1_.setBlockState(pos.down(2), Blocks.air.getDefaultState(), 2);
			EntityBlaze blaze = new EntityBlaze(p_149726_1_);
			blaze.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() - 1.95D, pos.getZ() + 0.5D, 0.0F, 0.0F);
			blaze.getEntityData().setBoolean(TAG_FEL_SPAWNED, true);
			p_149726_1_.spawnEntityInWorld(blaze);
			p_149726_1_.notifyNeighborsOfStateChange(pos, Blocks.air);
			p_149726_1_.notifyNeighborsOfStateChange(pos.down(), Blocks.air);
			p_149726_1_.notifyNeighborsOfStateChange(pos.down(2), Blocks.air);
		}
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, p_149689_5_.getHorizontalFacing().getOpposite()));
	}

	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if(event.getEntityLiving() instanceof EntityBlaze && event.getEntityLiving().getEntityData().getBoolean(TAG_FEL_SPAWNED))
			if(event.getDrops().isEmpty())
				event.getDrops().add(new EntityItem(event.getEntityLiving().worldObj, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, new ItemStack(Items.blaze_powder, 6)));
			else for(EntityItem item : event.getDrops()) {
				ItemStack stack = item.getEntityItem();
				if(stack.getItem() == Items.blaze_rod)
					item.setEntityItemStack(new ItemStack(Items.blaze_powder, stack.stackSize * 10));
			}
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.gardenOfGlass;
	}

}
