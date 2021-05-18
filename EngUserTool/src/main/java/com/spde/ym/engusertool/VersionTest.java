package com.spde.ym.engusertool;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VersionTest extends TestItem {


    public VersionTest() {
        super("°æ±¾ºÅ","VERSION");
        COMMAND_NUMBER = 1;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
    }


    @Override
    public boolean ParseData(String receive) {
        if(receive.contains(" swend")){
            //setbTestResult(true);
            int start = receive.indexOf(":");
            int end = receive.indexOf(" swend");
            String sw;
            System.out.println("end "+end);
            if(end == -1)
                sw = receive.substring(start+1);
            else
                sw = receive.substring(start+1,end);

            MainUI.getmResultText().setText(sw);
            if(sw.trim().equals(MainUI.getUserInputSwVersion().trim())){
                setbTestResult(true);
            }else{
                setbTestResult(false);
            }
            // OnGetTestResult();
            return true;
        }else
            return false;
    }
}
