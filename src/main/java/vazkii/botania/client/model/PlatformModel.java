/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockCamo;
import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCamo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class PlatformModel implements IBakedModel {
	private final IBakedModel original;

	public PlatformModel(IBakedModel original) {
		this.original = original;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, Random rand) {
		if(state == null)
			return ImmutableList.of();

		if(!(state.getBlock() instanceof BlockPlatform))
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);

		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		if(layer == null) {
			layer = BlockRenderLayer.SOLID; // workaround for when this isn't set (digging, etc.)
		}

		IBlockState heldState = ((IExtendedBlockState) state).getValue(BotaniaStateProps.HELD_STATE);
		IBlockReader heldWorld = ((IExtendedBlockState) state).getValue(BotaniaStateProps.HELD_WORLD);
		BlockPos heldPos = ((IExtendedBlockState) state).getValue(BotaniaStateProps.HELD_POS);

		if (heldWorld == null || heldPos == null) {
			return ImmutableList.of();
		}

		Minecraft mc = Minecraft.getInstance();
		if(heldState == null && layer == BlockRenderLayer.SOLID) {
			// No camo
			return original.getQuads(state, side, rand);
		} else if(heldState != null) {

			// Some people used this to get an invisible block in the past, accommodate that.
			if(heldState.getBlock() == ModBlocks.manaGlass)
				return ImmutableList.of();

			if(heldState.getBlock().canRenderInLayer(heldState, layer)) {
				// Steal camo's model
				IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModel(heldState);

				// Their model can be smart too
				IBlockState extended = heldState.getBlock().getExtendedState(heldState, new FakeBlockAccess(heldWorld), heldPos);
				return model.getQuads(extended, side, rand);
			}
		}

		return ImmutableList.of(); // Nothing renders
	}

	@Override public boolean isAmbientOcclusion() {
		return original.isAmbientOcclusion();
	}
	@Override public boolean isGui3d() {
		return original.isGui3d();
	}
	@Override public boolean isBuiltInRenderer() {
		return original.isBuiltInRenderer();
	}
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() {
		return original.getItemCameraTransforms();
	}
	@Nonnull @Override public ItemOverrideList getOverrides() {
		return original.getOverrides();
	}

	private static class FakeBlockAccess implements IBlockReader {

		private final IBlockReader compose;

		private FakeBlockAccess(IBlockReader compose) {
			this.compose = compose;
		}

		@Override
		public TileEntity getTileEntity(@Nonnull BlockPos pos) {
			return compose.getTileEntity(pos);
		}

		@Nonnull
		@Override
		public IBlockState getBlockState(@Nonnull BlockPos pos) {
			IBlockState state = compose.getBlockState(pos);
			if(state.getBlock() instanceof BlockCamo) {
				state = ((TileCamo) compose.getTileEntity(pos)).camoState;
			}
			return state == null ? Blocks.AIR.getDefaultState() : state;
		}

		@Nonnull
		@Override
		public IFluidState getFluidState(@Nonnull BlockPos pos) {
			return compose.getFluidState(pos);
		}
	}

}
