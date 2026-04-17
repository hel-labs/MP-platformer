private void resolveTalkOption(TalkOption option) {
    TalkAction talkAction = (TalkAction) engine.getPlayerActions().get(1);
    lastResult = talkAction.resolveOption(option, ctx);
    ctx.setLastResult(lastResult);
    phase = Phase.PLAYER_RESULT;
    dialogueBox.setText(lastResult.getMessage());
}