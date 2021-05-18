package com.spde.ym.engusertool;

import javax.swing.JTextArea;

import static com.spde.ym.engusertool.UartAT.AT_COMMAND_OK;

public class RFCalibrationTest extends TestItem {

    public RFCalibrationTest() {
        super("ÉäÆµÐ£×¼","RFCalibration");
        COMMAND_NUMBER = 10;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }


    @Override
    public boolean ParseData(String receive) {
        if(receive.contains("lte calibrated") && receive.contains("gsm calibrated")){
            MainUI.getmResultText().setText(receive);
            setbTestResult(true);
            return true;
        }else if(receive.contains("lte not calibrated") && receive.contains("gsm not calibrated")){
            MainUI.getmResultText().setText(receive);
            setbTestResult(false);
            return true;
        }else
            return false;
    }
}
