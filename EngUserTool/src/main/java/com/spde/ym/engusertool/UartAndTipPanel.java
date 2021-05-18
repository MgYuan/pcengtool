package com.spde.ym.engusertool;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UartAndTipPanel extends JPanel {
    private UartManager uartManager = new UartManager();
    private JButton retestB = new JButton("�����������һ̨");
    private JButton exitB = new JButton("�ر�");
    private  JLabel labelTip = new JLabel("���ȵ�ˢ�£�Ȼ�����б���ѡ������ѡ��ť������");
    private  JLabel UserManu = new JLabel("�벻Ҫֱ�Ӱ���,���������һ̨,���߹رպ��ٰγ�");

    public UartAndTipPanel(){
        GridLayout gridLayout = new GridLayout(2,1,0,0);
        setLayout(gridLayout);

        JPanel topPanel = new JPanel();
        GridLayout tgridLayout = new GridLayout(2,1,0,0);
        topPanel.setLayout(tgridLayout);
        topPanel.add(labelTip);
        topPanel.add(uartManager);
        labelTip.setHorizontalAlignment(JLabel.CENTER);
        labelTip.setForeground(Color.RED);

        JPanel buttonPan = new JPanel();
        GridLayout buttonPLayout = new GridLayout(1,2,0,0);
        buttonPan.setLayout(buttonPLayout);
        buttonPan.add(retestB);
        buttonPan.add(exitB);

        JPanel bottomPanel = new JPanel();
        GridLayout bgridLayout = new GridLayout(2,1,0,0);
        bottomPanel.setLayout(bgridLayout);
        bottomPanel.add(UserManu);
        bottomPanel.add(buttonPan);
        UserManu.setHorizontalAlignment(JLabel.CENTER);
        UserManu.setForeground(Color.RED);

        add(topPanel);
        add(bottomPanel);

        retestB.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //mlayout.first(mainContent);
                uartManager.CloseSerialPort();
                MainUI.Reset();
            }
        });

        exitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                uartManager.CloseSerialPort();
                System.exit(0);
            }
        });
    }

    public UartManager getUartManager() {
        return uartManager;
    }
}
