/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 23, 2019, 16:41]
 */
package vazkii.botania.common.capability;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleCapProvider<C> implements ICapabilityProvider {
	private final C capInstance;
	private final LazyOptional<C> capOptional;
	
	private final Capability<C> capability;
	
	public SimpleCapProvider(Capability<C> capability, C capInstance) {
		this.capability = capability;
		this.capInstance = capInstance;
		this.capOptional = LazyOptional.of(() -> this.capInstance);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return capability.orEmpty(cap, capOptional);
	}

	public static <C> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<C> cap, C capInstance) {
		SimpleCapProvider<C> provider = new SimpleCapProvider<>(cap, capInstance);
		event.addCapability(key, provider);
		event.addListener(provider.capOptional::invalidate);
	}
}
