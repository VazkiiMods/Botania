/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 16, 2014, 5:50:31 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockModFlower extends BlockFlower implements ILexiconable, IGrowable {
	protected final EnumDyeColor color;
	protected BlockModFlower(EnumDyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		int hex = this.color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		Vec3d offset = state.getOffset(world, pos);
		double x = pos.getX() + offset.x;
		double y = pos.getY() + offset.y;
		double z = pos.getZ() + offset.z;

		if(rand.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(x + 0.3 + rand.nextFloat() * 0.5, y + 0.5 + rand.nextFloat() * 0.5, z + 0.3 + rand.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, rand.nextFloat(), 5);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean fuckifiknow) {
		return world.getBlockState(pos.up()).isAir(world, pos);
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return canGrow(world, pos, state, false);
	}

	@Override
	public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		placeDoubleFlower(world, pos, color, 1 | 2);
	}

	public static void placeDoubleFlower(World world, BlockPos pos, EnumDyeColor color, int flags) {
		Block flower = color.getMetadata() >= 8 ? ModBlocks.doubleFlower2 : ModBlocks.doubleFlower1;
		world.setBlockState(pos, flower.getDefaultState().with(color.getMetadata() >= 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, color).with(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), flags);
		world.setBlockState(pos.up(), flower.getDefaultState().with(color.getMetadata() >= 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, color).with(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), flags);
	}
}
