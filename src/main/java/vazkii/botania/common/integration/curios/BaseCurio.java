package vazkii.botania.common.integration.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.curios.api.capability.ICurio;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.lib.LibMisc;

public class BaseCurio implements ICurio {
    protected final ItemStack stack;

    public BaseCurio(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public void onEquipped(String identifier, LivingEntity living) {
        if(!living.world.isRemote && living instanceof ServerPlayerEntity) {
            PlayerHelper.grantCriterion((ServerPlayerEntity) living, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");
        }
    }

    @Override
    public void playEquipSound(LivingEntity living) {
        living.world.playSound(null, living.posX, living.posY, living.posZ, ModSounds.equipBauble, living.getSoundCategory(), 0.1F, 1.3F);
    }
}
