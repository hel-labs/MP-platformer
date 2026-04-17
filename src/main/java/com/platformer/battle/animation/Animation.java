package com.platform.battle.animation;

import java.awt.image.BufferedImage;

public class Animation{
    private final BufferedImage[] frames;
    private final float frameDuration;
    private final boolean looping;
    private float timer = 0f;
    private int currentFrame =0;
    private boolean finished = false;

    public Animation(BufferedImage[] frames, float frameDuration, boolean looping){
        if(frames == null || frames.length ==0){
            throw new IllegalArgumentException("Zero Animation Frames provided. At least one is required!");
        }
        this.frames=frames;
        this.frameDuration=frameDuration;
        this.looping=looping;
    }

    public void update(float delta){
        if(finished){
            return;
        }

        timer += delta;
        if(timer>=frameDuration){
            timer -= frameDuration;
            currentFrame++;

            if(currentFrame >= frames.length){
                if(looping){
                    currentFrame=0;
                }else{
                    currentFrame = frames.length-1;
                    finished = true;
                }
            }
        }
    }

    public BufferedImage getCurrentFrame(){
        return frames[currentFrame];
    }

    public boolean isFinished(){
        return finished;
    }

    public void reset(){
        currentFrame =0;
        timer =0;
        finished = false;
    }

    public void skipToEnd(){
        currentFrame=frames.length-1;
        finished = !looping;
    }

    public int getFrameCount(){
        return frames.length;
    }

    public boolean isLooping(){
        return looping;
    }
}