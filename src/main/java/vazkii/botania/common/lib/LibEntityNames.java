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

	public static final String MANA_BURST = LibResources.PREFIX_MOD + "manaBurst";
	public static final String SIGNAL_FLARE = LibResources.PREFIX_MOD + "signalFlare";
	public static final String PIXIE = LibResources.PREFIX_MOD + "pixie";
	public static final String FLAME_RING = LibResources.PREFIX_MOD + "flameRing";
	public static final String VINE_BALL = LibResources.PREFIX_MOD + "vineBall";
	public static final String DOPPLEGANGER = LibResources.PREFIX_MOD + "doppleganger";
	public static final String MAGIC_LANDMINE = LibResources.PREFIX_MOD + "magicLandmine";
	public static final String SPARK = LibResources.PREFIX_MOD + "spark";
	public static final String THROWN_ITEM = LibResources.PREFIX_MOD + "thrownItem";
	public static final String MAGIC_MISSILE = LibResources.PREFIX_MOD + "magicMissile";
	public static final String THORN_CHAKRAM = LibResources.PREFIX_MOD + "thornChakram";
	public static final String CORPOREA_SPARK = LibResources.PREFIX_MOD + "corporeaSpark";
	public static final String ENDER_AIR_BOTTLE = LibResources.PREFIX_MOD + "enderAirBottle";
	public static final String POOL_MINECART = LibResources.PREFIX_MOD + "poolMinecart";
	public static final String PINK_WITHER = LibResources.PREFIX_MOD + "pinkWither";
	public static final String PLAYER_MOVER = LibResources.PREFIX_MOD + "playerMover";
	public static final String MANA_STORM = LibResources.PREFIX_MOD + "manaStorm";
	public static final String BABYLON_WEAPON = LibResources.PREFIX_MOD + "babylonWeapon";
	public static final String FALLING_STAR = LibResources.PREFIX_MOD + "fallingStar";

	public static final ResourceLocation MANA_BURST_REGISTRY = makeName("mana_burst");
	public static final ResourceLocation SIGNAL_FLARE_REGISTRY = makeName("signal_flare");
	public static final ResourceLocation PIXIE_REGISTRY = makeName("pixie");
	public static final ResourceLocation FLAME_RING_REGISTRY = makeName("flame_ring");
	public static final ResourceLocation VINE_BALL_REGISTRY = makeName("vine_ball");
	public static final ResourceLocation DOPPLEGANGER_REGISTRY = makeName("doppleganger");
	public static final ResourceLocation MAGIC_LANDMINE_REGISTRY = makeName("magic_landmine");
	public static final ResourceLocation SPARK_REGISTRY = makeName("spark");
	public static final ResourceLocation THROWN_ITEM_REGISTRY = makeName("thrown_item");
	public static final ResourceLocation MAGIC_MISSILE_REGISTRY = makeName("magic_missile");
	public static final ResourceLocation THORN_CHAKRAM_REGISTRY = makeName("thorn_chakram");
	public static final ResourceLocation CORPOREA_SPARK_REGISTRY = makeName("corporea_spark");
	public static final ResourceLocation ENDER_AIR_BOTTLE_REGISTRY = makeName("ender_air_bottle");
	public static final ResourceLocation POOL_MINECART_REGISTRY = makeName("pool_minecart");
	public static final ResourceLocation PINK_WITHER_REGISTRY = makeName("pink_wither");
	public static final ResourceLocation PLAYER_MOVER_REGISTRY = makeName("player_mover");
	public static final ResourceLocation MANA_STORM_REGISTRY = makeName("mana_storm");
	public static final ResourceLocation BABYLON_WEAPON_REGISTRY = makeName("babylon_weapon");
	public static final ResourceLocation FALLING_STAR_REGISTRY = makeName("falling_star");
	
	private static ResourceLocation makeName(String s) {
		return new ResourceLocation(LibMisc.MOD_ID, s);
	}

}
