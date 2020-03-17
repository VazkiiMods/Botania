package vazkii.botania.common.core.loot;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DisposeModifier extends LootModifier {
		protected DisposeModifier(ILootCondition[] conditions) {
				super(conditions);
		}

		@SubscribeEvent
		public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
			evt.getRegistry().register(new Serializer().setRegistryName(prefix("dispose")));
		}

		@Nonnull
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
				Entity e = context.get(LootParameters.THIS_ENTITY);
				ItemStack tool = context.get(LootParameters.TOOL);
				if (e != null && tool != null && !tool.isEmpty()) {
						ItemElementiumPick.filterDisposable(generatedLoot, e, tool);
				}
				return generatedLoot;
		}

		public static class Serializer extends GlobalLootModifierSerializer<DisposeModifier> {
				@Override
				public DisposeModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
						return new DisposeModifier(conditions);
				}
		}
}
