package vazkii.botania.common.integration.curios;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

// Classloading-safe way to attach curio behaviour to our items
public class CurioIntegration extends EquipmentHandler {

	private static class Provider implements ICapabilityProvider {
		private final ICurio curio;
		private final LazyOptional<ICurio> curioCap;

		Provider(ICurio curio) {
			this.curio = curio;
			curioCap = LazyOptional.of(() -> this.curio);
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			return CuriosCapability.ITEM.orEmpty(cap, curioCap);
		}
	}

	@SubscribeEvent
	public static void sendImc(InterModEnqueueEvent evt) {
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("belt"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("body"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("trinket"));
	}

	@Override
	protected LazyOptional<IItemHandlerModifiable> getAllWornItems(LivingEntity living) {
		return CuriosAPI.getCuriosHandler(living).map(h -> {
			IItemHandlerModifiable[] invs = h.getCurioMap().values().toArray(new IItemHandlerModifiable[0]);
			return new CombinedInvWrapper(invs);
		});
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return CuriosAPI.getCurioEquipped(item, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
		return CuriosAPI.getCurioEquipped(pred, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ICapabilityProvider initCap(ItemStack stack) {
		return new Provider(new Wrapper(stack));
	}

	private static class Wrapper implements ICurio {
		private final ItemStack stack;

		Wrapper(ItemStack stack) {
			this.stack = stack;
		}

		private ItemBauble getItem() {
			return (ItemBauble) stack.getItem();
		}

		@Override
		public void onCurioTick(String identifier, LivingEntity entity) {
			getItem().onWornTick(stack, entity);
		}

		@Override
		public void onEquipped(String identifier, LivingEntity entity) {
			getItem().onEquipped(stack, entity);
		}

		@Override
		public void onUnequipped(String identifier, LivingEntity entity) {
			getItem().onUnequipped(stack, entity);
		}

		@Override
		public boolean canEquip(String identifier, LivingEntity entity) {
			return getItem().canEquip(stack, entity);
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
			return getItem().getEquippedAttributeModifiers(stack);
		}

		@Override
		public boolean shouldSyncToTracking(String identifier, LivingEntity entity) {
			return getItem().shouldSyncToTracking(stack, entity);
		}

		@Override
		public void playEquipSound(LivingEntity entity) {
			entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, ModSounds.equipBauble, entity.getSoundCategory(), 0.1F, 1.3F);
		}

		@Override
		public boolean canRightClickEquip() {
			return true;
		}

		@Override
		public boolean hasRender(String identifier, LivingEntity entity) {
			return getItem().hasRender(stack, entity);
		}

		@Override
		public void doRender(String identifier, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			getItem().doRender(stack, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
}
