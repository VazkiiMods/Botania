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
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.shaders.Effect;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.ProgramManager;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import org.lwjgl.system.MemoryUtil;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;

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
		ENCHANTER_RUNE(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_ENCHANTER_RUNE_FRAG),
		DOPPLEGANGER(LibResources.SHADER_DOPLLEGANGER_VERT, LibResources.SHADER_DOPLLEGANGER_FRAG),
		HALO(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_HALO_FRAG),
		DOPPLEGANGER_BAR(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_DOPLLEGANGER_BAR_FRAG),
		TERRA_PLATE(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_TERRA_PLATE_RUNE_FRAG),
		FILM_GRAIN(LibResources.SHADER_PASSTHROUGH_VERT, LibResources.SHADER_FILM_GRAIN_FRAG),
		;

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
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			private final PreparableReloadListener inner = (ResourceManagerReloadListener) manager -> {
				PROGRAMS.values().forEach(ProgramManager::releaseProgram);
				PROGRAMS.clear();
				loadShaders(manager);
			};

			@Override
			public ResourceLocation getFabricId() {
				return prefix("shader_loader");
			}

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
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

		int program = prog.getId();
		ProgramManager.glUseProgram(program);

		int time = GlStateManager._glGetUniformLocation(program, "time");
		GlStateManager._glUniform1i(time, ClientTickHandler.ticksInGame);

		if (callback != null) {
			callback.call(program);
		}
	}

	public static void useShader(BotaniaShader shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		// todo 1.17 ProgramManager.glUseProgram(0);
	}

	public static boolean useShaders() {
		return false;
		// todo 1.17 return ConfigHandler.CLIENT.useShaders.getValue() && checkIncompatibleMods();
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
			Program vert = createShader(manager, shader.vertexShaderPath, Program.Type.VERTEX);
			Program frag = createShader(manager, shader.fragmentShaderPath, Program.Type.FRAGMENT);
			int progId = ProgramManager.createProgram();
			ShaderProgram prog = new ShaderProgram(progId, vert, frag);
			// todo 1.17
			// ProgramManager.linkProgram(prog);
			// PROGRAMS.put(shader, prog);
		} catch (IOException ex) {
			Botania.LOGGER.error("Failed to load program {}", shader.name(), ex);
		}
	}

	private static Program createShader(ResourceManager manager, String filename, Program.Type shaderType) throws IOException {
		ResourceLocation loc = prefix(filename);
		try (InputStream is = new BufferedInputStream(manager.getResource(loc).getInputStream())) {
			return Program.compileShader(shaderType, loc.toString(), is, shaderType.name().toLowerCase(Locale.ROOT), new GlslPreprocessor() {
				@Override
				public String applyImport(boolean bl, String string) {
					return null;
				}
			});
		}
	}

	private static class ShaderProgram implements Effect {
		private final int program;
		private final Program vert;
		private final Program frag;

		private ShaderProgram(int program, Program vert, Program frag) {
			this.program = program;
			this.vert = vert;
			this.frag = frag;
		}

		@Override
		public int getId() {
			return program;
		}

		@Override
		public void markDirty() {

		}

		@Override
		public Program getVertexProgram() {
			return vert;
		}

		@Override
		public Program getFragmentProgram() {
			return frag;
		}

		@Override
		public void attachToProgram() {

		}
	}

}
