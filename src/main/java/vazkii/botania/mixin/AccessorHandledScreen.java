package vazkii.botania.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;

@Mixin(HandledScreen.class)
public interface AccessorHandledScreen {
	@Nullable
	@Accessor
	Slot getFocusedSlot();
}
