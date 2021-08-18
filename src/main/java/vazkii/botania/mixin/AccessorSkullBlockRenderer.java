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

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.level.block.SkullBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

import java.util.Map;

@Mixin(SkullBlockRenderer.class)
public interface AccessorSkullBlockRenderer {
	@Accessor("MODEL_BY_TYPE")
	static Map<SkullBlock.Type, SkullModel> getModels() {
		throw new IllegalStateException();
	}

	@Invoker("getRenderType")
	static RenderType botania_getRenderType(SkullBlock.Type skullType, @Nullable GameProfile profile) {
		throw new IllegalStateException();
	}
}
