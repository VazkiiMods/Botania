package vazkii.botania.common.core.handler;

import baubles.api.BaublesApi;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IPixieSpawner;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class PixieHandler {

	private PixieHandler() {}

	private static final Potion[] potions = {
			MobEffects.BLINDNESS,
			MobEffects.WITHER,
			MobEffects.SLOWNESS,
			MobEffects.WEAKNESS
	};

	@SubscribeEvent
	public static void onDamageTaken(LivingHurtEvent event) {
		if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer && event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, IPixieSpawner.class);

			float chance = getChance(stack);
			for (ItemStack element : player.inventory.armorInventory)
				chance += getChance(element);

			IItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++)
				chance += getChance(baubles.getStackInSlot(i));

			if(Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.world);
				pixie.setPosition(player.posX, player.posY + 2, player.posZ);

				if(((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(new PotionEffect(potions[event.getEntityLiving().world.rand.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if(!stack.isEmpty() && stack.getItem() == ModItems.elementiumSword)
					dmg += 2;

				pixie.setProps((EntityLivingBase) event.getSource().getTrueSource(), player, 0, dmg);
				pixie.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(pixie)), null);
				player.world.spawnEntity(pixie);
			}
		}
	}

	private static float getChance(ItemStack stack) {
		if(stack.isEmpty() || !(stack.getItem() instanceof IPixieSpawner))
			return 0F;
		else return ((IPixieSpawner) stack.getItem()).getPixieChance(stack);
	}

}
