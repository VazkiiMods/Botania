package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileShulkMeNot extends SubTileGenerating {

	private static final int RADIUS = 8;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		int generate = getMaxMana();
		
		World world = getWorld();
		BlockPos pos = getPos();
		List<EntityShulker> shulkers = world.getEntitiesWithinAABB(EntityShulker.class, new AxisAlignedBB(pos).grow(RADIUS));
		if(!world.isRemote)
			for(EntityShulker shulker : shulkers) {
				if(getMaxMana() - mana < generate)
					break;
				
				if(!shulker.isDead && shulker.getDistanceSq(pos) < RADIUS * RADIUS) {
					EntityLivingBase target = shulker.getAttackTarget();
					if(target != null && target instanceof IMob && !target.isDead 
							&& target.getDistanceSq(pos) < RADIUS * RADIUS && target.getActivePotionEffect(MobEffects.LEVITATION) != null) {
						target.setDead();
						shulker.setDead();
	
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
		if(world instanceof WorldServer) {
			WorldServer ws = (WorldServer) world;
			ws.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX + entity.width / 2, entity.posY + entity.height / 2, entity.posZ + entity.width / 2, 100, entity.width, entity.height, entity.width, 0.05);
			ws.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 0, 0, 0, 0.6);
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
