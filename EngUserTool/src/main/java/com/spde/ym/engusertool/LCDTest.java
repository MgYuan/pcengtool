package com.spde.ym.engusertool;

import javax.swing.JPanel;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class LCDTest extends TestItem {

    public LCDTest() {
        super("LCD", "MAINLCD");
        COMMAND_NUMBER = 13;
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
