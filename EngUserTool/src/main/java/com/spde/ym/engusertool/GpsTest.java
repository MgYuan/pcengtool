package com.spde.ym.engusertool;


public class GpsTest extends TestItem {
    public GpsTest() {
        super("Gps≤‚ ‘","GPS");
        COMMAND_NUMBER = 6;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
        if(receive.contains("gps pass")){
            MainUI.getmResultText().setText("pass");
            setbTestResult(true);
            return true;
        }else if(receive.contains("gps testing")){
            MainUI.getmResultText().setText("testing");
            return true;
        }else if(receive.contains("gps fail")){
            MainUI.getmResultText().setText("fail");
            setbTestResult(false);
            return true;
        }else{
            return false;
        }
    }
}
