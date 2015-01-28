/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 28, 2015, 4:27:39 PM (GMT)]
 */
package vazkii.botania.common.achievement;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibAchievementNames;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public final class ModAchievements {

	public static AchievementPage botaniaPage;
	
	public static Achievement flowerPickup;
	public static Achievement lexiconUse;
	public static Achievement daybloomPickup;
	public static Achievement manaPoolPickup;
	public static Achievement endoflamePickup;
	public static Achievement tinyPotatoPet;
	public static Achievement sparkCraft;
	public static Achievement baubleWear;
	public static Achievement manaCookieEat;
	public static Achievement runePickup;
	public static Achievement dirtRodCraft;
	public static Achievement manaBlasterShoot;
	public static Achievement terrasteelPickup;
	public static Achievement terrasteelWeaponCraft;
	public static Achievement elfPortalOpen;
	public static Achievement kekimurusPickup;
	public static Achievement heiseiDreamPickup;
	public static Achievement gaiaGuardianKill;
	public static Achievement spawnerMoverUse;
	public static Achievement tiaraWings;
	
	public static Achievement signalFlareStun;
	public static Achievement l20ShardUse;
	public static Achievement gaiaGuardianNoArmor;
	public static Achievement rankSSPick;
	
	public static Achievement desuGun;
	
	public static void init() {
		flowerPickup = new AchievementMod(LibAchievementNames.FLOWER_PICKUP, 0, 0, new ItemStack(ModBlocks.flower, 1, 6), null);
		lexiconUse = new AchievementMod(LibAchievementNames.LEXICON_USE, 1, 1, ModItems.lexicon, flowerPickup);
		daybloomPickup = new AchievementMod(LibAchievementNames.DAYBLOOM_PICKUP, 3, 1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM), lexiconUse);
		manaPoolPickup = new AchievementMod(LibAchievementNames.MANA_POOL_PICKUP, 3, -1, ModBlocks.pool, daybloomPickup);
		
		endoflamePickup = new AchievementMod(LibAchievementNames.ENDOFLAME_PICKUP, 1, -3, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENDOFLAME), manaPoolPickup);
		tinyPotatoPet = new AchievementMod(LibAchievementNames.TINY_POTATO_PET, 2, -5, ModBlocks.tinyPotato, manaPoolPickup);
		sparkCraft = new AchievementMod(LibAchievementNames.SPARK_CRAFT, 4, -5, ModItems.spark, manaPoolPickup);
		baubleWear = new AchievementMod(LibAchievementNames.BAUBLE_WEAR, 5, -3, ModItems.manaRing, manaPoolPickup);
		manaCookieEat = new AchievementMod(LibAchievementNames.MANA_COOKIE_EAT, 3, -7, ModItems.manaCookie, manaPoolPickup);
		runePickup = new AchievementMod(LibAchievementNames.RUNE_PICKUP, 6, -1, ModBlocks.runeAltar, manaPoolPickup);
		
		dirtRodCraft = new AchievementMod(LibAchievementNames.DIRT_ROD_CRAFT, 8, 0, ModItems.dirtRod, runePickup);
		manaBlasterShoot = new AchievementMod(LibAchievementNames.MANA_BLASTER_SHOOT, 8, -2, ModItems.manaGun, runePickup);
		terrasteelPickup = new AchievementMod(LibAchievementNames.TERRASTEEL_PICKUP, 6, 4, new ItemStack(ModItems.manaResource, 1, 4), runePickup).setSpecial();
		
		terrasteelWeaponCraft = new AchievementMod(LibAchievementNames.TERRASTEEL_WEAPON_CRAFT, 8, 5, ModItems.terraSword, terrasteelPickup); 
		elfPortalOpen = new AchievementMod(LibAchievementNames.ELF_PORTAL_OPEN, 4, 4, ModBlocks.alfPortal, terrasteelPickup).setSpecial();
		
		kekimurusPickup = new AchievementMod(LibAchievementNames.KEKIMURUS_PICKUP, 3, 6, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS), elfPortalOpen);
		heiseiDreamPickup = new AchievementMod(LibAchievementNames.HEISEI_DREAM_PICKUP, 5, 6, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HEISEI_DREAM), elfPortalOpen);
		gaiaGuardianKill = new AchievementMod(LibAchievementNames.GAIA_GUARDIAN_KILL, 2, 4, new ItemStack(ModItems.manaResource, 1, 5), elfPortalOpen).setSpecial();
		
		spawnerMoverUse = new AchievementMod(LibAchievementNames.SPAWNER_MOVER_USE, -1, 5, ModItems.spawnerMover, gaiaGuardianKill);
		tiaraWings = new AchievementMod(LibAchievementNames.TIARA_WINGS, -1, 3, ModItems.flightTiara, gaiaGuardianKill);
		
		signalFlareStun = new AchievementMod(LibAchievementNames.SIGNAL_FLARE_STUN, -3, -3, ModItems.signalFlare, null).setSpecial();
		l20ShardUse = new AchievementMod(LibAchievementNames.L20_SHARD_USE, -5, -1, ModItems.laputaShard, null).setSpecial();
		gaiaGuardianNoArmor = new AchievementMod(LibAchievementNames.GAIA_GUARDIAN_NO_ARMOR, -5, -3, new ItemStack(Items.skull, 1, 3), null).setSpecial();
		rankSSPick = new AchievementMod(LibAchievementNames.RANK_SS_PICK, -3, -1, ModItems.terraPick, null).setSpecial();
		
		ItemStack desu = new ItemStack(ModItems.manaGun);
		desu.setStackDisplayName("desu gun");
		desuGun = new AchievementMod(LibAchievementNames.DESU_GUN, -8, -2, desu, null).setSpecial();
		
		botaniaPage = new AchievementPage(LibMisc.MOD_NAME, AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));
		AchievementPage.registerAchievementPage(botaniaPage);
		
		FMLCommonHandler.instance().bus().register(new AchievementTriggerer());
	}
	
}
