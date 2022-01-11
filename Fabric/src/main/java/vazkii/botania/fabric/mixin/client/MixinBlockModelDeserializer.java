/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import net.minecraft.client.renderer.block.model.BlockModel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.fabric.client.FabricFloatingFlowerModel;

import java.lang.reflect.Type;

@Mixin(BlockModel.Deserializer.class)
public class MixinBlockModelDeserializer {
	@Inject(
		method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockModel;",
		at = @At("HEAD"), cancellable = true
	)
	private void hookDeserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext context,
			CallbackInfoReturnable<BlockModel> cir) {
		FabricFloatingFlowerModel.hookModelLoad(jsonElement, context, cir);
	}
}
