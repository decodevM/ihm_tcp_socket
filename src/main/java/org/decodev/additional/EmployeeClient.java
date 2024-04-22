package org.decodev.additional;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class EmployeeClient extends JFrame implements ActionListener {

    private Socket socket = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private JButton jbtGetResult = new JButton("Get Result");
    private JTextField jtfInputValue = new JTextField();
    private JTextField jtfResult = new JTextField();
    private JRadioButton retirementOption = new JRadioButton("Retirement", true);
    private JRadioButton birthdayOption = new JRadioButton("Birthday", false);
    private JRadioButton birthdayAndWorkYearsOption = new JRadioButton("Birthday & Work years", false);
    private int radioButtonSelected = 1;
    private CustomActionAdapter buttonRadioListener = new CustomActionAdapter() {
        public void actionPerformed(ActionEvent e) {

            switch (e.getActionCommand()){
                case "Retirement":
                    radioButtonSelected = 1;
                    retirementOption.setSelected(true);
                    birthdayOption.setSelected(false);
                    birthdayAndWorkYearsOption.setSelected(false);
                    break;
                case "Birthday":
                    radioButtonSelected = 2;
                    retirementOption.setSelected(false);
                    birthdayOption.setSelected(true);
                    birthdayAndWorkYearsOption.setSelected(false);
                    break;
                case "Birthday & Work years":
                    radioButtonSelected = 3;
                    retirementOption.setSelected(false);
                    birthdayOption.setSelected(false);
                    birthdayAndWorkYearsOption.setSelected(true);
                    break;
                default: radioButtonSelected = 1;
            }
        }
    };

    public EmployeeClient() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        jtfResult.setEnabled(false);

        retirementOption.addActionListener(buttonRadioListener);
        birthdayOption.addActionListener(buttonRadioListener);
        birthdayAndWorkYearsOption.addActionListener(buttonRadioListener);
        panel.add(new JLabel("Option 1"));
        panel.add(retirementOption);
        panel.add(new JLabel("Option 2"));
        panel.add(birthdayOption);
        panel.add(new JLabel("Option 3"));
        panel.add(birthdayAndWorkYearsOption);


        panel.add(new JLabel("Input Value"));
        panel.add(jtfInputValue);
        panel.add(new JLabel("Result"));
        panel.add(jtfResult);
        add(panel, BorderLayout.CENTER);
        add(jbtGetResult, BorderLayout.SOUTH);
        jbtGetResult.addActionListener(this);

        setTitle("Search for Employee");
        setSize(350, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        try {
            socket = new Socket("localhost", 7777);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String result = "";
        try {
            String input = jtfInputValue.getText();
            input = radioButtonSelected + "|" + input;
            dos.writeUTF(input);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending data to server: " + e.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            result = dis.readUTF();
            jtfResult.setText(result);
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error receiving data from server: " + e1.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EmployeeClient client = new EmployeeClient();
        client.init();
        client.setVisible(true);
    }
}


class CustomActionAdapter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}