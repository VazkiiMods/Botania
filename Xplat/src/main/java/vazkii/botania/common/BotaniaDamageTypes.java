package vazkii.botania.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaDamageTypes {
	public static final ResourceKey<DamageType> PLAYER_ATTACK_ARMOR_PIERCING =
			ResourceKey.create(Registries.DAMAGE_TYPE, prefix("player_attack_armor_piercing"));
}
