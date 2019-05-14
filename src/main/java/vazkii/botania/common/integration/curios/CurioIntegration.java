package vazkii.botania.common.integration.curios;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.IRegistryDelegate;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemOdinRing;
import vazkii.botania.common.item.relic.ItemThorRing;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

// Classloading-safe way to attach curio behaviour to our items
public class CurioIntegration {
    private static final ResourceLocation KEY = new ResourceLocation(LibMisc.MOD_ID, "curio");

    private static class Provider implements ICapabilityProvider {
        private final ICurio curio;
        private final LazyOptional<ICurio> curioCap;

        public Provider(ICurio curio) {
            this.curio = curio;
            curioCap = LazyOptional.of(() -> this.curio);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
            return CuriosCapability.ITEM.orEmpty(cap, curioCap);
        }
    }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("belt"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("body"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("trinket"));
    }

    public static LazyOptional<IItemHandlerModifiable> getAllCurios(EntityLivingBase living) {
        return CuriosAPI.getCuriosHandler(living).map(h -> {
            IItemHandlerModifiable[] invs = h.getCurioMap().values().toArray(new IItemHandlerModifiable[0]);
            return new CombinedInvWrapper(invs);
        });
    }

    // Convenience methods, also avoids referencing FinderData class in common code
    public static ItemStack findOrEmpty(Item item, EntityLivingBase living) {
        CuriosAPI.FinderData result = CuriosAPI.getCurioEquipped(item, living);
        return result == null ? ItemStack.EMPTY : result.getStack();
    }

    public static ItemStack findOrEmpty(Predicate<ItemStack> pred, EntityLivingBase living) {
        CuriosAPI.FinderData result = CuriosAPI.getCurioEquipped(pred, living);
        return result == null ? ItemStack.EMPTY : result.getStack();
    }

    public static void init() {
        FACTORIES.put(ModItems.auraRing.delegate, ItemAuraRing.Curio::new);
        FACTORIES.put(ModItems.auraRingGreater.delegate, ItemGreaterAuraRing.Curio::new);
        FACTORIES.put(ModItems.balanceCloak.delegate, ItemHolyCloak.Curio::new);
        FACTORIES.put(ModItems.bloodPendant.delegate, ItemBloodPendant.Curio::new);
        FACTORIES.put(ModItems.divaCharm.delegate, ItemDivaCharm.Curio::new);
        FACTORIES.put(ModItems.dodgeRing.delegate, ItemDodgeRing.Curio::new);
        FACTORIES.put(ModItems.flightTiara.delegate, ItemFlightTiara.Curio::new);
        FACTORIES.put(ModItems.goddessCharm.delegate, ItemGoddessCharm.Curio::new);
        FACTORIES.put(ModItems.holyCloak.delegate, ItemHolyCloak.Curio::new);
        FACTORIES.put(ModItems.icePendant.delegate, ItemIcePendant.Curio::new);
        FACTORIES.put(ModItems.invisibilityCloak.delegate, ItemInvisibilityCloak.Curio::new);
        FACTORIES.put(ModItems.itemFinder.delegate, ItemItemFinder.Curio::new);
        FACTORIES.put(ModItems.knockbackBelt.delegate, ItemKnockbackBelt.Curio::new);
        FACTORIES.put(ModItems.lavaPendant.delegate, ItemLavaPendant.Curio::new);
        FACTORIES.put(ModItems.lokiRing.delegate, s -> new ItemLokiRing.Curio(s, ((ItemLokiRing) ModItems.lokiRing).getDummy()));
        FACTORIES.put(ModItems.magnetRing.delegate, ItemMagnetRing.Curio::new);
        FACTORIES.put(ModItems.magnetRingGreater.delegate, ItemMagnetRing.Curio::new);
        FACTORIES.put(ModItems.manaRing.delegate, BaseCurio::new);
        FACTORIES.put(ModItems.manaRingGreater.delegate, BaseCurio::new);
        FACTORIES.put(ModItems.miningRing.delegate, ItemMiningRing.Curio::new);
        FACTORIES.put(ModItems.odinRing.delegate, s -> new ItemOdinRing.Curio(s, ((ItemOdinRing) ModItems.odinRing).getDummy()));
        FACTORIES.put(ModItems.pixieRing.delegate, BaseCurio::new);
        FACTORIES.put(ModItems.reachRing.delegate, ItemReachRing.Curio::new);
        FACTORIES.put(ModItems.superLavaPendant.delegate, ItemSuperLavaPendant.Curio::new);
        FACTORIES.put(ModItems.superTravelBelt.delegate, ItemTravelBelt.Curio::new);
        FACTORIES.put(ModItems.swapRing.delegate, ItemSwapRing.Curio::new);
        FACTORIES.put(ModItems.thirdEye.delegate, ItemThirdEye.Curio::new);
        FACTORIES.put(ModItems.tinyPlanet.delegate, ItemTinyPlanet.Curio::new);
        FACTORIES.put(ModItems.thorRing.delegate, s -> new RelicCurio(s, ((ItemThorRing) ModItems.thorRing).getDummy()));
        FACTORIES.put(ModItems.travelBelt.delegate, ItemTravelBelt.Curio::new);
        FACTORIES.put(ModItems.unholyCloak.delegate, ItemHolyCloak.Curio::new);
        FACTORIES.put(ModItems.waterRing.delegate, ItemWaterRing.Curio::new);
        for (Item i : ModItems.cosmetics.values()) {
            FACTORIES.put(i.delegate, ItemBaubleCosmetic.Curio::new);
        }
    }

    private static final Map<IRegistryDelegate<Item>, Function<ItemStack, ICurio>> FACTORIES = new HashMap<>();

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();
        Function<ItemStack, ICurio> factory = FACTORIES.get(stack.getItem().delegate);
        if (factory != null) {
            evt.addCapability(KEY, new Provider(factory.apply(stack)));
        }
    }
}
