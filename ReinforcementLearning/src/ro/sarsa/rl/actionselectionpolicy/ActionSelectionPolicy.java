package ro.sarsa.rl.actionselectionpolicy;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.utilities.AbstractQValues;

public interface ActionSelectionPolicy {

	public abstract Action chooseAction(AbstractQValues qVals, Enviroment env, List<Action> possibleActions);

}