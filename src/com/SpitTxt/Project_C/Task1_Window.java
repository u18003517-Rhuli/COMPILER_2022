package com.SpitTxt.Project_C;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Task1_Window extends JPanel{

    private JTable table = new JTable();
    private JScrollPane scrollPane = new JScrollPane(table);
    //private ArrayList<JPanel> pages;

    //private JButton christmasTimeButton ;
    //private JLabel infoLabel;
    private JButton loadCSVButton;

    private Timer timer;

    Task1_Window()  {
        JPanel thisObject = this;

        /**items**/
        JLabel currentTimeLabel = new JLabel("-");
        currentTimeLabel.setBounds(new Rectangle(350,50,200,50));
        //currentTimeLabel.setPreferredSize(new Dimension(200,50));

        JLabel infoLabel = new JLabel("please select file representing your code");
        infoLabel.setBounds(new Rectangle(350,100,350,50));


        //JButton christmasTimeButton = new JButton("Days until christmas");
        //christmasTimeButton.setBounds(new Rectangle(50,100,200,50));

        JButton loadCSVButton = new JButton("Load file");
        loadCSVButton.setBounds(new Rectangle(50,100,200,50));


        /**adding event listener**
        christmasTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                christmasTimeLabel.setText("Days until christmas : " + showChrismasTime());
            }
        });*/

        loadCSVButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(this, textBox.getText());

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("text files", "csv", "txt"));
                fileChooser.setCurrentDirectory(new File("."));

                if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    //getting data from file
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                    try {
                        SpitTxt spitTxt = new SpitTxt (filePath);
                    //if (SpitTxt.isDone() ==true){
                    } catch (IOException ex) {

                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Failed running compiler (check output folder)", "Error", JOptionPane.INFORMATION_MESSAGE);
                    } catch ( Exception b) {

                        b.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Failed running compiler systems", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }


                    try {
                        File htmlFile = new File("C:/Users/Rhuli/IdeaProjects/COS_341_COMPILER_2022/src/com/SpitTxt/Project_C/compiler.html");
                        //File htmlFile = new File("compiler.html");
                        //Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        Desktop.getDesktop().browse(htmlFile.toURI());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Failed opening compiler results (check html/xml)", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                    //}

                    infoLabel.setText("done. compiling complete");


                    /*Object[][] dataValues = showLoadedCSV(filePath);
                    String[] headings = new String[dataValues[0].length];

                    for(int i =0; i < dataValues[0].length; i++) {
                        headings[i] = (String) dataValues[0][i];
                        System.out.println(headings[i]);
                    }

                    //setup table & view
                    table = new JTable(dataValues,headings);
                    table.setPreferredScrollableViewportSize(new Dimension(500,250));
                    table.setFillsViewportHeight(true);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                    for(int i =0; i < headings.length; i++) {
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(200);
                    }


                    scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setBounds(new Rectangle(300,200,500,250));
                    thisObject.add(scrollPane, BorderLayout.CENTER);*/
                }
            }
            /*********************************SHOW HTML***************************************/



            /*try {
                Runtime r = Runtime.getRuntime();
                r.exec(browser + ” ” + file.getPath());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, “Not a valid file!”, “ERROR!”,
                JOptionPane.ERROR_MESSAGE);
            }


            Runtime rTime = Runtime.getRuntime();
            String url = http://www.mywebserver.net/somepage.html
            String command = “/path/to/internet/explorer/iexplore.exe ” + url;
            rTime.exec(command);*/
        });


        /**panel properties**/
        this.setLayout(null);
        this.add(currentTimeLabel);
        //this.add(christmasTimeButton);
        this.add(infoLabel);
        this.add(loadCSVButton);
        this.setBackground(Color.CYAN);


        /**update clock**/
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss a");
                LocalDateTime now = LocalDateTime.now();
                String date = formatter.format(now);
                currentTimeLabel.setText(date);
            }
        }, 0, 1000);

    }


    public JPanel getPanel(){
        return this;
    }

    private String showChrismasTime(){
        LocalDate today = LocalDate.now();
        LocalDate christmasDay = LocalDate.of(today.getYear(),12,25);

        return Long.toString(today.until(christmasDay, ChronoUnit.DAYS));
    }

    private Object[][] showLoadedCSV(String filePath){ //return csv data as object array
        File csvFile = new File(filePath);

        ArrayList<ArrayList<Object>> list = new ArrayList<>();


        try {
            BufferedReader bReader  = new BufferedReader(new FileReader(csvFile));
            String line = "";
            int count = 0;

            while ((line = bReader.readLine()) != null){
                ArrayList<Object> innerList = new ArrayList<>();
                count = count +1;

                String[] values = line.split(",");
                for(int i =0 ; i < values.length; i++){
                    innerList.add(values[i]);
                }

                list.add(innerList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object[][] fileOutput = new Object[list.size()][list.get(0).size()];

        for(int i=0; i < list.size(); i++){
            fileOutput[i] = list.get(i).toArray();
        }

        return fileOutput;
    }

}



