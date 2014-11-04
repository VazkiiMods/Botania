/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 27, 2014, 8:49:01 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemGoldenLaurel extends ItemBauble {

	public ItemGoldenLaurel() {
		super(LibItemNames.GOLDEN_LAUREL);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(LivingDeathEvent event) {
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			ItemStack amulet = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0);

			if(amulet != null && amulet.getItem() == this) {
				event.setCanceled(true);
				player.setHealth(player.getMaxHealth());
				player.addPotionEffect(new PotionEffect(Potion.resistance.id, 300, 6));
				player.addChatMessage(new ChatComponentTranslation("botaniamisc.savedByLaurel"));
				player.worldObj.playSoundAtEntity(player, "botania:goldenLaurel", 1F, 0.3F);
				PlayerHandler.getPlayerBaubles(player).setInventorySlotContents(0, null);
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}
}
