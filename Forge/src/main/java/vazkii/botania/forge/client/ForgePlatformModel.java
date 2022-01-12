package vazkii.botania.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForgePlatformModel extends BakedModelWrapper<BakedModel> {
	public static final ModelProperty<TilePlatform.PlatformData> PROPERTY = new ModelProperty<>();

	public ForgePlatformModel(BakedModel originalModel) {
		super(originalModel);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
		var data = extraData.getData(PROPERTY);
		if (state == null || !(state.getBlock() instanceof BlockPlatform) || data == null) {
			return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper()
					.getModelManager().getMissingModel().getQuads(state, side, rand, extraData);
		}

		BlockState heldState = data.state();

		if (heldState == null) {
			// No camo
			return super.getQuads(state, side, rand, extraData);
		} else {
			// Some people used this to get an invisible block in the past, accommodate that.
			if (heldState.is(ModBlocks.manaGlass)) {
				return Collections.emptyList();
			}

			BakedModel model = Minecraft.getInstance().getBlockRenderer()
					.getBlockModelShaper().getBlockModel(heldState);
			return model.getQuads(state, side, rand, EmptyModelData.INSTANCE);
		}
	}
}
