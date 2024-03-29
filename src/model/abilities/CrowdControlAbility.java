package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}
	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException{
		for(int i =0; i< targets.size();i++){
			if (targets.get(i) instanceof Champion){
			Effect x = (Effect)this.getEffect().clone();
			x.apply((Champion)(targets.get(i)));
			((Champion)targets.get(i)).getAppliedEffects().add(x);
			}
		}
	}

}
