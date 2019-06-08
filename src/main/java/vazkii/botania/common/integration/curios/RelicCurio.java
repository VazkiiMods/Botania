package vazkii.botania.common.integration.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.relic.ItemRelic;

public class RelicCurio extends BaseCurio{
    private final ItemRelic relicDelegate;
    public RelicCurio(ItemStack stack, ItemRelic relicDelegate) {
        super(stack);
        this.relicDelegate = relicDelegate;
    }

    @Override
    public void onCurioTick(String identifier, LivingEntity living) {
        if(living instanceof PlayerEntity) {
            PlayerEntity ePlayer = (PlayerEntity) living;
            relicDelegate.updateRelic(stack, ePlayer);
            if(relicDelegate.isRightPlayer(ePlayer, stack))
                onValidPlayerWornTick(ePlayer);
        }
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity living) {
        return living instanceof PlayerEntity && relicDelegate.isRightPlayer((PlayerEntity) living, stack);
    }

    public void onValidPlayerWornTick(PlayerEntity player) {}
}
