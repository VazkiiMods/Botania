package vazkii.botania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	@Invoker("renderShape")
	static void botania_renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double offsetX, double offsetY, double offsetZ, float r, float g, float b, float a) {
		throw new IllegalStateException();
	}
}
