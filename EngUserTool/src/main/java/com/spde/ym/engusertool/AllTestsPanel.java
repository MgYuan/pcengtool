package com.spde.ym.engusertool;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AllTestsPanel extends JPanel {

    private  JPanel mInputPanel;
    private JTextArea mResultText;
    private JPanel mSWInputPanel = null;
    private JLabel mTip = null;
    private JTextField mSwInput = null;

    private VersionTest versionTest;
    private LCDTest lcdTest;
    private BacklightTest backlightTest;
    private KeypadTest keypadTest;
    private VibrateTest vibrateTest;
    private LoopTest loopTest;
    private MelodyTest melodyTest;
    private GpsTest gpsTest;
    private WifiTest wifiTest;
    private ChargerTest chargerTest;
    private RFIDTest rfidTest;
    private RFCalibrationTest rfCalibrationTest;
    private SimTest simTest;
    private EmccTest emccTest;
    private TestResult testResult;
    private ResetFactory resetFactory ;
    private JButton refreshStatusBut;

    private ArrayList<TestItem> mTestsArray = new ArrayList<TestItem>();

    public AllTestsPanel(){
        mInputPanel = new JPanel();
        mResultText = new JTextArea();

        versionTest = new VersionTest();
        lcdTest = new LCDTest();
        backlightTest = new BacklightTest();
        keypadTest = new KeypadTest();
        vibrateTest = new VibrateTest();
        loopTest = new LoopTest();
        melodyTest = new MelodyTest();
        gpsTest = new GpsTest();
        wifiTest = new WifiTest();
        chargerTest = new ChargerTest();
        rfidTest = new RFIDTest();
        rfCalibrationTest = new RFCalibrationTest();
        simTest = new SimTest();
        emccTest = new EmccTest();
        testResult = new TestResult();
        resetFactory = new ResetFactory();
        refreshStatusBut = new JButton("根据测试结果刷新状态");

        GridLayout gridLayout = new GridLayout(1,2,0,0);
        setLayout(gridLayout);

        JPanel fixTestPanel = new JPanel();
        GridLayout mlayout = new GridLayout(18, 1,0,0);
        mlayout.setVgap(-20);
        fixTestPanel.setLayout(mlayout);
        fixTestPanel.add(versionTest);
        fixTestPanel.add(lcdTest);
        fixTestPanel.add(backlightTest);
        fixTestPanel.add(keypadTest);
        fixTestPanel.add(vibrateTest);
        fixTestPanel.add(loopTest);
        fixTestPanel.add(melodyTest);
        fixTestPanel.add(gpsTest);
        fixTestPanel.add(wifiTest);
        fixTestPanel.add(chargerTest);
        fixTestPanel.add(rfidTest);
        fixTestPanel.add(rfCalibrationTest);
        fixTestPanel.add(simTest);
        fixTestPanel.add(emccTest);
        fixTestPanel.add(testResult);
        fixTestPanel.add(resetFactory);
        fixTestPanel.add(refreshStatusBut);

        JPanel variablePannel = new JPanel();
        GridLayout vLayout = new GridLayout(2,1,0,0);
        variablePannel.setLayout(vLayout);
        variablePannel.add(mInputPanel);
        variablePannel.add(mResultText);

        mSWInputPanel = new JPanel();
        GridLayout SWgridLayout = new GridLayout(1,2,0,0);
        mSWInputPanel.setLayout(SWgridLayout);
        mTip = new JLabel("请输入软件版本号:");
        mSwInput = new JTextField(20);
        mSWInputPanel.add(mTip);
        mSWInputPanel.add(mSwInput);
        mInputPanel.add(mSWInputPanel);

        add(fixTestPanel);
        add(variablePannel);

        mTestsArray.add(versionTest);
        mTestsArray.add(lcdTest);
        mTestsArray.add(backlightTest);
        mTestsArray.add(keypadTest);
        mTestsArray.add(vibrateTest);
        mTestsArray.add(loopTest);
        mTestsArray.add(melodyTest);
        mTestsArray.add(gpsTest);
        mTestsArray.add(wifiTest);
        mTestsArray.add(chargerTest);
        mTestsArray.add(rfidTest);
        mTestsArray.add(rfCalibrationTest);
        mTestsArray.add(simTest);
        mTestsArray.add(emccTest);

        refreshStatusBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                refreshAllTestStatus();
            }
        });
    }

    private void refreshAllTestStatus(){
        if(!testResult.isCanJudgeAllStatus()) {
            JOptionPane.showMessageDialog(null, "请先执行［测试结果］", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(TestItem item:mTestsArray){
            testResult.UpdateItemTestStatusByResult(item);
        }
    }

    public void Reset(){
        versionTest.retest();
        lcdTest.retest();
        backlightTest.retest();
        keypadTest.retest();
        vibrateTest.retest();
        loopTest.retest();
        melodyTest.retest();
        gpsTest.retest();
        wifiTest.retest();
        chargerTest.retest();
        rfidTest.retest();
        rfCalibrationTest.retest();
        simTest.retest();
        emccTest.retest();
        testResult.retest();
        mResultText.setText("");
    }


    public JPanel getmInputPanel() {
        return mInputPanel;
    }

    public void setmInputPanel(JPanel mInputPanel) {
        this.mInputPanel = mInputPanel;
    }

    public JTextArea getmResultText() {
        return mResultText;
    }

    public String getUserInputSw(){
        return  mSwInput.getText();
    }

    public void CheckAndGoNextTest(TestItem current){
        TestItem Next = null;
        int curposition = 0;
        boolean found =false;
        for(TestItem testItem:mTestsArray){
            if(testItem.COMMAND_NUMBER == current.COMMAND_NUMBER){
                found =true;
                break;
            }
            curposition++;
        }
        if(found == false)
            return;

        for(int i=curposition+1;i<mTestsArray.size();i++){
            TestItem item = mTestsArray.get(i);
            if (item != null && !item.isbTestResult() && !item.isTested()) {
                Next = item;
                break;
            }
        }
        if(Next == null){
            for(int i=0;i<curposition;i++){
                TestItem item = mTestsArray.get(i);
                if (item != null && !item.isbTestResult() && !item.isTested()) {
                    Next = item;
                    break;
                }
            }
        }
            if (Next != null) {
                Next.StartTest();
            }
    }

}
