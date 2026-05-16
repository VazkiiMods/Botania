package vazkii.botania.common.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;

import java.util.Collection;
import java.util.List;

public class BotaniaMonsterTeam extends PlayerTeam {
	public BotaniaMonsterTeam(String name) {
		super(null, name);
	}

	@Override
	public MutableComponent getFormattedName(Component component) {
		return component.copy();
	}

	@Override
	public boolean canSeeFriendlyInvisibles() {
		return true;
	}

	@Override
	public boolean isAllowFriendlyFire() {
		return true;
	}

	@Override
	public Visibility getNameTagVisibility() {
		return Visibility.ALWAYS;
	}

	@Override
	public ChatFormatting getColor() {
		return ChatFormatting.RESET;
	}

	@Override
	public Collection<String> getPlayers() {
		return List.of();
	}

	@Override
	public Visibility getDeathMessageVisibility() {
		return Visibility.ALWAYS;
	}

	@Override
	public CollisionRule getCollisionRule() {
		return CollisionRule.ALWAYS;
	}
}
