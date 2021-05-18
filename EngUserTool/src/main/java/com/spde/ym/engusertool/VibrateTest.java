package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class VibrateTest extends TestItem {
    public VibrateTest() {
        super("’Ò∂Ø≤‚ ‘","VIBRATE");
        COMMAND_NUMBER = 3;
        //MainUI.getmResultText().setText(StrTerminalTestTip);
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }
    
    @Override
    public void retest() {
    	super.retest();
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
