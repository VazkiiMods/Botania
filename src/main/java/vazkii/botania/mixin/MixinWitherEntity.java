package vazkii.botania.mixin;

import net.minecraft.entity.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WitherEntity.class)
public interface MixinWitherEntity {
	@Invoker
	double callGetHeadX(int idx);

	@Invoker
	double callGetHeadY(int idx);

	@Invoker
	double callGetHeadZ(int idx);
}
