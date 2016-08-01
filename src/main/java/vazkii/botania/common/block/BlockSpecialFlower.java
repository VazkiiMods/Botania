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
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.block.statemap.StateMap;
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
import net.minecraftforge.client.model.ModelLoader;
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
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockSpecialFlower extends BlockFlower implements ISpecialFlower, IWandable, ILexiconable, IWandHUD, IRegisterCallback, IModelRegister {

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
				LibBlockNames.SUBTILE_SOLEGNOLIA,
				LibBlockNames.SUBTILE_BERGAMUTE));
	}

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.8, 1, 0.8);

	protected BlockSpecialFlower() {
		ModBlocks.ALL_BLOCKS.add(this);
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SPECIAL_FLOWER));
		setUnlocalizedName(LibBlockNames.SPECIAL_FLOWER);
		setHardness(0.1F);
		setSoundType(SoundType.PLANT);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE).withProperty(type, EnumFlowerType.POPPY)
		);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { getTypeProperty(), BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.SUBTILE_ID } );
	}

	@Nonnull
	@Override
	public IExtendedBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
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

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > 15) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);

		return world.getTileEntity(pos) == null ? 0 : ((TileSpecialFlower) world.getTileEntity(pos)).getLightValue();
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

	@Nonnull
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, @Nonnull List<ItemStack> stacks) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu) {
			stacks.add(ItemBlockSpecialFlower.ofType(s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				stacks.add(ItemBlockSpecialFlower.ofType(BotaniaAPI.miniFlowers.get(s)));
		}
	}

	@Nonnull
	@Override
	public EnumFlowerColor getBlockType() {
		return EnumFlowerColor.RED;
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		String name = ((TileSpecialFlower) world.getTileEntity(pos)).subTileName;
		return ItemBlockSpecialFlower.ofType(name);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == ModBlocks.redStringRelay
				|| world.getBlockState(pos.down()).getBlock() == Blocks.MYCELIUM
				|| super.canPlaceBlockAt(world, pos);
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.canSustainBush(state);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockHarvested(world, pos, state, player);
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			// Copy of super.removedByPlayer but don't set to air yet
			// This is so getDrops below will have a TE to work with
			onBlockHarvested(world, pos, state, player);
			return true;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		}
	}

	@Override
	public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		// Now delete the block and TE
		world.setBlockToAir(pos);
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
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
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int par5, int par6) {
		super.eventReceived(state, world, pos, par5, par6);
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
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
			return true;
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
		worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(Blocks.WATERLILY.getDefaultState()));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer) {
		if (world.getBlockState(pos).getBlock() == this) {
			int i = 4;
			ParticleDigging.Factory factory = new ParticleDigging.Factory();
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					for (int l = 0; l < i; ++l) {
						double d0 = (double)pos.getX() + ((double)j + 0.5D) / (double)i;
						double d1 = (double)pos.getY() + ((double)k + 0.5D) / (double)i;
						double d2 = (double)pos.getZ() + ((double)l + 0.5D) / (double)i;
						effectRenderer.addEffect(factory.getEntityFX(-1, world, d0, d1, d2, d0 - (double)pos.getX() - 0.5D, d1 - (double)pos.getY() - 0.5D, d2 - (double)pos.getZ() - 0.5D, Block.getStateId(Blocks.WATERLILY.getDefaultState())));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void register() {
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockSpecialFlower(this), getRegistryName());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		// Let custom loader work
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).ignore(getTypeProperty()).build());
		ModelHandler.registerInventoryVariant(this);
	}
}
