package vazkii.botania.common.integration.curios;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.relic.ItemRelic;

public class RelicCurio extends BaseCurio{
    private final ItemRelic relicDelegate;
    public RelicCurio(ItemStack stack, ItemRelic relicDelegate) {
        super(stack);
        this.relicDelegate = relicDelegate;
    }

    @Override
    public void onCurioTick(String identifier, EntityLivingBase living) {
        if(living instanceof EntityPlayer) {
            EntityPlayer ePlayer = (EntityPlayer) living;
            relicDelegate.updateRelic(stack, ePlayer);
            if(relicDelegate.isRightPlayer(ePlayer, stack))
                onValidPlayerWornTick(ePlayer);
        }
    }

    @Override
    public boolean canEquip(String identifier, EntityLivingBase living) {
        return living instanceof EntityPlayer && relicDelegate.isRightPlayer((EntityPlayer) living, stack);
    }

    public void onValidPlayerWornTick(EntityPlayer player) {}
}
