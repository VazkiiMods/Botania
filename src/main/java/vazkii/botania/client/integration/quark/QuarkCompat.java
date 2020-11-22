/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.quark;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.quark.api.IRuneColorProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class QuarkCompat {
	private static Capability<IRuneColorProvider> runeColorCap = null;

	@CapabilityInject(IRuneColorProvider.class)
	public static void runesAvailable(Capability<IRuneColorProvider> runeColorCap) {
		QuarkCompat.runeColorCap = runeColorCap;
		MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, QuarkCompat::itemCapInit);
	}

	private static void itemCapInit(AttachCapabilitiesEvent<ItemStack> evt) {
		evt.addCapability(prefix("headflower_glint"), new Provider(evt.getObject()));
	}

	private static class Provider implements ICapabilityProvider {
		private static final LazyOptional<IRuneColorProvider> COLOR = LazyOptional.of(() -> s -> DyeColor.YELLOW.getId());
		private final ItemStack stack;

		public Provider(ItemStack stack) {
			this.stack = stack;
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			CompoundNBT tag = stack.getTag();
			if (tag != null && tag.getBoolean(ContributorFancinessHandler.TAG_HEADFLOWER)) {
				return runeColorCap.orEmpty(cap, COLOR);
			}
			return LazyOptional.empty();
		}
	}
}
