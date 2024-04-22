package org.decodev.exo1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SearchAverageClient extends JFrame implements ActionListener {

    Socket socket = null;

    DataOutputStream dos = null;
    DataInputStream dis = null;
    private JButton jbtGetAverage = new JButton("Greater than 10?");
//    private JButton jbtGetAverage = new JButton("Get Average");
    private JTextField jtfName = new JTextField();
    private JTextField jtfAverage = new JTextField();

    public SearchAverageClient(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.add(new JLabel("Name"));
        panel.add(jtfName);
        panel.add(new JLabel("Result"));
//        panel.add(new JLabel("Average"));
        panel.add(jtfAverage);
        add(panel, BorderLayout.CENTER);
        add(jbtGetAverage, BorderLayout.SOUTH);
        jbtGetAverage.addActionListener(this);

        setTitle("Search Average Client");
        setSize(350, 150);
        setLocationRelativeTo(null);
    }

    public void init(){
        try{
            socket = new Socket("localhost",7777);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void actionPerformed(ActionEvent event) {
        boolean result = false;
//        double score = 0;
        try{
            System.out.println(jtfName.getText());
            dos.writeUTF(jtfName.getText());
            dos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            result = dis.readBoolean();
//            score = dis.readDouble();
//            if(score < 0){
//                jtfAverage.setText("Name not found");
//            }else{
//                jtfAverage.setText(score + "");
//            }

            jtfAverage.setText(result ? "True" : "False");
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    public static void main(String[] args){
        SearchAverageClient client = new SearchAverageClient();
        client.init();
        client.setVisible(true);
    }

}
