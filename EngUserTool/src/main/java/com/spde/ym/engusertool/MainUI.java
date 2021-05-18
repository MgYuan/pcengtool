package com.spde.ym.engusertool;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.JTabbedPane;

public class MainUI {

    public static final String StrNext = "下一项";
    public static final String StrPrev = "上一项";
    public static final String StrBeginTest = "开始测试";
    private static JFrame frame;

    private static JTabbedPane  mMainTab = new JTabbedPane();

    /*EngUserTest Interface*/
    private static JTextArea mComData;



    private static UartAndTipPanel mUartAndTipPage;
    private static AllTestsPanel mEngTestPage;
    private static IMEICheckPanel mImeiCheck;
    private static LongtimeTest mLongTimeTest;



    public static void main(String[] agrs)
    {
        ConstructAllTestPages();
    }

    public static UartManager getmComManager() {
        return mUartAndTipPage.getUartManager();
    }


    public static void UpdateComeData(String data,boolean isWrit){
        if(isWrit){
            mComData.append("W:"+data+"\n");
        }else{
            mComData.append("R:"+data+"\n");
        }
        int length = mComData.getText().length();
        mComData.setCaretPosition(length);
        if(length > 300)
            mComData.setText("");
    }

    private static void ConstructAllTestPages(){
        frame=new JFrame("EngUserTool");
        frame.setLayout(null);
        frame.setContentPane(mMainTab);
        mUartAndTipPage = new UartAndTipPanel();
        mEngTestPage = new AllTestsPanel();
        mImeiCheck = new IMEICheckPanel();
        mLongTimeTest = new LongtimeTest();
        mMainTab.addTab("串口设置",mUartAndTipPage);
        mMainTab.addTab("整机测试",mEngTestPage);
        mMainTab.addTab("IMEI检验",mImeiCheck);
        mMainTab.addTab("老化测试",mLongTimeTest);

        frame.setBounds(300,50,800,1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

    }

    public static JPanel getmInputPanel() {
        return mEngTestPage.getmInputPanel();
    }

    public static JTextArea getmResultText() {
        return mEngTestPage.getmResultText();
    }

    public static String getUserInputSwVersion(){
        return mEngTestPage.getUserInputSw();
    }

    public static void CheckAndGoNextTest(TestItem cur){
        if(mMainTab.getSelectedIndex() == 1) {
            mEngTestPage.CheckAndGoNextTest(cur);
        }
        else if(mMainTab.getSelectedIndex() == 3){

        }
    }
    public static AllTestsPanel getAllTestsPanel(){
        return mEngTestPage;
    }

    public static void Reset(){
        mEngTestPage.Reset();
        mImeiCheck.Reset();
        mLongTimeTest.Reset();
    }
}
