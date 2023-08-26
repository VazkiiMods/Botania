package vazkii.botania.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.Properties.class)
public interface BlockPropertiesAccessor {
	@Accessor("hasCollision")
	void botania_setHasCollision(boolean value);

	@Accessor("isRandomlyTicking")
	void botania_setIsRandomlyTicking(boolean value);

	@Accessor("replaceable")
	void botania_setReplaceable(boolean value);
}
