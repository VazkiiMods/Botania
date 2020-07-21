/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;

import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderTileTinyPotato extends TileEntityRenderer<TileTinyPotato> {
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TINY_POTATO);
	private static final ResourceLocation textureGrayscale = new ResourceLocation(LibResources.MODEL_TINY_POTATO_GS);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TINY_POTATO_HALLOWEEN);
	private final ModelRenderer potatoModel = new ModelRenderer(16, 16, 0, 0);

	public RenderTileTinyPotato(TileEntityRendererDispatcher manager) {
		super(manager);
		potatoModel.addBox(0F, 0F, 0F, 4, 6, 4);
		potatoModel.setRotationPoint(-2F, 18F, -2F);
		potatoModel.setTextureSize(64, 32);
	}

	private static boolean matches(String name, String match) {
		return name.equals(match) || name.startsWith(match + " ");
	}

	private static String removeFromFront(String name, String match) {
		return name.substring(match.length()).trim();
	}

	private static Pair<ShaderHelper.BotaniaShader, String> stripShaderName(String name) {
		if (matches(name, "gaia")) {
			return Pair.of(ShaderHelper.BotaniaShader.DOPPLEGANGER, removeFromFront(name, "gaia"));
		} else if (matches(name, "hot")) {
			return Pair.of(ShaderHelper.BotaniaShader.HALO, removeFromFront(name, "hot"));
		} else if (matches(name, "magic")) {
			return Pair.of(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, removeFromFront(name, "magic"));
		} else if (matches(name, "gold")) {
			return Pair.of(ShaderHelper.BotaniaShader.GOLD, removeFromFront(name, "gold"));
		} else if (matches(name, "snoop")) {
			return Pair.of(ShaderHelper.BotaniaShader.TERRA_PLATE, removeFromFront(name, "snoop"));
		} else {
			return Pair.of(null, name);
		}
	}

	private static RenderType getRenderLayer(@Nullable ShaderHelper.BotaniaShader shader, String name) {
		RenderType base;
		if ("kyle hyde".equals(name)) {
			base = RenderType.getEntitySolid(textureGrayscale);
		} else if (ClientProxy.dootDoot) {
			base = RenderType.getEntitySolid(textureHalloween);
		} else {
			base = RenderType.getEntitySolid(texture);
		}
		return shader == null || !ShaderHelper.useShaders() ? base : new ShaderWrappedRenderLayer(shader, null, base);

	}

	@Override
	public void render(@Nonnull TileTinyPotato potato, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();

		String name = potato.name.getString().toLowerCase().trim();
		Pair<ShaderHelper.BotaniaShader, String> shaderStrippedName = stripShaderName(name);
		ShaderHelper.BotaniaShader shader = shaderStrippedName.getFirst();
		name = shaderStrippedName.getSecond();
		RenderType layer = getRenderLayer(shader, name);

		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);

		Direction potatoFacing = potato.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
		float rotY = 0;
		switch (potatoFacing) {
		default:
		case SOUTH:
			break;
		case NORTH:
			rotY = 180F;
			break;
		case EAST:
			rotY = 270F;
			break;
		case WEST:
			rotY = 90F;
			break;
		}
		ms.rotate(Vector3f.YP.rotationDegrees(rotY));

		float jump = potato.jumpTicks;
		if (jump > 0) {
			jump -= partialTicks;
		}

		float up = (float) -Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		ms.translate(0F, up, 0F);
		ms.rotate(Vector3f.ZP.rotationDegrees(rotZ));

		ms.push();
		float r = 1;
		float g = 1;
		float b = 1;
		switch (name) {
		case "pahimar":
			ms.scale(1F, 0.3F, 1F);
			ms.translate(0F, 3.5F, 0F);
			break;
		case "dinnerbone":
		case "grumm":
			ms.rotate(Vector3f.ZP.rotationDegrees(180F));
			ms.translate(0F, -2.625F, 0F);
			break;
		case "aureylian":
			g = 0.5F;
			break;
		}

		IVertexBuilder buffer = buffers.getBuffer(layer);
		boolean render = !(name.equals("mami") || name.equals("soaryn") || name.equals("eloraam") && jump != 0);
		if (render) {
			potatoModel.render(ms, buffer, light, overlay, r, g, b, 1);
		}
		if (name.equals("kingdaddydmac")) {
			ms.translate(0.5F, 0F, 0F);
			potatoModel.render(ms, buffer, light, overlay, r, g, b, 1);
		}
		ms.pop();

		renderItems(potato, potatoFacing, name, partialTicks, ms, buffers, light, overlay);

		ms.push();
		MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, potato.name, partialTicks, ms, buffers, light, overlay));
		ms.pop();

		ms.rotate(Vector3f.ZP.rotationDegrees(-rotZ));
		ms.rotate(Vector3f.YP.rotationDegrees(-rotY));
		ms.scale(1F, -1F, -1F);

		renderName(potato, name, ms, buffers, light);
		ms.pop();
	}

	private void renderName(TileTinyPotato potato, String name, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult pos = mc.objectMouseOver;
		if (!name.isEmpty() && pos != null && pos.getType() == RayTraceResult.Type.BLOCK
				&& potato.getPos().equals(((BlockRayTraceResult) pos).getPos())) {
			ms.push();
			ms.translate(0F, -0.6F, 0F);
			ms.rotate(mc.getRenderManager().getCameraOrientation());
			float f1 = 0.016666668F * 1.6F;
			ms.scale(-f1, -f1, f1);
			ms.translate(0.0F, 0F / f1, 0.0F);
			int halfWidth = mc.fontRenderer.getStringWidth(potato.name.getString()) / 2;

			float opacity = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int opacityRGB = (int) (opacity * 255.0F) << 24;
			mc.fontRenderer.func_238416_a_(potato.name, -halfWidth, 0, 0x20FFFFFF, false, ms.getLast().getMatrix(), buffers, true, opacityRGB, light);
			mc.fontRenderer.func_238416_a_(potato.name, -halfWidth, 0, 0xFFFFFFFF, false, ms.getLast().getMatrix(), buffers, false, 0, light);
			if (name.equals("pahimar") || name.equals("soaryn")) {
				ms.translate(0F, 14F, 0F);
				String str = name.equals("pahimar") ? "[WIP]" : "(soon)";
				halfWidth = mc.fontRenderer.getStringWidth(str) / 2;

				mc.fontRenderer.renderString(str, -halfWidth, 0, 0x20FFFFFF, false, ms.getLast().getMatrix(), buffers, true, opacityRGB, light);
				mc.fontRenderer.renderString(str, -halfWidth, 0, 0xFFFFFFFF, false, ms.getLast().getMatrix(), buffers, true, 0, light);
			}

			ms.pop();
		}
	}

	private void renderItems(TileTinyPotato potato, Direction facing, String name, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		ms.rotate(Vector3f.ZP.rotationDegrees(180F));
		ms.translate(0F, -1F, 0F);
		float s = 1F / 3.5F;
		ms.scale(s, s, s);

		for (int i = 0; i < potato.inventorySize(); i++) {
			ItemStack stack = potato.getItemHandler().getStackInSlot(i);
			if (stack.isEmpty()) {
				continue;
			}

			ms.push();
			Direction side = Direction.values()[i];
			if (side.getAxis() != Axis.Y) {
				float sideAngle = side.getHorizontalAngle() - facing.getHorizontalAngle();
				side = Direction.fromAngle(sideAngle);
			}

			boolean block = stack.getItem() instanceof BlockItem;
			boolean mySon = stack.getItem() instanceof ItemBlockTinyPotato;

			switch (side) {
			case UP:
				if (mySon) {
					ms.translate(0F, 0.6F, 0.5F);
				} else if (block) {
					ms.translate(0F, 0.3F, 0.5F);
				}
				ms.translate(0F, -0.5F, -0.4F);
				break;
			case DOWN:
				ms.translate(0, -2.3F, -0.88F);
				if (mySon) {
					ms.translate(0, .65F, 0.6F);
				} else if (block) {
					ms.translate(0, 1, 0.6F);
				}
				break;
			case NORTH:
				ms.translate(0, -1.9F, 0.02F);
				if (mySon) {
					ms.translate(0, 1, 0.6F);
				} else if (block) {
					ms.translate(0, 1, 0.6F);
				}
				break;
			case SOUTH:
				ms.translate(0, -1.6F, -0.89F);
				if (mySon) {
					ms.translate(0, 1.4F, 0.5F);
				} else if (block) {
					ms.translate(0, 1.0F, 0.5F);
				}
				break;
			case EAST:
				if (mySon) {
					ms.translate(-0.4F, 0.65F, 0F);
				} else if (block) {
					ms.translate(-0.4F, 0.8F, 0F);
				} else {
					ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				}
				ms.translate(-0.3F, -1.9F, 0.04F);
				break;
			case WEST:
				if (mySon) {
					ms.translate(1F, 0.65F, 1F);
				} else if (block) {
					ms.translate(1F, 0.8F, 1F);
				} else {
					ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				}
				ms.translate(-0.3F, -1.9F, -0.92F);
				break;
			}

			if (mySon) {
				ms.scale(1.1F, 1.1F, 1.1F);
			} else if (block) {
				ms.scale(0.5F, 0.5F, 0.5F);
			}
			if (block && side == Direction.NORTH) {
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
			}
			renderItem(ms, buffers, light, overlay, stack);
			ms.pop();
		}
		ms.pop();

		ms.push();
		if (!name.isEmpty()) {
			ContributorFancinessHandler.firstStart();

			float scale = 1F / 4F;
			ms.translate(0F, 1F, 0F);
			ms.scale(scale, scale, scale);
			if (name.equals("phi") || name.equals("vazkii")) {
				ms.push();
				ms.translate(-0.15, 0.1, 0.4);
				ms.rotate(Vector3f.YP.rotationDegrees(90F));
				ms.rotate(new Vector3f(1, 0, 1).rotationDegrees(20));
				renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.phiFlowerModel);
				ms.pop();

				if (name.equals("vazkii")) {
					ms.scale(1.25F, 1.25F, 1.25F);
					ms.rotate(Vector3f.XP.rotationDegrees(180F));
					ms.rotate(Vector3f.YP.rotationDegrees(-90F));
					ms.translate(0.2, -1.25, 0);
					renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.nerfBatModel);
				}
			} else if (name.equals("haighyorkie")) {
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.rotate(Vector3f.ZP.rotationDegrees(180F));
				ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				ms.translate(-0.5F, -1.2F, -0.075F);
				renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.goldfishModel);
			} else if (name.equals("martysgames") || name.equals("marty")) {
				ms.scale(0.7F, 0.7F, 0.7F);
				ms.rotate(Vector3f.ZP.rotationDegrees(180F));
				ms.translate(-0.3F, -2.7F, -1.2F);
				ms.rotate(Vector3f.ZP.rotationDegrees(15F));
				renderItem(ms, buffers, light, overlay, new ItemStack(ModItems.infiniteFruit, 1).setDisplayName(new StringTextComponent("das boot")));
			} else if (name.equals("jibril")) {
				ms.scale(1.5F, 1.5F, 1.5F);
				ms.translate(0F, -0.8F, 0F);
				ms.rotate(Vector3f.YP.rotationDegrees(90));
				ItemFlightTiara.renderHalo(null, ms, buffers, partialTicks);
			} else if (name.equals("kingdaddydmac")) {
				ms.scale(0.5F, 0.5F, 0.5F);
				ms.rotate(Vector3f.ZP.rotationDegrees(180));
				ms.rotate(Vector3f.YP.rotationDegrees(90));
				ms.push();
				ms.translate(0F, -2.5F, 0.65F);
				ItemStack ring = new ItemStack(ModItems.manaRing);
				renderItem(ms, buffers, light, overlay, ring);
				ms.translate(0F, 0F, -4F);
				renderItem(ms, buffers, light, overlay, ring);
				ms.pop();

				ms.translate(1.5, -4, -2.5);
				renderBlock(ms, buffers, light, overlay, Blocks.CAKE);
			} else {
				ItemStack icon = ContributorFancinessHandler.getFlower(name);
				if (!icon.isEmpty()) {
					ms.rotate(Vector3f.XP.rotationDegrees(180));
					ms.rotate(Vector3f.YP.rotationDegrees(180));
					ms.translate(0, -0.75, -0.5);
					RenderHelper.renderItemModelGold(null, icon, ItemCameraTransforms.TransformType.HEAD, ms, buffers, null, light, overlay);
				}
			}
		}
		ms.pop();
	}

	private void renderModel(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, IBakedModel model) {
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffers.getBuffer(Atlases.getTranslucentCullBlockType()), null, model, 1, 1, 1, light, overlay);
	}

	private void renderItem(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, ItemStack stack) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.HEAD, light, overlay, ms, buffers);
	}

	private void renderBlock(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, Block block) {
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(block.getDefaultState(), ms, buffers, light, overlay);
	}
}
