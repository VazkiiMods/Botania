/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:26 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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

		if(player != null && player.isSneaking()) {
			if(world.isRemote) {
				for(int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.05F - (float) Math.random() * 0.05F, 0);
				}
			} else {
				ItemStack stack = ctx.getItem();
				GlobalPos loc = GlobalPos.of(world.getDimension().getType(), pos);
				ItemNBTHelper.set(stack, TAG_LOCATION, loc.serialize(NBTDynamicOps.INSTANCE));
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 5F);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		float x = (float) (living.getX() - Math.random() * living.getWidth());
		float y = (float) (living.getY() + Math.random());
		float z = (float) (living.getZ() - Math.random() * living.getWidth());
		WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
		living.world.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
    }

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.success(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
		INBT nbt = ItemNBTHelper.get(stack, TAG_LOCATION);
		if(nbt == null)
			return stack;
		GlobalPos loc = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt));
		int x = loc.getPos().getX();
		int y = loc.getPos().getY();
		int z = loc.getPos().getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, living.getX(), living.getY(), living.getZ()) * 10);

		if(loc.getDimension() == world.getDimension().getType()
			&& (!(living instanceof PlayerEntity) || ManaItemHandler.requestManaExact(stack, (PlayerEntity) living, cost, true))) {
			moveParticlesAndSound(living);
			living.setPositionAndUpdate(x + 0.5, y + 1.5, z + 0.5);
			moveParticlesAndSound(living);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketHandler.sendToNearby(entity.world, entity, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.getX(), entity.getY(), entity.getZ(), entity.getEntityId()));
		entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
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
		if(nbt != null)
			return GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt)).getPos();
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
