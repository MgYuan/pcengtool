package com.spde.ym.engusertool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTextArea;

public class TestResult extends TestItem {
    private static boolean isAllTestsPassed = false;
    private static Map testResultMap = new HashMap();

    boolean mIsAcceptResponse = false;

    public TestResult() {
        super("²âÊÔ½á¹û","RESULT");
        COMMAND_NUMBER = 15;
        isShowTestResultText = true;
    }

    @Override
    public void SendATCommand() {
        SendBasicATCommand();
        mIsAcceptResponse = true;
    }

    public boolean isCanJudgeAllStatus(){
        if(testResultMap.size() == 0)
            return false;
        else
            return true;
    }

    public void UpdateItemTestStatusByResult(TestItem item){
        String result;
        String testkey = item.getmTestName();
        if(testResultMap.containsKey(testkey)){
            result = (String) testResultMap.get(testkey);
            item.updataStatusByResult(result);
        }
    }

    @Override
    public boolean ParseData(String receive) {
        String data = receive;

        if (data.contains("EMCC t p") || data.contains("EMCC t f") || data.contains("EMCC n")) {
            if (isShowTestResultText) {
                MainUI.getmResultText().setText(mStrResult);
            }
            int pos = data.indexOf("KEY");
            data =  data.substring(pos);

            GetAllTestsPassed(data);
            if (isAllTestsPassed) {
                setbTestResult(true);
            } else {
                setbTestResult(false);
            }
            return true;
        }else{
            return false;
        }
    }

    public static boolean GetAllTestsPassed(String data) {
        isAllTestsPassed = true;
        testResultMap.clear();
        String result[] = data.split("\n");

        for(String s: result){
            String itemArry[] = s.split(" ");
            if(itemArry.length != 3)
                break;

            if(itemArry[1] .equals("t") && itemArry[2].equals("p")){
                testResultMap.put(itemArry[0],"pass");
            }else if(itemArry[1] .equals("t") && itemArry[2].equals("f")){
                testResultMap.put(itemArry[0],"fail");
            }else {
                testResultMap.put(itemArry[0],"not test");
            }
        }
        Iterator iterator = testResultMap.keySet().iterator();

        while (iterator.hasNext()) {
            Object key = iterator.next();
            if(!testResultMap.get(key).equals("pass")) {
                isAllTestsPassed = false;
                break;
            }
        }
        return isAllTestsPassed;
    }

    public boolean isAllTestsPassed() {
        return isAllTestsPassed;
    }

    @Override
    public void retest(){
        super.retest();
        testResultMap.clear();
    }
}
