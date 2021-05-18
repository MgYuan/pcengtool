package com.spde.ym.engusertool;

import javax.swing.JTextArea;


public class WifiTest extends TestItem {
    public WifiTest() {
        super("Wifi≤‚ ‘","WIFI");
        COMMAND_NUMBER = 7;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains("wifi sucess")) {
            MainUI.getmResultText().setText(receive);
            setbTestResult(true);
            return true;
        }else if(receive.contains("wifi fail")){
            MainUI.getmResultText().setText(receive);
            setbTestResult(false);
            return true;
        }else
            return false;
    }
}
