package vazkii.botania.forge.internal_caps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.internal_caps.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.forge.CapabilityUtil;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ForgeInternalEntityCapabilities {
	public static final Capability<EthicalComponent> TNT_ETHICAL = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<GhostRailComponent> GHOST_RAIL = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<ItemFlagsComponent> INTERNAL_ITEM = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<KeptItemsComponent> KEPT_ITEMS = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<LooniumComponent> LOONIUM_DROP = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<NarslimmusComponent> NARSLIMMUS = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<TigerseyeComponent> TIGERSEYE = CapabilityManager.get(new CapabilityToken<>() {});

	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void registerCaps(RegisterCapabilitiesEvent evt) {
			evt.register(EthicalComponent.class);
			evt.register(GhostRailComponent.class);
			evt.register(ItemFlagsComponent.class);
			evt.register(KeptItemsComponent.class);
			evt.register(LooniumComponent.class);
			evt.register(NarslimmusComponent.class);
			evt.register(TigerseyeComponent.class);
		}
	}

	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBusEvents {
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<Entity> evt) {
			var entity = evt.getObject();

			if (entity instanceof PrimedTnt tnt) {
				evt.addCapability(prefix("tnt_ethical"), CapabilityUtil.makeProvider(TNT_ETHICAL, new ForgeEthicalCap(tnt)));
			}
			if (entity instanceof AbstractMinecart) {
				evt.addCapability(prefix("ghost_rail"), CapabilityUtil.makeProvider(GHOST_RAIL, new ForgeGhostRailCap()));
			}
			if (entity instanceof ItemEntity) {
				evt.addCapability(prefix("iitem"), CapabilityUtil.makeProvider(INTERNAL_ITEM, new ForgeItemFlagsCap()));
			}
			if (entity instanceof Player) {
				evt.addCapability(prefix("kept_items"), CapabilityUtil.makeProvider(KEPT_ITEMS, new ForgeKeptItemsCap()));
			}
			for (Class<?> clz : SubTileLoonuim.VALID_MOBS) {
				if (clz.isInstance(entity)) {
					evt.addCapability(prefix("loonium_drop"), CapabilityUtil.makeProvider(LOONIUM_DROP, new ForgeLooniumCap()));
					break;
				}
			}
			if (entity instanceof Slime) {
				evt.addCapability(prefix("narslimmus"), CapabilityUtil.makeProvider(NARSLIMMUS, new ForgeNarslimmusCap()));
			}
			if (entity instanceof Creeper) {
				evt.addCapability(prefix("tigerseye_pacified"), CapabilityUtil.makeProvider(TIGERSEYE, new ForgeTigerseyeCap()));
			}
		}
	}

	private static class ForgeEthicalCap extends EthicalComponent implements INBTSerializable<CompoundTag> {
		public ForgeEthicalCap(PrimedTnt entity) {
			super(entity);
		}
	}

	private static class ForgeGhostRailCap extends GhostRailComponent implements INBTSerializable<CompoundTag> {
	}

	private static class ForgeItemFlagsCap extends ItemFlagsComponent implements INBTSerializable<CompoundTag> {
	}

	private static class ForgeKeptItemsCap extends KeptItemsComponent implements INBTSerializable<CompoundTag> {
	}

	private static class ForgeLooniumCap extends LooniumComponent implements INBTSerializable<CompoundTag> {
	}

	private static class ForgeNarslimmusCap extends NarslimmusComponent implements INBTSerializable<CompoundTag> {
	}

	private static class ForgeTigerseyeCap extends TigerseyeComponent implements INBTSerializable<CompoundTag> {
	}

	private ForgeInternalEntityCapabilities() {}
}
