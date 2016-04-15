/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 22, 2014, 7:06:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.integration.coloredlights.LightHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockSpecialFlower extends BlockFlower implements ISpecialFlower, IWandable, ILexiconable, IWandHUD {

	static {
		BotaniaAPI.subtilesForCreativeMenu.addAll(Arrays.asList(
				// Misc
				LibBlockNames.SUBTILE_PUREDAISY,
				LibBlockNames.SUBTILE_MANASTAR,

				// Generating
				LibBlockNames.SUBTILE_DAYBLOOM,
				LibBlockNames.SUBTILE_NIGHTSHADE,
				LibBlockNames.SUBTILE_ENDOFLAME,
				LibBlockNames.SUBTILE_HYDROANGEAS,
				LibBlockNames.SUBTILE_THERMALILY,
				LibBlockNames.SUBTILE_ARCANE_ROSE,
				LibBlockNames.SUBTILE_MUNCHDEW,
				LibBlockNames.SUBTILE_ENTROPINNYUM,
				LibBlockNames.SUBTILE_KEKIMURUS,
				LibBlockNames.SUBTILE_GOURMARYLLIS,
				LibBlockNames.SUBTILE_NARSLIMMUS,
				LibBlockNames.SUBTILE_SPECTROLUS,
				LibBlockNames.SUBTILE_RAFFLOWSIA,
				LibBlockNames.SUBTILE_DANDELIFEON,

				// Functional
				LibBlockNames.SUBTILE_JADED_AMARANTHUS,
				LibBlockNames.SUBTILE_BELLETHORN,
				LibBlockNames.SUBTILE_DREADTHORN,
				LibBlockNames.SUBTILE_HEISEI_DREAM,
				LibBlockNames.SUBTILE_TIGERSEYE,
				LibBlockNames.SUBTILE_MARIMORPHOSIS,
				LibBlockNames.SUBTILE_ORECHID,
				LibBlockNames.SUBTILE_ORECHID_IGNEM,
				LibBlockNames.SUBTILE_FALLEN_KANADE,
				LibBlockNames.SUBTILE_EXOFLAME,
				LibBlockNames.SUBTILE_AGRICARNATION,
				LibBlockNames.SUBTILE_HOPPERHOCK,
				LibBlockNames.SUBTILE_RANNUNCARPUS,
				LibBlockNames.SUBTILE_TANGLEBERRIE,
				LibBlockNames.SUBTILE_JIYUULIA,
				LibBlockNames.SUBTILE_HYACIDUS,
				LibBlockNames.SUBTILE_MEDUMONE,
				LibBlockNames.SUBTILE_POLLIDISIAC,
				LibBlockNames.SUBTILE_CLAYCONIA,
				LibBlockNames.SUBTILE_LOONIUM,
				LibBlockNames.SUBTILE_DAFFOMILL,
				LibBlockNames.SUBTILE_VINCULOTUS,
				LibBlockNames.SUBTILE_SPECTRANTHEMUM,
				LibBlockNames.SUBTILE_BUBBELL,
				LibBlockNames.SUBTILE_SOLEGNOLIA));
	}

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.8, 1, 0.8);

	protected BlockSpecialFlower() {
		super();
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SPECIAL_FLOWER));
		GameRegistry.register(new ItemBlockSpecialFlower(this), getRegistryName());
		setUnlocalizedName(LibBlockNames.SPECIAL_FLOWER);
		setHardness(0.1F);
		setSoundType(SoundType.PLANT);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(((IExtendedBlockState) blockState.getBaseState())
				.withProperty(BotaniaStateProps.SUBTILE_ID, "daybloom")
				.withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE).withProperty(type, EnumFlowerType.POPPY)
		);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { getTypeProperty(), BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.SUBTILE_ID } );
	}

	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileSpecialFlower && ((TileSpecialFlower) te).getSubTile() != null) {
			Class<? extends SubTileEntity> clazz = ((TileSpecialFlower) te).getSubTile().getClass();
			String id = BotaniaAPI.getSubTileStringMapping(clazz);
			return ((IExtendedBlockState) state).withProperty(BotaniaStateProps.SUBTILE_ID, id);
		} else {
			return ((IExtendedBlockState) state);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > 15) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta)).withProperty(type, EnumFlowerType.POPPY);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		int currentLight = world.getTileEntity(pos) == null ? -1 : ((TileSpecialFlower) world.getTileEntity(pos)).getLightValue();
		if(currentLight == -1)
			currentLight = 0;
		return LightHelper.getPackedColor(state.getValue(BotaniaStateProps.COLOR), currentLight);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getComparatorInputOverride();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getPowerLevel(side);
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu) {
			par3List.add(ItemBlockSpecialFlower.ofType(s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				par3List.add(ItemBlockSpecialFlower.ofType(BotaniaAPI.miniFlowers.get(s)));
		}
	}

	@Override
	public EnumFlowerColor getBlockType() {
		return EnumFlowerColor.RED;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		String name = ((TileSpecialFlower) world.getTileEntity(pos)).subTileName;
		return ItemBlockSpecialFlower.ofType(name);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) || world.getBlockState(pos.down()).getBlock() == ModBlocks.redStringRelay || world.getBlockState(pos.down()).getBlock() == Blocks.mycelium;
	}

	@Override
	public void onBlockHarvested(World par1World, BlockPos pos, IBlockState state, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode) {
			dropBlockAsItem(par1World, pos, state, 0);
			((TileSpecialFlower) par1World.getTileEntity(pos)).onBlockHarvested(par1World, pos, state, par6EntityPlayer);
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<>();
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			String name = ((TileSpecialFlower) tile).subTileName;
			list.add(ItemBlockSpecialFlower.ofType(name));
			((TileSpecialFlower) tile).getDrops(list);
		}

		return list;
	}

	@Override
	public boolean onBlockEventReceived(World par1World, BlockPos pos, IBlockState state, int par5, int par6) {
		super.onBlockEventReceived(par1World, pos, state, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(pos);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSpecialFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getEntry();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).onWanded(stack, player);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockAdded(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(stack != null && stack.getItem() == ModItems.dye) {
			EnumDyeColor newColor = EnumDyeColor.byMetadata(stack.getItemDamage());
			EnumDyeColor oldColor = state.getValue(BotaniaStateProps.COLOR);
			if(newColor != oldColor)
				world.setBlockState(pos, state.withProperty(BotaniaStateProps.COLOR, newColor), 1 | 2);
		}

		return ((TileSpecialFlower) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, hand, stack, side, hitX, hitY, hitZ);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TileSpecialFlower) world.getTileEntity(pos)).renderHUD(mc, res);
	}

	@Override
	public boolean addLandingEffects(IBlockState state, net.minecraft.world.WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
		float f = (float) MathHelper.ceiling_float_int(entity.fallDistance - 3.0F);
		double d0 = (double)Math.min(0.2F + f / 15.0F, 10.0F);
		if (d0 > 2.5D) {
			d0 = 2.5D;
		}
		int i = (int)(150.0D * d0);
		worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(Blocks.waterlily.getDefaultState()));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		if (world.getBlockState(pos).getBlock() == this) {
			int i = 4;
			EntityDiggingFX.Factory factory = new EntityDiggingFX.Factory();
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					for (int l = 0; l < i; ++l) {
						double d0 = (double)pos.getX() + ((double)j + 0.5D) / (double)i;
						double d1 = (double)pos.getY() + ((double)k + 0.5D) / (double)i;
						double d2 = (double)pos.getZ() + ((double)l + 0.5D) / (double)i;
						effectRenderer.addEffect(factory.getEntityFX(-1, world, d0, d1, d2, d0 - (double)pos.getX() - 0.5D, d1 - (double)pos.getY() - 0.5D, d2 - (double)pos.getZ() - 0.5D, Block.getStateId(Blocks.waterlily.getDefaultState())));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
