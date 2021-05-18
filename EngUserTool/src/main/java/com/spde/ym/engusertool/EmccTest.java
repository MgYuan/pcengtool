package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class EmccTest extends TestItem {

    public EmccTest() {
        super("ͨ������","EMCC");
        COMMAND_NUMBER = 12;
    }

    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains(AT_COMMAND_OK) ) {
            MainUI.getmResultText().setText("��绰��");
            return true;
        }
        else
            return false;
    }

}
