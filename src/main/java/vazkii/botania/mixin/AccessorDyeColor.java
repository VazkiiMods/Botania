package vazkii.botania.mixin;

import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DyeColor.class)
public interface AccessorDyeColor {
	@Accessor
	int getColor();
}
