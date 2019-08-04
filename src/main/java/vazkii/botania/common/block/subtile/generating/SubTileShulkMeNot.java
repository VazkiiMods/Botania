package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.Effects;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileShulkMeNot extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":shulk_me_not")
	public static TileEntityType<SubTileShulkMeNot> TYPE;

	private static final int RADIUS = 8;

	public SubTileShulkMeNot() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		
		int generate = getMaxMana();
		
		World world = getWorld();
		BlockPos pos = getPos();
		Vec3d posD = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
		List<ShulkerEntity> shulkers = world.getEntitiesWithinAABB(ShulkerEntity.class, new AxisAlignedBB(pos).grow(RADIUS));
		if(!world.isRemote)
			for(ShulkerEntity shulker : shulkers) {
				if(getMaxMana() - mana < generate)
					break;

				if(shulker.isAlive() && shulker.getDistanceSq(posD) < RADIUS * RADIUS) {
					LivingEntity target = shulker.getAttackTarget();
					if(target != null && target instanceof IMob && target.isAlive()
							&& target.getDistanceSq(posD) < RADIUS * RADIUS && target.getActivePotionEffect(Effects.LEVITATION) != null) {
						target.remove();
						shulker.remove();
	
						for(int i = 0; i < 10; i++) // so it's really loud >_>
							world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_SHULKER_DEATH, SoundCategory.BLOCKS, 10F, 0.1F);
						particles(world, pos, target);
						particles(world, pos, shulker);
						
						mana += generate; 
						sync();
					}
				}
			}
	}
	
	private void particles(World world, BlockPos pos, Entity entity) {
		if(world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;
			ws.spawnParticle(ParticleTypes.EXPLOSION,
					entity.posX + entity.getWidth() / 2,
					entity.posY + entity.getHeight() / 2,
					entity.posZ + entity.getWidth() / 2,
					100, entity.getWidth(), entity.getHeight(), entity.getWidth(), 0.05);
			ws.spawnParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 0, 0, 0, 0.6);
		}
	}
	
	@Override
	public int getColor() {
		return 0x815598;
	}
	
	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getPos(), RADIUS);
	}
	
	@Override
	public int getMaxMana() {
		return 75000;
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.shulkMeNot;
	}
	
}
