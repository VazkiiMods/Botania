package vazkii.botania.common.integration.curios;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ConfigHandler;

public class RenderableCurio extends BaseCurio {
    public RenderableCurio(ItemStack stack) {
        super(stack);
    }

    @Override
    public boolean hasRender(String identifier, EntityLivingBase living) {
        boolean hasCosmetic = stack.getItem() instanceof ICosmeticAttachable && !((ICosmeticAttachable) stack.getItem()).getCosmeticItem(stack).isEmpty();
        boolean ink = stack.getItem() instanceof IPhantomInkable && ((IPhantomInkable) stack.getItem()).hasPhantomInk(stack);
        boolean config = ConfigHandler.CLIENT.renderAccessories.get();
        boolean invis = living.getActivePotionEffect(MobEffects.INVISIBILITY) != null;
        return !hasCosmetic && !ink && config && !invis;
    }
}
