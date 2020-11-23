package vazkii.botania.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Criteria.class)
public interface AccessorCriteria {
	@Invoker("register")
	static <T extends Criterion<?>> T botania_register(T thing) {
		throw new IllegalStateException();
	}
}
