/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2014, 5:30:03 PM (GMT)]
 */
package vazkii.botania.common.lib;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public final class LibEntityNames {
	public static final ResourceLocation MANA_BURST = makeName("mana_burst");
	public static final ResourceLocation SIGNAL_FLARE = makeName("signal_flare");
	public static final ResourceLocation PIXIE = makeName("pixie");
	public static final ResourceLocation FLAME_RING = makeName("flame_ring");
	public static final ResourceLocation VINE_BALL = makeName("vine_ball");
	public static final ResourceLocation DOPPLEGANGER = makeName("doppleganger");
	public static final ResourceLocation MAGIC_LANDMINE = makeName("magic_landmine");
	public static final ResourceLocation SPARK = makeName("spark");
	public static final ResourceLocation THROWN_ITEM = makeName("thrown_item");
	public static final ResourceLocation MAGIC_MISSILE = makeName("magic_missile");
	public static final ResourceLocation THORN_CHAKRAM = makeName("thorn_chakram");
	public static final ResourceLocation CORPOREA_SPARK = makeName("corporea_spark");
	public static final ResourceLocation ENDER_AIR_BOTTLE = makeName("ender_air_bottle");
	public static final ResourceLocation POOL_MINECART = makeName("pool_minecart");
	public static final ResourceLocation PINK_WITHER = makeName("pink_wither");
	public static final ResourceLocation PLAYER_MOVER = makeName("player_mover");
	public static final ResourceLocation MANA_STORM = makeName("mana_storm");
	public static final ResourceLocation BABYLON_WEAPON = makeName("babylon_weapon");
	public static final ResourceLocation FALLING_STAR = makeName("falling_star");
	
	private static ResourceLocation makeName(String s) {
		return new ResourceLocation(LibMisc.MOD_ID, s);
	}

}
