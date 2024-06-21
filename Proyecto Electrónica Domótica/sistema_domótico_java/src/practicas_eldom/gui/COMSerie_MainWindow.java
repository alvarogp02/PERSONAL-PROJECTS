package practicas_eldom.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import CommTransport.CommTransport;
import CommTransport.Comm.io.ConnTransportAdaption;
import CommTransport.Comm.net.Connection;
import SerialComm.SerialCommTransport;
import SerialComm.connectors.jssc.SerialPortList;
import SerialComm.gui.SerialConfig;
import SerialComm.net.SerialAction;
import SerialComm.net.SerialConnection;
import Utilidades.modbus.ModBus_Communications;
import modbus.ModBusEvent;
import practicas_eldom.ConstantesApp;
import practicas_eldom.config.ConfigProperties;
import practicas_eldom.config.ConfigurationConnect;
import practicas_eldom.config.MB_Registers;
import practicas_eldom.gui.panel.Console;
import practicas_eldom.gui.visualizers.AppTest;
import practicas_eldom.gui.visualizers.DomoBoardGui;
import practicas_eldom.gui.visualizers.Visualizer;
import practicas_eldom.messages.Messages;
import practicas_eldom.messages.VisualizerMessages;

import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class COMSerie_MainWindow {
	
	private 	boolean 						doExitOnRequest = true;
	private 	JCheckBoxMenuItem 				ConsoleSet;	
	private     Console							serialConsole;
	private 	JMenu 							mnMenuSerie;
	private		JLabel 							statusBar_msgLeft;	
	private 	JFrame 							window;
	private		JTabbedPane 					mainPanel;
	private 	CommTransport 					sn_Transport;
	private 	HashMap<String, JTabbedPane> 	categoryTable = new HashMap<String, JTabbedPane>();
	
	private 	ConfigurationConnect			configConnect;
	
	private 	ArrayList<Visualizer> 			visualizers;
	private 	ArrayList<JRadioButtonMenuItem>	practicaSel;
	private  	int 							Aregs[];
	
	public COMSerie_MainWindow() {	
		
		Aregs = new int [MB_Registers.MB_Analog_Output_Holding.MB_AREGS.getReg()];
		
		configConnect = new ConfigurationConnect();
		
		// Make sure we have nice window decorations
		JFrame.setDefaultLookAndFeelDecorated(true);

		Rectangle maxSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
								.getMaximumWindowBounds();
		
		window = new JFrame(ConstantesApp.WINDOW_TITLE);
		window.setAlwaysOnTop(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);		
		
		window.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {		
				Dimension windowSize = window.getSize();
				
				configConnect.setProperty(ConfigProperties.PROP_WINSIZE, windowSize.width +", "+windowSize.height);		
				
				serialConsoleLocation();	
			}
			@Override
			public void componentMoved(ComponentEvent arg0) {
				Point WinLocation = window.getLocation();
				
				configConnect.setProperty(ConfigProperties.PROP_WINLOCATION, WinLocation.x + ", " + WinLocation.y);
				
				serialConsoleLocation();
			}			
		});
				
		window.setLocationByPlatform(true);
		if (maxSize != null) {
			window.setMaximizedBounds(maxSize);
		}
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//===================================
		//               Menú               =
		//===================================

		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("");
		menuBar.setForeground(Color.WHITE);
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		menuBar.setBackground(Color.ORANGE);
		window.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu(Messages.ARCHIVO);
		menuBar.add(mnNewMenu);

		JMenuItem ExitItem = new JMenuItem(Messages.SALIR);
		ExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Panel Inicial/pruebas");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = VisualizerMessages.PRUEBAS;
				boolean exisPane = false;
				
				for(int n=0;n<mainPanel.getTabCount();n++)
				{			
					if(title.equals(mainPanel.getTitleAt(n))){
						mainPanel.remove(n);
						categoryTable.remove(title);
						exisPane = true;
					}			
				}
				
				if(!exisPane){
					AppTest appTest = new AppTest(VisualizerMessages.PRUEBAS, sn_Transport);
					addVisualizer(appTest);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		mnNewMenu.add(ExitItem);
		
		JMenu mnDomoboard = new JMenu("DomoBoard");
		mnDomoboard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String title = VisualizerMessages.DOMOBOARD;
				boolean exisPane = false;
				
				for(int n=0;n<mainPanel.getTabCount();n++)
				{			
					if(title.equals(mainPanel.getTitleAt(n))){
						mainPanel.remove(n);
						categoryTable.remove(title);
						exisPane = true;
					}			
				}
				
				if(!exisPane){

					
					int slaveAdd = 0x01;
					DomoBoardGui domoboardGui = new DomoBoardGui(VisualizerMessages.DOMOBOARD, slaveAdd, sn_Transport);
					addVisualizer(domoboardGui);
				}
			}
		});
		
		JMenu mnSeleccionarPrctica = new JMenu("Seleccionar Pr\u00E1ctica");
		menuBar.add(mnSeleccionarPrctica);
		
		JMenu mnPrctica_2 = new JMenu("Pr\u00E1ctica 1");
		mnSeleccionarPrctica.add(mnPrctica_2);
		
		JRadioButtonMenuItem P1_Pulsadores = new JRadioButtonMenuItem("1.- Pulsadores");
		P1_Pulsadores.addActionListener(Select_Practica);
		mnPrctica_2.add(P1_Pulsadores);
		
		JRadioButtonMenuItem P1_Interruptor = new JRadioButtonMenuItem("2.- Interruptor");
		P1_Interruptor.addActionListener(Select_Practica);
		mnPrctica_2.add(P1_Interruptor);
		
		JRadioButtonMenuItem P1_Conmutador = new JRadioButtonMenuItem("3.- Conmutador");
		P1_Conmutador.addActionListener(Select_Practica);
		mnPrctica_2.add(P1_Conmutador);
		
		JMenu mnPrctica_3 = new JMenu("Pr\u00E1ctica 3 - Rel\u00E9");
		mnSeleccionarPrctica.add(mnPrctica_3);
		
		JRadioButtonMenuItem P3_Conmutador = new JRadioButtonMenuItem("Conmutador");
		P3_Conmutador.addActionListener(Select_Practica);
		mnPrctica_3.add(P3_Conmutador);
		
		JMenu mnPrctica_4 = new JMenu("Pr\u00E1ctica 4.- Triac");
		mnSeleccionarPrctica.add(mnPrctica_4);
		
		JRadioButtonMenuItem P4_Pulsadores = new JRadioButtonMenuItem("1.- Pulsadores");
		P4_Pulsadores.addActionListener(Select_Practica);
		mnPrctica_4.add(P4_Pulsadores);
		
		JRadioButtonMenuItem P4_Interruptor = new JRadioButtonMenuItem("2.- Interruptor");
		P4_Interruptor.addActionListener(Select_Practica);
		mnPrctica_4.add(P4_Interruptor);
		
		JRadioButtonMenuItem P4_Conmutador = new JRadioButtonMenuItem("3.- Conmutador");
		P4_Conmutador.addActionListener(Select_Practica);
		mnPrctica_4.add(P4_Conmutador);
		
		JMenu mnPractica_5 = new JMenu("Pr\u00E1ctica 5 - ModBus");
		mnSeleccionarPrctica.add(mnPractica_5);
		
		JRadioButtonMenuItem P5_Interruptor = new JRadioButtonMenuItem("Interruptor");
		P5_Interruptor.addActionListener(Select_Practica);
		mnPractica_5.add(P5_Interruptor);
		
		JMenu mnPrctica = new JMenu("Pr\u00E1ctica 6 - Entradas Digitales");
		mnSeleccionarPrctica.add(mnPrctica);
		
		JRadioButtonMenuItem P6_Interruptor = new JRadioButtonMenuItem("Interruptor");
		P6_Interruptor.addActionListener(Select_Practica);
		mnPrctica.add(P6_Interruptor);
		
		JMenu mnPrctica_7 = new JMenu("Práctica 7 - Sensor Movimiento - PIR");
		mnSeleccionarPrctica.add(mnPrctica_7);
		
		JRadioButtonMenuItem P7_InterruptorTemporizado = new JRadioButtonMenuItem("Interruptor Temporizado");
		P7_InterruptorTemporizado.addActionListener(Select_Practica);
		mnPrctica_7.add(P7_InterruptorTemporizado);
		
		JMenu mnPrctica9 = new JMenu("Práctica 9");
		mnSeleccionarPrctica.add(mnPrctica9);
		
		JRadioButtonMenuItem P9_SensoresAnalgicosall = new JRadioButtonMenuItem("Sensores Analógicos (All)");
		P9_SensoresAnalgicosall.addActionListener(Select_Practica);
		mnPrctica9.add(P9_SensoresAnalgicosall);
		
		JMenu mnPrctica_1 = new JMenu("Práctica 10");
		mnSeleccionarPrctica.add(mnPrctica_1);
		
		JRadioButtonMenuItem P10_1_ControlPersiana = new JRadioButtonMenuItem("Control Persiana");
		P10_1_ControlPersiana.addActionListener(Select_Practica);
		mnPrctica_1.add(P10_1_ControlPersiana);
		
		JRadioButtonMenuItem P10_2_PuertaGarage = new JRadioButtonMenuItem("Puerta Garage");
		P10_2_PuertaGarage.addActionListener(Select_Practica);
		mnPrctica_1.add(P10_2_PuertaGarage);
		
		menuBar.add(mnDomoboard);
		
		//=======================================================
		//             Menú COMUNICACIONES
		//=======================================================
		JMenu mnNewMenu_1 = new JMenu(Messages.COMUNICACIONES);
		mnNewMenu_1.setForeground(Color.BLACK);
		mnNewMenu_1.setBackground(Color.BLACK);
		menuBar.add(mnNewMenu_1);
		
		initSerialTransport(mnMenuSerie);
		
		//Submenú SERIE
		genMenuSerie(mnNewMenu_1);		
		
		//Monitor Serie
		ConsoleSet = new JCheckBoxMenuItem(Messages.CONSOLE);
		mnNewMenu_1.add(ConsoleSet);
		
		ConsoleSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				AbstractButton aButton = (AbstractButton) paramActionEvent.getSource();
		        boolean selected = aButton.getModel().isSelected();
		        
		        if(selected){
		        	serialConsole = new Console();
		        	serialConsole.addActionListener(new ActionListener() {
		    			public void actionPerformed(ActionEvent e) {
		    				ConsoleMessage(e.getActionCommand());
		    			}
		    		});
		        	serialConsole.setVisible(true);
		        	
		        	serialConsoleLocation();
		        }
		        else{ 
		        	serialConsole.Close();
		        }
		        
		        configConnect.setProperty(ConfigProperties.PROP_SERIALCONSOLE,String.valueOf(selected));
		        configConnect.SaveConfig();
			}
		});
		window.getContentPane().setLayout(new BorderLayout(0, 0));
		
		//-----------------------------------------------------
		//-------------  STATUS BAR
		//-----------------------------------------------------
		JPanel statusBar = new JPanel();
		window.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		statusBar_msgLeft = new JLabel(" " + "Good Day!", JLabel.LEFT);
		statusBar_msgLeft.setForeground(Color.black);
		statusBar.add(statusBar_msgLeft);
		
		JLabel welcomedate = new JLabel();
        welcomedate.setOpaque(true);//to set the color for jlabel
        welcomedate.setBackground(Color.black);
        welcomedate.setForeground(Color.WHITE);
        statusBar.add(welcomedate);
        
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusBar.setBackground(Color.LIGHT_GRAY);
        statusBar.add(statusBar_msgLeft, BorderLayout.WEST);
        statusBar.add(welcomedate, BorderLayout.EAST);
        
        //display date time to status bar
        Timer timer = new Timer (1000, new ActionListener ()
		{
			public void actionPerformed(ActionEvent e)
		    {	java.util.Date now = new java.util.Date();
            String ss = DateFormat.getDateTimeInstance().format(now);
            welcomedate.setText(ss);
            welcomedate.setToolTipText("Welcome, Today is " + ss);
		    }
		});
		
		timer.start();
		
		statusBar_msgLeft.setText(Messages.PORTSELTOCOMM);
		
		//-----------------------------------------------------
		//-----------------------------------------------------
		
		mainPanel = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		window.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		//-----------------------------------------------------
		//-----------------------------------------------------
		
		visualizers = new ArrayList<Visualizer>();
		
		practicaSel = new ArrayList<JRadioButtonMenuItem>();
		
		practicaSel.add(P1_Pulsadores);
		practicaSel.add(P1_Interruptor);
		practicaSel.add(P1_Conmutador);
		practicaSel.add(P3_Conmutador);
		practicaSel.add(P4_Pulsadores);
		practicaSel.add(P4_Interruptor);
		practicaSel.add(P4_Conmutador);
		practicaSel.add(P5_Interruptor);
		practicaSel.add(P6_Interruptor);
		practicaSel.add(P7_InterruptorTemporizado);
		practicaSel.add(P9_SensoresAnalgicosall);
		practicaSel.add(P10_1_ControlPersiana);
		practicaSel.add(P10_2_PuertaGarage);
		
		Modbus_Regular_Call();
		
	}
	
	private void Modbus_Regular_Call(){
		Timer timer = new Timer (ConstantesApp.MS_REGULAR_CALL, new ActionListener ()
		{
			public void actionPerformed(ActionEvent e)
		    {	
				//We must be sure all request are done wetween calls (in a second)
				if(sn_Transport.isConnected()){					
					for (int i = 0, n = visualizers.size(); i < n; i++) {
	        			visualizers.get(i).Actualize();
	        		}
				}
		    }
		});
		
		timer.start();
	}
	
	private ActionListener Select_Practica = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			handle_Menu(arg0);
		}
	};
	
	/*
	 * Maneja menú para seleccionar la práctica con la que trabajamos
	 */
	private void handle_Menu(ActionEvent arg0){
		JRadioButtonMenuItem Source = (JRadioButtonMenuItem)arg0.getSource();
		
		for (int i = 0, n = practicaSel.size(); i < n; i++) {
			if(practicaSel.get(i).equals(Source)) {
				int slaveAdd = 0x01;
				ModBus_Communications.writeSingleRegister(slaveAdd, MB_Registers.MB_Analog_Output_Holding.MB_PRACT.getReg(), 
						MB_Registers.SELPRACT[i] , sn_Transport);
			}
		}
		
		//Esperamos 500 ms para dar tiempo a mostrar el mensage de la práctica seleccionada.
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		leerPracticaConfigurada();
		
	}
	
	protected void addVisualizer(Visualizer visualizer){
		String category = visualizer.getCategory();
		
		JTabbedPane pane = categoryTable.get(category);
		if (pane == null) {
			pane = new JTabbedPane();
			categoryTable.put(category, pane);
			if(visualizer.isCategory()){
				mainPanel.add(category, pane);
			}
			else
				mainPanel.add(category, visualizer.getPanel());
		}
		
		if(visualizer.isCategory())
			pane.add(visualizer.getTitle(), visualizer.getPanel());
		
		visualizers.add(visualizer);	
	}
	
	public void genMenuSerie(JMenu mnMenu){
		mnMenuSerie = new JMenu(Messages.SERIE);
		
		mnMenuSerie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				log(Messages.LOOKPORTS + "\n\s");
				SetMenu_SerialPorts(mnMenuSerie);
				SetMenu_PortConfig(mnMenuSerie);
				
			}
		});
		
		SetMenu_SerialPorts(mnMenuSerie);
		SetMenu_PortConfig(mnMenuSerie);
		mnMenu.add(mnMenuSerie);

		mnMenu.addSeparator();
		
	}
	
	private void SetMenu_PortConfig(JMenu mnMenu) {
		mnMenu.addSeparator();
		
		JMenuItem mntmNewMenuItem = new JMenuItem(Messages.OPT_SERIE);
		mnMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				SerialConfig frame = new SerialConfig(configConnect.getProperties(), ConfigProperties.CONFIG_FILE);
				frame.addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e)
					  {
						window.setAlwaysOnTop(true);
					  }
		
	            });
				window.setAlwaysOnTop(false);
				
		        frame.setVisible(true); //necessary as of 1.3
				frame.setAlwaysOnTop(true);
		        frame.toFront();
			}
		});
	}

	
	public int SetMenu_SerialPorts(JMenu mnMenu){
		  
		//Obtenemos los puertos Serie disponibles
		
		SerialConnection serialConnection = ((SerialCommTransport)sn_Transport).getSerialConnection();
		
		String[] ports = SerialPortList.getPortNames();
		
		if(ports.length == 0){
			mnMenu.setEnabled(false);
		}
		else
		{	
			mnMenu.setEnabled(true);
			mnMenu.removeAll();
		
			for(String port : ports){
				JRadioButtonMenuItem mntmPrueba = new JRadioButtonMenuItem(port);
				mnMenu.add(mntmPrueba);
				
				mntmPrueba.addActionListener(new SerialAction("Connect to serial", serialConnection));
			}
		}
				
		return ports.length;
	}
	
	private void initSerialTransport(JMenu mnMenu){
		
		sn_Transport = new SerialCommTransport(ConstantesApp.SERIALCONNECTION);
		sn_Transport.loadConfig(configConnect.getProperties());
		//sn_Transport.
		sn_Transport.addTransportListener(new ConnTransportAdaption(){
			@Override
			public void logTransport(String message){
				log(message);
			}
			
			@Override 
			public void SystemMessage(String message){
				setSystemMessage(message);
			}
			
			@Override 
			public void CT_Opened(String message){
				statusBar_msgLeft.setText(ConstantesApp.STATUSBAR_MSGLEFT +  message);
				
				leerPracticaConfigurada();
			}
			
			@Override
			public void CT_Closed() {
				statusBar_msgLeft.setText(ConstantesApp.STATUSBAR_MSGLEFT);
			}
			
			@Override
			public void CTInData(Connection connection, String message) {		
				log(message);		
			}
		});
	}
	
	private void leerPracticaConfigurada(){		
		int slaveAdd = 0x01;
		ModBus_Communications.readMultipleRegisters(slaveAdd, MB_Registers.MB_Analog_Output_Holding.MB_PRACT.getReg(), 
				1, sn_Transport, this::configPractResult, Aregs);
				
	}
	
	private void configPractResult(final ModBusEvent e){
		int i = 0;
		
		int pract = e.getRegs()[0];
		
		while(i < MB_Registers.SELPRACT.length){
			if(MB_Registers.SELPRACT[i] == pract){
				
				MB_Registers.PRACTICE_SELECTED = pract;
				
				practicaSel.get(i).setSelected(true);
				//break;
			}else practicaSel.get(i).setSelected(false);
			
			i++;
		}
	}
	
	protected void setSystemMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				if (message == null) {
					window.setTitle(ConstantesApp.WINDOW_TITLE);
				} else {
					window.setTitle(ConstantesApp.WINDOW_TITLE + " (" + message + ')');
				}
			}
		});
	}
		
	private void log(String Msg){
		if((serialConsole == null)||(!serialConsole.isVisible())){
			System.out.print(Msg);
		}
		else
			serialConsole.log(Msg);
	}
	
	private void exit() {
		if (doExitOnRequest) {
			//stop();
			System.exit(0);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}


	private void serialConsoleLocation(){
		if(serialConsole != null){
			if(serialConsole.isVisible()){ 
				Dimension windowSize = window.getSize();
				serialConsole.setSize(serialConsole.getWidth(), windowSize.height);
    	
				Point WinLocation = window.getLocation(); 
    	
				serialConsole.setLocation(WinLocation.x + windowSize.width, WinLocation.y);
			}
		}
	}

	
	private void ConsoleMessage(final String message){
		if(message == "Close"){
			ConsoleSet.setState(false);
		}
	}
	
	
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				String s = configConnect.getProperty(ConfigProperties.PROP_WINSIZE);
				String l = configConnect.getProperty(ConfigProperties.PROP_WINLOCATION);
				
				window.setVisible(true);
				
				if(s != null){
					String[] r = s.split(", ");
					window.setSize(new Dimension(Integer.parseInt(r[0]),
	                        Integer.parseInt(r[1])));
				} else window.setSize(new Dimension(799, 535)); 
				
				if(l != null){
					String[] r = l.split(", ");

					window.setLocation(Integer.parseInt(r[0]),
	                        Integer.parseInt(r[1]));
				} 
				
				if(Boolean.valueOf(configConnect.getProperty(ConfigProperties.PROP_SERIALCONSOLE))){
					ConsoleSet.doClick();
				}
			}
		});
	}
}
