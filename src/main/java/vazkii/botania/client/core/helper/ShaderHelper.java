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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;

import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class ShaderHelper {
	// Scratch buffer to use for uniforms
	public static final FloatBuffer FLOAT_BUF = MemoryUtil.memAllocFloat(1);
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
			GLX.glDeleteShader(id);
		}
	}

	@SuppressWarnings("deprecation")
	public static void initShaders() {
		// Can be null when running datagenerators due to the unfortunate time we call this
		if (Minecraft.getInstance() != null
			&& Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
					(IResourceManagerReloadListener) manager -> {
						deleteShader(pylonGlow);
						pylonGlow = 0;
						deleteShader(enchanterRune);
						enchanterRune = 0;
						deleteShader(manaPool);
						manaPool = 0;
						deleteShader(doppleganger);
						doppleganger = 0;
						deleteShader(halo);
						halo = 0;
						deleteShader(dopplegangerBar);
						dopplegangerBar = 0;
						deleteShader(terraPlateRune);
						terraPlateRune = 0;
						deleteShader(filmGrain);
						filmGrain = 0;
						deleteShader(gold);
						gold = 0;
						deleteShader(categoryButton);
						categoryButton = 0;
						deleteShader(alpha);
						alpha = 0;

						loadShaders(manager);
					});
		}
	}

	private static void loadShaders(IResourceManager manager) {
		if(!useShaders())
			return;

		pylonGlow = createProgram(manager, null, LibResources.SHADER_PYLON_GLOW_FRAG);
		enchanterRune = createProgram(manager, null, LibResources.SHADER_ENCHANTER_RUNE_FRAG);
		manaPool = createProgram(manager, null, LibResources.SHADER_MANA_POOL_FRAG);
		doppleganger = createProgram(manager, LibResources.SHADER_DOPLLEGANGER_VERT, LibResources.SHADER_DOPLLEGANGER_FRAG);
		halo = createProgram(manager, null, LibResources.SHADER_HALO_FRAG);
		dopplegangerBar = createProgram(manager, null, LibResources.SHADER_DOPLLEGANGER_BAR_FRAG);
		terraPlateRune = createProgram(manager, null, LibResources.SHADER_TERRA_PLATE_RUNE_FRAG);
		filmGrain = createProgram(manager, null, LibResources.SHADER_FILM_GRAIN_FRAG);
		gold = createProgram(manager, null, LibResources.SHADER_GOLD_FRAG);
		categoryButton = createProgram(manager, null, LibResources.SHADER_CATEGORY_BUTTON_FRAG);
		alpha = createProgram(manager, LibResources.SHADER_ALPHA_VERT, LibResources.SHADER_ALPHA_FRAG);
	}

	public static void useShader(int shader, ShaderCallback callback) {
		if(!useShaders())
			return;

		lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();
		GLX.glUseProgram(shader);

		if(shader != 0) {
		    int time = GLX.glGetUniformLocation(shader, "time");
		    GLX.glUniform1i(time, ClientTickHandler.ticksInGame);

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
		// usePostProcess equivalent to hasShaders
		return ConfigHandler.CLIENT.useShaders.get() && GLX.usePostProcess && checkIncompatibleMods();
	}
	
	private static boolean checkIncompatibleMods() {
		if(!checkedIncompatibility) {
			hasIncompatibleMods = ModList.get().isLoaded("optifine");
			checkedIncompatibility = true;
		}
		
		return !hasIncompatibleMods;
	}

	private static int createProgram(IResourceManager manager, String vert, String frag) {
		int vertId = 0, fragId = 0, program;
		if(vert != null)
			vertId = createShader(manager, vert, GLX.GL_VERTEX_SHADER);
		if(frag != null)
			fragId = createShader(manager, frag, GLX.GL_FRAGMENT_SHADER);

		program = GLX.glCreateProgram();
		if(program == 0)
			return 0;

		if(vert != null)
			GLX.glAttachShader(program, vertId);
		if(frag != null)
			GLX.glAttachShader(program, fragId);

		GLX.glLinkProgram(program);
		if (GLX.glGetProgrami(program, GLX.GL_LINK_STATUS) == GL11.GL_FALSE) {
			Botania.LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", vert, frag);
			Botania.LOGGER.warn(GLX.glGetProgramInfoLog(program, 32768));
			return 0;
		}

		return program;
	}

	private static int createShader(IResourceManager manager, String filename, int shaderType){
		int shader = 0;
		try {
		    shader = GLX.glCreateShader(shaderType);

			if(shader == 0)
				return 0;

			GLX.glShaderSource(shader, readFileAsString(manager, filename));
			GLX.glCompileShader(shader);

			if (GLX.glGetShaderi(shader, GLX.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				String s1 = StringUtils.trim(GLX.glGetShaderInfoLog(shader, 32768));
				throw new IOException("Couldn't compile " + filename + ": " + s1);
			}

			return shader;
		} catch(Exception e) {
			GLX.glDeleteShader(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String readFileAsString(IResourceManager manager, String filename) throws Exception {
		InputStream in = manager.getResource(new ResourceLocation(LibMisc.MOD_ID, filename)).getInputStream();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

}
