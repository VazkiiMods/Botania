package vazkii.botania.common.network;

public enum EffectType {
	PAINT_LENS(1), // Arg: EnumDyeColor
	ARENA_INDICATOR(0),
	ITEM_SMOKE(2), // Arg: Entity ID, number of particles
	SPARK_NET_INDICATOR(2), // Arg: Entity ID from, Entity ID towards
	SPARK_MANA_FLOW(3), // Arg: Entity ID from, Entity ID towards, color
	ENCHANTER_DESTROY(0),
	BLACK_LOTUS_DISSOLVE(0),
	TERRA_PLATE(1), // Arg: Completion proportion (transmuted from float)
	FLUGEL_EFFECT(1), // Arg: Entity ID
	PARTICLE_BEAM(3), // Args: dest xyz
	DIVA_EFFECT(1), // Arg: Entity ID
	HALO_CRAFT(1), // Arg: Entity ID
	AVATAR_TORNADO_JUMP(1), // Arg: Entity ID
	AVATAR_TORNADO_BOOST(1), // Arg: Entity ID
	;

	public final int argCount;

	EffectType(int argCount) {
		this.argCount = argCount;
	}
}
