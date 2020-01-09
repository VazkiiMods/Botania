package vazkii.botania.common.item.material;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.dimension.EndDimension;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemEnderAir extends ItemMod {
    public ItemEnderAir(Properties props) {
        super(props);
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        boolean correctStack = !stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE;
        boolean ender = event.getWorld().getDimension() instanceof EndDimension;

        if(correctStack && ender) {
            if (event.getWorld().isRemote) {
                event.getEntityPlayer().swingArm(event.getHand());
            } else {
                ItemStack stack1 = new ItemStack(ModItems.enderAirBottle);

                ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), stack1);

                stack.shrink(1);

                event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1F);
            }

            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.SUCCESS);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(!player.abilities.isCreativeMode)
            stack.shrink(1);

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if(!world.isRemote) {
            EntityEnderAirBottle b = new EntityEnderAirBottle(player, world);
            b.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
            world.addEntity(b);
        }
        else player.swingArm(hand);
        return ActionResult.success(stack);
    }
}
