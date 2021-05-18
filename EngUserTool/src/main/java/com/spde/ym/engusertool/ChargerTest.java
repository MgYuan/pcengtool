package com.spde.ym.engusertool;

import javax.swing.JTextArea;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class ChargerTest extends TestItem {

    public ChargerTest() {
        super("³äµç","CHARGING");
        COMMAND_NUMBER = 8;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains("Charging State")) {
            MainUI.getmResultText().setText(receive);
            return true;
        }
        else
            return false;
    }

}
