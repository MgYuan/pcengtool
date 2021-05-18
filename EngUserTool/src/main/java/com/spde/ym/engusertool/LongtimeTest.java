package com.spde.ym.engusertool;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static com.spde.ym.engusertool.TestItem.TIME_1_SECOND;

public class LongtimeTest extends JPanel {
    private static final String strStart = "开始";
    private static final String strStop = "结束";
    private static final String strComplete = "测试完成";

    private long mSetTestALLTime = 0;
    private long mTestTimes = 0;
    private static final long ONE_ITEM_LAST_TIME = TIME_1_SECOND*30;

    JPanel mTopPanel = new JPanel();
    JPanel mBottomPanel = new JPanel();
    LtTestItem mLcdTest = new LtTestItem(new LCDTest());
    LtTestItem mBacklightTest = new LtTestItem(new BacklightTest());
    LtTestItem mVibTest = new LtTestItem(new VibrateTest());
    LtTestItem mLoopTest = new LtTestItem(new LoopTest());
    LtTestItem mMelodyTest = new LtTestItem(new MelodyTest());
    LtTestItem mGpsTest = new LtTestItem(new GpsTest());
    LtTestItem mWifiTest = new LtTestItem(new WifiTest());
    LtTestItem mEmccTest = new LtTestItem(new EmccTest());



    JLabel mTimeSetTip = new JLabel("请输入测试时间(单位:分钟)");
    JTextField mTimeInput = new JTextField(20);
    JButton mTestBut = new JButton(strStart);

    ArrayList<LtTestItem> mTestArray = new ArrayList<LtTestItem>();

    LtTestItem mCurItem = null;
    int mCurpos = -1;
    Timer mOperationTimer;
    boolean isStarted = false;

    public LongtimeTest(){
        mTopPanel.add(mLcdTest);
        mTopPanel.add(mBacklightTest);
        mTopPanel.add(mVibTest);
        mTopPanel.add(mLoopTest);
        mTopPanel.add(mMelodyTest);
        mTopPanel.add(mGpsTest);
        mTopPanel.add(mWifiTest);
        mTopPanel.add(mEmccTest);

        GridLayout gridLayout = new GridLayout(8,1,0,0);
        mTopPanel.setLayout(gridLayout);

        mTestArray.add(mLcdTest);
        mTestArray.add(mBacklightTest);
        mTestArray.add(mVibTest);
        mTestArray.add(mLoopTest);
        mTestArray.add(mMelodyTest);
        mTestArray.add(mGpsTest);
        mTestArray.add(mWifiTest);
        mTestArray.add(mEmccTest);

        add(mTopPanel);

        mBottomPanel.add(mTimeSetTip);
        mBottomPanel.add(mTimeInput);
        mBottomPanel.add(mTestBut);

        GridLayout mainridLayout = new GridLayout(2,1,0,0);
        setLayout(mainridLayout);

        add(mTopPanel);
        add(mBottomPanel);

        mTestBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isStarted) {
                    String input = mTimeInput.getText();
                    if (input.length() == 0) {
                        JOptionPane.showMessageDialog(null, "请输入测试时间!", "错误", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int time = Integer.parseInt(input);
                        mSetTestALLTime = time * TIME_1_SECOND * 60;
                        StartLongTimeTest();
                        mTestBut.setText("结束");
                        isStarted = true;
                    }
                }else{
                    if(mOperationTimer != null){
                        mOperationTimer.cancel();
                    }

                    mTestBut.setText("开始");
                    mCurItem.testItem.setbTestResult(true);
                    mCurpos = 0;
                    isStarted = false;
                }
            }
        });
    }



    private class LtTestItem extends JPanel{
        JCheckBox mCheckBox = new JCheckBox();
        TestItem testItem = null;
        public LtTestItem(TestItem test){
            testItem = test;
            add(mCheckBox);
            add(testItem);
            mCheckBox.setSelected(true);
        }

        boolean IsSelected(){
            return mCheckBox.isSelected();
        }
    }

    private void StartLongTimeTest(){
        mCurItem = GetNextTextItem();
        if(mCurItem == null){
            JOptionPane.showMessageDialog(null, "alert", "请选择要测试的项目!", JOptionPane.ERROR_MESSAGE);
        }else{
            mCurItem.testItem.StartTest();
            if(mOperationTimer != null){
                mOperationTimer.cancel();
            }
            mOperationTimer = new Timer();
            mOperationTimer.schedule(new TimerTask() {
                public void run() {
                    mCurItem.testItem.setbTestResult(true);
                    mTestTimes++;
                    if(mTestTimes*ONE_ITEM_LAST_TIME < mSetTestALLTime ){
                        StartLongTimeTest();
                    }else{
                        stopTest();
                    }
                }
            },ONE_ITEM_LAST_TIME);
        }
    }

    private void stopTest(){
        if(mOperationTimer != null){
            mOperationTimer.cancel();
        }
        mTestBut.setText("开始");
        mTimeInput.setText(strComplete);
        isStarted = false;
        mCurItem = null;
        mCurpos = -1;
        mTestTimes=0;
    }

    private LtTestItem GetNextTextItem(){
        if(mCurpos == -1){
            for(int i=0;i<mTestArray.size();i++){
                LtTestItem item = mTestArray.get(i);
                if(item.IsSelected()) {
                    mCurItem = item;
                    mCurpos = i;
                    break;
                }
            }
        }else{
            boolean isfound =false;
            for(int i=mCurpos+1;i<mTestArray.size();i++){
                LtTestItem item = mTestArray.get(i);
                if(item.IsSelected()) {
                    mCurItem = item;
                    mCurpos = i;
                    isfound = true;
                    break;
                }
            }
            if(!isfound){
                for(int i=0;i<=mCurpos;i++){
                    LtTestItem item = mTestArray.get(i);
                    if(item.IsSelected()) {
                        mCurItem = item;
                        mCurpos = i;
                        isfound = true;
                        break;
                    }
                }
            }

        }
        return mCurItem;
    }

    public void Reset(){
        stopTest();
        mTimeInput.setText("");
        for(LtTestItem item:mTestArray){
            item.testItem.retest();
        }
    }
}
