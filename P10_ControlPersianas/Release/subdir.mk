################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
..\Gest_Modbus.cpp \
..\P10_ControlPersiana.cpp \
..\config_practicas.cpp \
..\utils_domoBoard.cpp 

LINK_OBJ += \
.\Gest_Modbus.cpp.o \
.\P10_ControlPersiana.cpp.o \
.\config_practicas.cpp.o \
.\utils_domoBoard.cpp.o 

CPP_DEPS += \
.\Gest_Modbus.cpp.d \
.\P10_ControlPersiana.cpp.d \
.\config_practicas.cpp.d \
.\utils_domoBoard.cpp.d 


# Each subdirectory must supply rules for building sources it contributes
Gest_Modbus.cpp.o: ..\Gest_Modbus.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\tools\avr-gcc\7.3.0-atmel3.6.1-arduino7/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10812 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\DomoBoard" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\ModbusSlave" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\cores\arduino" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\variants\standard" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\libraries\EEPROM\src" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\libraries\TimerOne\1.1.1" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -D__IN_ECLIPSE__=1 "$<" -o "$@"
	@echo 'Finished building: $<'
	@echo ' '

P10_ControlPersiana.cpp.o: ..\P10_ControlPersiana.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\tools\avr-gcc\7.3.0-atmel3.6.1-arduino7/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10812 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\DomoBoard" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\ModbusSlave" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\cores\arduino" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\variants\standard" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\libraries\EEPROM\src" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\libraries\TimerOne\1.1.1" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -D__IN_ECLIPSE__=1 "$<" -o "$@"
	@echo 'Finished building: $<'
	@echo ' '

config_practicas.cpp.o: ..\config_practicas.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\tools\avr-gcc\7.3.0-atmel3.6.1-arduino7/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10812 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\DomoBoard" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\ModbusSlave" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\cores\arduino" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\variants\standard" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\libraries\EEPROM\src" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\libraries\TimerOne\1.1.1" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -D__IN_ECLIPSE__=1 "$<" -o "$@"
	@echo 'Finished building: $<'
	@echo ' '

utils_domoBoard.cpp.o: ..\utils_domoBoard.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\tools\avr-gcc\7.3.0-atmel3.6.1-arduino7/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10812 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\DomoBoard" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\ModbusSlave" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\cores\arduino" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\variants\standard" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\libraries\EEPROM\src" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\libraries\TimerOne\1.1.1" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -D__IN_ECLIPSE__=1 "$<" -o "$@"
	@echo 'Finished building: $<'
	@echo ' '


