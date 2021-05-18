package com.spde.ym.engusertool;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_HEAD;
import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_SUC_COMMAND;
import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_TAIL;

public class IMEICheckPanel extends JPanel {
    JPanel mEngTestResultPanel = new JPanel();
    JLabel mEngTestTip = new JLabel("整机测试结果查询");
    JButton mEngTestCheck = new JButton("查询");
    JTextField mEngResult = new JTextField(20);

    JPanel mImeiCheckPanel = new JPanel();;
    JLabel mImeiCheckTip = new JLabel("请扫码输入IMEI");
    JTextField mImeiInput = new JTextField(25);
    JButton mImeiCheck = new JButton("查询");
    JTextField mImeiCheckResult = new JTextField(10);

    UartAT mEngTestResult;
    UartAT mImeiGet;
    String mStrTestResult = "";
    String mImeiResult="";
    boolean isAllTestsPassed=false;
    String mImei="";

    String strCloseCmd = BASIC_AT_OVER_HEAD+BASIC_AT_OVER_SUC_COMMAND+BASIC_AT_OVER_TAIL;

    public IMEICheckPanel(){
        GridLayout topgridLayout = new GridLayout(1,1,0,0);
        mEngTestResultPanel.setLayout(topgridLayout);

        JPanel bPanel = new JPanel();
        bPanel.add(mEngTestTip);
        bPanel.add(mEngTestCheck);
        bPanel.add(mEngResult);
        mEngTestResultPanel.add(bPanel);

        mEngTestResult = new UartAT(new UartATParser() {
            @Override
            public void OnReceiveDataFromTerminal(String receive) {
                mStrTestResult = mStrTestResult+receive;

                if (mStrTestResult.contains("EMCC t p") || mStrTestResult.contains("EMCC t f") || mStrTestResult.contains("EMCC n")) {
                    int pos = mStrTestResult.indexOf("KEY");
                    mStrTestResult =  mStrTestResult.substring(pos);

                    isAllTestsPassed = TestResult.GetAllTestsPassed(mStrTestResult);
                    if (isAllTestsPassed) {
                        mEngResult.setText("整机测试通过");
                        mEngResult.setForeground(Color.GREEN);
                    } else {
                        mEngResult.setText("整机测试未通过");
                        mEngResult.setForeground(Color.RED);
                    }
                    mStrTestResult="";
                    mEngTestResult.WriteData2Uart(strCloseCmd);

                }
            }
        });

        mEngTestCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String data = UartAT.BASIC_AT_COMMAND+15+",\"\"\n";
                mEngTestResult.WriteData2Uart(data);
                mStrTestResult="";
            }
        });

        GridLayout bottomgridLayout = new GridLayout(1,1,0,0);
        mImeiCheckPanel.setLayout(bottomgridLayout);

        JPanel cPanel = new JPanel();
        cPanel.add(mImeiCheckTip);
        cPanel.add(mImeiInput);
        cPanel.add(mImeiCheck);
        cPanel.add(mImeiCheckResult);
        mImeiCheckPanel.add(cPanel);
        mImeiGet = new UartAT(new UartATParser() {
            @Override
            public void OnReceiveDataFromTerminal(String receive) {
                mImeiResult = mImeiResult+receive;
                if (receive.contains("IMEI ")) {
                    int pos = receive.indexOf("IMEI ") + 5;
                    String imei = receive.substring(pos);
                    if (imei.length() >= 15) {
                        mImei = imei.substring(0, 15);
                        String mInput = mImeiInput.getText();
                        if(mInput.contains(mImei)){
                            mImeiCheckResult.setText("IMEI检查成功");
                            mImeiCheckResult.setForeground(Color.GREEN);
                        }else{
                            mImeiCheckResult.setText("IMEI不一致");
                            mImeiCheckResult.setForeground(Color.RED);
                        }
                        mImeiResult="";
                        mImeiGet.WriteData2Uart(strCloseCmd);
                    }
                }
            }
        });

        mImeiCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String data = UartAT.BASIC_AT_COMMAND+9+",\"\"\n";
                mImeiGet.WriteData2Uart(data);
                mImeiResult="";
                mImei="";
            }
        });


        GridLayout gridLayout = new GridLayout(2,1,0,0);
        setLayout(gridLayout);

        add(mEngTestResultPanel);
        add(mImeiCheckPanel);
    }

    public void Reset(){
        mEngResult.setText("");
        mEngResult.setForeground(getForeground());
        mImeiCheckResult.setText("");
        mImeiCheckResult.setForeground(getForeground());
    }

}
