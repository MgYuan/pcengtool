package com.spde.ym.engusertool;

import javax.swing.JTextArea;

public class RFIDTest extends TestItem {
    String mImei = "";

    public RFIDTest() {
        super("RFID&IMEI","RFID");
        COMMAND_NUMBER = 9;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }


    @Override
    public boolean ParseData(String receive) {
        if (receive.contains("IMEI ")) {
            int pos = receive.indexOf("IMEI ") + 5;
            String imei = receive.substring(pos);
            if (imei.length() >= 15) {
                mImei = imei.substring(0, 15);
                MainUI.getmResultText().setText(receive);
                setbTestResult(true);
                return true;
            }

        }
        return false;
    }

    public String getmImei() {
        return mImei;
    }
}
