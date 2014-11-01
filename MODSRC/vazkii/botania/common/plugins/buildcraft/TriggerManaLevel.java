package vazkii.botania.common.plugins.buildcraft;

import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaReceiver;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;

public class TriggerManaLevel extends StatementBase implements ITriggerExternal {
	public enum State {
		Empty, Contains, Space, Full
	};

	private State state;
	
	public TriggerManaLevel(State state) {
		this.state = state;
	}
	
	@Override
	public String getUniqueTag() {
		return "botania:mana" + state.name();
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon("botania:triggers/mana" + state.name() + ".png");
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal("botania.trigger.mana" + state.name());
	}

	@Override
	public boolean isTriggerActive(TileEntity target, ForgeDirection side,
			IStatementContainer source, IStatementParameter[] parameters) {
		if (target instanceof IManaBlock) {
			if (state == State.Empty) {
				return ((IManaBlock) target).getCurrentMana() == 0;
			} else if (state == State.Contains) {
				return ((IManaBlock) target).getCurrentMana() > 0;
			} else if (state == State.Space) {
				if (target instanceof IManaReceiver) {
					return !((IManaReceiver) target).isFull();
				}
			} else if (state == State.Full) {
				if (target instanceof IManaReceiver) {
					return ((IManaReceiver) target).isFull();
				}
			}
		}
		
		return false;
	}
}
