package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class BacklightTest extends TestItem {
    public BacklightTest() {
        super("±≥π‚≤‚ ‘","BACKLIGHT");
        COMMAND_NUMBER = 14;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }



    @Override
    public void AddExtraTestInputControls(){
        MainUI.getmResultText().setText(StrTerminalTestTip);
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains(AT_COMMAND_OK))
            return true;
        else
            return false;
    }
}

