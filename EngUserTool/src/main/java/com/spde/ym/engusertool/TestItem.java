package com.spde.ym.engusertool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.Timer;
import java.util.TimerTask;

import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_FAIL_COMMAND;
import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_HEAD;
import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_SUC_COMMAND;
import static com.spde.ym.engusertool.UartAT.BASIC_AT_OVER_TAIL;

public abstract class TestItem extends JPanel implements UartATParser{

    public static final long TIME_1_SECOND = 1000;

    public static final int layout_gap = 5;

    protected static final String StrStart = "开始";
    protected static final String StrReStart = "重新测试";
    protected static final String StrTesting = "正在测试";
    protected static final String StrSucess = "成功";   
    protected static final String StrFail = "失败";
    protected static final String StrNotTest = "未测试";
    protected static final String StrPls_Wait = "请等待";  
    protected static final String StrTerminalTestTip = "请在终端测试,并判断结果";
    protected static final String StrOvertime = "测试超时";

    protected int COMMAND_NUMBER = 0;
    private boolean bTestResult = false;

    private Timer mOperationTimer = null;
    private long mLOperationTime = TIME_1_SECOND;
    protected boolean isShowTestResultText = false;

    private boolean isTested = false;   //flag to mark if tested
    protected JLabel mLabel;
    protected JButton mStartBut;
    protected JButton mSucBut;
    protected JButton mFailBut;

    private UartAT mUart;

    String mStrResult = "";
    String mTestName = "";

    public TestItem(String tip,String name){
        mTestName = name;
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        /*add test name*/
        mLabel = new JLabel(tip);
        this.add(mLabel);
        this.add(Box.createHorizontalStrut(layout_gap));

        //add start button
        mStartBut = new JButton(StrStart);
        add(mStartBut);
        add(Box.createHorizontalStrut(layout_gap));

        //add result buttons
        mSucBut = new JButton(StrSucess);
        mFailBut = new JButton(StrFail);
        add(mSucBut);
        add(mFailBut);
        enableTest(true);
        mStartBut.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mStartBut.setBackground(getBackground());
                StartTest();
            }
        });

        mSucBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setbTestResult(true);
            }
        });
        mFailBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setbTestResult(false);
            }
        });

        mUart = new UartAT(this);
    }

    public String getmTestName() {
        return mTestName;
    }

    public void updataStatusByResult(String result){
        if(result.equals("pass")){
            mStartBut.setText(StrStart);
            mStartBut.setBackground(getBackground());
            mSucBut.setBackground(Color.GREEN);
            mFailBut.setBackground(getBackground());
            bTestResult = true;
        }else if(result.equals("fail")){
            mStartBut.setText(StrStart);
            mStartBut.setBackground(getBackground());
            mSucBut.setBackground(getBackground());
            mFailBut.setBackground(Color.RED);
            bTestResult = false;
        }else if(result.equals("not test")){
            mStartBut.setText(StrNotTest);
            mStartBut.setBackground(Color.YELLOW);
            mSucBut.setBackground(getBackground());
            mFailBut.setBackground(getBackground());
            bTestResult = false;
        }
    }

    public boolean isbTestResult() {
        return bTestResult;
    }

    public void setbTestResult(boolean bTestResult) {
        System.out.println("setbTestResult "+bTestResult);
        this.bTestResult = bTestResult;
        stopTest();
        SendResultBackToTerminal(bTestResult);
        UpdateSucAndFailButtonByTestResult(bTestResult);
        OnGetTestConclusion(bTestResult);
        if(bTestResult) {
            MainUI.CheckAndGoNextTest(this);
        }
        setTested(true);
        enableTest(true);
    }

    /*
        flag to mark if tested
     */
    public boolean isTested() {
        return isTested;
    }

    public void setTested(boolean tested) {
        isTested = tested;
    }

    /*
            update buttons by test result
         */
    protected void UpdateSucAndFailButtonByTestResult(boolean isSuc){
        if(isSuc){
            mSucBut.setBackground(Color.GREEN);
            mFailBut.setBackground(getBackground());
        }else{
            mFailBut.setBackground(Color.RED);
            mSucBut.setBackground(getBackground());
        }
    }

    /*
        Send test result to terminal, tell terminal to close it's test win
     */
    protected void SendResultBackToTerminal(boolean isSuc){
        String content = isSuc?BASIC_AT_OVER_SUC_COMMAND:BASIC_AT_OVER_FAIL_COMMAND;
        String atcmd = BASIC_AT_OVER_HEAD+content+BASIC_AT_OVER_TAIL;
        //MainUI.getmComManager().WriteString(this,atcmd);
        mUart.WriteData2Uart(atcmd);
    }

    private void ClearOperationTimer(){
        if(mOperationTimer != null) {
            mOperationTimer.cancel();
            mOperationTimer = null;
        }
    }
    /*
        When test start delay a second, to wait at rsp from treminal
     */
    private void StartOperationTimer(){
        ClearOperationTimer();
        mOperationTimer = new Timer();
        mOperationTimer.schedule(new TimerTask() {
            public void run() {
                enableTest(true);
            }
        },mLOperationTime);
    }

    /*
       begin test
     */
    public void StartTest(){
        SendATCommand();
        MainUI.getmResultText().setText(StrPls_Wait);
        StartOperationTimer();
        mStartBut.setText(StrTesting);
        AddExtraTestInputControls();
    }

    public void stopTest(){
        mStartBut.setText(StrReStart);
        ClearOperationTimer();
    }

    protected void OnGetTestConclusion(boolean isSuc){
        if(isShowTestResultText)
            return;
        if(isSuc){
            MainUI.getmResultText().setText(StrSucess);
        }else{
            MainUI.getmResultText().setText(StrFail);
        }
    }

    public  abstract void  SendATCommand();
    public  abstract boolean  ParseData(String receive);

    public void OnReceiveDataFromTerminal(String data){
        mStrResult = mStrResult+data;
        if(ParseData(mStrResult)){
            mStrResult = "";
        }
    }

    /*
        inherite this to add extra test input data
     */
    public void AddExtraTestInputControls(){
        //empty,realise in son classes
    }

    /*
        Send this testitem's at command to terminal
     */
    public void SendBasicATCommand() {
        String data = UartAT.BASIC_AT_COMMAND+COMMAND_NUMBER+",\"\"\n";
        mUart.WriteData2Uart(data);
    }

    /*
        reset test buttons for test next terminal
     */
    public void retest() {
    	bTestResult = false;    	
    	mStartBut.setEnabled(true);
    	mStartBut.setText(StrStart);
    	mSucBut.setEnabled(false);
    	mFailBut.setEnabled(false);
    	mSucBut.setBackground(getBackground());
    	mFailBut.setBackground(getBackground());
        MainUI.getmResultText().setText(StrPls_Wait);
        mStrResult="";
    }

    public void setmLOperationTime(long mLOperationTime) {
        this.mLOperationTime = mLOperationTime;
    }

    /*
        some time porbit user to press buttons
     */
    public void enableTest(boolean isEn){
        mStartBut.setEnabled(isEn);
        mSucBut.setEnabled(isEn);
        mFailBut.setEnabled(isEn);
    }
}
