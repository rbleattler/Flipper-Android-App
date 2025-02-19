package com.flipperdevices.bridge.connection.device.fzero.impl.utils

import com.flipperdevices.bridge.connection.feature.alarm.api.FAlarmFeatureApi
import com.flipperdevices.bridge.connection.feature.appstart.api.FAppStartFeatureApi
import com.flipperdevices.bridge.connection.feature.common.api.FDeviceFeature
import com.flipperdevices.bridge.connection.feature.common.api.FDeviceFeatureApi
import com.flipperdevices.bridge.connection.feature.devicecolor.api.FDeviceColorFeatureApi
import com.flipperdevices.bridge.connection.feature.emulate.api.FEmulateFeatureApi
import com.flipperdevices.bridge.connection.feature.getinfo.api.FGattInfoFeatureApi
import com.flipperdevices.bridge.connection.feature.getinfo.api.FGetInfoFeatureApi
import com.flipperdevices.bridge.connection.feature.protocolversion.api.FSdkVersionFeatureApi
import com.flipperdevices.bridge.connection.feature.protocolversion.api.FVersionFeatureApi
import com.flipperdevices.bridge.connection.feature.restartrpc.api.FRestartRpcFeatureApi
import com.flipperdevices.bridge.connection.feature.rpc.api.FRpcFeatureApi
import com.flipperdevices.bridge.connection.feature.rpcinfo.api.FRpcInfoFeatureApi
import com.flipperdevices.bridge.connection.feature.screenstreaming.api.FScreenStreamingFeatureApi
import com.flipperdevices.bridge.connection.feature.screenstreaming.api.FScreenUnlockFeatureApi
import com.flipperdevices.bridge.connection.feature.seriallagsdetector.api.FLagsDetectorFeature
import com.flipperdevices.bridge.connection.feature.serialspeed.api.FSpeedFeatureApi
import com.flipperdevices.bridge.connection.feature.storage.api.FStorageFeatureApi
import com.flipperdevices.bridge.connection.feature.storageinfo.api.FStorageInfoFeatureApi
import com.flipperdevices.bridge.connection.feature.update.api.FUpdateFeatureApi
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlin.reflect.KClass

object FZeroFeatureClassToEnumMapper {
    private val classToEnumMap: ImmutableMap<KClass<out FDeviceFeatureApi>, FDeviceFeature> =
        FDeviceFeature.entries.associateBy { featureEnumToClass(it) }.toPersistentMap()

    @Suppress("CyclomaticComplexMethod")
    private fun featureEnumToClass(feature: FDeviceFeature): KClass<out FDeviceFeatureApi> {
        return when (feature) {
            FDeviceFeature.RPC -> FRpcFeatureApi::class
            FDeviceFeature.SERIAL_LAGS_DETECTOR -> FLagsDetectorFeature::class
            FDeviceFeature.SERIAL_RESTART_RPC -> FRestartRpcFeatureApi::class
            FDeviceFeature.SERIAL_SPEED -> FSpeedFeatureApi::class
            FDeviceFeature.VERSION -> FVersionFeatureApi::class
            FDeviceFeature.RPC_INFO -> FRpcInfoFeatureApi::class
            FDeviceFeature.STORAGE_INFO -> FStorageInfoFeatureApi::class
            FDeviceFeature.GET_INFO -> FGetInfoFeatureApi::class
            FDeviceFeature.STORAGE -> FStorageFeatureApi::class
            FDeviceFeature.ALARM -> FAlarmFeatureApi::class
            FDeviceFeature.DEVICE_COLOR -> FDeviceColorFeatureApi::class
            FDeviceFeature.GATT_INFO -> FGattInfoFeatureApi::class
            FDeviceFeature.SDK_VERSION -> FSdkVersionFeatureApi::class
            FDeviceFeature.APP_START -> FAppStartFeatureApi::class
            FDeviceFeature.SCREEN_STREAMING -> FScreenStreamingFeatureApi::class
            FDeviceFeature.SCREEN_UNLOCK -> FScreenUnlockFeatureApi::class
            FDeviceFeature.UPDATE -> FUpdateFeatureApi::class
            FDeviceFeature.EMULATE -> FEmulateFeatureApi::class
        }
    }

    fun get(clazz: KClass<out FDeviceFeatureApi>): FDeviceFeature? {
        return classToEnumMap[clazz]
    }
}
