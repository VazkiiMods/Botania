package vazkii.botania.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.block_entity.PlatformBlockEntity;

import java.util.Collections;
import java.util.List;

public class ForgePlatformModel extends BakedModelWrapper<BakedModel> {
	public static final ModelProperty<PlatformBlockEntity.PlatformData> PROPERTY = new ModelProperty<>();

	public ForgePlatformModel(BakedModel originalModel) {
		super(originalModel);
	}

	@NotNull
	@Override
	public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
		if (world.getBlockEntity(pos) instanceof PlatformBlockEntity platform) {
			return ModelData.builder()
					.with(PROPERTY, new PlatformBlockEntity.PlatformData(platform))
					.build();
		}
		return tileData;
	}

	@NotNull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
			@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
		var data = extraData.get(PROPERTY);
		if (state == null || !(state.getBlock() instanceof BlockPlatform) || data == null) {
			return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper()
					.getModelManager().getMissingModel().getQuads(state, side, rand, extraData, renderType);
		}

		BlockState heldState = data.state();

		if (heldState == null) {
			// No camo
			return super.getQuads(state, side, rand, extraData, renderType);
		} else {
			// Some people used this to get an invisible block in the past, accommodate that.
			if (heldState.is(ModBlocks.manaGlass)) {
				return Collections.emptyList();
			}

			BakedModel model = Minecraft.getInstance().getBlockRenderer()
					.getBlockModelShaper().getBlockModel(heldState);
			return model.getQuads(heldState, side, rand, ModelData.EMPTY, renderType);
		}
	}
}
