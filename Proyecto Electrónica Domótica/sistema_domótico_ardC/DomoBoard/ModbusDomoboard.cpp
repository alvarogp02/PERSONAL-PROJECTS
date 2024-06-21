

#include <ModbusDomoboard.h>
#include "utils_domoboard.h"

/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/

/***************************************************
 * Definición Bancos de registros usados en ModBus *
 ***************************************************/
uint16_t	Cregs[MB_O_COILS];		//Registros para "Dicrete Output Coils"
uint16_t	Dregs[MB_I_CONTATCS];	//Registros para "Dicrete Input Contacts"
uint16_t	Aregs[MB_A_REGS];		//Registros para "Analog Output Holding Registers"
uint16_t	Iregs[MB_I_REGS];		//Registros para "Analog Input Registers"

ModbusDomoboard mbDomoboard;

ModbusDomoboard::ModbusDomoboard():DomoBoard() {
	//*****  Initialize ModBus Sensors  ****

	//Initialize BOTON1 for ModBus
	BOTON1.Sensor 		= &(DomoBoard::BOTON1);
	BOTON1.mbRegs 		= &Dregs[MB_BOTON1];
	Dregs[MB_BOTON1]	= BOTON1.Sensor->valor;

	//Initialize BOTON2 for ModBus
	BOTON2.Sensor 		= &(DomoBoard::BOTON2);
	BOTON2.mbRegs 		= &Dregs[MB_BOTON2];
	Dregs[MB_BOTON2]	= BOTON2.Sensor->valor;

	//Initialize BOTON OPTOCOPLADO for ModBus
	BTN_OPT.Sensor 		= &(DomoBoard::BTN_OPT);
	BTN_OPT.mbRegs 		= &Dregs[MB_BTNOPT];
	Dregs[MB_BTNOPT]	= BTN_OPT.Sensor->valor;

	//Inicializamos el registros ModBus Sensor PIR
	PIR_MOV.Sensor 			= &(DomoBoard::PIR_MOV);
	PIR_MOV.Sensor->Activo 	= false;					//Inicialmente, Configuramos el sensor como no activo
	Cregs[MB_ACTPIR] 		= 0x00; 					//Actualizamos el registro ModBus que monitoriza el PIR
	PIR_MOV.mbRegs 			= &Dregs[MB_PIRMOV];
	Dregs[MB_PIRMOV] 		= PIR_MOV.Sensor->valor;	//Actualizamos el registro ModBus con el estado del sensor
	Aregs[MB_TMP_PIR] 		= 0x03;						//Configuración inicial 3 Segundos activo

	//Initialize ModBus Analog sensors
	POT1.Sensor = &(DomoBoard::POT1);
	POT1.mbRegs = &Iregs[MB_POT1];

	POT2.Sensor = &(DomoBoard::POT2);
	POT2.mbRegs = &Iregs[MB_POT2];

	PHOTORES.Sensor			=   &(DomoBoard::PHOTORES);
	PHOTORES.mbRegs			=	&Iregs[MB_PHOTORES];

	TEMPSEN.Sensor			=   &(DomoBoard::TEMPSEN);
	TEMPSEN.mbRegs			=	&Iregs[MB_TEMPSEN];
	TEMPSEN.mbSensorEvent	=   Calc_Temperatura;

	PHOTOTTOR.Sensor		=   &(DomoBoard::PHOTOTTOR);
	PHOTOTTOR.mbRegs		=	&Iregs[MB_PHOTOTTOR];

	//Acturadores
	RELE.actuator = &(DomoBoard::RELE);
	RELE.mbRegs = &Cregs[MB_RELE];

	TRIAC.actuator = &(DomoBoard::TRIAC);
	TRIAC.mbRegs = &Cregs[MB_TRIAC];

	//Configuración Estado Persiana
	PERUP.mbRegs = &Cregs[MB_PERUP];
	Cregs[MB_PERUP] = OFF;

	PERDOWN.mbRegs = &Cregs[MB_PERDOWN];
	Cregs[MB_PERDOWN] = OFF;
}

void ModbusDomoboard::leerAllSensor(void){
	leerSensor(&BOTON1);
	leerSensor(&BOTON2);
	leerSensor(&BTN_OPT);
	leerSensor(&PIR_MOV);
	leerSensor(&POT1);
	leerSensor(&POT2);
	leerSensor(&PHOTORES);
	leerSensor(&TEMPSEN);
	leerSensor(&PHOTOTTOR);
}

void ModbusDomoboard::leerSensor(TpmbSensor Sensor){

	DomoBoard::leerSensor(Sensor->Sensor);

	//compueba si el valor leído por el sensor difiere del valor almacenado en el registro correspondiente
	//del banco de registros
	if((int16_t)(*(Sensor->mbRegs)) != Sensor->Sensor->valor){
		//Estado Sensor ha cambiado
		//Se actualiza el registro correspondiente con el nuevo valor leído en el sensor.
		*(Sensor->mbRegs) = Sensor->Sensor->valor;
		//Se inícia el evento asociado a la actualización del banco de registro correpondiente
		if(Sensor->mbSensorEvent != NULL){
//			DEBUGLOGLN("%s: %s", __FUNCTION__, Sensor->Sensor->name.c_str());
			Sensor->mbSensorEvent(Sensor);
		}
	}
}

void	ModbusDomoboard::clear_Actuators(){
	// lambda function
	auto ClearSensor = [&](TpmbSensor sensor) {
		sensor->mbActuators.clear();
		sensor->mbSensorEvent = NULL;
		sensor->RelatedSensors = NULL;
	};

	ClearSensor(&BOTON1);
	ClearSensor(&BOTON2);
	ClearSensor(&BTN_OPT);
	ClearSensor(&PIR_MOV);
	ClearSensor(&POT1);
	ClearSensor(&POT2);
	ClearSensor(&PHOTORES);
	ClearSensor(&PHOTOTTOR);
}


void  ModbusDomoboard::setmbActuator(TmbActuator *Actuator, TStateDigitalDev val){
	bool newVal = (bool)val;

	if(val == TOGGLE){

		newVal = !(*Actuator->mbRegs);
	}

	if(*(Actuator->mbRegs) != newVal){
		*(Actuator->mbRegs) = newVal;

		setActuator(Actuator->actuator, *(Actuator->mbRegs));

	}
}

void ModbusDomoboard::setModBusSlave(ModbusSlave *mbSlave){
	mbs = mbSlave;
}

void ModbusDomoboard::manager_mbActuators(TmbActuators *Actuators, TStateDigitalDev val){

	for(int n = 0; n < Actuators->count(); n++){
		setmbActuator(Actuators->peek(n), val);
	}

}

void ModbusDomoboard::set_coilRegister(uint16_t addReg, uint16_t value){
	Cregs[addReg] = value;
}
