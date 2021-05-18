package com.spde.ym.engusertool;

public class UartAT {
    public static final String BASIC_AT_COMMAND = "AT+VALIDATE=";
    public static final String BASIC_AT_OVER_HEAD = "AT+VALIDATE=19,\"";
    public static final String BASIC_AT_OVER_TAIL = "\"\n";
    public static final String BASIC_AT_OVER_SUC_COMMAND = "suc";
    public static final String BASIC_AT_OVER_FAIL_COMMAND = "fail";
    public static final String AT_COMMAND_OK = "OK";
    public static final String AT_COMMAND_ERROR = "ERROR";
    private UartATParser mParser;

    public UartAT(UartATParser parser){
        mParser = parser;
    }

    public void WriteData2Uart(String wdata){
        MainUI.getmComManager().WriteString(this,wdata);
    }

    public void OnReceiveDataFromTerminal(String rdata){
        mParser.OnReceiveDataFromTerminal(rdata);
    }
}
