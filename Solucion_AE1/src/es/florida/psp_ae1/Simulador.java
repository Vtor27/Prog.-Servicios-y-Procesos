package es.florida.psp_ae1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class Simulador {

	private JFrame frmAlphafoldForJava;
	private JLabel lblMP, lblMT;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulador window = new Simulador();
					window.frmAlphafoldForJava.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Simulador() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAlphafoldForJava = new JFrame();
		frmAlphafoldForJava.setTitle("AlphaFold for Java");
		frmAlphafoldForJava.setBounds(100, 100, 475, 399);
		frmAlphafoldForJava.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAlphafoldForJava.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Primary structure (type 1):");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(29, 78, 167, 26);
		frmAlphafoldForJava.getContentPane().add(lblNewLabel);

		JLabel lblJavaProteinSimulator = new JLabel("Java Protein Simulator");
		lblJavaProteinSimulator.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblJavaProteinSimulator.setBounds(29, 30, 199, 26);
		frmAlphafoldForJava.getContentPane().add(lblJavaProteinSimulator);

		JLabel lblSecondaryStructuretype = new JLabel("Secondary structure (type 2):");
		lblSecondaryStructuretype.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSecondaryStructuretype.setBounds(29, 114, 167, 26);
		frmAlphafoldForJava.getContentPane().add(lblSecondaryStructuretype);

		JLabel lblNewLabel_1_1 = new JLabel("Tertiary structure (type 3):");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1.setBounds(29, 150, 167, 26);
		frmAlphafoldForJava.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("Quaternary structure (type 4):");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1_1.setBounds(29, 186, 181, 26);
		frmAlphafoldForJava.getContentPane().add(lblNewLabel_1_1_1);

		JSpinner spinnerType1 = new JSpinner();
		spinnerType1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerType1.setBounds(208, 81, 50, 20);
		frmAlphafoldForJava.getContentPane().add(spinnerType1);

		JSpinner spinnerType2 = new JSpinner();
		spinnerType2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerType2.setBounds(208, 117, 50, 20);
		frmAlphafoldForJava.getContentPane().add(spinnerType2);

		JSpinner spinnerType3 = new JSpinner();
		spinnerType3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerType3.setBounds(208, 153, 50, 20);
		frmAlphafoldForJava.getContentPane().add(spinnerType3);

		JSpinner spinnerType4 = new JSpinner();
		spinnerType4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerType4.setBounds(208, 189, 50, 20);
		frmAlphafoldForJava.getContentPane().add(spinnerType4);

		JButton btnSimulate = new JButton("Simulate");
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int type1 = (Integer) spinnerType1.getValue();
				int type2 = (Integer) spinnerType2.getValue();
				int type3 = (Integer) spinnerType3.getValue();
				int type4 = (Integer) spinnerType4.getValue();
				runSims(type1, type2, type3, type4);
			}
		});
		btnSimulate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSimulate.setBounds(320, 190, 85, 21);
		frmAlphafoldForJava.getContentPane().add(btnSimulate);

		JLabel lblMultiprocess = new JLabel("Multiprocess:");
		lblMultiprocess.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMultiprocess.setBounds(117, 264, 85, 26);
		frmAlphafoldForJava.getContentPane().add(lblMultiprocess);

		JLabel lblMultithread = new JLabel("Multithread:");
		lblMultithread.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMultithread.setBounds(117, 300, 85, 26);
		frmAlphafoldForJava.getContentPane().add(lblMultithread);

		lblMP = new JLabel("0.00 s");
		lblMP.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMP.setBounds(212, 264, 85, 26);
		frmAlphafoldForJava.getContentPane().add(lblMP);

		lblMT = new JLabel("0.00 s");
		lblMT.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMT.setBounds(212, 300, 85, 26);
		frmAlphafoldForJava.getContentPane().add(lblMT);
	}

	private void runSims(int type1, int type2, int type3, int type4) {

		// Multiprocess
		double startTimeMP = System.currentTimeMillis();
		int count = 1;
		Process p = null;
		for (int i = 0; i < type1; i++) {
			p = runMP(1, count);
			count++;
		}
		for (int i = 0; i < type2; i++) {
			p = runMP(2, count);
			count++;
		}
		for (int i = 0; i < type3; i++) {
			p = runMP(3, count);
			count++;
		}
		for (int i = 0; i < type4; i++) {
			p = runMP(4, count);
			count++;
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		double endTimeMP = System.currentTimeMillis();
		int seconds = (int) ((endTimeMP - startTimeMP) / 1000);
		int ms = (int) ((endTimeMP - startTimeMP) % 1000);
		int cs = ms / 10;
		String timeMP = seconds + "." + cs + "s";
		lblMP.setText(timeMP);
		
		//Multithread
		double startTimeMT = System.currentTimeMillis();
		count = 1;
		Thread t = null;
		SimuladorMT simMT;
		for (int i = 0; i < type1; i++) {
			simMT = new SimuladorMT(1, count);
			t = new Thread(simMT);
			t.start();
			count++;
		}
		for (int i = 0; i < type2; i++) {
			simMT = new SimuladorMT(2, count);
			t = new Thread(simMT);
			t.start();
			count++;
		}
		for (int i = 0; i < type3; i++) {
			simMT = new SimuladorMT(3, count);
			t = new Thread(simMT);
			t.start();
			count++;
		}
		for (int i = 0; i < type4; i++) {
			simMT = new SimuladorMT(4, count);
			t = new Thread(simMT);
			t.start();
			count++;
		}
		try {
			t.join();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		double endTimeMT = System.currentTimeMillis();
		seconds = (int) ((endTimeMT - startTimeMT) / 1000);
		ms = (int) ((endTimeMT - startTimeMT) % 1000);
		cs = ms / 10;
		String timeMT = seconds + "." + cs + "s";
		lblMT.setText(timeMT);
		
	}

	private Process runMP(int type, int count) {
		Process p = null;
		try {
			String clase = "es.florida.psp_ae1.SimuladorMP";
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classpath = System.getProperty("java.class.path");
			String className = clase;
			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classpath);
			command.add(className);
			command.add(String.valueOf(type));
			command.add(String.valueOf(count));
			ProcessBuilder builder = new ProcessBuilder(command);
			p = builder.inheritIO().start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return p;
	}
}
