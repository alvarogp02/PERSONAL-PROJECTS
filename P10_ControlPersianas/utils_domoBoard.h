

#ifndef UTILS_DOMOBOARD_H_
#define UTILS_DOMOBOARD_H_

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include	"debuglog.h"
#include	"domoBoard.h"
#include	"ModbusDomoboard.h"

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
#define 	ON   				HIGH
#define     OFF					LOW

/****************************************************************************/
/***        Exported Functions                                            ***/
/****************************************************************************/
void Interruptor(void *Sensor);
void Pulsado_Soltado(void *Sensor);
void conmutador(void *Sensor);

void mbInterruptor(void *mbSensor);
void mbConmutador(void *mbSensor);
void mbInterruptorTemporizado(void *Sensor);
void Calc_Temperatura(void *mbSensor);
void interruptor_SRC(void *Sensor);
void trigger_Level(void *Sensor);

void Persiana(void *Sensor);
void UpDown_Persiana();
void Ctrl_PosicionPersiana(TPCtrlTime ctrlPosPer, tsStaPer staPer);
void update_PersianaState();

void PuertaGarage(void  *Sensor);

void AccionesTemporizadas(void);


/****************************************************************************/
/***        Exported Variables                                            ***/
/****************************************************************************/
extern QueueList<TpmbSensor>		SensoresTemporizados;


#endif /* UTILS_DOMOBOARD_H_ */
