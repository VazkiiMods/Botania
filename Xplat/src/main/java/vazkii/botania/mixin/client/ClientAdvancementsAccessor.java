package vazkii.botania.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import vazkii.patchouli.client.base.ClientAdvancements;

@Mixin(ClientAdvancements.class)
public interface ClientAdvancementsAccessor {
	@Accessor("gotFirstAdvPacket")
	static void botania_setGotFirstAdvPacket(boolean gotFirstAdvPacket) {
		throw new IllegalStateException();
	}
}
