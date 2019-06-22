package vazkii.botania.common.core.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import vazkii.botania.api.item.IPixieSpawner;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.integration.curios.CurioIntegration;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class PixieHandler {

	private PixieHandler() {}

	private static final Effect[] potions = {
			Effects.BLINDNESS,
			Effects.WITHER,
			Effects.SLOWNESS,
			Effects.WEAKNESS
	};

	@SubscribeEvent
	public static void onDamageTaken(LivingHurtEvent event) {
		if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, IPixieSpawner.class);

			float chance = getChance(stack);
			for (ItemStack element : player.inventory.armorInventory)
				chance += getChance(element);

			if(Botania.curiosLoaded) {
				LazyOptional<IItemHandlerModifiable> cap = CurioIntegration.getAllCurios(player);
				if(cap.isPresent()) {
					IItemHandlerModifiable handler = cap.orElseThrow(NullPointerException::new);
					for(int i = 0; i < handler.getSlots(); i++)
						chance += getChance(handler.getStackInSlot(i));
				}
			}

			if(Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.world);
				pixie.setPosition(player.posX, player.posY + 2, player.posZ);

				if(((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(new EffectInstance(potions[event.getEntityLiving().world.rand.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if(!stack.isEmpty() && stack.getItem() == ModItems.elementiumSword)
					dmg += 2;

				pixie.setProps((LivingEntity) event.getSource().getTrueSource(), player, 0, dmg);
				pixie.onInitialSpawn(player.world, player.world.getDifficultyForLocation(new BlockPos(pixie)),
						SpawnReason.EVENT, null, null);
				player.world.addEntity(pixie);
			}
		}
	}

	private static float getChance(ItemStack stack) {
		if(stack.isEmpty() || !(stack.getItem() instanceof IPixieSpawner))
			return 0F;
		else return ((IPixieSpawner) stack.getItem()).getPixieChance(stack);
	}

}
