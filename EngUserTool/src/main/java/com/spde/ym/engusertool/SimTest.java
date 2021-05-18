package com.spde.ym.engusertool;

import javax.swing.JTextArea;

public class SimTest extends TestItem {

    public SimTest() {
        super("SIMø®≤‚ ‘","SIM");
        COMMAND_NUMBER = 11;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }

    @Override
    public void OnReceiveDataFromTerminal(String data) {
        if(data.contains("sim")){
            MainUI.getmResultText().setText(data);
        }
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains("sim ok") ){
            MainUI.getmResultText().setText(receive);
            setbTestResult(true);
            return true;
        }else if( receive.contains("sim fail")){
            MainUI.getmResultText().setText(receive);
            setbTestResult(false);
            return true;
        }
        else
            return false;
    }
}
