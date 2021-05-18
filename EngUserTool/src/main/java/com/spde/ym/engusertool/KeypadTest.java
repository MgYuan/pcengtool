package com.spde.ym.engusertool;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import static com.spde.ym.engusertool.UartAT.BASIC_AT_COMMAND;

public class KeypadTest extends TestItem {

    JButton mKey1,mKey2,mKey3,mKey4,mKeySos;
    ArrayList<JButton> buttons = new ArrayList<JButton>();
    JPanel keyPanel = null;
    protected static final String StrPressOnTerminal = "ÇëÔÚÖÕ¶ËÉÏ°´¼ü";
    public KeypadTest() {
        super("°´¼ü²âÊÔ","KEY");
        mKey1 = new JButton("Key 1");
        mKey2 = new JButton("Key 2");
        mKey3 = new JButton("Key 3");
        mKey4 = new JButton("Key 4");
        mKeySos = new JButton("Key SOS");

        COMMAND_NUMBER = 2;
    }

    @Override
    public void AddExtraTestInputControls(){

        if(keyPanel == null) {
            keyPanel= new JPanel();
            keyPanel.add(mKey1);
            keyPanel.add(mKey2);
            keyPanel.add(mKey3);
            keyPanel.add(mKey4);
            keyPanel.add(mKeySos);

            buttons.add(mKey1);
            buttons.add(mKey2);
            buttons.add(mKey3);
            buttons.add(mKey4);
            buttons.add(mKeySos);

            MainUI.getmInputPanel().add(keyPanel);
        }
        MainUI.getmResultText().setText(StrTerminalTestTip);
    }
    @Override
    public void SendATCommand() {
        super.SendBasicATCommand();
    }

    @Override
    public boolean ParseData(String receive) {
            if(receive.contains("OK")){
                MainUI.getmResultText().setText(StrPressOnTerminal);
                return true;
            }else {
                int sNum = receive.indexOf("presskey ");
                if (sNum >= 0) {
                    String str = receive.substring(sNum + 9, sNum + 10);
                    int keyIndex = Integer.parseInt(str);
                    JButton button = buttons.get(keyIndex);
                    button.setForeground(Color.GREEN);
                    return true;
                }
                return false;
            }
    }
    
    @Override
    public void retest() {
    	super.retest();
        MainUI.getmResultText().setText(StrTerminalTestTip);
    	for(JButton b:buttons) {
    		b.setForeground(getForeground());
    	}
    }
}
