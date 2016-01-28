/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 17, 2014, 4:52:15 PM (GMT)]
 */
package vazkii.botania.common.lib;

public final class LibObfuscation {

	// RenderManager
	public static final String[] RENDERPOSX = { "renderPosX", "field_78725_b", "o" };
	public static final String[] RENDERPOSY = { "renderPosY", "field_78726_c", "p" };
	public static final String[] RENDERPOSZ = { "renderPosZ", "field_78723_d", "q" };

	// EntityItem
	public static final String[] AGE = { "age", "field_70292_b", "c" };
	public static final String[] PICKUP_DELAY = { "delayBeforeCanPickup", "field_145804_b", "d" };

	// EffectRenderer
	public static final String[] PARTICLE_TEXTURES = new String[] { "particleTextures", "field_110737_b", "b" };

	// EntityAINearestAttackableTarget
	public static final String[] TARGET_CLASS = new String[] { "targetClass", "field_75307_b", "a" };
	public static final String[] TARGET_ENTITY = new String[] { "targetEntity", "field_75309_a", "d" };
	public static final String[] TARGET_ENTITY_SELECTOR = new String[] { "targetEntitySelector", "field_82643_g", "c" };

	// EntityAIAttackOnCollide
	public static final String[] CLASS_TARGET = new String[] { "classTarget", "field_75444_h", "g" };

	// EntityAIAvoidEntity
	public static final String[] TARGET_ENTITY_CLASS = new String[] { "field_181064_i", "i" };

	// EntityCreeper
	public static final String[] TIME_SINCE_IGNITED = new String[] {  "timeSinceIgnited", "field_70833_d", "c" };

	// ThreadDownloadImageData
	public static final String[] TEXTURE_UPLOADED = new String[] { "textureUploaded", "field_110559_g", "n" };
	public static final String[] BUFFERED_IMAGE = new String[] { "bufferedImage", "field_110560_d", "l" };

	// Entity
	public static final String[] IS_IMMUNE_TO_FIRE = new String[] { "isImmuneToFire", "field_70178_ae", "ab" };

	// ItemReed
	public static final String[] REED_ITEM = new String[] { "block", "field_150935_a", "a" };

	// EntityAnimal
	public static final String[] IN_LOVE = new String[] { "inLove", "field_70881_d", "bk" };

	// EntityPlayer
	public static final String[] ITEM_IN_USE = new String[] { "itemInUse", "field_71074_e", "g" };
	public static final String[] ITEM_IN_USE_COUNT = new String[] { "itemInUseCount", "field_71072_f", "h" };

	// EntityHorse
	public static final String[] HORSE_JUMP_STRENGTH = new String[] { "horseJumpStrength", "field_110271_bv", "br" };
	public static final String[] HORSE_CHEST = new String[] { "horseChest", "field_110296_bG", "bC" };

	// PlayerControllerMP
	public static final String[] NET_CLIENT_HANDLER = new String[] { "netClientHandler", "field_78774_b", "b" };
	public static final String[] CURRENT_GAME_TYPE = new String[] { "currentGameType", "field_78779_k", "i" };

	// MobSpawnerBaseLogic
	public static final String[] IS_ACTIVATED = new String[] { "isActivated" ,"func_98279_f", "g" };
	public static final String[] SPAWN_RANGE = new String[] { "spawnRange", "field_98290_m", "m" };
	public static final String[] SPAWN_COUNT = new String[] { "spawnCount", "field_98294_i", "i" };
	public static final String[] MAX_NEARBY_ENTITIES = new String[] { "maxNearbyEntities", "field_98292_k", "k" };
	public static final String[] MAX_SPAWN_DELAY = new String[] { "maxSpawnDelay", "field_98293_h", "h" };
	public static final String[] MIN_SPAWN_DELAY = new String[] { "minSpawnDelay", "field_98283_g", "g" };
	public static final String[] POTENTIAL_ENTITY_SPAWNS = new String[] { "minecartToSpawn", "field_98285_e", "c" }; // Todo 1.8 new name makes no sense -> "minecartToSpawn" see MCP name github issue
	public static final String[] SPAWN_DELAY = new String[] { "spawnDelay", "field_98286_b", "a" };
	public static final String[] PREV_MOB_ROTATION = new String[] { "prevMobRotation", "field_98284_d", "f" };
	public static final String[] MOB_ROTATION = new String[] { "mobRotation", "field_98287_c", "e" };
	public static final String[] GET_ENTITY_TO_SPAWN = new String[] { "getEntityNameToSpawn", "func_98276_e", "f" };
	public static final String[] SPAWN_NEW_ENTITY = new String[] { "spawnNewEntity", "func_180613_a", "a" };

	// GuiIngame
	public static final String[] REMAINING_HIGHLIGHT_TICKS = new String[] { "remainingHighlightTicks", "field_92017_k", "r" };

	// EntityThrowable
	public static final String[] THROWER = new String[] { "thrower", "field_70192_c", "g" };

	// GuiContainer
	public static final String[] THE_SLOT = new String[] { "theSlot", "field_147006_u", "u" };

	// GuiChat
	public static final String[] INPUT_FIELD = new String[] { "inputField", "field_146415_a", "a" };
	public static final String[] COMPLETE_FLAG = new String[] { "waitingOnAutocomplete", "field_146414_r", "r" };

	// Entityliving
	public static final String[] GET_LIVING_SOUND = new String[] { "getLivingSound", "func_70639_aQ", "z" };
	
	// RenderGlobal
	public static final String[] STAR_GL_CALL_LIST = new String[] { "starGLCallList", "field_72772_v", "p" };
	public static final String[] GL_SKY_LIST = new String[] { "glSkyList", "field_72771_w", "q" };
	public static final String[] GL_SKY_LIST2 = new String[] { "glSkyList2", "field_72781_x", "r" };

}
