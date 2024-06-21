

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include 	"utils_domoBoard.h"
#include 	"HardwareSerial.h"
#include	"config_practicas.h"

/*************************************************************************/
/***                   Constantes Control Persiana                     ***/
/*************************************************************************/

#define	STEP_CTRL_BLIND		5
#define PERCENTAGE(x)		((x % STEP_CTRL_BLIND) > 2) ? x + (STEP_CTRL_BLIND-(x % STEP_CTRL_BLIND)) : x - (x % STEP_CTRL_BLIND);

#define V_PERCENTAGE(x)    PERCENTAGE((((TPCtrlTime)x)->actTime)*100/(*((TPCtrlTime)x)->maxTime));

const uint16_t	persianaTiempoParada 	= 1; //Seg.
const uint16_t	persianaTiempoAbierta 	= 5; //seg.

/******************************************************************/
/***                      Variables Locales                     ***/
/******************************************************************/
//Lista de sensores temporizados
QueueList<TpmbSensor>		SensoresTemporizados;


/*============================================*/
/*		 		  INTERRUPTOR                 */
/*============================================*/
void Interruptor(void *Sensor)
{
	if(((ptsSensor)Sensor)->valor_Df == ((ptsSensor)Sensor)->valor){
		Serial.print(((ptsSensor)Sensor)->name);
		Serial.print(" : Interruptor --> ");
		if(((ptsSensor)Sensor)->Aux == OFF){
			((ptsSensor)Sensor)->Aux = ON;
			DEBUGLNF("ON");
		}else{
			((ptsSensor)Sensor)->Aux = OFF;
			DEBUGLNF("OFF");
		}
	}

	DomoBoard::manageSensorActuators(&((ptsSensor)Sensor)->managerActuators, ((ptsSensor)Sensor)->Aux);
}

/*============================================*/
/*		 		    PULSADOR                  */
/*============================================*/
void Pulsado_Soltado(void *Sensor){
	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	Serial.print(sensor->name);
	if(sensor->valor_Df == sensor->valor){
		DEBUGLNF(" --> Soltado");
	}else{
		DEBUGLNF(" --> Pulsado");
	}

	DomoBoard::manageSensorActuators(&(sensor->managerActuators), !(sensor->valor_Df == sensor->valor));
}

/*============================================*/
/*		     	   CONMUTADOR                 */
/*============================================*/
void conmutador(void *Sensor)
{
	static int valor = OFF;

	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	if(sensor->valor_Df == sensor->valor){
		DEBUGF("Conmutador --> ");
		if(valor == OFF){
			valor = ON;
			DEBUGLNF("ON");
		}else{
			valor = OFF;
			DEBUGLNF("OFF");
		}

		//Actualiza Actuadores
		//DomoBoard::setActuator(&domoboard.RELE, valor);

		DomoBoard::manageSensorActuators(&(sensor->managerActuators), valor);
	}
}

/*============================================*/
/*		      INTERRUPTOR MODBUS              */
/*============================================*/
void mbInterruptor(void *mbSensor)
{
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(mbSensor);

	if(sensor->Sensor->valor_Df == sensor->Sensor->valor){
		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), TOGGLE);
	}
}


/*============================================*/
/*		 	   CONMUTADOR MODBUS              */
/*============================================*/
void mbConmutador(void *mbSensor)
{
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(mbSensor);

	if(sensor->Sensor->valor_Df == sensor->Sensor->valor){
		//Estado del sensor ha cambiado
		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), TOGGLE);
	}
}

/*========================================================*/
/*		      INTERRUPTOR TEMPORIZADO MODBUS              */
/*========================================================*/

/*
 * Este sensor será activado mediante Sensor (Variable de control) y aunque se vuelva inactivo,
 * el interruptor, permanecerá activo durante el tiempo que indique el temporizador.
 * Si durante el tiempo que el interruptor está activo, la variable de control permanece inactiva,
 * o se activa de nuevo, el tiempo que el interruptor permanece activo se
 * irá actualizando, de tal forma que el tiempo que el interruptor permanece ativo,
 * siempre se cuenta desde la última vez que el sensor se activo.
 *
 */

void	mbInterruptorTemporizado(void *Sensor){

	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	if(sensor->Sensor->valor_Df != sensor->Sensor->valor){
		sensor->asyncWait->restart();

		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), (TStateDigitalDev)ON);

	}else{
		if(!sensor->asyncWait->isWaiting() && !sensor->asyncWait->isVerified()){

			DEBUGLNF("ASYNWAIT TERMINADO");

			sensor->asyncWait->setVerified();
			mbDomoboard.manager_mbActuators(&(sensor->mbActuators), (TStateDigitalDev)OFF);
		}
	}

}

/*****************************************************************************************/
/***********************************  Leer Temperatura ***********************************/
/*****************************************************************************************/
//mas/menos 1 - 2ºC de exactitud para el TMP36, por lo que escribimos valores decimales
void Calc_Temperatura(void *Sensor){
	float valTMP;
	int   temp;

	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	valTMP = sensor->Sensor->valor*.004882812;	//Conviere resultado convertidor a voltios

	valTMP = (valTMP - .5)*100;          //Convierte Temperatura de 10 mV por grado con 500 mV de Offset


	temp = (valTMP - (int)valTMP)*100;

    if(temp < 50) temp = 0;
    //else if ((25 < temp)&&(temp < 75)) temp = 5;
    //else if ((75 < temp)&&(temp <= 99)){
    else{
    	temp = 0;
    	valTMP = (int)valTMP +1;
    }

	*(sensor->mbRegs) = (((int)valTMP & 0xff) << 8) | (temp & 0xff);

#ifdef DEBUG_TEMP
    static float vT = 0;

    if(vT != valTMP){
    	vT = valTMP;
    	/*
    	DEBUGF("Temperatura = " );
    	Serial.print((int)valTMP,DEC);
    	Serial.print(".");
    	Serial.println(temp,DEC);
    	*/
    	DEBUGLOG("Temperatura = %d.%d\n", (int)valTMP, temp);

    //	Serial.print("Temperatura F -> ");
    //	Serial.println(Sensor->Regs_App[Sensor->MBReg_App],BIN);
    }
#endif
}

/*============================================*/
/*			  INTERRUPTOR LUMINOSIDAD         */
/*============================================*/
/*
 * Interruptor por nivel de luminosidad. funcionará con una histeresis, es decir,
 * Si el interruptor está desactivado, se activará cuando alcance el "highlevel".
 *
 * Si el interruptor está ativado, se desactivará cuando alcanze el "lowlevel".
 */
void	interruptor_SRC(void *Sensor){
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	if((int)*(sensor->ctrlLevelPtr.lInf) > (int)*(sensor->ctrlLevelPtr.lSup))
		return;

	int state = sensor->Sensor->Aux;

	switch(state){
	case 1:
		if(sensor->Sensor->valor <= (int)*(sensor->ctrlLevelPtr.lInf)){
			state = 2;
			//Estado del sensor ha cambiado
			mbDomoboard.manager_mbActuators(&(sensor->mbActuators), (TStateDigitalDev)ON);
		}

		break;

	case 2:
		if(sensor->Sensor->valor >= (int)*(sensor->ctrlLevelPtr.lSup)){
			state = 1;
			mbDomoboard.manager_mbActuators(&(sensor->mbActuators), (TStateDigitalDev)OFF);
		}

		break;

	default:
		state = 2;
		mbDomoboard.set_coilRegister(MB_TRIAC, OFF);
	}

	sensor->Sensor->Aux = state;
}

/*============================================*/
/*			  TRIGGER WITH LUMINOSIDAD        */
/*============================================*/
/*
 * tRIGGER por nivel de luminosidad. Dispara un evento cuando el nivel del sensor
 * se encuentra por debajo de un determinado nivel
 */
void	trigger_Level(void *Sensor){
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	uint16_t limInfe = *(sensor->ctrlLevelPtr.lInf);

	if(((uint16_t) sensor->Sensor->valor <= limInfe)){
		if(sensor->Sensor->Aux == 0){
			mbDomoboard.manager_mbActuators(&(sensor->mbActuators), (TStateDigitalDev)ON);
			sensor->Sensor->Aux = 1;
		}
	} else sensor->Sensor->Aux = 0;
}

/*============================================*/
/*			    CONTROL DE PERSIANA           */
/*============================================*/

void UpDown_Persiana(){
	bool UpP,DownP;
	tsStaPer state = (tsStaPer)(Aregs[MB_STAPER]&0xFF);

	UpP = (bool)(*mbDomoboard.PERUP.mbRegs);
	DownP = (bool)(*mbDomoboard.PERDOWN.mbRegs);

    switch(state){
    	case PER_STOP: //Parada
    		if (UpP == ON) {
    			state = PER_UP; //Subiendo
    		}

    		if (DownP == ON) {
    			state = PER_DOWN; //Bajando
    		}

    		break;

    	case PER_UP: //Subiendo
    		if(!UpP && !DownP) {
    			state = PER_STOP;
    		}

    		if(!UpP && DownP){
    			state = PER_STOP2;
    		}
    		break;

    	case PER_DOWN: //Bajando
    		if(!UpP && !DownP) {
    			state = PER_STOP;
    		}

    		if(UpP && !DownP){
    			state = PER_STOP2;
    		}
    		break;

    	case PER_STOP2:
    		if(!UpP && !DownP) {
    			state = PER_STOP;
    		}
    		break;

    }

    if(Aregs[MB_STAPER] != state){
    	//mbDomoboard.set_holdingRegister(MB_STAPER, state);
    	Aregs[MB_STAPER] = state;

    	update_PersianaState();
    }
}

void Persiana(void *Sensor){

	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	//Se asigna un estado al actuador, en función del estado del sensor
	for(int n = 0; n < sensor->mbActuators.count(); n++){
		*(sensor->mbActuators.peek(n))->mbRegs = (sensor->Sensor->valor != sensor->Sensor->valor_Df);
	}

	UpDown_Persiana();
}

void Ctrl_PosicionPersiana(TPCtrlTime ctrlPosPer, tsStaPer staPer){
	static tsStaPer lastStePer = PER_STOP;

	// lambda function
	auto actualizeTime = [&](int8_t sign){

		ctrlPosPer->actTime = ctrlPosPer->actTime + sign*(millis() - ctrlPosPer->lastAct);

		if(ctrlPosPer->actTime < 0){
			ctrlPosPer->actTime = 0;
		}else if(uint16_t(ctrlPosPer->actTime) > *ctrlPosPer->maxTime){
			ctrlPosPer->actTime = *ctrlPosPer->maxTime;
		}

		uint8_t per = PERCENTAGE((ctrlPosPer->actTime)*100/(*ctrlPosPer->maxTime));

		uint8_t per2 = V_PERCENTAGE(ctrlPosPer);

		//DEBUGLOGLN("POS PER: %d - %d", per, per2);

		if(per != Iregs[MB_POSPER]){
			Iregs[MB_POSPER] = per;
		}
	};

	switch(staPer){
	case PER_STOP:
	case PER_STOP2:
		ctrlPosPer->activa = false;
		switch(lastStePer){
		case PER_DOWN:
			actualizeTime(-1);
			break;
		case PER_UP:
			actualizeTime(1);
			break;
		default:
			break;
		}
		break;

	case PER_DOWN:
		if(ctrlPosPer->activa){
			//Continúa bajado
			actualizeTime(-1);
		}else{
			//Comienza a bajada
			ctrlPosPer->activa = true;
		}
		ctrlPosPer->lastAct = millis();
		break;

	case PER_UP:
		if(ctrlPosPer->activa){
			//Continúa subiendo
			actualizeTime(1);
		}else{
			//Comienza a subir
			ctrlPosPer->activa = true;
		}
		ctrlPosPer->lastAct = millis();
		break;
	}

	lastStePer = staPer;
}

void update_PersianaState(){
	//Actualiza posición Persiana
	Ctrl_PosicionPersiana(&ctrlPosPer, tsStaPer(Aregs[MB_STAPER]&0xFF));

	mbDomoboard.SetPersiana(tsStaPer(Aregs[MB_STAPER]&0xFF));
}

/*============================================*/
/*			  CONTROL PUERTA GARAJE           */
/*============================================*/
void PuertaGarage(void  *Sensor){
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(Sensor);

	static uint8_t state = 'A';
	tsStaPer statePer = (tsStaPer)(Aregs[MB_STAPER]&0xFF);

//	DEBUGLOGLN("%s:  State:%c", __FUNCTION__, state);

	TmbRelatedSensors *RelatedSensors = (TmbRelatedSensors *)sensor->RelatedSensors;

	// lambda function
	auto checkSensors = [&](){

		if(Cregs[MB_KEYGAR]){
			return true;
		}else if(sensor->asyncWait != NULL){
			//BTN1 Sensor
			if(sensor->Sensor->valor != sensor->Sensor->valor_Df){
				//Llave/Btn1 Accionado
				Cregs[MB_KEYGAR] = true;
				return true;
			}
		}else{
			//FhotoTTor Sensor

			uint16_t limInfe = *(sensor->ctrlLevelPtr.lInf);

			if(((uint16_t) sensor->Sensor->valor) <= limInfe){
				//Barrera de luz interrumpida
				return true;
			}
		}

		return false;
	};


	// lambda function
	auto Init_timelastAct = [&](uint16_t *tmpAct = NULL){
		if(sensor->asyncWait != NULL){
			sensor->asyncWait->startWaiting(tmpAct);
		}else if(RelatedSensors->count() > 0){
			RelatedSensors->peek(0)->asyncWait->startWaiting(tmpAct);
		}
	};

	switch(state){
	case 'A':
		//Sólo se debe abrir con llave/Btn1
		if(sensor->asyncWait != NULL){
			if(*sensor->mbRegs || Cregs[MB_KEYGAR]){
				Cregs[MB_KEYGAR] = false;
				state = 'B';		//llave accionada, abrimos puerta
				statePer = PER_UP;
			}
		}
		break;

	case 'B':
		if(Iregs[MB_POSPER] >= 100){
			//Puerta abierta
			state = 'C';
			statePer = PER_STOP;

			Init_timelastAct((uint16_t *)&persianaTiempoAbierta);
		}
		break;

	case 'C':
		//Comprobar estado BTN1 y FotoTTOR
		if(checkSensors()){
			Cregs[MB_KEYGAR] = false;
			Init_timelastAct((uint16_t *)&persianaTiempoAbierta);
		}else{
			if(sensor->asyncWait != NULL){
				if(!sensor->asyncWait->isWaiting()){
					state = 'D';
					statePer = PER_DOWN;
				}
			}
		}
		break;

	case 'D':
		//Comprobar estado BTN1 y FotoTTOR
		if(checkSensors()){
			//Llave/Btn1 Accionada, o Barrera de luz (fotottor) interrumpida
			Cregs[MB_KEYGAR] = false;
			state = 'E';
			statePer = PER_STOP;

			//Inicia temporizador para esperar 1 segundo
			Init_timelastAct((uint16_t *)&persianaTiempoParada);

		}else{
			if(Iregs[MB_POSPER] == 0){
				state = 'A';
				statePer = PER_STOP;
			}
		}
		break;

	case 'E':
		if(sensor->asyncWait != NULL){
			if(!sensor->asyncWait->isWaiting()){
				state = 'B';
				statePer = PER_UP;
			}
		}
		break;

	default:
		break;
	}

	if(Aregs[MB_STAPER] != statePer){
		Aregs[MB_STAPER] = statePer;
		//mbDomoboard.set_holdingRegister(MB_STAPER, statePer);

		update_PersianaState();

//		DEBUGLOGLN("%s:  State:%c", __FUNCTION__, state);
	}
}

/*====================================================================================
 * Llamada regular usada para gestionar los temporizadores usados por la aplicación. =
 * es llamada cada LOOP_TIME (10 ms)
 *====================================================================================*/
void AccionesTemporizadas(void){

	TpmbSensor pmbSensor;

	for(int n=0; n<SensoresTemporizados.count(); n++){

		pmbSensor = SensoresTemporizados.peek(n);

		pmbSensor->mbSensorEvent(pmbSensor);
	}
}




