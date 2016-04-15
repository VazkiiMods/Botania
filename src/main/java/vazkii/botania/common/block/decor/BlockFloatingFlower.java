/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2014, 10:16:53 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFloatingFlower.IslandType;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;
import vazkii.botania.common.item.IFloatingFlowerVariant;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.List;
import java.util.Random;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockFloatingFlower extends BlockMod implements ILexiconable, IInfusionStabiliser {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

	public BlockFloatingFlower() {
		this(LibBlockNames.MINI_ISLAND);
	}

	public BlockFloatingFlower(String name) {
		super(Material.ground, name);
		setHardness(0.5F);
		setSoundType(SoundType.GROUND);
		setLightLevel(1F);

		setDefaultState(((IExtendedBlockState) blockState.getBaseState())
				.withProperty(BotaniaStateProps.ISLAND_TYPE, IslandType.GRASS)
				.withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.ISLAND_TYPE });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			 meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = getActualState(state, world, pos);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileFloatingFlower) {
			state = ((IExtendedBlockState) state).withProperty(BotaniaStateProps.ISLAND_TYPE, ((TileFloatingFlower) te).getIslandType());
		} else if (te instanceof TileFloatingSpecialFlower) {
			state = ((IExtendedBlockState) state).withProperty(BotaniaStateProps.ISLAND_TYPE, ((TileFloatingSpecialFlower) te).getIslandType());
		}
		return state;
	}

	@Override
	public boolean addLandingEffects(IBlockState state, net.minecraft.world.WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
		float f = (float) MathHelper.ceiling_float_int(entity.fallDistance - 3.0F);
		double d0 = (double)Math.min(0.2F + f / 15.0F, 10.0F);
		if (d0 > 2.5D) {
			d0 = 2.5D;
		}
		int i = (int)(150.0D * d0);
		worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(Blocks.dirt.getDefaultState()));
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
						effectRenderer.addEffect(factory.getEntityFX(-1, world, d0, d1, d2, d0 - (double)pos.getX() - 0.5D, d1 - (double)pos.getY() - 0.5D, d2 - (double)pos.getZ() - 0.5D, Block.getStateId(Blocks.dirt.getDefaultState())));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ColoredLightHelper.getPackedColor(world.getBlockState(pos).getValue(BotaniaStateProps.COLOR), originalLight);
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockWithMetadataAndName(this), getRegistryName());
	}

	@Override
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		int hex = state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, pos.getX() + 0.3 + par5Random.nextFloat() * 0.5, pos.getY() + 0.5 + par5Random.nextFloat() * 0.5, pos.getZ() + 0.3 + par5Random.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, par5Random.nextFloat(), 5);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List<ItemStack> par3) {
		for(int i = 0; i < 16; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(stack != null) {
			IFloatingFlower flower = (IFloatingFlower) world.getTileEntity(pos);
			IslandType type = null;
			if(stack.getItem() == Items.snowball)
				type = IslandType.SNOW;
			else if(stack.getItem() instanceof IFloatingFlowerVariant) {
				IslandType newType = ((IFloatingFlowerVariant) stack.getItem()).getIslandType(stack);
				if(newType != null)
					type = newType;
			}

			if(type != null && type != flower.getIslandType()) {
				if(!world.isRemote) {
					flower.setIslandType(type);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}

				if(!player.capabilities.isCreativeMode)
					stack.stackSize--;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFloatingFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.shinyFlowers;
	}

	@Override
	public boolean canStabaliseInfusion(World world, BlockPos pos) {
		return ConfigHandler.enableThaumcraftStablizers;
	}
}
