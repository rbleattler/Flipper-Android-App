package com.flipperdevices.bridge.connection.transport.ble.impl.di

import android.content.Context
import com.flipperdevices.bridge.connection.transport.ble.api.FBleDeviceConnectionConfig
import com.flipperdevices.bridge.connection.transport.ble.impl.BleDeviceConnectionApiImpl
import com.flipperdevices.bridge.connection.transport.ble.impl.api.FBleApiWithSerialFactory
import com.flipperdevices.bridge.connection.transport.ble.impl.utils.BLEConnectionDeviceHelper
import com.flipperdevices.bridge.connection.transport.common.api.di.DeviceConnectionApiHolder
import com.flipperdevices.bridge.connection.transport.common.api.di.toHolder
import com.flipperdevices.core.di.AppGraph
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner
import javax.inject.Singleton

@Module
@ContributesTo(AppGraph::class)
class BleDeviceConnectionModule {

    @Provides
    @IntoMap
    @ClassKey(FBleDeviceConnectionConfig::class)
    fun provideBleDeviceConnectionApi(
        context: Context,
        bleApiWithSerialFactory: FBleApiWithSerialFactory,
        connectionHelper: BLEConnectionDeviceHelper
    ): DeviceConnectionApiHolder = BleDeviceConnectionApiImpl(
        context = context,
        bleApiWithSerialFactory = bleApiWithSerialFactory,
        connectionHelper = connectionHelper
    ).toHolder()

    @Provides
    @Singleton
    fun provideBluetoothScanner(context: Context): BleScanner {
        return BleScanner(context)
    }
}
