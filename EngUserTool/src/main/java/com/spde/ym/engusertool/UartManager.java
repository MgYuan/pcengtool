package com.spde.ym.engusertool;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class UartManager extends JPanel implements ItemListener, ActionListener {
    static SerialPort mSerialPort=null;
    private UartAT mCurrentTestItem = null;
    JPanel mComboPanel;
    JLabel mLabel;
    JComboBox mCombo;
    JButton mRefreshBut;
    JButton mSelectBut;
    JButton mTestBut;
    String mSelComName;
    JLabel mComStatus;
    private static final String strSelectCom = "��ѡ��";

    public UartManager(){
        mComboPanel = new JPanel();
        mLabel = new JLabel("COM:");
        mCombo = new JComboBox();
        mCombo.addItemListener(this);
        mTestBut = new JButton("Test");
        mTestBut.addActionListener(this);
        mRefreshBut = new JButton("ˢ��");
        mRefreshBut.addActionListener(this);
        mSelectBut = new JButton("ѡ��");
        mSelectBut.addActionListener(this);
        mComStatus = new JLabel("");
        GridLayout layout = new GridLayout(2, 1);
        layout.setHgap(2);
        layout.setVgap(2);
        this.setLayout(layout);
        mComboPanel.add(mLabel);
        mComboPanel.add(mCombo);
        mComboPanel.add(mTestBut);
        mComboPanel.add(mSelectBut);
        mComboPanel.add(mRefreshBut);

        this.add(mComboPanel);
        this.add(mComStatus);
        mComStatus.setHorizontalAlignment(JLabel.CENTER);
        mComStatus.setVerticalAlignment(JLabel.NORTH);
        //FillItemsToCombo();
        mCombo.addItem("��ˢ�²�ѡ��˿�");
        mTestBut.setVisible(false);
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if(ItemEvent.SELECTED == itemEvent.getStateChange()){
        }
    }


    private void FillItemsToCombo(){
        mCombo.removeAllItems();
        //mCombo.addItem(strSelectCom);
        ArrayList<String> comList = findPort();
        for(String s : comList){
            mCombo.addItem(s);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == mTestBut){
//            mSerialPort = openPort("COM4",115200);

            String str = "AT+VALIDATE=1,\"SM\"\n";
            byte[] data = new byte[str.length()+1];
            for(byte b:data){
                b = 0;
            }

            for(int i = 0;i<str.length();i++){
                data[i] = (byte)str.charAt(i);
            }
            sendToPort(mSerialPort,data);
            //WriteString(str);
        }else if(actionEvent.getSource() == mRefreshBut){
            FillItemsToCombo();
        }else if(actionEvent.getSource() == mSelectBut){
            if(mSerialPort != null){
                mSerialPort.removeEventListener();
                mSerialPort.close();
            }
            String selectedItem = mCombo.getSelectedItem().toString();
            mSelComName = selectedItem;
            mSerialPort = openPort(mSelComName,115200);
            addListener(mSerialPort,new SerialListener() );
            System.out.printf("new selected item : %s\n",selectedItem);
        }

    }

    private static final ArrayList<String> findPort() {

        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();//������д���

        ArrayList<String> portNameList = new ArrayList<>();
        //����������ӵ�List������

        while (portList.hasMoreElements()) {

            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }

        return portNameList;

    }

    private  final SerialPort openPort(String portName, int baudrate) {

        try {

            //ͨ���˿���ʶ��˿�

            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            //�򿪶˿ڣ������˿����ֺ�һ��timeout

            CommPort commPort = portIdentifier.open(portName, 2000);

            //�ж��ǲ��Ǵ���

            if (commPort instanceof SerialPort) {

                SerialPort serialPort = (SerialPort) commPort;

                try {

                    //����һ�´��ڵĲ����ʵȲ���

                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                } catch (UnsupportedCommOperationException e) {

                    System.out.println("COM Open error: failure "+ e);
                    mComStatus.setText(portName+"��ʧ��");
                    return null;
                }

                System.out.println("COM Open: " + portName + " sucessfully !");
                mComStatus.setText(portName+"�򿪳ɹ�");
                return serialPort;

            } else {

                System.out.println("COM Open: This port is not a serialport");
                mComStatus.setText(portName+"��ʧ��");
                return null;
            }

        } catch (NoSuchPortException | PortInUseException e) {

            System.out.println("COM Open: There is no " + portName + "or it's occupied! "+ e);
            mComStatus.setText(portName+"��ʧ��");
            return null;

        }

    }

    private void sendToPort(SerialPort serialPort, byte[] order) {
        try {
            OutputStream out = serialPort.getOutputStream();
            out.write(order);
            //out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println("Send to SerialPort failure"+ e);
        }
    }

    private byte[] readFromPort(InputStream inStream) {

        byte[] bytes = null;

        try {

            while (true) {

                //��ȡbuffer������ݳ���

                int bufflenth = inStream.available();

                System.out.println("Read available"+bufflenth);

                if (0 == bufflenth) {

                    break;

                }
                bytes = new byte[bufflenth+10];

                inStream.read(bytes);

                String receive = new String(bytes).trim();
                System.out.println("COM Read:");
                System.out.println(receive);
                //MainUI.UpdateComeData(receive,false);
                mCurrentTestItem.OnReceiveDataFromTerminal(receive);
            }

        } catch (IOException e) {

            System.out.println("Read Data Failure"+e);

        }

        return bytes;

    }

    private class SerialListener implements SerialPortEventListener {

        @Override

        public void serialEvent(SerialPortEvent serialPortEvent) {

            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10ͨѶ�ж�
                    System.out.println("receive SerialPortEvent.BI");
                 break;
                case SerialPortEvent.OE: // 7��λ����
                    System.out.println("receive SerialPortEvent.OE");
                    break;
                case SerialPortEvent.FE: // 9֡����
                    System.out.println("receive SerialPortEvent.FE");
                    break;
                case SerialPortEvent.PE: // 8��żУ���
                    System.out.println("receive SerialPortEvent.PE");
                    break;
                case SerialPortEvent.CD: // 6�ز����
                    System.out.println("receive SerialPortEvent.CD");
                    break;
                case SerialPortEvent.CTS: // 3�������
                    System.out.println("receive SerialPortEvent.CTS");
                    break;
                case SerialPortEvent.DSR: // 4�����豸׼����
                    System.out.println("receive SerialPortEvent.DSR");
                    break;
                case SerialPortEvent.RI: // 5����ָʾ
                    System.out.println("receive SerialPortEvent.RI");
                    break;
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2��������������
                    System.out.println("receive SerialPortEvent.OUTPUT_BUFFER_EMPTY");
                    break;
                case SerialPortEvent.DATA_AVAILABLE: // 1������������ʱ����
                {
                    int i = 0;
                    //byte[] data = readFromPort(mSerialPort);
                    try {
                        InputStream is = mSerialPort.getInputStream();
                        byte[] data = readFromPort(is);

                    }catch (Exception e){
                        System.out.println("COM Read: error "+e);
                    }
                }
                break;
            }

        }

    }

    private static void addListener(SerialPort port, SerialPortEventListener listener) {

        try {

            // ��������Ӽ�����

            port.addEventListener(listener);

            // ���õ������ݵ���ʱ���Ѽ��������߳�

            port.notifyOnDataAvailable(true);

            port.notifyOnBreakInterrupt(true);

            System.out.println("Add listeners to " + port.getName() + " sucessfully !");

        } catch (TooManyListenersException e) {
            System.out.println("There is too many listeners !"+ e);
        }

    }

    public void WriteString(UartAT test,String s){
        if(mSerialPort == null){
            JOptionPane.showMessageDialog(null, "alert", "������ҳ��ѡ�񴮿�!", JOptionPane.ERROR_MESSAGE);
        }

        if(test == null)
            return;

        mCurrentTestItem = test;

        byte[] data = new byte[s.length()+1];
        for(byte b:data){
            b = 0;
        }

        for(int i = 0;i<s.length();i++){
            data[i] = (byte)s.charAt(i);
        }
        sendToPort(mSerialPort,data);
        System.out.println("COM Write:");
        System.out.println(s);
        //MainUI.UpdateComeData(s,true);
    }

    public  void CloseSerialPort(){
        if(mSerialPort != null){
            mSerialPort.removeEventListener();
            mSerialPort.close();
            System.out.println("COM Closed!!!");
            mSerialPort = null;
        }
        mCombo.removeAllItems();
        mCombo.addItem("��ˢ�²�ѡ��˿�");
        mComStatus.setText("");
    }

}
