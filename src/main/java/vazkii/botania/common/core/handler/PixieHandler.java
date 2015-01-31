package vazkii.botania.common.core.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.api.item.IPixieSpawner;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PixieHandler {

	@SubscribeEvent
	public void onDamageTaken(LivingHurtEvent event) {
		if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack stack = player.getCurrentEquippedItem();

			float chance = getChance(stack);
			for (ItemStack element : player.inventory.armorInventory)
				chance += getChance(element);

			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			for(int i = 0; i < baubles.getSizeInventory(); i++)
				chance += getChance(baubles.getStackInSlot(i));

			if(Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.worldObj);
				pixie.setPosition(player.posX, player.posY + 2, player.posZ);

				if(((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					int[] potions = new int[] {
							Potion.blindness.id,
							Potion.wither.id,
							Potion.moveSlowdown.id,
							Potion.weakness.id
					};
					pixie.setApplyPotionEffect(new PotionEffect(potions[event.entity.worldObj.rand.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if(stack != null && stack.getItem() == ModItems.elementiumSword)
					dmg += 2;

				pixie.setProps((EntityLivingBase) event.source.getEntity(), player, 0, dmg);
				player.worldObj.spawnEntityInWorld(pixie);
			}
		}
	}

	float getChance(ItemStack stack) {
		if(stack == null)
			return 0F;

		Item item = stack.getItem();
		if(item instanceof IPixieSpawner)
			return ((IPixieSpawner) item).getPixieChance(stack);

		return 0F;
	}

}
