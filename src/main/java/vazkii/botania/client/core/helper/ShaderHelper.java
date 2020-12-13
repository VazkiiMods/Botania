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

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gl.GlProgram;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.client.gl.GlShader;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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

	public static void initShaders() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			private final ResourceReloadListener inner = (SynchronousResourceReloadListener) manager -> {
				PROGRAMS.values().forEach(GlProgramManager::deleteProgram);
				PROGRAMS.clear();
				loadShaders(manager);
			};

			@Override
			public Identifier getFabricId() {
				return prefix("shader_loader");
			}

			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
				return inner.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
			}
		});
	}

	private static void loadShaders(ResourceManager manager) {
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

		int program = prog.getProgramRef();
		GlProgramManager.useProgram(program);

		int time = GlStateManager.getUniformLocation(program, "time");
		GlStateManager.uniform1(time, ClientTickHandler.ticksInGame);

		if (callback != null) {
			callback.call(program);
		}
	}

	public static void useShader(BotaniaShader shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		GlProgramManager.useProgram(0);
	}

	public static boolean useShaders() {
		return ConfigHandler.CLIENT.useShaders.getValue() && checkIncompatibleMods();
	}

	private static boolean checkIncompatibleMods() {
		if (!checkedIncompatibility) {
			hasIncompatibleMods = FabricLoader.getInstance().isModLoaded("optifabric");
			checkedIncompatibility = true;
		}

		return !hasIncompatibleMods;
	}

	private static void createProgram(ResourceManager manager, BotaniaShader shader) {
		try {
			GlShader vert = createShader(manager, shader.vertexShaderPath, GlShader.Type.VERTEX);
			GlShader frag = createShader(manager, shader.fragmentShaderPath, GlShader.Type.FRAGMENT);
			int progId = GlProgramManager.createProgram();
			ShaderProgram prog = new ShaderProgram(progId, vert, frag);
			GlProgramManager.linkProgram(prog);
			PROGRAMS.put(shader, prog);
		} catch (IOException ex) {
			Botania.LOGGER.error("Failed to load program {}", shader.name(), ex);
		}
	}

	private static GlShader createShader(ResourceManager manager, String filename, GlShader.Type shaderType) throws IOException {
		Identifier loc = prefix(filename);
		try (InputStream is = new BufferedInputStream(manager.getResource(loc).getInputStream())) {
			return GlShader.createFromResource(shaderType, loc.toString(), is, shaderType.name().toLowerCase(Locale.ROOT));
		}
	}

	private static class ShaderProgram implements GlProgram {
		private final int program;
		private final GlShader vert;
		private final GlShader frag;

		private ShaderProgram(int program, GlShader vert, GlShader frag) {
			this.program = program;
			this.vert = vert;
			this.frag = frag;
		}

		@Override
		public int getProgramRef() {
			return program;
		}

		@Override
		public void markUniformsDirty() {

		}

		@Override
		public GlShader getVertexShader() {
			return vert;
		}

		@Override
		public GlShader getFragmentShader() {
			return frag;
		}
	}

}
