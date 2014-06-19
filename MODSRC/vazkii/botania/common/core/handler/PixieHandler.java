package vazkii.botania.common.core.handler;

import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PixieHandler {

	@SubscribeEvent
	public void onDamageTaken(LivingHurtEvent event) {
		if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack stack = player.getCurrentEquippedItem();
			
			float chance = getChance(stack);
			for(int i = 0; i < player.inventory.armorInventory.length; i++)
				chance += getChance(player.inventory.armorInventory[i]);
			
			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			for(int i = 0; i < baubles.getSizeInventory(); i++)
				chance += getChance(baubles.getStackInSlot(i));
			
			System.out.println(chance);
			if(Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.worldObj);
				pixie.setPosition(player.posX, player.posY + 2, player.posZ);
				
				float dmg = 4;
				if(stack != null && stack.getItem() == ModItems.elementiumSword)
					dmg += 2;
				
				pixie.setProps((EntityLivingBase) event.source.getEntity(), player, dmg);
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
	
	public static interface IPixieSpawner {
		
		public float getPixieChance(ItemStack stack);
		
	}
	
}
