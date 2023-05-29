package vazkii.botania.common.entity;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.entity.ManaSensitive;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;

public class ManaStormChargeMinecartEntity extends AbstractMinecart implements ManaSensitive {

    public ManaStormChargeMinecartEntity(EntityType<ManaStormChargeMinecartEntity> type, Level world) {
		super(type, world);
	}

    public ManaStormChargeMinecartEntity(Level world, double x, double y, double z) {
		super(BotaniaEntities.CHARGE_MINECART, world, x, y, z);
	}

    @Override
    protected Item getDropItem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDropItem'");
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
	protected boolean canAddPassenger(Entity passenger) {
		return false;
	}

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return BotaniaBlocks.manaBomb.defaultBlockState();
    }

    @Override
    public boolean onBurstCollision(ManaBurst burst) {
        Level world = this.getLevel();
        if (!world.isClientSide()) {
            ManaStormEntity storm = BotaniaEntities.MANA_STORM.create(world);
		    storm.burstColor = burst.getColor();
		    storm.setPos(this.position().add(0, 0.4375, 0)); //Since carts sit 0.625 off the ground
		    world.addFreshEntity(storm);
            this.discard();
        }
        return true;
    }
    
}
