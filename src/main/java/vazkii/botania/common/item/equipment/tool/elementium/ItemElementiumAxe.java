package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;

import java.util.Random;

public class ItemElementiumAxe extends ItemManasteelAxe {

	public ItemElementiumAxe(Properties props) {
		super(BotaniaAPI.ELEMENTIUM_ITEM_TIER, props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityDrops);
	}

	// Thanks to SpitefulFox for the drop rates
	// https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/com/spiteful/forbidden/FMEventHandler.java

    private void onEntityDrops(LivingDropsEvent event) {
		if(event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
			ItemStack weapon = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
			if(!weapon.isEmpty() && weapon.getItem() == this) {
				Random rand = event.getEntityLiving().world.rand;
				int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, weapon);

				if(event.getEntityLiving() instanceof AbstractSkeleton && rand.nextInt(26) <= 3 + looting)
					addDrop(event, new ItemStack(event.getEntity() instanceof EntityWitherSkeleton ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL));
				else if(event.getEntityLiving() instanceof EntityZombie && !(event.getEntityLiving() instanceof EntityPigZombie) && rand.nextInt(26) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(Items.ZOMBIE_HEAD));
				else if(event.getEntityLiving() instanceof EntityCreeper && rand.nextInt(26) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(Items.CREEPER_HEAD));
				else if(event.getEntityLiving() instanceof EntityPlayer && rand.nextInt(11) <= 1 + looting) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					ItemNBTHelper.setString(stack, "SkullOwner", ((EntityPlayer) event.getEntityLiving()).getGameProfile().getName());
					addDrop(event, stack);
				} else if(event.getEntityLiving() instanceof EntityDoppleganger && rand.nextInt(13) < 1 + looting)
					addDrop(event, new ItemStack(ModItems.gaiaHead));
			}
		}
	}

	private void addDrop(LivingDropsEvent event, ItemStack drop) {
		EntityItem entityitem = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
		entityitem.setPickupDelay(10);
		event.getDrops().add(entityitem);
	}

}
