/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import org.lwjgl.system.MemoryUtil;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ShaderHelper {
	public enum BotaniaShader {
		PYLON_GLOW(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_PYLON_GLOW_FRAG),
		ENCHANTER_RUNE(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_ENCHANTER_RUNE_FRAG),
		MANA_POOL(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_MANA_POOL_FRAG),
		DOPPLEGANGER(LibResources.SHADER_DOPLLEGANGER_VERT, LibResources.SHADER_DOPLLEGANGER_FRAG),
		HALO(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_HALO_FRAG),
		DOPPLEGANGER_BAR(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_DOPLLEGANGER_BAR_FRAG),
		TERRA_PLATE(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_TERRA_PLATE_RUNE_FRAG),
		FILM_GRAIN(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_FILM_GRAIN_FRAG),
		GOLD(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_GOLD_FRAG),
		ALPHA(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_ALPHA_FRAG);

		public final String vertexShaderPath;
		public final String fragmentShaderPath;

		BotaniaShader(String vertexShaderPath, String fragmentShaderPath) {
			this.vertexShaderPath = vertexShaderPath;
			this.fragmentShaderPath = fragmentShaderPath;
		}
	}

	// Scratch buffer to use for uniforms
	public static final FloatBuffer FLOAT_BUF = MemoryUtil.memAllocFloat(1);
	private static final Map<BotaniaShader, ShaderProgram> PROGRAMS = new EnumMap<>(BotaniaShader.class);

	private static boolean hasIncompatibleMods = false;
	private static boolean checkedIncompatibility = false;

	@SuppressWarnings("deprecation")
	public static void initShaders() {
		// Can be null when running datagenerators due to the unfortunate time we call this
		if (Minecraft.getInstance() != null
				&& Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
					(IResourceManagerReloadListener) manager -> {
						PROGRAMS.values().forEach(ShaderLinkHelper::deleteShader);
						PROGRAMS.clear();
						loadShaders(manager);
					});
		}
	}

	private static void loadShaders(IResourceManager manager) {
		if (!useShaders()) {
			return;
		}

		for (BotaniaShader shader : BotaniaShader.values()) {
			createProgram(manager, shader);
		}
	}

	public static void useShader(BotaniaShader shader, @Nullable ShaderCallback callback) {
		if (!useShaders()) {
			return;
		}

		ShaderProgram prog = PROGRAMS.get(shader);
		if (prog == null) {
			return;
		}

		int program = prog.getProgram();
		ShaderLinkHelper.func_227804_a_(program);

		int time = GlStateManager.getUniformLocation(program, "time");
		GlStateManager.uniform1i(time, ClientTickHandler.ticksInGame);

		if (callback != null) {
			callback.call(program);
		}
	}

	public static void useShader(BotaniaShader shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		ShaderLinkHelper.func_227804_a_(0);
	}

	public static boolean useShaders() {
		return ConfigHandler.CLIENT.useShaders.get() && checkIncompatibleMods();
	}

	private static boolean checkIncompatibleMods() {
		if (!checkedIncompatibility) {
			hasIncompatibleMods = ModList.get().isLoaded("optifine");
			checkedIncompatibility = true;
		}

		return !hasIncompatibleMods;
	}

	private static void createProgram(IResourceManager manager, BotaniaShader shader) {
		try {
			ShaderLoader vert = createShader(manager, shader.vertexShaderPath, ShaderLoader.ShaderType.VERTEX);
			ShaderLoader frag = createShader(manager, shader.fragmentShaderPath, ShaderLoader.ShaderType.FRAGMENT);
			int progId = ShaderLinkHelper.createProgram();
			ShaderProgram prog = new ShaderProgram(progId, vert, frag);
			ShaderLinkHelper.linkProgram(prog);
			PROGRAMS.put(shader, prog);
		} catch (IOException ex) {
			Botania.LOGGER.error("Failed to load program {}", shader.name(), ex);
		}
	}

	private static ShaderLoader createShader(IResourceManager manager, String filename, ShaderLoader.ShaderType shaderType) throws IOException {
		ResourceLocation loc = prefix(filename);
		try (InputStream is = new BufferedInputStream(manager.getResource(loc).getInputStream())) {
			return ShaderLoader.func_216534_a(shaderType, loc.toString(), is);
		}
	}

	private static class ShaderProgram implements IShaderManager {
		private final int program;
		private final ShaderLoader vert;
		private final ShaderLoader frag;

		private ShaderProgram(int program, ShaderLoader vert, ShaderLoader frag) {
			this.program = program;
			this.vert = vert;
			this.frag = frag;
		}

		@Override
		public int getProgram() {
			return program;
		}

		@Override
		public void markDirty() {

		}

		@Override
		public ShaderLoader getVertexShaderLoader() {
			return vert;
		}

		@Override
		public ShaderLoader getFragmentShaderLoader() {
			return frag;
		}
	}

}
