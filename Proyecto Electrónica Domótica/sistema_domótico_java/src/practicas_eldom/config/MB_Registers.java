package practicas_eldom.config;

public class MB_Registers {

	// Defininición de comando de configuración
	public static final int P1_PULSADORES 		= 0x11;
	public static final int P1_INTERRUPTOR 		= 0x12;
	public static final int P1_CONMUTADOR 		= 0x13;
	public static final int P3_CONMUTADOR 		= 0x32;
	public static final int P4_PULSADORES 		= 0x41;
	public static final int P4_INTERRUPTOR 		= 0x42;
	public static final int P4_CONMUTADOR 		= 0x43;
	public static final int PRACTICAS_MODBUS 	= 0x50;
	public static final int P5_INTERRUPTOR 		= 0x54;
	public static final int P6_INTERRUPTOR 		= 0x62;
	public static final int P07_PIR				= 0x70;
	public static final int P09_ALL				= 0x90;
	public static final int PA1_PERSIANA		= 0xA1;
	public static final int PA2_GARAGE			= 0xA2;

	// Selección configuración Práctica
	public static final int SELPRACT[] = { P1_PULSADORES, P1_INTERRUPTOR, P1_CONMUTADOR, P3_CONMUTADOR, P4_PULSADORES,
			P4_INTERRUPTOR, P4_CONMUTADOR, P5_INTERRUPTOR, P6_INTERRUPTOR, P07_PIR, P09_ALL, PA1_PERSIANA, PA2_GARAGE };

	public static int PRACTICE_SELECTED = 0x00;

	// Analog Output Holding Registers
	public enum MB_Analog_Output_Holding {

		// Discrete Output Coils
		MB_PRACT(0), // Registro para indicar la práctica con la que trabajamos
		MB_TMP_PIR(1),			// Registro para controlar el tiempo activo del sensor PIR (Segundos)
		MB_SRC_HL(2),			// Registro para controlar el nivel superior de activación SRC
		MB_SRC_LL(3),			// Registro para controlar el nivel Inferior de activación SRC
		MB_TTOR_LL(4),			// Registro para controlar el nivel de detección del phototransistor
		MB_STATEPER(5),			// Registro para controlar/Monitorizar el estado de la persiana
		MB_AREGS(6);

		int reg;

		MB_Analog_Output_Holding(int rg) {
			reg = rg;
		}

		public int getReg() {
			return reg;
		}

		public void setReg(int rg) {
			reg = rg;
		}
	}
	
public enum MB_Analog_Input_Register {
		
		//Discrete Output Coils
		MB_POT1(0),
		MB_POT2(1),
		MB_PHOTORES(2),				// Foto Resistencia
		MB_TEMPSEN(3),				// Sensor de Temperatura
		MB_PHOTOTTOR(4),			// Photo Transistor
		MB_POSPER(5),				// Posición puerta garage/Persiana
		MB_I_AREGS(6);
		
		int reg;
		
		MB_Analog_Input_Register(int rg){
			reg = rg;
		}
		
		public int getReg(){
			return reg;
		}
		
		public void setReg(int rg) {
			reg = rg;
		}
		
		public int getDefaultValue() {
			switch(this) {
			case MB_POT1:
				return 0;
			case MB_POT2:
				return 1;
			default:
				return -1;
			}
		}
	}

	
	public enum MB_Discrete_Output_Coils {
		
		//Discrete Output Coils
		MB_RELE(0),
		MB_TRIAC(1),
		MB_ACTPIR(2),					//Salida virtual para Activar/Desactivar PIR
		MB_ACTSRC(3),					//Salida virtual para Activar/Desactivar SRC --> PhotoResistor
		MB_ACTTOR(4),					//Salida virtual para Activar/Desactivar Trigger --> PhotoTransistor
		MB_PERUP(5),					//Salida virtual para el control de la persiana UP
		MB_PERDOWN(6),					//Salida Virtual para el control de la persiana DOWN	
		MB_KEYGAR(7),					//Entrada Virtual para indicar que se ha accionado la llave del garage
		MB_ACTPOT(8),
		MB_ACTPOT2(9),
		MB_ACTDIMM(10),
		MB_ACTDIMM2(11),
		MB_O_COILS(12);
		
		int reg;
		
		MB_Discrete_Output_Coils(int rg){
			reg = rg;
		}
		
		public int getReg(){
			return reg;
		}
		
		public void setReg(int rg) {
			reg = rg;
		}
	}
	
	public enum Ctrl_Persianas {
		PER_STOP,
		PER_DOWN,
		PER_UP;
		
		public static Ctrl_Persianas fromInteger(int x) {
	        switch(x) {
	        case 1:
	            return PER_DOWN;
	        case 2:
	        	return PER_UP;
	        default:
	        	return PER_STOP;
	        }
	    }
	}
	
	public enum MB_Discrete_Input_Contacts {
		
		//Discrete Output Coils
		MB_BTN1(0),
		MB_BTN2(1),
		MB_OPT(2),
		MB_PIR(3),			//Sensor de movimiento
		MB_I_REGS(4);
		
		int reg;
		
		MB_Discrete_Input_Contacts(int rg){
			reg = rg;
		}
		
		public int getReg(){
			return reg;
		}
		
		public void setReg(int rg) {
			reg = rg;
		}
		
		public int getDefaultValue() {
			switch(this) {
			case MB_BTN1:
				return 0;
			case MB_BTN2:
				return 0;
			case MB_OPT:
				return 1;
			case MB_PIR:
				return 0;
			default:
				return -1;
			}
		}
	}
	
	public enum TSwitchState { 
		OFF, 
		ON,  
		TOGGLE;
		
		public static String ToNumberString(TSwitchState x) {
	        switch(x) {
	        case OFF:
	            return "0";
	        case ON:
	        	return "1";
	        case TOGGLE:
	        	return "2";
	        default:
	        	return "0";
	        }
	    }
		
		public static int ToNumber(TSwitchState x) {
	        switch(x) {
	        case OFF:
	            return 0;
	        case ON:
	        	return 1;
	        case TOGGLE:
	        	return 2;
	        default:
	        	return 0;
	        }
	    }
		
	}
}
