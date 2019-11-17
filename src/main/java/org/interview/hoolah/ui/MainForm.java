package org.interview.hoolah.ui;

import org.interview.hoolah.ReportService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MainForm extends JFrame {

    File file;
    JFileChooser jf = new JFileChooser();
    ReportService service = new ReportService();
    JComboBox<String> cboMerchant;

    SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public MainForm() {
        super();
        init();
    }

    public void init() {
        // file choose
        jf.setAcceptAllFileFilterUsed(false);
        jf.setMultiSelectionEnabled(false);
        jf.setApproveButtonText("Select");
//        jf.addChoosableFileFilter();
        jf.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV";
            }
        });


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // let app exit on clicking cross sign
        getContentPane().setLayout(new BorderLayout());

        // north
        JPanel pnlNorth = new JPanel();
        pnlNorth.setBorder(new TitledBorder("Step 1. Input"));
        pnlNorth.add(new JLabel("Choose CSV File"));
        JButton btnChoose = (JButton) pnlNorth.add(new JButton("Choose file"));

        btnChoose.addActionListener(e -> {
            if (jf.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                file = jf.getSelectedFile();
                cboMerchant.removeAllItems();
                try {
                    service.setFile(file).forEach(item -> cboMerchant.addItem(item));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // center
        JPanel pnlCenter = new JPanel(new SpringLayout()); // form style layout
        pnlCenter.setBorder(new TitledBorder("Step 2. Report Criteria"));
        pnlCenter.add(new JLabel("From Date : ")); // From Date Label
        // -- date format
        DateFormatter displayFormatter = new DateFormatter(inputFormatter);
        DefaultFormatterFactory factory = new DefaultFormatterFactory(displayFormatter,
                displayFormatter, displayFormatter);

        JFormattedTextField txtFromDate = (JFormattedTextField) pnlCenter.add(new JFormattedTextField()); // from input field


        pnlCenter.add(new JLabel("To Date : ")); // To Date Label
        JFormattedTextField txtToDate = (JFormattedTextField) pnlCenter.add(new JFormattedTextField()); // to input field
        // -- adding formatter
        txtFromDate.setFormatterFactory(factory);
        txtToDate.setFormatterFactory(factory);

        // resize component
        txtFromDate.setSize(180, 20);

        pnlCenter.add(new JLabel("Merchant : ")); // Merchant Label
        cboMerchant = (JComboBox<String>) pnlCenter.add(new JComboBox()); // Merchant Label

        SpringUtilities.makeGrid(pnlCenter, 3, 2, 5, 5, 5, 5); // make panel to grid form // must set after all components have been added

        // south
        JPanel pnlSouth = new JPanel();
        pnlSouth.setBorder(new TitledBorder("Step 3. Generation"));
        pnlSouth.add(new JLabel("Choose CSV File"));
        JButton btnGenerate = (JButton) pnlSouth.add(new JButton("Generate"));
        btnGenerate.addActionListener(e -> {
            if (service.isEmpty() || cboMerchant.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No Record found", "Warning", JOptionPane.ERROR_MESSAGE);

            } else {
                Map<String, Object> map = service.generate((Date) txtFromDate.getValue(), (Date) txtToDate.getValue(), cboMerchant.getSelectedItem().toString());
                System.out.println(map);
                if ((int)map.get("trans") == 0) {
                    JOptionPane.showMessageDialog(this, "No Record found", "Warning", JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(this, "Number of Transactions :" + map.get("trans") + "\nAverage : " + map.get("avg"), "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // panels to form
        getContentPane().add(pnlNorth, BorderLayout.NORTH);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);
        getContentPane().add(pnlSouth, BorderLayout.SOUTH);
        setSize(360, 333);
        setVisible(true);
        addComponentListener(new ComponentAdapter() // for debug and checking size
        {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                System.out.println(c.getSize());
                //........
            }
        });
    }
}
