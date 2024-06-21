package Utilidades.modbus;

import modbus.ModBusEvent;

public interface IModBusResponse {
	
	void onModBusResponse(final ModBusEvent mbe);

}
