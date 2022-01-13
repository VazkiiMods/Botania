package vazkii.botania.forge.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.forge.integration.CurioIntegration;

// [SelfMixin] IForgeItem#initCapabilities, which can't exist in common code
@Mixin(ItemBauble.class)
public class MixinItemBauble extends Item {
	public MixinItemBauble(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (EquipmentHandler.instance instanceof CurioIntegration ci) {
			return ci.initCapability(stack);
		}
		return null;
	}
}
