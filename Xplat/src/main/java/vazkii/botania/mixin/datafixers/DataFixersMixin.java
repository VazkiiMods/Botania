package vazkii.botania.mixin.datafixers;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;

import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.datafix.*;

import java.util.Map;
import java.util.function.UnaryOperator;

@Mixin(DataFixers.class)
public class DataFixersMixin {
	@Shadow
	private static UnaryOperator<String> createRenamer(Map<String, String> renameMap) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Inject(method = "addFixers", at = @At("TAIL"))
	private static void injectDataFixers(DataFixerBuilder builder, CallbackInfo ci, @Local(ordinal = 219) Schema v3825, @Local(ordinal = 225) Schema v3945) {
		builder.addFixer(BlockRenameFix.create(v3825, "Botania block renames", createRenamer(BotaniaDataFixers.RENAMED_BLOCKS)));
		builder.addFixer(ItemRenameFix.create(v3825, "Botania block item renames", createRenamer(BotaniaDataFixers.RENAMED_BLOCKS)));
		builder.addFixer(ItemRenameFix.create(v3825, "Botania item renames", createRenamer(BotaniaDataFixers.RENAMED_ITEMS)));
		builder.addFixer(new BotaniaEntityDataFix(v3825));
		builder.addFixer(new BotaniaDataAttachmentFixer(v3945));
	}
}
