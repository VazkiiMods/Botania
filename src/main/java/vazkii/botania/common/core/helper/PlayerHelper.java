package vazkii.botania.common.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class PlayerHelper {

    public static boolean hasAnyHeldItem(EntityPlayer player) {
        return player.getHeldItemMainhand() != null || player.getHeldItemOffhand() != null;
    }

    public static boolean hasHeldItem(EntityPlayer player, Item item) {
        return (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == item)
                || (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == item);
    }

    public static boolean hasHeldItemClass(EntityPlayer player, Item template) {
        return hasHeldItemClass(player, template.getClass());
    }

    public static boolean hasHeldItemClass(EntityPlayer player, Class<?> template) {
        return (player.getHeldItemMainhand() != null && template.isAssignableFrom(player.getHeldItemMainhand().getItem().getClass()))
                || (player.getHeldItemOffhand() != null && template.isAssignableFrom(player.getHeldItemOffhand().getItem().getClass()));
    }

    public static ItemStack getFirstHeldItem(EntityPlayer player, Item item) {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        if(main != null && item == main.getItem()) {
            return main;
        } else if(offhand != null && item == offhand.getItem()) {
            return offhand;
        } else return null;
    }

    public static ItemStack getFirstHeldItemClass(EntityPlayer player, Class<?> template) {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        if(main != null && template.isAssignableFrom(main.getItem().getClass())) {
            return main;
        } else if(offhand != null && template.isAssignableFrom(offhand.getItem().getClass())) {
            return offhand;
        } else return null;
    }

    private PlayerHelper() {}
}
