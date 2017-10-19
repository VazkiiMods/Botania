/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 9, 2014, 11:20:26 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.fml.common.Loader;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class ShaderHelper {

	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static int pylonGlow = 0;
	public static int enchanterRune = 0;
	public static int manaPool = 0;
	public static int doppleganger = 0;
	public static int halo = 0;
	public static int dopplegangerBar = 0;
	public static int terraPlateRune = 0;
	public static int filmGrain = 0;
	public static int gold = 0;
	public static int categoryButton = 0;
	public static int alpha = 0;
	
	private static boolean hasIncompatibleMods = false;
	private static boolean checkedIncompatibility = false;
	private static boolean lighting;

	private static void deleteShader(int id) {
		if (id != 0) {
			ARBShaderObjects.glDeleteObjectARB(id);
		}
	}

	public static void initShaders() {
		if (Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
			((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(manager -> {
				deleteShader(pylonGlow); pylonGlow = 0;
				deleteShader(enchanterRune); enchanterRune = 0;
				deleteShader(manaPool); manaPool = 0;
				deleteShader(doppleganger); doppleganger = 0;
				deleteShader(halo); halo = 0;
				deleteShader(dopplegangerBar); dopplegangerBar = 0;
				deleteShader(terraPlateRune); terraPlateRune = 0;
				deleteShader(filmGrain); filmGrain = 0;
				deleteShader(gold); gold = 0;
				deleteShader(categoryButton); categoryButton = 0;
				deleteShader(alpha); alpha = 0;

				loadShaders();
			});
		}
	}

	private static void loadShaders() {
		if(!useShaders())
			return;

		pylonGlow = createProgram(null, LibResources.SHADER_PYLON_GLOW_FRAG);
		enchanterRune = createProgram(null, LibResources.SHADER_ENCHANTER_RUNE_FRAG);
		manaPool = createProgram(null, LibResources.SHADER_MANA_POOL_FRAG);
		doppleganger = createProgram(LibResources.SHADER_DOPLLEGANGER_VERT, LibResources.SHADER_DOPLLEGANGER_FRAG);
		halo = createProgram(null, LibResources.SHADER_HALO_FRAG);
		dopplegangerBar = createProgram(null, LibResources.SHADER_DOPLLEGANGER_BAR_FRAG);
		terraPlateRune = createProgram(null, LibResources.SHADER_TERRA_PLATE_RUNE_FRAG);
		filmGrain = createProgram(null, LibResources.SHADER_FILM_GRAIN_FRAG);
		gold = createProgram(null, LibResources.SHADER_GOLD_FRAG);
		categoryButton = createProgram(null, LibResources.SHADER_CATEGORY_BUTTON_FRAG);
		alpha = createProgram(LibResources.SHADER_ALPHA_VERT, LibResources.SHADER_ALPHA_FRAG);
	}

	public static void useShader(int shader, ShaderCallback callback) {
		if(!useShaders())
			return;

		lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();
		
		ARBShaderObjects.glUseProgramObjectARB(shader);

		if(shader != 0) {
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);

			if(callback != null)
				callback.call(shader);
		}
	}

	public static void useShader(int shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		if(lighting)
			GlStateManager.enableLighting();
		useShader(0);
	}

	public static boolean useShaders() {
		return ConfigHandler.useShaders && OpenGlHelper.shadersSupported && checkIncompatibleMods();
	}
	
	private static boolean checkIncompatibleMods() {
		if(!checkedIncompatibility) {
			hasIncompatibleMods = Loader.isModLoaded("optifine");
			checkedIncompatibility = true;
		}
		
		return !hasIncompatibleMods;
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	private static int createProgram(String vert, String frag) {
		int vertId = 0, fragId = 0, program;
		if(vert != null)
			vertId = createShader(vert, VERT);
		if(frag != null)
			fragId = createShader(frag, FRAG);

		program = ARBShaderObjects.glCreateProgramObjectARB();
		if(program == 0)
			return 0;

		if(vert != null)
			ARBShaderObjects.glAttachObjectARB(program, vertId);
		if(frag != null)
			ARBShaderObjects.glAttachObjectARB(program, fragId);

		ARBShaderObjects.glLinkProgramARB(program);
		if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			Botania.LOGGER.error(getLogInfo(program));
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			Botania.LOGGER.error(getLogInfo(program));
			return 0;
		}

		return program;
	}

	private static int createShader(String filename, int shaderType){
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

			return shader;
		}
		catch(Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static String readFileAsString(String filename) throws Exception {
		InputStream in = ShaderHelper.class.getResourceAsStream(filename);

		if(in == null)
			return "";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

}
