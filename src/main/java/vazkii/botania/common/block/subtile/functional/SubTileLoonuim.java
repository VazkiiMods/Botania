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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class SubTileLoonuim extends SubTileFunctional {

	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_ITEMSTACK_TO_DROP = "botania:looniumItemStackToDrop";

	private ResourceLocation lootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

	@Override
	public void onUpdate() {
		super.onUpdate();

		World world = supertile.getWorld();
		if(!world.isRemote && redstoneSignal == 0 && ticksExisted % 100 == 0 && mana >= COST) {
			Random rand = world.rand;

			ItemStack stack;
			do {
				List<ItemStack> stacks = world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(rand, new LootContext.Builder((WorldServer) world).build());
				if (stacks.isEmpty())
					return;
				else {
					Collections.shuffle(stacks);
					stack = stacks.get(0);
				}
			} while(stack.isEmpty() || BotaniaAPI.looniumBlacklist.contains(stack.getItem()));

			int bound = RANGE * 2 + 1;
			int xp = supertile.getPos().getX() - RANGE + rand.nextInt(bound);
			int yp = supertile.getPos().getY();
			int zp = supertile.getPos().getZ() - RANGE + rand.nextInt(bound);
			
			BlockPos pos = new BlockPos(xp, yp - 1, zp);
			do {
				pos = pos.up();
				if(pos.getY() >= 254)
					return;
			} while(world.getBlockState(pos).causesSuffocation());
			pos = pos.up();

			double x = pos.getX() + Math.random();
			double y = pos.getY() + Math.random();
			double z = pos.getZ() + Math.random();
			
			EntityMob entity = null;
			if(world.rand.nextInt(50) == 0)
				entity = new EntityEnderman(world);
			else if(world.rand.nextInt(10) == 0) {
				entity = new EntityCreeper(world);
				if(world.rand.nextInt(200) == 0)
					entity.onStruckByLightning(null);
			} else 
				switch(world.rand.nextInt(3)) {
				case 0:
					if(world.rand.nextInt(10) == 0)
						entity = new EntityHusk(world);
					else entity = new EntityZombie(world);
					break;
				case 1:
					if(world.rand.nextInt(10) == 0)
						entity = new EntityStray(world);
					else entity = new EntitySkeleton(world);
					break;
				case 2:
					if(world.rand.nextInt(10) == 0)
						entity = new EntityCaveSpider(world);
					else entity = new EntitySpider(world);
					break;
				}

			entity.setPositionAndRotation(x, y, z, world.rand.nextFloat() * 360F, 0);
			entity.motionX = entity.motionY = entity.motionZ = 0;
			
			Multimap map = HashMultimap.create();
			map.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier("Loonium Modififer Health", 2, 1));
			map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier("Loonium Modififer Damage", 1.5, 1));
			entity.getAttributeMap().applyAttributeModifiers(map);

			entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, entity instanceof EntityCreeper ? 100 : Integer.MAX_VALUE, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, entity instanceof EntityCreeper ? 100 : Integer.MAX_VALUE, 0));

			NBTTagCompound cmp = stack.writeToNBT(new NBTTagCompound());
			entity.getEntityData().setTag(TAG_ITEMSTACK_TO_DROP, cmp);

			entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			world.spawnEntity(entity);
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
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.hasKey(TAG_LOOT_TABLE))
			lootTable = new ResourceLocation(cmp.getString(TAG_LOOT_TABLE));
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setString(TAG_LOOT_TABLE, lootTable.toString());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDrops(LivingDropsEvent event) {
		EntityLivingBase e = event.getEntityLiving();
		if(e.getEntityData().hasKey(TAG_ITEMSTACK_TO_DROP)) {
			NBTTagCompound cmp = e.getEntityData().getCompoundTag(TAG_ITEMSTACK_TO_DROP);
			ItemStack stack = new ItemStack(cmp);
			event.getDrops().clear();
			event.getDrops().add(new EntityItem(e.world, e.posX, e.posY, e.posZ, stack));
		}
	}
}
