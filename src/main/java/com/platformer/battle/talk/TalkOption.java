package com.platformer.battle.talk;

public class TalkOption{
    private final String text;
    private final String response;
    private final int hostilityDelta;

    public TalkOption(String text, String response, int hostilityDelta){
        this.text=text;
        this.response=response;
        this.hostilityDelta=hostilityDelta;
    }

    public String getText()           { return text;           }
    public String getResponse()       { return response;       }
    public int    getHostilityDelta() { return hostilityDelta; }

    public boolean isCalming()    { return hostilityDelta < 0; }
    public boolean isProvoking()  { return hostilityDelta > 0; }
    public boolean isNeutral()    { return hostilityDelta == 0; }
}