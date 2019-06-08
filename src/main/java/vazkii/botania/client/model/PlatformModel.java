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
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.IFluidState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockCamo;
import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCamo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class PlatformModel implements IDynamicBakedModel {
	private final IBakedModel original;

	public PlatformModel(IBakedModel original) {
		this.original = original;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
		if(state == null)
			return ImmutableList.of();

		if(!(state.getBlock() instanceof BlockPlatform))
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);

		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		if(layer == null) {
			layer = BlockRenderLayer.SOLID; // workaround for when this isn't set (digging, etc.)
		}

		BlockState heldState = data.getData(BotaniaStateProps.HELD_STATE);
		BlockPos heldPos = data.getData(BotaniaStateProps.HELD_POS);

		if (heldPos == null) {
			return ImmutableList.of();
		}

		Minecraft mc = Minecraft.getInstance();
		if(heldState == null && layer == BlockRenderLayer.SOLID) {
			// No camo
			return original.getQuads(state, side, rand, data);
		} else if(heldState != null) {

			// Some people used this to get an invisible block in the past, accommodate that.
			if(heldState.getBlock() == ModBlocks.manaGlass)
				return ImmutableList.of();

			if(heldState.getBlock().canRenderInLayer(heldState, layer)) {
				// Steal camo's model
				IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModel(heldState);

				// todo possible to reimplement calling camo's smart model stuff too?
				return model.getQuads(heldState, side, rand, EmptyModelData.INSTANCE);
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

}
