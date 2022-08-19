package vazkii.botania.common.integration.buildcraft;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.text.WordUtils;

import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibTriggerNames;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;

public class TriggerManaLevel extends StatementBase implements ITriggerExternal {
	public enum State {
		EMPTY,
		CONTAINS,
		SPACE,
		FULL
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
		icon = IconHelper.forName(iconRegister, "triggers/mana" + WordUtils.capitalizeFully(state.name()));
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal(LibTriggerNames.TRIGGER_MANA_PREFIX + WordUtils.capitalizeFully(state.name()));
	}

	@Override
	public boolean isTriggerActive(TileEntity target, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {
		if(target instanceof IManaBlock) {
			if(state == State.EMPTY) return ((IManaBlock) target).getCurrentMana() == 0;
			else if(state == State.CONTAINS) return ((IManaBlock) target).getCurrentMana() > 0;
			else if(target instanceof IManaReceiver) {
				if(state == State.SPACE) return !((IManaReceiver) target).isFull();
				else if(state == State.FULL) return ((IManaReceiver) target).isFull();
			}
		}

		return false;
	}
}
