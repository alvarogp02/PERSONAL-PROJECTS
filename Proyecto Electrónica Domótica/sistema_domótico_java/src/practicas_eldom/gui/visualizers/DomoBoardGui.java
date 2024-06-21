package practicas_eldom.gui.visualizers;

import java.awt.Component;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import CommTransport.CommTransport;
import Utilidades.modbus.ModBus_Communications;
import eu.hansolo.steelseries.extras.LightBulb;
import modbus.Const_Modbus;
import modbus.ModBusEvent;
import practicas_eldom.config.MB_Registers;
import practicas_eldom.config.MB_Registers.MB_Analog_Input_Register;
import practicas_eldom.config.MB_Registers.MB_Analog_Output_Holding;
import practicas_eldom.config.MB_Registers.MB_Discrete_Input_Contacts;
import practicas_eldom.config.MB_Registers.MB_Discrete_Output_Coils;
import practicas_eldom.config.MB_Registers.TSwitchState;

import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import eu.hansolo.steelseries.extras.Led;
import javax.swing.border.LineBorder;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import eu.hansolo.steelseries.gauges.Radial2Top;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.PointerType;
import eu.hansolo.steelseries.gauges.DisplaySingle;
import eu.hansolo.steelseries.gauges.DigitalRadial;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JSlider;

public class DomoBoardGui extends JPanel implements Visualizer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8619767299083215147L;
	private 		MouseAdapter 		ma_lightBulb;
	private final 	String 				category;
	private final 	boolean				isCategory = true;
	private final	CommTransport 		sn_Transport;
	private final 	int					address;
	private			LightBulb 			lightBulb1;
	private			LightBulb 			lightBulb2;
	private			Led 				ledBtn1;
	private			Led 				ledBtn2;
	private			Led 				ledBtnOpt;
	private         JCheckBox 			cbActPIR;
	private			Led 				ledPIR;
	private			Radial2Top 			r2T_Pot1;
	private			Radial2Top 			r2T_Pot2;
	private 		JCheckBox 			cbActSRC;
	private 		JCheckBox 			cbActTTOR;
	private			JCheckBox 			actDIMM;
	private			JCheckBox 			actDIMM2;
	private			JCheckBox			actPOT;
	private			JCheckBox			actPOT2;
	private			JSlider 			slider_1;
	private			JSlider 			slider;
	private 		DigitalRadial 		dRSRC;
	private			DisplaySingle 		dSTemp;
	private			DigitalRadial 		dRTtor;
	
	//Banco de registros para mantener sincronizada la comunicación Modbus 
	private  		int 				Cregs[];
	private  		int 				Dregs[];
	private         int 				Aregs[];
	private         int 				Iregs[];
	
	private			boolean     		stActualize = true;
	private 		JTextField 			tiempoPIR;
	private 		JTextField 			tf_HL_SRC;
	private 		JTextField 			tf_LL_SRC;
	private 		JTextField 			tf_LL_ttor;
	
	private			JLabel 				lbStadoPersiana; 
	private			JLabel 				label_PosPer;

	public DomoBoardGui(String category, int address, CommTransport sn_Transport) {
		
		super();
		
		this.category 		= category;
		this.address		= address;
		this.sn_Transport 	= sn_Transport;
		
		this.setLayout(null);
		
		//Crea Banco de registros para mantener sincronizada la comunicación Modbus 
		Cregs = new int [MB_Registers.MB_Discrete_Output_Coils.MB_O_COILS.getReg()];
		Dregs = new int [MB_Registers.MB_Discrete_Input_Contacts.MB_I_REGS.getReg()];
		Aregs = new int [MB_Registers.MB_Analog_Output_Holding.MB_AREGS.getReg()];
		Iregs = new int [MB_Registers.MB_Analog_Input_Register.MB_I_AREGS.getReg()];
		
		ma_lightBulb = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ONOFF_Bulb(((LightBulb)e.getComponent()));
			}
		};
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel.setBounds(10, 22, 172, 100);
		add(panel);
		
		JLabel label = new JLabel("Estado Pulsadores");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label.setBounds(0, 75, 166, 14);
		panel.add(label);
		
		ledBtn1 = new Led();
		ledBtn1.setBounds(11, 11, 36, 36);
		panel.add(ledBtn1);
		
		ledBtn2 = new Led();
		ledBtn2.setBounds(67, 11, 36, 36);
		panel.add(ledBtn2);
		
		ledBtnOpt = new Led();
		ledBtnOpt.setBounds(120, 11, 36, 36);
		panel.add(ledBtnOpt);
		
		JLabel label_1 = new JLabel("BTN 1");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(11, 43, 36, 14);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("BTN 2");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(67, 43, 36, 14);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("BTN_OPT");
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(110, 43, 56, 14);
		panel.add(label_3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 255), 2));
		panel_1.setBounds(199, 11, 215, 123);
		add(panel_1);
		panel_1.setLayout(null);
		
		lightBulb1 = new LightBulb();
		lightBulb1.setOn(true);
		lightBulb1.setGlowColor(Color.RED);
		lightBulb1.setBounds(10, 11, 78, 78);
		panel_1.add(lightBulb1);
		lightBulb1.addMouseListener(ma_lightBulb);
		
		JLabel lblNewLabel = new JLabel("REL\u00C9");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(20, 92, 76, 24);
		panel_1.add(lblNewLabel);
		
		lightBulb2 = new LightBulb();
		lightBulb2.setOn(true);
		lightBulb2.setGlowColor(Color.YELLOW);
		lightBulb2.setBounds(111, 11, 78, 78);
		panel_1.add(lightBulb2);
		lightBulb2.addMouseListener(ma_lightBulb);
		
		JLabel lblRel = new JLabel("TRIAC");
		lblRel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblRel.setBounds(119, 92, 70, 24);
		panel_1.add(lblRel);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_1_1.setBounds(10, 145, 248, 100);
		add(panel_1_1);
		
		ledPIR = new Led();
		ledPIR.setBounds(0, 2, 95, 95);
		panel_1_1.add(ledPIR);
		
		JLabel label_4 = new JLabel("PIR");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.RED);
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_4.setBounds(0, 73, 248, 24);
		panel_1_1.add(label_4);
		
		cbActPIR = new JCheckBox("Activar PIR");
		cbActPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TSwitchState vSel;
				
				if(((AbstractButton) arg0.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTPIR.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		cbActPIR.setSelected(true);
		cbActPIR.setBounds(101, 12, 87, 23);
		panel_1_1.add(cbActPIR);
		
		tiempoPIR = new JTextField();
		tiempoPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					int newval = Integer.parseInt(tiempoPIR.getText());
				
					//int newval = Integer.getInteger(tiempoPIR.getText(), 0);  
				
					if(newval != 0)
						ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_TMP_PIR.getReg(),
								newval, sn_Transport);	
					
				} catch(NumberFormatException ex){
					
				}
								
			}
		});
		
		tiempoPIR.setColumns(10);
		tiempoPIR.setBounds(105, 42, 37, 20);
		panel_1_1.add(tiempoPIR);
		
		JLabel label_5 = new JLabel("Tiempo (Segs.)");
		label_5.setBounds(152, 45, 85, 14);
		panel_1_1.add(label_5);
		
		r2T_Pot1 = new Radial2Top();
		r2T_Pot1.setUserLedVisible(true);
		r2T_Pot1.setUnitString("%");
		r2T_Pot1.setTrackVisible(true);
		r2T_Pot1.setTitle("Pot. 1");
		r2T_Pot1.setPointerType(PointerType.TYPE5);
		r2T_Pot1.setLedColor(LedColor.GREEN);
		r2T_Pot1.setLcdUnitStringVisible(true);
		r2T_Pot1.setLcdScientificFormat(true);
		r2T_Pot1.setLcdInfoString("Test");
		r2T_Pot1.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot1.setLcdBackgroundVisible(false);
		r2T_Pot1.setGaugeType(GaugeType.TYPE5);
		r2T_Pot1.setBounds(10, 256, 193, 193);
		add(r2T_Pot1);
		
		r2T_Pot2 = new Radial2Top();
		r2T_Pot2.setUserLedVisible(true);
		r2T_Pot2.setUnitString("%");
		r2T_Pot2.setTrackVisible(true);
		r2T_Pot2.setTitle("Pot. 2");
		r2T_Pot2.setPointerType(PointerType.TYPE5);
		r2T_Pot2.setLedColor(LedColor.GREEN);
		r2T_Pot2.setLcdUnitStringVisible(true);
		r2T_Pot2.setLcdScientificFormat(true);
		r2T_Pot2.setLcdInfoString("Test");
		r2T_Pot2.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot2.setLcdBackgroundVisible(false);
		r2T_Pot2.setGaugeType(GaugeType.TYPE5);
		r2T_Pot2.setBounds(223, 256, 193, 193);
		add(r2T_Pot2);
		
		dSTemp = new DisplaySingle();
		dSTemp.setLcdValueFont(new Font("Verdana", Font.PLAIN, 9));
		dSTemp.setLcdValue(24.0);
		dSTemp.setLcdUnitString("\u00BAC");
		dSTemp.setLcdUnitFont(new Font("Verdana", Font.PLAIN, 8));
		dSTemp.setLcdInfoString("Temperatura");
		dSTemp.setLcdInfoFont(new Font("Verdana", Font.PLAIN, 12));
		dSTemp.setLcdDecimals(0);
		dSTemp.setFont(new Font("Courier New", Font.PLAIN, 12));
		dSTemp.setCustomLcdUnitFontEnabled(true);
		dSTemp.setCustomLcdUnitFont(new Font("Verdana", Font.PLAIN, 16));
		dSTemp.setBounds(268, 145, 150, 88);
		add(dSTemp);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_2.setBounds(424, 11, 254, 124);
		add(panel_2);
		
		JLabel label_6 = new JLabel("Photo Resistencia");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_6.setBounds(0, 94, 218, 24);
		panel_2.add(label_6);
		
		cbActSRC = new JCheckBox("Activar SRC");
		cbActSRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTSRC.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		cbActSRC.setSelected(true);
		cbActSRC.setBounds(111, 7, 101, 23);
		panel_2.add(cbActSRC);
		
		tf_HL_SRC = new JTextField();
		tf_HL_SRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModBus_Communications.writeSingleRegister(address,  MB_Registers.MB_Analog_Output_Holding.MB_SRC_HL.getReg(),
						Integer.parseInt(tf_HL_SRC.getText()), sn_Transport);	
			}
		});
		tf_HL_SRC.setBounds(112, 37, 37, 20);
		panel_2.add(tf_HL_SRC);
		
		JLabel label_7 = new JLabel("High Level");
		label_7.setBounds(159, 40, 72, 14);
		panel_2.add(label_7);
		
		dRSRC = new DigitalRadial();
		dRSRC.setValue(500.0);
		dRSRC.setMaxValue(1023.0);
		dRSRC.setBounds(10, 7, 86, 86);
		panel_2.add(dRSRC);
		
		tf_LL_SRC = new JTextField();
		tf_LL_SRC.setColumns(10);
		tf_LL_SRC.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_SRC_LL.getReg(),
						Integer.parseInt(tf_LL_SRC.getText()), sn_Transport);	
			}
		});
		tf_LL_SRC.setBounds(111, 68, 37, 20);
		panel_2.add(tf_LL_SRC);
		
		JLabel label_8 = new JLabel("Low Level");
		label_8.setBounds(158, 71, 72, 14);
		panel_2.add(label_8);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_3.setBounds(424, 146, 254, 124);
		add(panel_3);
		
		JLabel lblPhotoTransistor = new JLabel("Photo Transistor");
		lblPhotoTransistor.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhotoTransistor.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPhotoTransistor.setBounds(0, 98, 237, 24);
		panel_3.add(lblPhotoTransistor);
		
		dRTtor = new DigitalRadial();
		dRTtor.setValue(500.0);
		dRTtor.setMaxValue(1000.0);
		dRTtor.setBounds(10, 6, 86, 86);
		panel_3.add(dRTtor);
		
		cbActTTOR = new JCheckBox("Activar Detecci\u00F3n");
		cbActTTOR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTTOR.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		cbActTTOR.setSelected(true);
		cbActTTOR.setBounds(109, 6, 109, 23);
		panel_3.add(cbActTTOR);
		
		tf_LL_ttor = new JTextField();
		tf_LL_ttor.setColumns(10);
		tf_LL_ttor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_TTOR_LL.getReg(),
						Integer.parseInt(tf_LL_ttor.getText()), sn_Transport);	
			}
		});
		tf_LL_ttor.setBounds(106, 46, 37, 20);
		panel_3.add(tf_LL_ttor);
		
		JLabel label_8_1 = new JLabel("Nivel Detecci\u00F3n");
		label_8_1.setBounds(151, 49, 93, 14);
		panel_3.add(label_8_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_4.setBounds(426, 281, 338, 271);
		add(panel_4);
		
		BasicArrowButton perUP = new BasicArrowButton(1);
		perUP.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(MB_Registers.PRACTICE_SELECTED == MB_Registers.PA1_PERSIANA)
					buildModBus(address, Const_Modbus.WRITE_COIL, MB_Registers.MB_Discrete_Output_Coils.MB_PERUP.getReg(), 1, Cregs);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(MB_Registers.PRACTICE_SELECTED == MB_Registers.PA1_PERSIANA)
					buildModBus(address, Const_Modbus.WRITE_COIL, MB_Registers.MB_Discrete_Output_Coils.MB_PERUP.getReg(), 0, Cregs);
			}
		});
		perUP.setBounds(44, 50, 57, 66);
		panel_4.add(perUP);
		
		BasicArrowButton perDOWN = new BasicArrowButton(5);
		perDOWN.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(MB_Registers.PRACTICE_SELECTED == MB_Registers.PA1_PERSIANA)
					buildModBus(address, Const_Modbus.WRITE_COIL, MB_Registers.MB_Discrete_Output_Coils.MB_PERDOWN.getReg(), 1, Cregs);
			}
		
			@Override
			public void mouseReleased(MouseEvent e) {
				if(MB_Registers.PRACTICE_SELECTED == MB_Registers.PA1_PERSIANA)
					buildModBus(address, Const_Modbus.WRITE_COIL, MB_Registers.MB_Discrete_Output_Coils.MB_PERDOWN.getReg(), 0, Cregs);
			}
		});
		perDOWN.setBounds(44, 117, 57, 66);
		panel_4.add(perDOWN);
		
		label_PosPer = new JLabel("100 %");
		label_PosPer.setHorizontalAlignment(SwingConstants.CENTER);
		label_PosPer.setForeground(Color.BLUE);
		label_PosPer.setFont(new Font("Verdana", Font.BOLD, 30));
		label_PosPer.setBounds(0, 197, 338, 39);
		panel_4.add(label_PosPer);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(MB_Registers.PRACTICE_SELECTED == MB_Registers.PA2_GARAGE)
					buildModBus(address, Const_Modbus.WRITE_COIL, MB_Registers.MB_Discrete_Output_Coils.MB_KEYGAR.getReg(), 1, Cregs);
			}
		});
		btnNewButton.setIcon(new ImageIcon("D:\\Trabajo\\Eclipse\\Workspaces\\Java\\Practicas_Electronica_para_Domotica\\P10_ControlPersianas\\key.png"));
		btnNewButton.setBounds(167, 89, 130, 50);
		panel_4.add(btnNewButton);
		
		JLabel lblPuertaGaraje = new JLabel("Puerta Garaje");
		lblPuertaGaraje.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuertaGaraje.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblPuertaGaraje.setBounds(183, 247, 145, 24);
		panel_4.add(lblPuertaGaraje);
		
		JLabel label_11_2_2 = new JLabel("CTRL. Persiana");
		label_11_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_11_2_2.setFont(new Font("SansSerif", Font.BOLD, 20));
		label_11_2_2.setBounds(0, 247, 173, 24);
		panel_4.add(label_11_2_2);
		
		lbStadoPersiana = new JLabel("Parada");
		lbStadoPersiana.setHorizontalAlignment(SwingConstants.CENTER);
		lbStadoPersiana.setForeground(Color.RED);
		lbStadoPersiana.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		lbStadoPersiana.setBounds(0, 0, 334, 39);
		panel_4.add(lbStadoPersiana);
		
		slider = new JSlider();
		slider.setToolTipText("1");
		slider.setBounds(29, 421, 347, 28);
		add(slider);
		
		slider_1 = new JSlider();
		slider_1.setBounds(29, 514, 347, 28);
		add(slider_1);
		
		actDIMM = new JCheckBox("activar DIMM");
		actDIMM.setBounds(29, 394, 93, 21);
		add(actDIMM);
		actDIMM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTDIMM.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		
		actPOT = new JCheckBox("activar POT");
		actPOT.setBounds(283, 394, 93, 21);
		add(actPOT);
		actPOT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTPOT.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		
		actDIMM2 = new JCheckBox("activar DIMM2");
		actDIMM2.setBounds(29, 487, 93, 21);
		add(actDIMM2);
		actDIMM2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTDIMM2.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		actPOT2 = new JCheckBox("activar POT2");
		actPOT2.setBounds(283, 487, 93, 21);
		add(actPOT2);
		actPOT2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TSwitchState vSel;
				
				if(((AbstractButton) e.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTPOT2.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
/*		
		JButton btnNewButton_1 = new JButton("Actualize");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stActualize = true;
			}
		});
		btnNewButton_1.setBounds(56, 456, 108, 38);
		add(btnNewButton_1);
*/		
		if(sn_Transport.isConnected())
			leerConfiguracionInicial();
	}
	
	private void leerConfiguracionInicial(){
		//******************************************
		//Leer elementos de configuración Analógico
		//******************************************
		
		ModBus_Communications.readMultipleRegisters(address, MB_Registers.MB_Analog_Output_Holding.MB_TMP_PIR.getReg(), 
				4, sn_Transport, this::UpdateElements , Aregs);

		
		//******************************************
		//Leer elementos de configuración Digitales
		//******************************************
		buildModBus(address, Const_Modbus.READ_COILS, MB_Registers.MB_Discrete_Output_Coils.MB_ACTPIR.getReg(), 3, Cregs);
		
	}
	
	private void ONOFF_Bulb(LightBulb lightBulb){
		
		int vBulb;
		int vReg;
		
		lightBulb.setOn(!lightBulb.isOn());
		
		if(lightBulb.isOn()) vBulb = TSwitchState.ToNumber(TSwitchState.ON);
		else vBulb = TSwitchState.ToNumber(TSwitchState.OFF);
		
		if(lightBulb == lightBulb1) vReg = MB_Registers.MB_Discrete_Output_Coils.MB_RELE.getReg();
		else vReg = MB_Registers.MB_Discrete_Output_Coils.MB_TRIAC.getReg();
		
		
		buildModBus(address, Const_Modbus.WRITE_COIL, vReg, vBulb, Cregs);
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getTitle() {
		return "Address : "+address;
	}

	@Override
	public Component getPanel() {
		return this;
	}

	@Override
	public boolean isCategory() {
		return isCategory;
	}

	@Override
	public void Actualize() {
		
		// Actualizar dispositivos modbus
		
		if(stActualize) {	
			//stActualize = false;
			
			//Read OutputCoils
			buildModBus(1, Const_Modbus.READ_COILS, MB_Registers.MB_Discrete_Output_Coils.MB_RELE.getReg(), MB_Registers.MB_Discrete_Output_Coils.MB_O_COILS.getReg(), Cregs);
			
			//Read Discrete Inputs
			buildModBus(1, Const_Modbus.READ_INPUT_DISCRETES, MB_Registers.MB_Discrete_Input_Contacts.MB_BTN1.getReg(),MB_Registers.MB_Discrete_Input_Contacts.MB_I_REGS.getReg(), Dregs);
			
			//Read Input Register (Registros analógicos de entrada)
			buildModBus(1, Const_Modbus.READ_INPUT_REGISTERS, MB_Registers.MB_Analog_Input_Register.MB_POT1.getReg(),MB_Registers.MB_Analog_Input_Register.MB_I_AREGS.getReg(), Iregs);
			
			//Read Output Register (Registros anal�gicos de Salida)
			buildModBus(1, Const_Modbus.READ_MULTIPLE_REGISTERS, MB_Registers.MB_Analog_Output_Holding.MB_STATEPER.getReg(),1, Aregs);
			
		}
	}
	
	public void buildModBus(int address, int func, int iReg, int nReg, int[] bReg) {
		String[] args = {String.valueOf(address), String.valueOf(func), 
				String.valueOf(iReg), String.valueOf(nReg)};

		ModBus_Communications.InitModbusComunication(args, sn_Transport, this::UpdateElements, bReg);
		
	}
	
	public void UpdateElements(final ModBusEvent e){

		int addr = Integer.parseInt(e.get_Args()[2]);
		int nReg = Integer.parseInt(e.get_Args()[3]);
					
		switch(Integer.parseInt(e.get_Args()[1])){
		case Const_Modbus.READ_MULTIPLE_REGISTERS:
			//Cada registro esta compruesto de dos bytes "little endian"
			for(int i = addr;i<(addr+nReg); i++){
				switch(MB_Analog_Output_Holding.values()[i]){
				case MB_TMP_PIR:
					//tiempoPIR.setText(Integer.toString(((e.getRegs()[i+1]&0xFF)<<8)|((e.getRegs()[i]&0xFF))));
					tiempoPIR.setText(Integer.toString(e.getRegs()[i-addr]));
					break;
					
				case MB_SRC_HL:					
					tf_HL_SRC.setText(Integer.toString(e.getRegs()[i-addr]));
					break;
					
				case MB_SRC_LL:
					//int test = ((e.getRegs()[i+1]&0xFF)<<8)|((e.getRegs()[i]&0xFF));
					tf_LL_SRC.setText(Integer.toString(e.getRegs()[i-addr]));
					break;
					
				case MB_TTOR_LL:
					tf_LL_ttor.setText(Integer.toString(e.getRegs()[i-addr]));
					break;	
					
				case MB_STATEPER:
					try{
						MB_Registers.Ctrl_Persianas ctrlPer= MB_Registers.Ctrl_Persianas.fromInteger(e.getRegs()[i-addr]);
						//Ctrl_Persianas ctrlPersianas = Ctrl_Persianas.fromInteger(e.getRegs()[i]);
						
						//System.out.println("ctrlPer: "+ ctrlPer);
					
						switch((ctrlPer)){
						case PER_DOWN:
							lbStadoPersiana.setText("Bajando");
							break;
					
						case PER_STOP:
							lbStadoPersiana.setText("Parada");
							break;
					
						case PER_UP:
							lbStadoPersiana.setText("Subiendo");
							break;
					
						default:
							break;
					
						}
					} catch (Exception ex) {
						System.err.println("Error Estado persiana");
			        }
					
					break;
					
				default:
					break;
				}
			}
			break;
		
		case Const_Modbus.READ_COILS:
			
			for(int i = addr;i<(addr+nReg); i++){
				switch(MB_Discrete_Output_Coils.values()[i]){
				case MB_RELE:							
					lightBulb1.setOn((e.getRegs()[i] == 1));
					break;
							
				case MB_TRIAC:							
					lightBulb2.setOn((e.getRegs()[i] == 1));
					break;
					
				case MB_ACTPIR:
					cbActPIR.setSelected((e.getRegs()[i] == 1));
					break;	
					
				case MB_ACTSRC:
					cbActSRC.setSelected((e.getRegs()[i] == 1));
					break;
					
				case MB_ACTTOR:
					cbActTTOR.setSelected((e.getRegs()[i] == 1));
					break;
					
				default:
					break;
				
				}
			}
			break;
						
		case Const_Modbus.READ_INPUT_DISCRETES:	
			
			MB_Discrete_Input_Contacts mbDIC; //= MB_Discrete_Input_Contacts.values()[addr];
			
			for(int i = addr;i<(addr+nReg); i++){
				mbDIC = MB_Discrete_Input_Contacts.values()[i];
				switch(mbDIC){
				case MB_BTN1:
					ledBtn1.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
								
				case MB_BTN2:
					ledBtn2.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
							
				case MB_OPT:
					ledBtnOpt.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
					
				case MB_PIR:
					ledPIR.setLedOn((e.getRegs()[i] == 1));
					break;	
					
				default:
					break;
				}
			}
			break;
			
		case Const_Modbus.READ_INPUT_REGISTERS:
			
			MB_Analog_Input_Register mbAIR; //= MB_Discrete_Input_Contacts.values()[addr];
			
			for(int i = addr;i<(addr+nReg); i++){
				mbAIR = MB_Analog_Input_Register.values()[i];
				switch(mbAIR){
				case MB_POT1:
					r2T_Pot1.setValue(100 - ((e.getRegs()[i]*100)/1024));
					break;
					
				case MB_POT2:
					r2T_Pot2.setValue((e.getRegs()[i]*100)/1024);
					break;
					
				case MB_PHOTORES:							
					dRSRC.setValue((e.getRegs()[i]));
					break;	
					
				case MB_TEMPSEN:							
					//dRSRC.setValue((e.getRegs()[i]));
					String Temp = ((e.getRegs()[i] >> 8)&0xff)+"."+(e.getRegs()[i]&0xff);
					//System.out.println("Temperatura : "+Temp);
					dSTemp.setLcdValue(Double.parseDouble(Temp));
					break;	
					
				case MB_PHOTOTTOR:							
					dRTtor.setValue((e.getRegs()[i]));
					break;		
					
				case MB_POSPER:
					label_PosPer.setText((e.getRegs()[i]) + " %");
					break;
					
				default:
					break;
				}
			}
			break;
		}				
	}
	
	@Override
	public void setActualize(boolean st) {
		stActualize = st;		
	}

	@Override
	public boolean getActualize() {
		
		return stActualize;
	}

	@Override
	public void vlog(String message) {
		// TODO Auto-generated method stub
		
	}
}
