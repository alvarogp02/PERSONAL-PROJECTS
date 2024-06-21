

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"config_practicas.h"

const uint16_t	persianaTiempoSubida 	= 7200;  //milisegundos

TCtrlTime 	ctrlPosPer;			//Control de posición persiana


/****************************************************************************/
/***                 Functions                                            ***/
/****************************************************************************/

void clear_Actuators(){
	domoboard.clear_Actuators();
	mbDomoboard.clear_Actuators();
	SensoresTemporizados.clear();
}

void config_practica1_apt_1(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = Pulsado_Soltado;

	domoboard.BOTON2.SensorEvent = Pulsado_Soltado;

	domoboard.BTN_OPT.SensorEvent = Pulsado_Soltado;
}

void config_practica1_apt_2(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = Interruptor;
	domoboard.BOTON1.Aux = OFF;

	domoboard.BOTON2.SensorEvent = Interruptor;
	domoboard.BOTON2.Aux = OFF;

	domoboard.BTN_OPT.SensorEvent = Interruptor;
	domoboard.BTN_OPT.Aux = OFF;
}

void config_practica1_apt_3(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = conmutador;

	domoboard.BOTON2.SensorEvent = conmutador;

	domoboard.BTN_OPT.SensorEvent = conmutador;
}

void config_practica3_apt_2(void){
	clear_Actuators();

	domoboard.BOTON1.SensorEvent = conmutador;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));

	domoboard.BOTON2.SensorEvent = conmutador;
	domoboard.BOTON2.managerActuators.push(&(domoboard.RELE));

	domoboard.BTN_OPT.SensorEvent = conmutador;
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.RELE));
}

void config_practica4_apt_1(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = Pulsado_Soltado;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));

	domoboard.BOTON2.SensorEvent = Pulsado_Soltado;
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = Pulsado_Soltado;
}

void config_practica4_apt_2(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = Interruptor;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));


	domoboard.BOTON2.SensorEvent = Interruptor;
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = Interruptor;
}

void config_practica4_apt_3(void){

	clear_Actuators();

	domoboard.BOTON1.SensorEvent = conmutador;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));
	domoboard.BOTON1.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BOTON2.SensorEvent = conmutador;
	domoboard.BOTON2.managerActuators.push(&(domoboard.RELE));
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = conmutador;
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.RELE));
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.TRIAC));
}


void config_practica5_apt_4(){
	clear_Actuators();

	mbDomoboard.BOTON1.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON1.mbActuators.push(&mbDomoboard.RELE);

	mbDomoboard.BOTON2.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON2.mbActuators.push(&mbDomoboard.TRIAC);
}


void config_practica6_apt_3(){
	clear_Actuators();

	mbDomoboard.BOTON1.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON1.mbActuators.push(&(mbDomoboard.TRIAC));

	mbDomoboard.BOTON2.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON2.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BTN_OPT.mbSensorEvent = mbInterruptor;
}

void Config_P7_SensorMovimiento(){

	static AsyncWait asyncWait;

	//Borramos los actuadores previos asignado a cada sensor de entrada
	clear_Actuators();

	//Inicialmente, habilitamos el sensor PIR
	mbDomoboard.PIR_MOV.Sensor->Activo = true;		//Habilitamos sensor PIR
	Cregs[MB_ACTPIR] = 0x01;  						//Actualiza registro que monitoriza la habilitación del sensor

	DEBUGLOGLN("Valor MB_TMP_PIR: %d", Aregs[MB_TMP_PIR]);

	asyncWait.startWaiting(&Aregs[MB_TMP_PIR]);		//Configuramos el tiempo de espera al tiempo almacenado
													//en el registro ModBus usado para tal fin
	mbDomoboard.PIR_MOV.asyncWait = &asyncWait;		//Asignamos el elemento de temporización al sensor
	mbDomoboard.PIR_MOV.mbSensorEvent = mbInterruptorTemporizado;
	mbDomoboard.PIR_MOV.mbActuators.push(&(mbDomoboard.TRIAC));

	mbDomoboard.BOTON1.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON1.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BOTON2.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON2.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BTN_OPT.mbSensorEvent = mbConmutador;
	mbDomoboard.BTN_OPT.mbActuators.push(&(mbDomoboard.RELE));

	SensoresTemporizados.push(&(mbDomoboard.PIR_MOV));
}

void Config_P9_SRC_Interruptor(void){

	//Borramos los actuadores previos asignado a cada sensor de entrada
	clear_Actuators();

	//mbDomoboard.set_coilRegister(MB_ACTPIR, 0x00);		//Disable PIR

	//Config Photoresistor
	mbDomoboard.PHOTORES.ctrlLevelPtr.lInf = &(Aregs[MB_SRC_LL]);
	mbDomoboard.PHOTORES.ctrlLevelPtr.lSup = &(Aregs[MB_SRC_HL]);
	mbDomoboard.PHOTORES.mbSensorEvent = interruptor_SRC;
	mbDomoboard.PHOTORES.mbActuators.push(&(mbDomoboard.TRIAC));

	//Config PhotoTtor
	mbDomoboard.PHOTOTTOR.ctrlLevelPtr.lInf = &(Aregs[MB_TTOR_LL]);
	//mbDomoboard.PHOTOTTOR.Aux = 0;
	mbDomoboard.PHOTOTTOR.mbSensorEvent = trigger_Level;
	mbDomoboard.PHOTOTTOR.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BOTON1.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON1.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BOTON2.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON2.mbActuators.push(&(mbDomoboard.RELE));

	mbDomoboard.BTN_OPT.mbSensorEvent = mbConmutador;
	mbDomoboard.BTN_OPT.mbActuators.push(&(mbDomoboard.RELE));
}

void Config_P10_CtrlPersianas(){
	//Borramos los actuadores previos asignado a cada sensor de entrada
	clear_Actuators();


	ctrlPosPer.activa		= false;
	ctrlPosPer.maxTime		= (uint16_t *)&persianaTiempoSubida;
	ctrlPosPer.actTime		= 0;

	mbDomoboard.BOTON1.mbSensorEvent = Persiana;
	mbDomoboard.BOTON1.mbActuators.push(&(mbDomoboard.PERUP));

	mbDomoboard.BOTON2.mbSensorEvent = Persiana;
	mbDomoboard.BOTON2.mbActuators.push(&(mbDomoboard.PERDOWN));
}

void Config_P10_CtrlGarage(){


	static uint16_t				ctrl_TiempoGaraje = 1000;   //1000 ms == 1 Seg.
	static AsyncWait 			asyncWait;
	static TmbRelatedSensors 	RelatedSensors;			//Lista de sensores relacionados/implicados en la utilidad

	//Borramos los actuadores previos asignado a cada sensor de entrada
	clear_Actuators();

	//Inicializamos control de temporización
	asyncWait.startWaiting(&ctrl_TiempoGaraje);		//Configuramos el tiempo de espera al tiempo almacenado

	ctrlPosPer.activa		= false;
	ctrlPosPer.maxTime		= (uint16_t *)&persianaTiempoSubida;
	ctrlPosPer.actTime		= 0;

	RelatedSensors.clear();
	RelatedSensors.push(&(mbDomoboard.BOTON1));

	mbDomoboard.BOTON1.mbSensorEvent 	= PuertaGarage;
	mbDomoboard.BOTON1.asyncWait 		= &asyncWait;

	mbDomoboard.PHOTOTTOR.ctrlLevelPtr.lInf = &(Aregs[MB_TTOR_LL]);
	mbDomoboard.PHOTOTTOR.asyncWait = NULL;
	mbDomoboard.PHOTOTTOR.mbSensorEvent = PuertaGarage;
	mbDomoboard.PHOTOTTOR.RelatedSensors = &RelatedSensors;

	//Para comprobar regularmente el estado de la puerta del garage
	SensoresTemporizados.push(&(mbDomoboard.BOTON1));
}

void SelectionConfiguration(uint8_t selConf){
	static uint8_t actConf = 0x00;

	if(selConf == actConf)
		return;

	actConf = selConf;

	switch(selConf){
		case P1_PULSADORES:
			DEBUGLNF("P1 PULSADORES Seleccionado");
			config_practica1_apt_1();
			break;

		case P1_INTERRUPTOR:
			DEBUGLNF("P1 INTERRUPTOR Seleccionado");
			config_practica1_apt_2();
			break;

		case P1_CONMUTADOR:
			DEBUGLNF("P1 CONMUTADOR Seleccionado");
			config_practica1_apt_3();
			break;

		case P3_CONMUTADOR:
			DEBUGLNF("P3 CONMUTADOR Seleccionado");
			config_practica3_apt_2();
			break;

		case P4_PULSADORES:
			DEBUGLNF("P4 PULSADORES Seleccionado ");
			config_practica4_apt_1();
			break;

		case P4_INTERRUPTOR:
			DEBUGLNF("P4 INTERRUPTOR Seleccionado");
			config_practica4_apt_2();
			break;

		case P4_CONMUTADOR:
			DEBUGLNF("P4 CONMUTADOR Seleccionado");
			config_practica4_apt_3();
			break;

		case P5_INTERRUPTOR:
			DEBUGLNF("P5 INTERRUPTOR Seleccionado");
			config_practica5_apt_4();
			break;

		case P6_INTERRUPTOR:
			DEBUGLNF("P6 INTERRUPTOR Seleccionado");
			config_practica6_apt_3();
			break;

		case P7_PIR:
			DEBUGLNF("P07 Seleccionada --> Sensor Movimiento (PIR)");
			Config_P7_SensorMovimiento();
			break;

		case P9_ALL:
			DEBUGLNF("P09 Seleccionada --> Sensores Analógicos (All)");
			Config_P9_SRC_Interruptor();
			break;

		case P10_PER:
			DEBUGLNF("P10-1 Control de persianas");
			Config_P10_CtrlPersianas();
			break;

		case P10_GAR:
			DEBUGLNF("P10-2 Control de Puerta Garage");
			Config_P10_CtrlGarage();
			break;

	}
}

