/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [17/11/2015, 18:33:30 (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockStem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public class BlockAltGrass extends BlockMod implements ILexiconable {

	public enum Variant {
		DRY,
		GOLDEN,
		VIVID,
		SCORCHED,
		INFUSED,
		MUTATED
	}

	private final Variant variant;

	public BlockAltGrass(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Override
	public boolean isToolEffective(IBlockState state, ToolType tool) {
		return tool.equals(ToolType.SHOVEL);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack held = player.getHeldItem(hand);
		if(held.getItem() instanceof ItemHoe && world.isAirBlock(pos.up())) {
			held.damageItem(1, player);
			world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 1 | 2);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(!world.isRemote && state.getBlock() == this && world.getLight(pos.up()) >= 9) {
			for(int l = 0; l < 4; ++l) {
				BlockPos pos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

				world.getBlockState(pos1.up()).getBlock();

				if(world.getBlockState(pos1).getBlock() == Blocks.DIRT && world.getLight(pos1.up()) >= 4 && world.getBlockLightOpacity(pos1.up()) <= 2)
					world.setBlockState(pos1, getDefaultState(), 1 | 2);
			}
		}
	}

	@Nonnull
	@Override
	public IItemProvider getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
		return Blocks.DIRT.getItemDropped(state, world, pos, fortune);
	}

	@Override
	public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
		EnumPlantType type = plantable.getPlantType(world, pos.down());
		return type == EnumPlantType.Plains || type == EnumPlantType.Beach || plantable instanceof BlockStem;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random r) {
		switch(variant) {
		case DRY:
			break;
		case GOLDEN:
			break;
		case VIVID:
			break;
		case SCORCHED:
			if(r.nextInt(80) == 0)
				world.spawnParticle(Particles.FLAME, pos.getX() + r.nextFloat(), pos.getY() + 1.1, pos.getZ() + r.nextFloat(), 0, 0, 0);
			break;
		case INFUSED:
			if(r.nextInt(100) == 0)
				Botania.proxy.sparkleFX(pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 0F, 1F, 1F, r.nextFloat() * 0.2F + 1F, 5);
			break;
		case MUTATED:
			if(r.nextInt(100) == 0) {
				if(r.nextInt(100) > 25)
					Botania.proxy.sparkleFX(pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 1F, 0F, 1F, r.nextFloat() * 0.2F + 1F, 5);
				else Botania.proxy.sparkleFX(pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 1F, 1F, 0F, r.nextFloat() * 0.2F + 1F, 5);
			}
			break;
		}
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.grassSeeds;
	}
}
