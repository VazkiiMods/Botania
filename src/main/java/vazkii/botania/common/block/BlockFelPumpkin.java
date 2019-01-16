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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockFelPumpkin extends BlockMod implements ILexiconable {
	private static final ResourceLocation LOOT_TABLE = new ResourceLocation(LibMisc.MOD_ID, "fel_blaze");
	private static final String TAG_FEL_SPAWNED = "Botania-FelSpawned";

	public BlockFelPumpkin() {
		super(Material.GOURD, LibBlockNames.FEL_PUMPKIN);
		setHardness(1F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.CARDINALS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getHorizontalIndex();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);

		if(!world.isRemote && world.getBlockState(pos.down()).getBlock() == Blocks.IRON_BARS && world.getBlockState(pos.down(2)).getBlock() == Blocks.IRON_BARS) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 2);
			world.setBlockState(pos.down(2), Blocks.AIR.getDefaultState(), 2);
			EntityBlaze blaze = new EntityBlaze(world);
			blaze.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() - 1.95D, pos.getZ() + 0.5D, 0.0F, 0.0F);
			blaze.deathLootTable = LOOT_TABLE;
			blaze.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			world.spawnEntity(blaze);
			world.notifyNeighborsOfStateChange(pos, Blocks.AIR, false);
			world.notifyNeighborsOfStateChange(pos.down(), Blocks.AIR, false);
			world.notifyNeighborsOfStateChange(pos.down(2), Blocks.AIR, false);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, placer.getHorizontalFacing().getOpposite()));
	}

	// Legacy support
	@SubscribeEvent
	public static void onDrops(LivingDropsEvent event) {
		if(event.getEntityLiving() instanceof EntityBlaze && event.getEntityLiving().getEntityData().getBoolean(TAG_FEL_SPAWNED))
			if(event.getDrops().isEmpty())
				event.getDrops().add(new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, new ItemStack(Items.BLAZE_POWDER, 6)));
			else for(EntityItem item : event.getDrops()) {
				ItemStack stack = item.getItem();
				if(stack.getItem() == Items.BLAZE_ROD)
					item.setItem(new ItemStack(Items.BLAZE_POWDER, stack.getCount() * 10));
			}
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.gardenOfGlass;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, 0, getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.NORTH));
	}

}
