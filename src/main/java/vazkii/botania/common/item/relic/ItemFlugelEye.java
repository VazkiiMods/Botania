/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;

import java.util.Optional;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye(Properties props) {
		super(props);
	}

	private static final String TAG_LOCATION = "location";

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		PlayerEntity player = ctx.getPlayer();

		if (player != null && player.isSneaking()) {
			if (world.isRemote) {
				for (int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.05F - (float) Math.random() * 0.05F, 0);
				}
			} else {
				ItemStack stack = ctx.getItem();
				GlobalPos loc = GlobalPos.func_239648_a_(world.func_234923_W_(), pos);
				INBT nbt = GlobalPos.field_239645_a_.encodeStart(NBTDynamicOps.INSTANCE, loc).result().get();
				ItemNBTHelper.set(stack, TAG_LOCATION, nbt);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 5F);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onUse(World world, LivingEntity living, ItemStack stack, int count) {
		if (world.isRemote) {
			float x = (float) (living.getPosX() - Math.random() * living.getWidth());
			float y = (float) (living.getPosY() + Math.random());
			float z = (float) (living.getPosZ() - Math.random() * living.getWidth());
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
			world.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
		INBT nbt = ItemNBTHelper.get(stack, TAG_LOCATION);
		if (nbt == null) {
			return stack;
		}
		Optional<GlobalPos> maybeLoc = GlobalPos.field_239645_a_.decode(NBTDynamicOps.INSTANCE, nbt).result().map(Pair::getFirst);
		if (!maybeLoc.isPresent()) {
			ItemNBTHelper.removeEntry(stack, TAG_LOCATION);
			return stack;
		}
		GlobalPos loc = maybeLoc.get();
		int x = loc.getPos().getX();
		int y = loc.getPos().getY();
		int z = loc.getPos().getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, living.getPosX(), living.getPosY(), living.getPosZ()) * 10);

		if (loc.func_239646_a_() == world.func_234923_W_()
				&& (!(living instanceof PlayerEntity) || ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) living, cost, true))) {
			moveParticlesAndSound(living);
			living.setPositionAndUpdate(x + 0.5, y + 1.5, z + 0.5);
			moveParticlesAndSound(living);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketHandler.sendToNearby(entity.world, entity, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.getEntityId()));
		entity.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public BlockPos getBinding(ItemStack stack) {
		INBT nbt = ItemNBTHelper.get(stack, TAG_LOCATION);
		if (nbt != null) {
			return GlobalPos.field_239645_a_.decode(NBTDynamicOps.INSTANCE, nbt).result()
					.map(Pair::getFirst)
					.map(GlobalPos::getPos).orElse(null);
		}
		return null;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/flugel_eye");
	}

}
