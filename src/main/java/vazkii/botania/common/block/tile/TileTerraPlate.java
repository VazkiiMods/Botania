/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Lazy;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TileTerraPlate extends TileMod implements ISparkAttachable, Tickable {
	public static final Lazy<IMultiblock> MULTIBLOCK = new Lazy<>(() -> PatchouliAPI.get().makeMultiblock(
			new String[][] {
					{
							"___",
							"_P_",
							"___"
					},
					{
							"RLR",
							"L0L",
							"RLR"
					}
			},
			'P', ModBlocks.terraPlate,
			'R', "#botania:terra_plate_base",
			'0', "#botania:terra_plate_base",
			'L', "#c:lapis_blocks"
	));

	private static final String TAG_MANA = "mana";

	private int mana;

	public TileTerraPlate() {
		super(ModTiles.TERRA_PLATE);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		boolean removeMana = true;

		if (hasValidPlatform()) {
			List<ItemStack> items = getItems();
			SimpleInventory inv = new SimpleInventory(items.toArray(new ItemStack[0]));

			ITerraPlateRecipe recipe = getCurrentRecipe(inv);
			if (recipe != null) {
				removeMana = false;
				ISparkEntity spark = getAttachedSpark();
				if (spark != null) {
					SparkHelper.getSparksAround(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, spark.getNetwork())
							.filter(otherSpark -> spark != otherSpark && otherSpark.getAttachedTile() instanceof IManaPool)
							.forEach(os -> os.registerTransfer(spark));
				}
				if (mana > 0) {
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
					int proportion = Float.floatToIntBits(getCompletion());
					PacketBotaniaEffect.sendNearby(world, getPos(),
							PacketBotaniaEffect.EffectType.TERRA_PLATE, getPos().getX(), getPos().getY(), getPos().getZ(), proportion);
				}

				if (mana >= recipe.getMana()) {
					for (ItemStack item : items) {
						item.decrement(1);
					}
					ItemEntity item = new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 0.2, getPos().getZ() + 0.5, recipe.craft(inv));
					item.setVelocity(Vec3d.ZERO);
					world.spawnEntity(item);
					world.playSound(null, item.getX(), item.getY(), item.getZ(), ModSounds.terrasteelCraft, SoundCategory.BLOCKS, 1, 1);
					mana = 0;
					world.updateComparators(pos, getCachedState().getBlock());
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
				}
			}
		}

		if (removeMana) {
			receiveMana(-1000);
		}
	}

	private List<ItemStack> getItems() {
		List<ItemEntity> itemEntities = world.getEntitiesByClass(ItemEntity.class, new Box(pos, pos.add(1, 1, 1)), EntityPredicates.VALID_ENTITY);
		List<ItemStack> stacks = new ArrayList<>();
		for (ItemEntity entity : itemEntities) {
			if (!entity.getStack().isEmpty()) {
				stacks.add(entity.getStack());
			}
		}
		return stacks;
	}

	private SimpleInventory getInventory() {
		List<ItemStack> items = getItems();
		return new SimpleInventory(items.toArray(new ItemStack[0]));
	}

	@Nullable
	private ITerraPlateRecipe getCurrentRecipe(SimpleInventory items) {
		return world.getRecipeManager().getFirstMatch(ModRecipeTypes.TERRA_PLATE_TYPE, items, world).orElse(null);
	}

	private boolean isActive() {
		return getCurrentRecipe(getInventory()) != null;
	}

	private boolean hasValidPlatform() {
		return MULTIBLOCK.get().validate(world, getPos().down()) != null;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		mana = cmp.getInt(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		ITerraPlateRecipe recipe = getCurrentRecipe(getInventory());
		return recipe == null || getCurrentMana() >= recipe.getMana();
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.max(0, this.mana + mana);
		world.updateComparators(pos, getCachedState().getBlock());
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return isActive();
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = world.getEntitiesByClass(Entity.class, new Box(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if (sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return !isActive();
	}

	@Override
	public int getAvailableSpaceForMana() {
		ITerraPlateRecipe recipe = getCurrentRecipe(getInventory());
		return recipe == null ? 0 : Math.max(0, recipe.getMana() - getCurrentMana());
	}

	public float getCompletion() {
		ITerraPlateRecipe recipe = getCurrentRecipe(getInventory());
		if (recipe == null) {
			return 0;
		}
		return ((float) getCurrentMana()) / recipe.getMana();
	}

	public int getComparatorLevel() {
		int val = (int) (getCompletion() * 15.0);
		if (getCurrentMana() > 0) {
			val = Math.max(val, 1);
		}
		return val;
	}

}
