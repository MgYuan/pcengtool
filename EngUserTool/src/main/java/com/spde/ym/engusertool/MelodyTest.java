package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class MelodyTest extends TestItem {
    public MelodyTest() {
        super("“Ù∆µ≤‚ ‘","MELODY");
        COMMAND_NUMBER = 5;
        //MainUI.getmResultText().setText(StrTerminalTestTip);
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains(AT_COMMAND_OK))
            return true;
        else
            return false;
    }

    @Override
    public void AddExtraTestInputControls(){
        MainUI.getmResultText().setText(StrTerminalTestTip);
    }

}
