/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)
public interface AccessorSkullTileEntityRenderer {
	@Accessor("MODELS")
	static Map<SkullBlock.SkullType, SkullEntityModel> getModels() {
		throw new IllegalStateException();
	}

	@Invoker
	static RenderLayer callGetRenderType(SkullBlock.SkullType skullType, @Nullable GameProfile profile) {
		throw new IllegalStateException();
	}
}
