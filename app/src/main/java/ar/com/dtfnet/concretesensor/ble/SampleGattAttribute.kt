package ar.com.dtfnet.concretesensor.ble

import java.util.*

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
object SampleGattAttributes {
    var attributes: HashMap<String, String> = HashMap()

    // Heart rate. Es el ejemplo. Habrá que borrarlo.
    var HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"

    val QCA402xServiceUUID: UUID = UUID.fromString("00001650-0000-1000-8000-00805f9b34fb")
    val QCA402xCharacteristicUUID: UUID = UUID.fromString("00001651-0000-1000-8000-00805f9b34fb")
    val QCA402xSendCharacteristicUUID: UUID = UUID.fromString("00001652-0000-1000-8000-00805f9b34fb")
    val CLIENT_CHARACTERISTIC_CONFIG: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")


    private const val GENERIC_ATTRIBUTE: String = "1801"
    private const val GENERIC_ACCESS: String = "1800"
    private const val DEVICE_NAME: String = "2a00"
    private const val DEVICE_APPEARANCE: String = "2a01"
    private const val DEVICE_INFORMATION: String = "180a"
    private const val MANUFACTURER_NAME: String = "2a29"
    private const val MODEL_NUMBER: String = "2a24"
    private const val SYSTEM_ID: String = "2a23"
    private const val BATTERY_SERVICE: String = "1807"
    private const val BATTERY_LEVEL: String = "2a19"


    // WiFi introduce service and characteristics
    private const val WIFI_INTRODUCE_SERVICE: String = "1b7e8251-2877-41c3-b46e-cf057c562524"
    private const val WIFI_CHARACTERISTIC_NOTIFY: String = "8ac32d3f-5cb9-4d44-bec2-ee689169f626"
    private const val WIFI_SECURITY: String = "cac2aba4-edbb-4c4a-bbaf-0a84a5cd93a1"
    private const val WIFI_SSID_VALUE: String = "aca0ef7c-eeaa-48ad-9508-19a6cef6b356"
    private const val WIFI_PASSPHRASE_VALUE: String = "40b7de33-93e4-4c8b-a876-d833b415a6ce"
    private const val WIFI_CONNECTION: String = "36ff80f9-3ced-689c-5e4b-8d81151e2c1d"

    // Mixer data characteristics
    private const val MIXER_DATA_VALUES = "3b604cda-cb56-43d6-a335-8c7dfd109842"
    private const val MIXER_PRESSURE = "f93ec7f6-ba2b-4873-9382-e318b5fc3285"
    private const val MIXER_RPM = "1dace3c8-ce30-419f-9676-972478e7e90d"
    private const val MIXER_ADD_WATER = "41d88b81-8bd7-4b10-9393-27743d527f2c"
    private const val MIXER_TURNS = "a2f95783-3b1c-45d1-b9cd-ac40fd51171e"
    private const val MIXER_SLUMP = "bbf8b42d-01a0-434f-bf27-064ca9274d71"


    // WiFi TxRx control characteristics
    private const val WIFI_TXRX_CONTROL = "ad7e3b41-d9e4-15be-9640-528cb8fe0d6a"
    private const val WIFI_TXRX_INIT = "36fbe439-8afd-5890-eb41-4f4893d72ea2"
    private const val WIFI_TXRX_START = "8c92db0a-c270-6687-5842-9e580646cdc5"
    private const val WIFI_TXRX_ON_OFF = "e4228370-f1c0-aa9a-c642-62d3ea480f50"

    // Device Id setup characteristics
    private const val DEVICE_ID_SETUP = "df785aa1-726a-0187-f545-7fa4083137e0"
    private const val DEVICE_ID_SENSOR = "bbbe8f9b-227f-57a7-cc4a-63f8555bd116"
    private const val DEVICE_ID_USER = "7ce81d02-7753-9488-4d4b-1f4c52e16867"
    private const val DEVICE_ID_TRUCK = "09c5b04c-39bc-cabd-1648-16aab25b859d"


    init {
        attributes.put("00001650-0000-1000-8000-00805f9b34fb", "QCA402X Service")
        attributes.put("00001651-0000-1000-8000-00805f9b34fb", "Characteristic")
        attributes.put("00001652-0000-1000-8000-00805f9b34fb", "Send Characteristic")
        attributes.put("00002902-0000-1000-8000-00805f9b34fb", "CLIENT_CHARACTERISTIC_CONFIG")

        // WiFi introducer service
        attributes.put(WIFI_INTRODUCE_SERVICE, "WiFi Introduce")
        // WiFi introducer characteristics
        attributes.put(WIFI_CHARACTERISTIC_NOTIFY, "Characteristic notify")
        attributes.put(WIFI_SECURITY, "Security")
        attributes.put(WIFI_SSID_VALUE, "SSID value")
        attributes.put(WIFI_PASSPHRASE_VALUE, "Passphrase value")
        attributes.put(WIFI_CONNECTION, "Connection")

        // Mixer data service
        attributes.put(MIXER_DATA_VALUES, "Mixer data values");
        // Mixer data characteristics
        attributes.put(MIXER_PRESSURE, "Pressure");
        attributes.put(MIXER_RPM, "RPM");
        attributes.put(MIXER_ADD_WATER, "Added water");
        attributes.put(MIXER_TURNS, "Turns");
        attributes.put(MIXER_SLUMP, "Slump");

        // WiFi TxRx control service
        attributes.put(WIFI_TXRX_CONTROL, "WiFi TxRx control");
        // WiFi TxRx control characteristics
        attributes.put(WIFI_TXRX_INIT ,"WiFi TxRx init");
        attributes.put(WIFI_TXRX_START ,"WiFi TxRx start");
        attributes.put(WIFI_TXRX_ON_OFF ,"WebSocket on, off");

        // Device Id setup service
        attributes.put(DEVICE_ID_SETUP, "Device Id Setup");

        // Device Id setup characteristics
        attributes.put(DEVICE_ID_SENSOR, "Sensor id setup");
        attributes.put(DEVICE_ID_USER, "User id setup");
        attributes.put(DEVICE_ID_TRUCK, "Truck id setup");

    }

    fun lookup(uuid: String, defaultName: String): String {
        val name = attributes.get(uuid)
        return name ?: defaultName
    }
}
