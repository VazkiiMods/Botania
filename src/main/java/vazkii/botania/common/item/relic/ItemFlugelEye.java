/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye(Properties props) {
		super(props);
	}

	private static final String TAG_TARGET_PREFIX = "target_";

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

				GameProfile boundPlayer = null;
				if (ConfigHandler.COMMON.flugelPlayerportEnabled.get()) {
					TileEntity te = world.getTileEntity(pos);
					if (te instanceof SkullTileEntity) {
						boundPlayer = ((SkullTileEntity) te).getPlayerProfile();
					}
				}

				INBT nbt;
				if (boundPlayer != null && boundPlayer.getId() != player.getUniqueID()) {
					CompoundNBT playerTag = new CompoundNBT();
					NBTUtil.writeGameProfile(playerTag, boundPlayer);
					nbt = playerTag;
				} else {
					nbt = BlockPos.field_239578_a_.encodeStart(NBTDynamicOps.INSTANCE, pos).get().orThrow();
				}
				ItemNBTHelper.set(stack, TAG_TARGET_PREFIX + world.func_234923_W_().func_240901_a_().toString(), nbt);
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
		return ActionResult.resultConsume(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
		Vector3d loc = getTeleportPos(world, stack);
		if (loc == null) {
			return stack;
		}
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x, y, z, living.getPosX(), living.getPosY(), living.getPosZ()) * 10);

		if (!(living instanceof PlayerEntity) || ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) living, cost, true)) {
			moveParticlesAndSound(living);
			living.setPositionAndUpdate(x, y, z);
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

	@Nullable
	public GameProfile getGameProfile(World world, ItemStack stack) {
		String tag = TAG_TARGET_PREFIX + world.func_234923_W_().func_240901_a_().toString();
		CompoundNBT nbt = ItemNBTHelper.getCompound(stack, tag, true);
		if (nbt != null) {
			return NBTUtil.readGameProfile(nbt);
		}
		return null;
	}

	@Nullable
	public Vector3d getTeleportPos(World world, ItemStack stack) {
		if (world instanceof ServerWorld) {
			GameProfile gp = getGameProfile(world, stack);
			if (gp != null) {
				Entity e = ((ServerWorld) world).getEntityByUuid(gp.getId());
				if (e != null) {
					return e.getPositionVec();
				}
			} else {
				BlockPos binding = getBinding(world, stack);
				if (binding != null) {
					return Vector3d.copyCentered(getBinding(world, stack)).add(0, 1, 0);
				}
			}
		}
		return null;
	}

	@Nullable
	@Override
	public BlockPos getBinding(World world, ItemStack stack) {
		String tag = TAG_TARGET_PREFIX + world.func_234923_W_().func_240901_a_().toString();
		INBT nbt = ItemNBTHelper.get(stack, tag);
		if (nbt != null) {
			BlockPos pos = BlockPos.field_239578_a_.parse(NBTDynamicOps.INSTANCE, nbt).result().orElse(null);
			if (pos == null) {
				ItemNBTHelper.removeEntry(stack, tag);
			}
			return pos;
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformationAfterShift(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformationAfterShift(stack, world, tooltip, flags);

		if (world == null) {
			return;
		}

		BlockPos binding = getBinding(world, stack);
		GameProfile gp = getGameProfile(world, stack);
		ITextComponent worldText = new StringTextComponent(world.func_234923_W_().func_240901_a_().toString()).mergeStyle(TextFormatting.GREEN);

		if (binding == null && gp == null) {
			tooltip.add(new TranslationTextComponent("botaniamisc.flugelUnbound", worldText).mergeStyle(TextFormatting.GRAY));
		} else {
			ITextComponent bindingText = (gp != null && gp.getName() != null ? new StringTextComponent(gp.getName()).mergeStyle(TextFormatting.GOLD) : new StringTextComponent("[").mergeStyle(TextFormatting.WHITE)
					.append(new StringTextComponent(Integer.toString(binding.getX())).mergeStyle(TextFormatting.GOLD))
					.appendString(", ")
					.append(new StringTextComponent(Integer.toString(binding.getY())).mergeStyle(TextFormatting.GOLD))
					.appendString(", ")
					.append(new StringTextComponent(Integer.toString(binding.getZ())).mergeStyle(TextFormatting.GOLD))
					.appendString("]"));

			tooltip.add(new TranslationTextComponent("botaniamisc.flugelBound", bindingText, worldText).mergeStyle(TextFormatting.GRAY));
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return prefix("challenge/flugel_eye");
	}

}
