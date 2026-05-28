package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import org.jetbrains.annotations.UnknownNullability;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.common.handler.DogEnabler;
import vazkii.patchouli.client.book.BookEntry;

@Mixin(BookEntry.class)
public class BookEntryMixin {
	@Unique
	@UnknownNullability
	private Boolean botania_cachedDog;

	@WrapOperation(
		method = { "isLocked", "compareTo(Lvazkii/patchouli/client/book/BookEntry;)I" },
		at = @At(value = "FIELD", target = "Lvazkii/patchouli/client/book/BookEntry;locked:Z", opcode = Opcodes.GETFIELD)
	)
	private boolean invertDogLock(BookEntry instance, Operation<Boolean> original) {
		boolean locked = original.call(instance);
		return instance == (Object) this
				? botania_isDog() != locked
				: locked;
	}

	@Unique
	private boolean botania_isDog() {
		if (botania_cachedDog == null) {
			botania_cachedDog = DogEnabler.isDog(((BookEntry) (Object) this).getId());
		}
		return botania_cachedDog;
	}
}
