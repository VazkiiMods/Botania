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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

import java.util.Map;

@Mixin(SkullTileEntityRenderer.class)
public interface AccessorSkullTileEntityRenderer {
	@Accessor("MODELS")
	static Map<SkullBlock.ISkullType, GenericHeadModel> getModels() {
		throw new IllegalStateException();
	}

	@Invoker
	static RenderType callGetRenderType(SkullBlock.ISkullType skullType, @Nullable GameProfile profile) {
		throw new IllegalStateException();
	}
}
