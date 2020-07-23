package spider65.ebike.tsdz2_esp32.data;

import android.util.Log;

import static spider65.ebike.tsdz2_esp32.TSDZConst.DEBUG_ADV_SIZE;

public class TSDZ_Debug {

    private static final String TAG = "TSDZ_Debug";

    public int dutyCycle; // D
    public int motorERPS; // D
    public int focAngle; // D
    public int torqueSensorValue; // D ADC torque sensor
    public int adcThrottle; // value from ADC Throttle/Temperature
    public int throttle; // Throttled mapped to 0-255
    public float pTorque; // Torque in Nm
    public int notUsed; // motorTemperature mapped to 0-255
    public float pcbTemperature;
    public int rxcErrors; // ESP32 RXC errors counter
    public int rxlErrors; // ESP32 RXL errors counter

    /*
    #pragma pack(1)
    typedef struct _tsdz_debug
    {
      volatile uint8_t ui8_adc_throttle;
      volatile uint8_t ui8_throttle;
      volatile uint16_t ui16_adc_pedal_torque_sensor;
      volatile uint8_t ui8_duty_cycle;
      volatile uint16_t ui16_motor_speed_erps;
      volatile uint8_t ui8_foc_angle;
      volatile uint16_t ui16_pedal_torque_x100;
      volatile uint16_t ui16_cadence_sensor_pulse_high_percentage_x10;
      volatile int16_t i16_pcb_temperaturex10;
    } struct_tsdz_debug;
     */

    public boolean setData(byte[] data) {
        if (data.length != DEBUG_ADV_SIZE) {
            Log.e(TAG, "Wrong Debug BT message size!");
            return false;
        }
        adcThrottle = (data[0] & 255);
        throttle = (data[1] & 255);
        torqueSensorValue = ((data[3] & 255) << 8) + (data[2] & 255);
        dutyCycle = (data[4] & 255);
        motorERPS = ((data[6] & 255) << 8) + (data[5] & 255);
        focAngle = (data[7] & 255);
        pTorque = (float)(((data[9] & 255) << 8) + (data[8] & 255)) / 100;
        notUsed = ((data[11] & 255) << 8) + (data[10] & 255);
        short s = (short) ((data[12] & 0xff) | (data[13] << 8));
        pcbTemperature = (float)(s) / 10;
        rxcErrors = (data[14] & 255);
        rxlErrors = (data[15] & 255);
        return true;
    }
}
