package org.decodev.exo3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class CalculatorClient {
      
    public static void main(String[] args) throws IOException {
        
        Socket client = new Socket("localhost", 6666); 
        Runnable runner = new Runnable() {      
            
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());
        
            JFrame frame;
            String requete;
            String resultat;
            String [] opps = {"ADD", "MUL", "SOUS", "DIV", "PUIS"};
            JTextField opd1;
            JTextField opd2;
            JComboBox<String> opperation;
            JButton btn;
            JLabel lblResult;
            boolean erreur;
            public void run() {
                frame = new JFrame("Calculatrice");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JMenuBar menu = new JMenuBar();
                JMenu calculatrice = new JMenu("Calculatrice");
                JMenu help = new JMenu("?");
                JMenuItem exit = new JMenuItem("Quitter");
                JMenuItem about = new JMenuItem("A propos");
                exit.addActionListener(this::menuExitListener);
                about.addActionListener(this::menuAboutListener);
                calculatrice.add(exit);
                help.add(about);
                menu.add(calculatrice);
                menu.add(help);
                    
                JPanel panel = new JPanel(new GridLayout(4, 0));                
                JPanel panelCalc = new JPanel();
                JPanel panelResult = new JPanel();
                      
                opd1 = new JTextField();
                opd2 = new JTextField();
                opd1.setPreferredSize(new Dimension(100, 20));
                opd2.setPreferredSize(new Dimension(100, 20));                
                opperation = new JComboBox<>(opps);                
                btn = new JButton("Calculer");
                btn.addActionListener(this::btnListener);
                
                panelCalc.add(opd1);
                panelCalc.add(opperation);
                panelCalc.add(opd2);       
                panelCalc.add(btn);
                
                lblResult = new JLabel("Résultat : pas encore calculé");
                lblResult.setFont(new Font("serif", Font.BOLD, 15));
                panelResult.add(lblResult);
                
                panel.add(panelCalc);
                panel.add(panelResult);
                
                frame.add(menu, BorderLayout.NORTH);
                frame.add(panel);
                frame.setSize(480, 200);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
            private void btnListener(ActionEvent event) {
                erreur = false;
                if (opd1.getText().equals("") || opd2.getText().equals("") 
                        || !isNumeric(opd1.getText()) || !isNumeric(opd2.getText())) {
                    lblResult.setForeground(Color.red);
                    lblResult.setText("Veuillez vérifier votre saisie");
                    erreur = true;
                }              
                if(!erreur) {
                                
                    lblResult.setForeground(Color.black);                    
                    requete = opperation.getSelectedItem() + " " + opd1.getText() + " " + opd2.getText();
                    try {
                        dos.writeUTF(requete);
                        resultat = dis.readUTF();
                    } catch (IOException ex) {
                    }
                    lblResult.setText("Résultat : " + resultat);                    
                }          
            }
            
            private void menuExitListener(ActionEvent event) {
		frame.dispose();
            }	
            
            private void menuAboutListener(ActionEvent event) {
		JOptionPane.showMessageDialog(frame, "Calculatrice Client/Serveur\n Badr BENMAMMAR", "A propos",JOptionPane.INFORMATION_MESSAGE);
            }
        };
        EventQueue.invokeLater(runner);
    }   
    
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }
}
