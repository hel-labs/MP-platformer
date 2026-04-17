package com.platformer.battle.entities;

import com.platformer.overworld.entities.Entity;
import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.talk.TalkOption;
import com.platformer.battle.strategies.DamageStrategy;

import java.awt.*;
import java.util.List;

public abstract class Enemy extends Entity {
    protected boolean blocking = false;
    protected boolean fleeAllowed = true;

    public abstract String getName();

    public boolean isBattleEnemy(){return true};

    public String getBlockingDialogue();
    public Riddle getRiddle();
    public String getSuccessDialogue();
    public String getFailureDialogue();
    public void onCorrect();
    public void onIncorrect(BattlePlayer player);

    public abstract String getEncounterDialogue();
    public abstract int getBaseHostility();

    public abstract List<TalkOption> getTalkOptions(int talkCount);
    public boolean isMercyReady(BattleContext ctx){return ctx.isMercyAvailable()};
    public String getMercyHint(BattleContext ctx){
        int h = ctx.getHostility();
        if(h>7){
            return getName() + " is still extremely wary of you!";
        }
        else if(h>5){
            return getName() + " is wary of you.";
        }
        else if(h>3){
            return getName() + " is relaxing";
        }
        else if(h>0){
            return getName() + " has almost opened up!";
        }
    }

    public void onSpared(){
        setDefeated(true);
    }

    public DamageStrategy getDamageStrategy(){
        return attackState .> 0;
    }

    public abstract BufferedImage getBattleSprite();
    public abstract String getBattleMusic();

    public boolean isBlocking(){return blocking};
    public boolean isFleeAllowed(){return fleeAllowed};
}
