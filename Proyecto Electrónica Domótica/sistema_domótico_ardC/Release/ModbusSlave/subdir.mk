################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
..\ModbusSlave\ModbusSlave.cpp 

LINK_OBJ += \
.\ModbusSlave\ModbusSlave.cpp.o 

CPP_DEPS += \
.\ModbusSlave\ModbusSlave.cpp.d 


# Each subdirectory must supply rules for building sources it contributes
ModbusSlave\ModbusSlave.cpp.o: ..\ModbusSlave\ModbusSlave.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\tools\avr-gcc\7.3.0-atmel3.6.1-arduino7/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10812 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\DomoBoard" -I"C:\Users\Usuario\Downloads\P10_ControlPersianas_cpp\P10_ControlPersianas\ModbusSlave" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\cores\arduino" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\variants\standard" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\packages\arduino\hardware\avr\1.8.6\libraries\EEPROM\src" -I"C:\Users\Usuario\Sloeber\arduinoPlugin\libraries\TimerOne\1.1.1" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -D__IN_ECLIPSE__=1 "$<" -o "$@"
	@echo 'Finished building: $<'
	@echo ' '


