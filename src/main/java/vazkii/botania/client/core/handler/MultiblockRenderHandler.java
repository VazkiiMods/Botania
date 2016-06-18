/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 7:28:29 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.block.ModBlocks;

public final class MultiblockRenderHandler {

	private static final MultiblockBlockAccess blockAccess = new MultiblockBlockAccess();
	private static int dimension;

	public static boolean rendering = false;
	public static MultiblockSet currentMultiblock;
	public static BlockPos anchor;
	public static EnumFacing angle;

	static {
		// todo 1.8.8 temporary shim, because cannot renderBlockBrightness directly, see MinecraftForge issue 2353
		IMultiblockRenderHook.renderHooks.put(ModBlocks.pylon, new IMultiblockRenderHook() {
			@Override
			public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, IBlockState state, MultiblockComponent comp) {
				// Steal itemstack model since it has the proper group visibilities configured
				ItemStack stack = new ItemStack(ModBlocks.pylon, 1, state.getBlock().getMetaFromState(state));
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
				GlStateManager.scale(0.65F, 0.65, 0.65F);
				GlStateManager.translate(0.5F, -0.75F, 0.5F);
				Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, false);
			}

			@Override
			public boolean needsTranslate(IBlockState state) {
				return true;
			}
		});
	}

	public static void setMultiblock(MultiblockSet set) {
		currentMultiblock = set;
		anchor = null;
		angle = EnumFacing.SOUTH;

		Minecraft mc = Minecraft.getMinecraft();
		if(mc.theWorld != null)
			dimension = mc.theWorld.provider.getDimension();
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer != null && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && (!mc.thePlayer.isSneaking() || anchor != null)) {
			renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if(currentMultiblock != null && anchor == null && event.getEntityPlayer() == Minecraft.getMinecraft().thePlayer) {
			anchor = event.getPos();
			angle = event.getEntityPlayer().getHorizontalFacing();
			event.setCanceled(true);
		}
	}

	private void renderPlayerLook(EntityPlayer player, RayTraceResult src) {
		if(currentMultiblock != null && dimension == player.worldObj.provider.getDimension()) {
			BlockPos anchorPos = anchor != null ? anchor : src.getBlockPos();

			GlStateManager.pushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			rendering = true;
			Multiblock mb = anchor != null ? currentMultiblock.getForFacing(angle) : currentMultiblock.getForEntity(player);
			boolean didAny = false;

			blockAccess.update(player.worldObj, mb, anchorPos);

			ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
				int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
				ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
			});

			for(MultiblockComponent comp : mb.getComponents()) {
				if(renderComponentInWorld(player.worldObj, mb, comp, anchorPos))
					didAny = true;
			}

			ShaderHelper.releaseShader();

			rendering = false;
			GL11.glPopAttrib();
			GlStateManager.popMatrix();
			
			if(!didAny) {
				setMultiblock(null);
				player.addChatComponentMessage(new TextComponentTranslation("botaniamisc.structureComplete").setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
		}
	}

	private boolean renderComponentInWorld(World world, Multiblock mb, MultiblockComponent comp, BlockPos anchorPos) {
		double renderPosX, renderPosY, renderPosZ;

		try {
			renderPosX = (double) ClientMethodHandles.renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosY = (double) ClientMethodHandles.renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosZ = (double) ClientMethodHandles.renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
		} catch (Throwable t) {
			return true;
		}

		BlockPos pos = comp.getRelativePosition();
		BlockPos pos_ = pos.add(anchorPos);
		if(anchor != null && comp.matches(world, pos_))
			return false;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepth();
		doRenderComponent(mb, comp, pos_);
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderMultiblockOnPage(Multiblock mb) {
		GlStateManager.translate(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.offPos);
		for(MultiblockComponent comp : mb.getComponents()) {
			BlockPos pos = comp.getRelativePosition();
			doRenderComponent(mb, comp, pos.add(mb.offPos));
		}
	}

	private static void doRenderComponent(Multiblock mb, MultiblockComponent comp, BlockPos pos) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		IBlockState state = comp.getBlockState();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if(state == null)
			return;
		if(IMultiblockRenderHook.renderHooks.containsKey(state.getBlock())) {
			GlStateManager.color(1F, 1F, 1F, 1F);
			IMultiblockRenderHook renderHook = IMultiblockRenderHook.renderHooks.get(state.getBlock());
			if(renderHook.needsTranslate(state)) {
				GlStateManager.translate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			}
			renderHook.renderBlockForMultiblock(blockAccess, mb, state, comp);
		}
		else {
			BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
			GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
			GlStateManager.color(1, 1, 1, 1);
			brd.renderBlockBrightness(state, 1.0F);
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
}
