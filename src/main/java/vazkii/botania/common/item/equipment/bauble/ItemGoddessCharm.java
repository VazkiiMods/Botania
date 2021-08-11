/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class ItemGoddessCharm extends ItemBauble implements IManaUsingItem {

	public static final int COST = 1000;

	public ItemGoddessCharm(Properties props) {
		super(props);
	}

	public static void onExplosion(Level world, Vec3 vec, List<BlockPos> affectedBlocks) {
		List<Player> players = world.getEntitiesOfClass(Player.class, new AABB(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).inflate(8));

		for (Player player : players) {
			ItemStack charm = EquipmentHandler.findOrEmpty(ModItems.goddessCharm, player);
			if (!charm.isEmpty() && ManaItemHandler.instance().requestManaExact(charm, player, COST, true)) {
				affectedBlocks.clear();
				return;
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		bipedModel.head.translateAndRotate(ms);
		ms.translate(0.275, -0.4, 0);
		ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
		ms.scale(0.55F, -0.55F, -0.55F);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE,
				light, OverlayTexture.NO_OVERLAY, ms, buffers, player.getId());
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
