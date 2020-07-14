/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Random;

public class ItemKingKey extends ItemRelic implements IManaUsingItem {

	private static final String TAG_WEAPONS_SPAWNED = "weaponsSpawned";
	private static final String TAG_CHARGING = "charging";

	public static final int WEAPON_TYPES = 12;

	public ItemKingKey(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		setCharging(stack, true);
		return ActionResult.resultSuccess(stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int time) {
		int spawned = getWeaponsSpawned(stack);
		if (spawned == 20) {
			setCharging(stack, false);
			setWeaponsSpawned(stack, 0);
		}
	}

	@Override
	public void onUse(World world, LivingEntity living, ItemStack stack, int count) {
		int spawned = getWeaponsSpawned(stack);

		if (count != getUseDuration(stack) && spawned < 20 && !world.isRemote && (!(living instanceof PlayerEntity) || ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) living, 150, true))) {
			Vector3 look = new Vector3(living.getLookVec()).multiply(1, 0, 1);

			double playerRot = Math.toRadians(living.rotationYaw + 90);
			if (look.x == 0 && look.z == 0) {
				look = new Vector3(Math.cos(playerRot), 0, Math.sin(playerRot));
			}

			look = look.normalize().multiply(-2);

			int div = spawned / 5;
			int mod = spawned % 5;

			Vector3 pl = look.add(Vector3.fromEntityCenter(living)).add(0, 1.6, div * 0.1);

			Random rand = world.rand;
			Vector3 axis = look.normalize().crossProduct(new Vector3(-1, 0, -1)).normalize();

			double rot = mod * Math.PI / 4 - Math.PI / 2;

			Vector3 axis1 = axis.multiply(div * 3.5 + 5).rotate(rot, look);
			if (axis1.y < 0) {
				axis1 = axis1.multiply(1, -1, 1);
			}

			Vector3 end = pl.add(axis1);

			EntityBabylonWeapon weapon = new EntityBabylonWeapon(living, world);
			weapon.setPosition(end.x, end.y, end.z);
			weapon.rotationYaw = living.rotationYaw;
			weapon.setVariety(rand.nextInt(WEAPON_TYPES));
			weapon.setDelay(spawned);
			weapon.setRotation(MathHelper.wrapDegrees(-living.rotationYaw + 180));

			world.addEntity(weapon);
			weapon.playSound(ModSounds.babylonSpawn, 1F, 1F + world.rand.nextFloat() * 3F);
			setWeaponsSpawned(stack, spawned + 1);
		}
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public static boolean isCharging(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_CHARGING, false);
	}

	public static int getWeaponsSpawned(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_WEAPONS_SPAWNED, 0);
	}

	public static void setCharging(ItemStack stack, boolean charging) {
		ItemNBTHelper.setBoolean(stack, TAG_CHARGING, charging);
	}

	public static void setWeaponsSpawned(ItemStack stack, int count) {
		ItemNBTHelper.setInt(stack, TAG_WEAPONS_SPAWNED, count);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/king_key");
	}

}
