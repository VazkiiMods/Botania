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

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
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

	private static void deleteProgram(int id) {
		if (id != 0) {
			GlStateManager.deleteProgram(id);
		}
	}

	@SuppressWarnings("deprecation")
	public static void initShaders() {
		// Can be null when running datagenerators due to the unfortunate time we call this
		if (Minecraft.getInstance() != null
			&& Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
					(IResourceManagerReloadListener) manager -> {
						deleteProgram(pylonGlow);
						pylonGlow = 0;
						deleteProgram(enchanterRune);
						enchanterRune = 0;
						deleteProgram(manaPool);
						manaPool = 0;
						deleteProgram(doppleganger);
						doppleganger = 0;
						deleteProgram(halo);
						halo = 0;
						deleteProgram(dopplegangerBar);
						dopplegangerBar = 0;
						deleteProgram(terraPlateRune);
						terraPlateRune = 0;
						deleteProgram(filmGrain);
						filmGrain = 0;
						deleteProgram(gold);
						gold = 0;
						deleteProgram(categoryButton);
						categoryButton = 0;
						deleteProgram(alpha);
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
		GlStateManager.useProgram(shader);

		if(shader != 0) {
		    int time = GlStateManager.getUniformLocation(shader, "time");
		    GlStateManager.uniform1(time, ClientTickHandler.ticksInGame);

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
			vertId = createShader(manager, vert, GL20.GL_VERTEX_SHADER);
		if(frag != null)
			fragId = createShader(manager, frag, GL20.GL_FRAGMENT_SHADER);

		program = GlStateManager.createProgram();
		if(program == 0)
			return 0;

		if(vert != null)
			GlStateManager.attachShader(program, vertId);
		if(frag != null)
			GlStateManager.attachShader(program, fragId);

		GlStateManager.linkProgram(program);
		if (GlStateManager.getProgram(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			Botania.LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", vert, frag);
			Botania.LOGGER.warn(GlStateManager.getProgramInfoLog(program, 32768));
			return 0;
		}

		// Free the shader immediately, it will stay alive until the program is deleted
		GlStateManager.deleteShader(vertId);
		GlStateManager.deleteShader(fragId);

		return program;
	}

	private static int createShader(IResourceManager manager, String filename, int shaderType){
		int shader = 0;
		try {
		    shader = GlStateManager.createShader(shaderType);

			if(shader == 0)
				return 0;

			GlStateManager.shaderSource(shader, readFileAsString(manager, filename));
			GlStateManager.compileShader(shader);

			if (GlStateManager.getShader(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				String s1 = StringUtils.trim(GlStateManager.getShaderInfoLog(shader, 32768));
				throw new IOException("Couldn't compile " + filename + ": " + s1);
			}

			return shader;
		} catch(Exception e) {
			GlStateManager.deleteShader(shader);
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
