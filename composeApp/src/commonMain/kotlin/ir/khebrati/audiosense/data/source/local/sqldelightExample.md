```SQL
-- In .sq files
CREATE TABLE IF NOT EXISTS DeviceEntity(
deviceEntityId INTEGER PRIMARY KEY,
deviceEntityName TEXT,
SN TEXT,
deviceModelEntityId INTEGER NOT NULL,
firmwareEntityId INTEGER NOT NULL,
side TEXT AS Side NOT NULL,
FOREIGN KEY (deviceModelEntityId) REFERENCES DeviceModelEntity(deviceModelEntityId) ON DELETE CASCADE,
FOREIGN KEY (firmwareEntityId) REFERENCES FirmwareEntity(firmwareEntityId) ON DELETE CASCADE
);


addDeviceEntity:
INSERT INTO DeviceEntity (deviceEntityName, SN, deviceModelEntityId, firmwareEntityId, side) VALUES (?, ?, ?,?,?);

updateDeviceEntity:
UPDATE DeviceEntity SET deviceEntityName = ?, SN = ?, deviceModelEntityId = ?, firmwareEntityId = ?, side = ? WHERE deviceEntityId = ?;
```
repositories
```kotlin
class DeviceRepositoryImpl(private val fittingDb: FittingDb) : DeviceRepository {

    private val deviceQueries = fittingDb.deviceQueries

    override suspend fun addDevice(device: Device, deviceModelId: Long, firmwareId: Long): Long {
        device.let {
            deviceQueries.addDeviceEntity(
                deviceModelEntityId = deviceModelId,
                firmwareEntityId = firmwareId,
                deviceEntityName = it.name,
                SN = it.SN,
                side = it.side)

            return deviceQueries.getLastDeviceEntity().executeAsOne().toDevice().id
        }
    }

    override fun updateDevice(
        deviceId: Long,
        device: Device,
        deviceModelId: Long,
        firmwareId: Long
    ) {
        device.let {
            deviceQueries.updateDeviceEntity(
                it.name, it.SN, deviceModelId, firmwareId, it.side, deviceId)
        }
    }
    /**
     * Fetches all DeviceModels from the database.
     *
     * @return A Flow that emits a list of all DeviceModels in the database. if no deviceModel
     *   exists, an empty list is emitted.
     */
    override fun getAllDeviceModels() =
        deviceModelQueries.getAllDeviceModelEntities().asFlow().mapToList(Dispatchers.IO).map {
                deviceModelEntities ->
            deviceModelEntities.map { it.toDeviceModel() }
        }


```

