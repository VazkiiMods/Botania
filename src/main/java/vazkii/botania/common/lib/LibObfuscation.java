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

	// EntityHorse
	public static final String[] INIT_HORSE_CHEST = { "initHorseChest", "func_110226_cD", "dJ" };

	// RenderManager
	public static final String[] RENDERPOSX = { "renderPosX", "field_78725_b", "o" };
	public static final String[] RENDERPOSY = { "renderPosY", "field_78726_c", "p" };
	public static final String[] RENDERPOSZ = { "renderPosZ", "field_78723_d", "q" };

	// EntityItem
	public static final String[] AGE = { "age", "field_70292_b", "d" };
	public static final String[] PICKUP_DELAY = { "delayBeforeCanPickup", "field_145804_b", "e" };

	// ParticleManager
	public static final String[] PARTICLE_TEXTURES = { "particleTextures", "field_110737_b", "b" };

	// EntityAINearestAttackableTarget
	public static final String[] TARGET_CLASS = { "targetClass", "field_75307_b", "a" };
	public static final String[] TARGET_ENTITY_SELECTOR = { "targetEntitySelector", "field_82643_g", "c" };

	// EntityAIAvoidEntity
	public static final String[] TARGET_ENTITY_CLASS = { "classToAvoid", "field_181064_i", "i" };

	// EntityCreeper
	public static final String[] TIME_SINCE_IGNITED = {  "timeSinceIgnited", "field_70833_d", "bw" };

	// Entity
	public static final String[] IS_IMMUNE_TO_FIRE = { "isImmuneToFire", "field_70178_ae", "Y" };

	// ItemBlockSpecial
	public static final String[] REED_ITEM = { "block", "field_150935_a", "a" };

	// EntityAnimal
	public static final String[] IN_LOVE = { "inLove", "field_70881_d", "bv" };

	// EntityLivingBase
	public static final String[] ITEM_IN_USE_COUNT = { "activeItemStackUseCount", "field_184628_bn", "bn" };

	// EntityHorse
	public static final String[] HORSE_JUMP_STRENGTH = { "horseJumpStrength", "field_110271_bv", "bC" };

	// PlayerControllerMP
	public static final String[] NET_CLIENT_HANDLER = { "netClientHandler", "field_78774_b", "b" };

	// MobSpawnerBaseLogic
	public static final String[] IS_ACTIVATED = { "isActivated" ,"func_98279_f", "h" };
	public static final String[] SPAWN_RANGE = { "spawnRange", "field_98290_m", "l" };
	public static final String[] SPAWN_COUNT = { "spawnCount", "field_98294_i", "h" };
	public static final String[] MAX_NEARBY_ENTITIES = { "maxNearbyEntities", "field_98292_k", "j" };
	public static final String[] MAX_SPAWN_DELAY = { "maxSpawnDelay", "field_98293_h", "g" };
	public static final String[] MIN_SPAWN_DELAY = { "minSpawnDelay", "field_98283_g", "f" };
	public static final String[] POTENTIAL_ENTITY_SPAWNS = { "minecartToSpawn", "field_98285_e", "b" };
	public static final String[] SPAWN_DELAY = { "spawnDelay", "field_98286_b", "a" };
	public static final String[] PREV_MOB_ROTATION = { "prevMobRotation", "field_98284_d", "e" };
	public static final String[] MOB_ROTATION = { "mobRotation", "field_98287_c", "d" };
	public static final String[] RANDOM_ENTITY = { "randomEntity", "field_98282_f", "c" };

	// GuiIngame
	public static final String[] REMAINING_HIGHLIGHT_TICKS = { "remainingHighlightTicks", "field_92017_k", "q" };

	// EntityThrowable
	public static final String[] THROWER = { "thrower", "field_70192_c", "h" };

	// GuiContainer
	public static final String[] THE_SLOT = { "theSlot", "field_147006_u", "u" };

	// GuiChat
	public static final String[] INPUT_FIELD = { "inputField", "field_146415_a", "a" };
	public static final String[] COMPLETE_FLAG = { "waitingOnAutocomplete", "field_146414_r", "r" }; // todo 1.9

	// Entityliving
	public static final String[] GET_LIVING_SOUND = { "getAmbientSound", "func_184639_G", "G" };
	
	// RenderGlobal
	public static final String[] STAR_GL_CALL_LIST = { "starGLCallList", "field_72772_v", "p" };
	public static final String[] STAR_VBO = { "starVBO", "field_175013_s", "t" };
	public static final String[] GL_SKY_LIST = { "glSkyList", "field_72771_w", "q" };
	public static final String[] SKY_VBO = { "skyVBO", "field_175012_t", "u" };

}
