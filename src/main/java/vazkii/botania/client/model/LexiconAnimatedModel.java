package vazkii.botania.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.Event;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.client.core.handler.ClientTickHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexiconAnimatedModel implements ISmartItemModel {

	private final IBakedModel baseModel;
	private final Map<IModelState, IBakedModel> cache; // No need to free this, LexiconAnimatedModels are destroyed on resource reload
	private final IAnimationStateMachine asm;

	protected LexiconAnimatedModel(IBakedModel baseModel) {
		this.baseModel = baseModel;
		this.cache = new HashMap<>();
		this.asm = Animation.INSTANCE.load(new ResourceLocation("botania", "asms/item/lexicon_firstperson.json"), ImmutableMap.of());
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		float time = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 20F;
		Pair<IModelState, Iterable<Event>> pair = asm.apply(time);
		// baseModel
		return baseModel; // todo
	}

	@Override public List<BakedQuad> getFaceQuads(EnumFacing e) { return baseModel.getFaceQuads(e); }
	@Override public List<BakedQuad> getGeneralQuads() { return baseModel.getGeneralQuads(); }
	@Override public boolean isAmbientOcclusion() { return baseModel.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return baseModel.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return baseModel.isBuiltInRenderer(); }
	@Override public TextureAtlasSprite getParticleTexture() { return baseModel.getParticleTexture(); }

	@SuppressWarnings("deprecation")
	@Override public ItemCameraTransforms getItemCameraTransforms() { return baseModel.getItemCameraTransforms(); }
}
