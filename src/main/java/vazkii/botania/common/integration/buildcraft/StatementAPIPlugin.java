package vazkii.botania.common.integration.buildcraft;

import java.util.Collection;

import buildcraft.api.statements.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileRuneAltar;

import javax.annotation.Nonnull;

public class StatementAPIPlugin implements ITriggerProvider {
	static final ITriggerExternal triggerManaEmpty = new TriggerManaLevel(TriggerManaLevel.State.EMPTY);
	static final ITriggerExternal triggerManaContains = new TriggerManaLevel(TriggerManaLevel.State.CONTAINS);
	static final ITriggerExternal triggerManaSpace = new TriggerManaLevel(TriggerManaLevel.State.SPACE);
	static final ITriggerExternal triggerManaFull = new TriggerManaLevel(TriggerManaLevel.State.FULL);
	private static final ITriggerInternal triggerManaDetector = new TriggerManaDetector();

	private static final ITriggerExternal triggerRuneAltarCanCraft = new TriggerRuneAltarCanCraft();

	public StatementAPIPlugin() {
		StatementManager.registerStatement(triggerManaEmpty);
		StatementManager.registerStatement(triggerManaContains);
		StatementManager.registerStatement(triggerManaSpace);
		StatementManager.registerStatement(triggerManaFull);
		StatementManager.registerStatement(triggerManaDetector);

		StatementManager.registerStatement(triggerRuneAltarCanCraft);

		StatementManager.registerTriggerProvider(this);
	}

	@Override
	public void addInternalTriggers(Collection<ITriggerInternal> triggers, IStatementContainer container) {
		triggers.add(triggerManaDetector);
	}

	@Override
	public void addInternalSidedTriggers(Collection<ITriggerInternalSided> triggers, IStatementContainer container, @Nonnull EnumFacing side) { }

	@Override
	public void addExternalTriggers(Collection<ITriggerExternal> triggers, @Nonnull EnumFacing side, TileEntity tile) {
		if (tile instanceof IManaBlock) {
			triggers.add(triggerManaEmpty);
			triggers.add(triggerManaContains);
			if (tile instanceof IManaReceiver) {
				triggers.add(triggerManaSpace);
				triggers.add(triggerManaFull);
			}
		}

		if (tile instanceof TileRuneAltar)
			triggers.add(triggerRuneAltarCanCraft);
	}
}
