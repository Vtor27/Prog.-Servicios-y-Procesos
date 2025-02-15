package florida.AppProteinas;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Vista {

	private JFrame frame;
	private JButton btnSimular;
	private JTextArea textArea_Multihilo;
	private JTextArea textArea_MultiProceso;
	private JTextField inputPrimaria;
	private JTextField inputSecundaria;
	private JTextField inputTerciaria;
	private JTextField inputCuaternaria;

	/**
	 * Create the application.
	 */
	public Vista() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.setBounds(100, 100, 1024, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_MP = new JScrollPane();
		scrollPane_MP.setBounds(36, 235, 436, 283);
		frame.getContentPane().add(scrollPane_MP);
		
		textArea_MultiProceso = new JTextArea();
		scrollPane_MP.setViewportView(textArea_MultiProceso);
		
		JScrollPane scrollPane_MT = new JScrollPane();
		scrollPane_MT.setBounds(534, 235, 436, 283);
		frame.getContentPane().add(scrollPane_MT);
		
		textArea_Multihilo = new JTextArea();
		scrollPane_MT.setViewportView(textArea_Multihilo);
		
		JLabel labelMultihilo = new JLabel("Simulación Multiproceso");
		labelMultihilo.setFont(new Font("Arial", Font.PLAIN, 14));
		labelMultihilo.setBounds(164, 205, 163, 28);
		frame.getContentPane().add(labelMultihilo);
		
		JLabel labelMultiproceso = new JLabel("Simulación Multihilo");
		labelMultiproceso.setFont(new Font("Arial", Font.PLAIN, 14));
		labelMultiproceso.setBounds(706, 205, 140, 28);
		frame.getContentPane().add(labelMultiproceso);
		
		JLabel labelTitulo = new JLabel("Simulación Multiproceso");
		labelTitulo.setFont(new Font("Arial Black", Font.PLAIN, 25));
		labelTitulo.setBounds(292, 11, 398, 42);
		frame.getContentPane().add(labelTitulo);
		
		JLabel lblProteínaPrimaria = new JLabel("Proteína primaria");
		lblProteínaPrimaria.setFont(new Font("Arial", Font.PLAIN, 14));
		lblProteínaPrimaria.setBounds(149, 60, 178, 22);
		frame.getContentPane().add(lblProteínaPrimaria);
		
		JLabel lblProteínaSecundaria = new JLabel("Proteína secundaria");
		lblProteínaSecundaria.setFont(new Font("Arial", Font.PLAIN, 14));
		lblProteínaSecundaria.setBounds(149, 93, 178, 22);
		frame.getContentPane().add(lblProteínaSecundaria);
		
		JLabel lblProtenaTerciaria = new JLabel("Proteína terciaria");
		lblProtenaTerciaria.setFont(new Font("Arial", Font.PLAIN, 14));
		lblProtenaTerciaria.setBounds(406, 60, 178, 22);
		frame.getContentPane().add(lblProtenaTerciaria);
		
		JLabel lblProtenaCuaternaria = new JLabel("Proteína cuaternaria");
		lblProtenaCuaternaria.setFont(new Font("Arial", Font.PLAIN, 14));
		lblProtenaCuaternaria.setBounds(406, 93, 178, 22);
		frame.getContentPane().add(lblProtenaCuaternaria);
		
		inputPrimaria = new JTextField();
		inputPrimaria.setFont(new Font("Arial", Font.PLAIN, 13));
		inputPrimaria.setBounds(93, 62, 46, 20);
		frame.getContentPane().add(inputPrimaria);
		inputPrimaria.setColumns(10);
		
		inputSecundaria = new JTextField();
		inputSecundaria.setFont(new Font("Arial", Font.PLAIN, 13));
		inputSecundaria.setColumns(10);
		inputSecundaria.setBounds(93, 95, 46, 20);
		frame.getContentPane().add(inputSecundaria);
		
		inputTerciaria = new JTextField();
		inputTerciaria.setFont(new Font("Arial", Font.PLAIN, 13));
		inputTerciaria.setColumns(10);
		inputTerciaria.setBounds(350, 62, 46, 20);
		frame.getContentPane().add(inputTerciaria);
		
		inputCuaternaria = new JTextField();
		inputCuaternaria.setFont(new Font("Arial", Font.PLAIN, 13));
		inputCuaternaria.setColumns(10);
		inputCuaternaria.setBounds(350, 95, 46, 20);
		frame.getContentPane().add(inputCuaternaria);
		
		btnSimular = new JButton("SIMULAR");
		btnSimular.setFont(new Font("Arial Black", Font.PLAIN, 18));
		btnSimular.setBounds(656, 62, 172, 42);
		frame.getContentPane().add(btnSimular);
		
		this.frame.setVisible(true);
	}
	
	public JButton getBtnSimular() {
		return btnSimular;
	}
	
	public JTextArea getTextArea_MT() {
		return textArea_Multihilo;
	}
	
	public JTextArea getTextArea_MP() {
		return textArea_MultiProceso;
	}
	
	public JTextField getInputPrimaria() {
		return inputPrimaria;
	}
	
	public JTextField getInputSecundaria() {
		return inputSecundaria;
	}
	
	public JTextField getInputTerciaria() {
		return inputTerciaria;
	}
	
	public JTextField getInputCuaternaria() {
		return inputCuaternaria;
	}
	
	
}
