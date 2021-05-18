package com.spde.ym.engusertool;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class ResetFactory extends TestItem {
    boolean isReciveResult =false;
    public ResetFactory() {
        super("�ָ�����","RESETFACTORY");
        COMMAND_NUMBER = 17;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
        isReciveResult = true;
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains(AT_COMMAND_OK) && isReciveResult) {
            MainUI.getmResultText().setText("�����");
            setbTestResult(true);
            isReciveResult=false;
            return true;
        }
        else
            return false;
    }
}
