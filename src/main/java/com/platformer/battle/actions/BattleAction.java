package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

public abstract class BattleAction {
    
    // Function that executes the effect of the selected action
    public abstract BattleResult execute(BattleContext ctx);
    
    // Function that returns the Label of the actions (Names)
    public abstract String getLabel();
    
    // Function that returns the description of the selected action
    public abstract String getDescription();
}