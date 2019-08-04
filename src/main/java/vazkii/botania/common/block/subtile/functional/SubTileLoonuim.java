/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 31, 2014, 7:49:43 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class SubTileLoonuim extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":loonium")
	public static TileEntityType<SubTileLoonuim> TYPE;

	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_ITEMSTACK_TO_DROP = "botania:looniumItemStackToDrop";
	private static final Tag<Item> BLACKLIST = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "loonium_blacklist"));

	private ResourceLocation lootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

	public SubTileLoonuim() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		World world = getWorld();
		if(!world.isRemote && redstoneSignal == 0 && ticksExisted % 100 == 0 && mana >= COST) {
			Random rand = world.rand;

			ItemStack stack;
			do {
				LootContext ctx = new LootContext.Builder((ServerWorld) world).build(LootParameterSets.EMPTY);
				List<ItemStack> stacks = ((ServerWorld) world).getServer().getLootTableManager()
						.getLootTableFromLocation(lootTable).generate(ctx);
				if (stacks.isEmpty())
					return;
				else {
					Collections.shuffle(stacks);
					stack = stacks.get(0);
				}
			} while(stack.isEmpty() || BLACKLIST.contains(stack.getItem()));

			int bound = RANGE * 2 + 1;
			int xp = getPos().getX() - RANGE + rand.nextInt(bound);
			int yp = getPos().getY();
			int zp = getPos().getZ() - RANGE + rand.nextInt(bound);
			
			BlockPos pos = new BlockPos(xp, yp - 1, zp);
			do {
				pos = pos.up();
				if(pos.getY() >= 254)
					return;
			} while(world.getBlockState(pos).causesSuffocation(world, pos));
			pos = pos.up();

			double x = pos.getX() + Math.random();
			double y = pos.getY() + Math.random();
			double z = pos.getZ() + Math.random();
			
			MonsterEntity entity = null;
			if(world.rand.nextInt(50) == 0)
				entity = new EndermanEntity(EntityType.ENDERMAN, world);
			else if(world.rand.nextInt(10) == 0) {
				entity = new CreeperEntity(EntityType.CREEPER, world);
				if(world.rand.nextInt(200) == 0)
					entity.onStruckByLightning(null);
			} else 
				switch(world.rand.nextInt(3)) {
				case 0:
					if(world.rand.nextInt(10) == 0)
						entity = new HuskEntity(EntityType.HUSK, world);
					else entity = new ZombieEntity(world);
					break;
				case 1:
					if(world.rand.nextInt(10) == 0)
						entity = new StrayEntity(EntityType.STRAY, world);
					else entity = new SkeletonEntity(EntityType.SKELETON, world);
					break;
				case 2:
					if(world.rand.nextInt(10) == 0)
						entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
					else entity = new SpiderEntity(EntityType.SPIDER, world);
					break;
				}

			entity.setPositionAndRotation(x, y, z, world.rand.nextFloat() * 360F, 0);
			entity.setMotion(Vec3d.ZERO);

			Multimap<String, AttributeModifier> map = HashMultimap.create();
			map.put(SharedMonsterAttributes.MAX_HEALTH.getName(),
					new AttributeModifier("Loonium Modififer Health", 2, AttributeModifier.Operation.MULTIPLY_BASE));
			map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier("Loonium Modififer Damage", 1.5, AttributeModifier.Operation.MULTIPLY_BASE));
			entity.getAttributes().applyAttributeModifiers(map);

			entity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));
			entity.addPotionEffect(new EffectInstance(Effects.REGENERATION,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));

			CompoundNBT cmp = stack.write(new CompoundNBT());
			entity.getEntityData().put(TAG_ITEMSTACK_TO_DROP, cmp);

			entity.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);
			world.addEntity(entity);
			entity.spawnExplosionParticle();
			
			mana -= COST;
			sync();
		}
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.loonium;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_LOOT_TABLE))
			lootTable = new ResourceLocation(cmp.getString(TAG_LOOT_TABLE));
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putString(TAG_LOOT_TABLE, lootTable.toString());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDrops(LivingDropsEvent event) {
		LivingEntity e = event.getEntityLiving();
		if(e.getEntityData().contains(TAG_ITEMSTACK_TO_DROP)) {
			CompoundNBT cmp = e.getEntityData().getCompound(TAG_ITEMSTACK_TO_DROP);
			ItemStack stack = ItemStack.read(cmp);
			event.getDrops().clear();
			event.getDrops().add(new ItemEntity(e.world, e.posX, e.posY, e.posZ, stack));
		}
	}
}
