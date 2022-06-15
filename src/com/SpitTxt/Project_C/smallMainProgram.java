package com.SpitTxt.Project_C;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class smallMainProgram {

    private static JPanel programPanel;

    //private static Task4_cSorterImplementation programSorter;

    public static void main(String[] args) {

        //TestFileCreator output = new TestFileCreator();

        Task1_Window programWindow =  new Task1_Window();
        //Task3_Distance programDistance = new Task3_Distance();
        //programSorter = new Task4_cSorterImplementation();




        ArrayList<JPanel> pages = new ArrayList<>();
        pages.add(programWindow.getPanel());
        //pages.add(programDistance.getPanel());
        //pages.add(programSorter.getPanel());

        //program
        JFrame programFrame = new JFrame("Small Program");

        programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        programFrame.setTitle("Widow task");
        programFrame.setSize(1250,750);
        programFrame.setLayout(new BorderLayout(15,0));
        programFrame.getContentPane().setBackground(new Color(13, 14, 20));  //new Color(13, 14, 20)


        //components

        JLabel menuLabel = new JLabel("DEFAULT");
        menuLabel.setForeground(Color.lightGray);

        /*JButton buttonPage1 = new JButton("PAGE 1 (Default)");

        JButton buttonPage2 = new JButton("PAGE 2");
        JButton buttonPage3 = new JButton("PAGE 3");

        buttonPage2.setPreferredSize(new Dimension(150, 30));
        buttonPage3.setPreferredSize(new Dimension(150, 30));

        navPanel.add(buttonPage2);
        navPanel.add(buttonPage3);





        JButton buttonSave = new JButton("SAVE");
        JButton buttonClear = new JButton("CLEAR");
        JButton buttonExit = new JButton("EXIT");

        buttonSave.setPreferredSize(new Dimension(80, 25));
        buttonClear.setPreferredSize(new Dimension(80, 25));
        buttonExit.setPreferredSize(new Dimension(80, 25));

        navPanel.add(buttonSave);
        navPanel.add(buttonClear);
        navPanel.add(buttonExit);*/



        //buttonPage1.setPreferredSize(new Dimension(150, 30));




        JPanel navPanel = new JPanel(new FlowLayout(0,25,50));

        navPanel.add(menuLabel);
        //navPanel.add(buttonPage1);





        navPanel.setPreferredSize(new Dimension(200,50)); //height not affected
        navPanel.setBackground(Color.DARK_GRAY);

        programPanel = new JPanel(new BorderLayout(100,100));
        //JPanel programPanel = progamWindow.getPages().get(0);
        programPanel.setBackground(Color.BLUE);
        programPanel.add(pages.get(0), BorderLayout.CENTER);



        programFrame.add(navPanel, BorderLayout.WEST);
        programFrame.add(programPanel, BorderLayout.CENTER);
        programFrame.setVisible(true);

        /**Listeners**/

        /*programFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(programDistance.getIsListChanged() == true || programSorter.getIsListChanged() == true) {

                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Notice. Found unsaved data, save them?", "Unsaved data",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {
                        programDistance.saveDistances();
                        programSorter.saveList();
                        JOptionPane.showMessageDialog(null,"Saved.", "Data saved", JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                programFrame.dispose();
            }
        });*/

        /*buttonPage1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuLabel.setText("DEFAULT");
                programPanel.remove(0);
                programPanel.add(pages.get(0));
                programFrame.revalidate();
                programFrame.repaint();
            }
        });




        buttonPage2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menuLabel.setText("DISTANCES");
                programPanel.remove(0);
                programPanel.add(pages.get(1));
                programFrame.revalidate();
                programFrame.repaint();
            }
        });


        buttonPage3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuLabel.setText("SORTED LIST");
                programPanel.remove(0);
                programPanel.add(pages.get(2));
                programFrame.revalidate();
                programFrame.repaint();

            }
        });*/


        /*buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Save all data?", "Save data",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null,"Saved.", "Data saved", JOptionPane.INFORMATION_MESSAGE);
                    programDistance.saveDistances();
                    programSorter.saveList();
                }
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete distance data?", "Delete distance data",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    programDistance.deleteDistances();
                    JOptionPane.showMessageDialog(null,"Deleted.", "Data deleted \"distance\"", JOptionPane.INFORMATION_MESSAGE);
                }

                confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete sorted list data?", "Delete list data",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    programSorter.deleteList();
                    JOptionPane.showMessageDialog(null,"Deleted.", "Data deleted \"list \"", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(programDistance.getIsListChanged() == true || programSorter.getIsListChanged() == true) {

                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Notice. Found unsaved data, save them?", "Unsaved data",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {
                        programDistance.saveDistances();
                        programSorter.saveList();
                        JOptionPane.showMessageDialog(null,"Saved.", "Data saved", JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                programFrame.dispose();
                System.exit(0);
            }
        });*/

    }
}
