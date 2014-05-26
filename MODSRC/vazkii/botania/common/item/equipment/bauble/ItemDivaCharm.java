/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 25, 2014, 10:30:39 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemDivaCharm extends ItemBauble implements IManaUsingItem {

	public ItemDivaCharm() {
		super(LibItemNames.DIVA_CHARM);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onEntityDamaged(LivingHurtEvent event) {
		if(event.source.getEntity() instanceof EntityPlayer && event.entityLiving instanceof EntityLiving && Math.random() < 0.3333F) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			ItemStack amulet = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0);
			if(amulet != null && amulet.getItem() == this) {
				final int cost = 250;
				if(ManaItemHandler.requestManaExact(amulet, player, cost, false)) {
					final int range = 20;

					List<IMob> mobs = player.worldObj.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(event.entity.posX - range, event.entity.posY - range, event.entity.posZ - range, event.entity.posX + range, event.entity.posY + range, event.entity.posZ + range));
					if(mobs.size() > 1) {
						SubTileHeiseiDream.brainwashEntity((EntityLiving) event.entityLiving, mobs);
						event.entityLiving.heal(event.entityLiving.getMaxHealth());
						ManaItemHandler.requestManaExact(amulet, player, cost, true);
						player.worldObj.playSoundAtEntity(player, "random.levelup", 1F, 1F);

						double x = event.entityLiving.posX;
						double y = event.entityLiving.posY;
						double z = event.entityLiving.posZ;

						for(int i = 0; i < 50; i++)
							Botania.proxy.sparkleFX(event.entityLiving.worldObj, x + Math.random() * event.entityLiving.width, y + Math.random() * event.entityLiving.height, z + Math.random() * event.entityLiving.width, 1F, 1F, 0.25F, 1F, 3);
					}
				}
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
