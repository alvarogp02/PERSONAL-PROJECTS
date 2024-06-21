

#ifndef DOMOBOARD_MODBUSDOMOBOARD_H_
#define DOMOBOARD_MODBUSDOMOBOARD_H_

/**********************************************************************/
/***                         Include Files                          ***/
/**********************************************************************/
#include <domoBoard.h>
#include "ModBusSlave.h"
#include "AsyncWait.h"

/**********************************************************************/
/***                         enums Definitions                      ***/
/**********************************************************************/
/* slave registers */

//Discrete Output Coils
enum {
	MB_RELE,
	MB_TRIAC,
	MB_ACTPIR,					//Salida virtual para Activar/Desactivar PIR
	MB_ACTSRC,					//Salida virtual para Activar/Desactivar SRC --> PhotoResistor
	MB_ACTTOR,					//Salida virtual para Activar/Desactivar Trigger --> PhotoTransistor
	MB_PERUP,				//Salida virtual para el control de la persiana UP
	MB_PERDOWN,				//Salida Virtual para el control de la persiana DOWN
	MB_KEYGAR,		//Salida Virtual para el accionado de la llave del garage
	MB_O_COILS
};

//Discrete Input Contacts
enum {
	MB_BOTON1,
	MB_BOTON2,
	MB_BTNOPT,
	MB_PIRMOV,
	MB_I_CONTATCS
};

//Registros ModBus para variables analógicas "Analog Output Holding Register"
enum{
	MB_SELPRACT,		// Registro ModBus Para seleccionar la configuración del sistema
	MB_TMP_PIR,			// Registro para controlar el tiempo activo del sensor PIR (Segundos)
	MB_SRC_HL,			// Registro para controlar el nivel superior de activación SRC
	MB_SRC_LL,			// Registro para controlar el nivel Inferior de activación SRC
	MB_TTOR_LL,			// Registro para controlar el nivel Inferior de activación del PHOTOTTOR
	MB_STAPER,		    // Estado Persiana
	MB_A_REGS			// Total de registros Analógicos
};

//Registros Analógicos de Entrada
enum{
	MB_POT1,			// POTENCIÓMETRO 1
	MB_POT2,			// POTENCIÓMETRO 2
	MB_PHOTORES,		// PhotoResistor
	MB_TEMPSEN,			// Sensor de Temperatura
	MB_PHOTOTTOR,		// Photo Transistor
	MB_POSPER,			// Porcentage de apertura de la Persiana
	MB_I_REGS			// Total de registros de Entrada
};

/****************************************************************************/
/***        Type Definitions                                              ***/
/****************************************************************************/
//State Actuator
typedef enum{
	OFF,
	ON,
	TOGGLE
}TStateDigitalDev;

typedef struct
{
	tpsActuator		actuator;
	uint16_t 		*mbRegs;			//Registro asociado para comunicaciones ModBus
}TmbActuator, *TpmbActuator;

typedef 	QueueList<TpmbActuator>		TmbActuators;

//Estructura para usar niveles.
typedef struct{
	uint16_t	*lSup;					//puntero al valor del nivel superior
	uint16_t	*lInf;					//puntero al valor del nivel inferior
}TctrlLevel, *TctrlLevelPtr;

typedef struct{
	ptsSensor			Sensor;				//Sensor asociado
	int					Aux;				//Variable auxiliar, a la espera de definir el banco de registros digitales de entrada
	uint16_t 			*mbRegs;			//Registro asociado para comunicaciones ModBus
	TmbActuators		mbActuators;		//Listado de actuadores manejados por el sensor
	TNotifyEvent		mbSensorEvent;		//Evento para aplicación asociada
	AsyncWait			*asyncWait;			//Para controlar si el evento asociado al sensor tiene un componente temporizado
	TctrlLevel			ctrlLevelPtr;   	//Estructura para controlar utilidades que requieran de niveles
	void				*RelatedSensors;	//Lista de sensores relacionados para procesar el evento de sensor
}TmbSensor, *TpmbSensor;

typedef QueueList<TpmbSensor>	TmbRelatedSensors;

//Estructura para controlar los tiempos/porcentajes de activación (Persiana)
typedef struct{
	bool			activa;
	uint32_t 		lastAct;			//última actualización del tiempo
	uint16_t		*maxTime;			//puntero al tiempo máximo de activación
	int32_t			actTime;			//puntero al tiempo actual de activación
}TCtrlTime, *TPCtrlTime;

/*************************************************************/
/***                         CLASS                         ***/
/*************************************************************/
class ModbusDomoboard: public DomoBoard {
private:
	ModbusSlave	*mbs = NULL;

public:

	//Sensores Digitales
	TmbSensor	BOTON1;
	TmbSensor	BOTON2;
	TmbSensor	BTN_OPT;
	TmbSensor	PIR_MOV;

	//Sensores Analógicos
	TmbSensor	POT1;				// Potenciómetro 1
	TmbSensor	POT2;				// Potenciómetro 2
	TmbSensor	PHOTORES;			// Photoresistor
	TmbSensor	TEMPSEN;			// Sensor Temperatura
	TmbSensor	PHOTOTTOR;			// Photo Transistor

	TmbActuator RELE;
	TmbActuator TRIAC;

	//Virtual Actuators
	TmbActuator PERUP;// Actuador virtual para el control de la persiana - Subir
	TmbActuator PERDOWN;// Actuador virtual para el control de la persiana - Bajar

	ModbusDomoboard();
	virtual ~ModbusDomoboard(){};

	void 	leerAllSensor(void);
	void 	leerSensor(TpmbSensor Sensor);

	void 	setmbActuator(TmbActuator *Actuator, TStateDigitalDev val);
	void 	manager_mbActuators(TmbActuators *Actuators, TStateDigitalDev val);
	void 	setModBusSlave(ModbusSlave *mbSlave);
	void    clear_Actuators();

	void 	set_coilRegister(uint16_t addReg, uint16_t value);
};

extern ModbusDomoboard mbDomoboard;
extern uint16_t	Cregs[MB_O_COILS];
extern uint16_t	Dregs[MB_I_CONTATCS];	//Registros para "Dicrete Input Contacts"
extern uint16_t	Aregs[MB_A_REGS];		//Registros para "Analog Output Holding Registers"
extern uint16_t	Iregs[MB_I_REGS];		//Registros para "Analog Input Registers"

#endif /* DOMOBOARD_MODBUSDOMOBOARD_H_ */
