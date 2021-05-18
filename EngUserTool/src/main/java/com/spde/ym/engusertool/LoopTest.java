package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class LoopTest extends TestItem {
    public LoopTest() {
        super("ªÿ…˘≤‚ ‘","LOOPBACK");
        COMMAND_NUMBER = 4;
        //MainUI.getmResultText().setText(StrTerminalTestTip);
    }
    @Override
    public void AddExtraTestInputControls(){
        MainUI.getmResultText().setText(StrTerminalTestTip);
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
}
