package vazkii.botania.mixin.datafixers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3818_3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(V3818_3.class)
public class V3818_3Mixin {
	@WrapOperation(
		// Lambda targets don't get remapped on fabric, so we also have to specify its name in intermediary
		method = { "lambda$registerTypes$0", "method_57277" },
		at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/DSL;optionalFields([Lcom/mojang/datafixers/util/Pair;)Lcom/mojang/datafixers/types/templates/TypeTemplate;")
	)
	private static TypeTemplate addBotaniaComponents(Pair<String, TypeTemplate>[] fields, Operation<TypeTemplate> original, @Local(argsOnly = true) Schema schema) {

		@SuppressWarnings("unchecked")
		Pair<String, TypeTemplate>[] botaniaComponents = new Pair[] {
				Pair.of("botania:placed_item", References.ITEM_NAME.in(schema)),
				Pair.of("botania:attached_lens", References.ITEM_STACK.in(schema)),
				Pair.of("botania:attached_lenses", DSL.list(References.ITEM_STACK.in(schema)))
		};
		Pair<String, TypeTemplate>[] newFields = Arrays.copyOf(fields, fields.length + botaniaComponents.length);
		System.arraycopy(botaniaComponents, 0, newFields, fields.length, botaniaComponents.length);
		return original.call((Object) newFields);
	}
}
