package com.flipperdevices.remotecontrols.impl.setup.presentation.viewmodel

import android.content.Context
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import com.flipperdevices.bridge.connection.feature.emulate.api.FEmulateFeatureApi
import com.flipperdevices.bridge.connection.feature.emulate.api.exception.AlreadyOpenedAppException
import com.flipperdevices.bridge.connection.feature.emulate.api.model.EmulateConfig
import com.flipperdevices.bridge.connection.feature.provider.api.FFeatureProvider
import com.flipperdevices.bridge.connection.feature.provider.api.FFeatureStatus
import com.flipperdevices.bridge.connection.feature.provider.api.get
import com.flipperdevices.bridge.connection.feature.provider.api.getSync
import com.flipperdevices.bridge.dao.api.model.FlipperFilePath
import com.flipperdevices.bridge.dao.api.model.FlipperKeyType
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.android.vibrateCompat
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.preference.pb.Settings
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import com.flipperdevices.faphub.target.api.FlipperTargetProviderApi
import com.flipperdevices.faphub.target.model.FlipperTarget
import com.flipperdevices.ifrmvp.model.IfrKeyIdentifier
import com.flipperdevices.infrared.editor.core.model.InfraredRemote
import com.flipperdevices.keyemulate.tasks.CloseEmulateAppTaskHolder
import com.flipperdevices.remotecontrols.api.DispatchSignalApi
import com.flipperdevices.remotecontrols.impl.setup.encoding.ByteArrayEncoder
import com.flipperdevices.remotecontrols.impl.setup.encoding.JvmEncoder
import com.flipperdevices.remotecontrols.impl.setup.util.toByteArray
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ContributesBinding(AppGraph::class, DispatchSignalApi::class)
class DispatchSignalViewModel @Inject constructor(
    private val closeEmulateAppTaskHolder: CloseEmulateAppTaskHolder,
    private val flipperTargetProviderApi: FlipperTargetProviderApi,
    private val settings: DataStore<Settings>,
    private val context: Context,
    private val fFeatureProvider: FFeatureProvider
) : DecomposeViewModel(),
    LogTagProvider,
    DispatchSignalApi {
    override val TAG: String = "DispatchSignalViewModel"

    private val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

    private val _state = MutableStateFlow<DispatchSignalApi.State>(DispatchSignalApi.State.Pending)
    override val state = _state.asStateFlow()

    private var latestDispatchJob: Job? = null

    override fun reset() {
        viewModelScope.launch {
            latestDispatchJob?.cancelAndJoin()
            _state.value = DispatchSignalApi.State.Pending
        }
    }

    override fun dispatch(
        identifier: IfrKeyIdentifier,
        isOneTime: Boolean,
        remotes: List<InfraredRemote>,
        ffPath: FlipperFilePath,
        onDispatched: () -> Unit
    ) {
        when (flipperTargetProviderApi.getFlipperTarget().value) {
            FlipperTarget.NotConnected -> {
                _state.update { DispatchSignalApi.State.FlipperNotConnected }
                return
            }

            FlipperTarget.Unsupported -> {
                _state.update { DispatchSignalApi.State.FlipperNotSupported }
                return
            }

            else -> Unit
        }
        val i = remotes.indexOfFirst { remote ->
            when (identifier) {
                is IfrKeyIdentifier.Name -> {
                    remote.name == identifier.name
                }

                is IfrKeyIdentifier.Sha256 -> {
                    val encoder = JvmEncoder(ByteArrayEncoder.Algorithm.SHA_256)
                    identifier.hash == encoder.encode(remote.toByteArray())
                }

                IfrKeyIdentifier.Unknown -> {
                    error { "Found Unknown key identifier on remote ${remote.name}" }
                    false
                }
            }
        }
        val remote = remotes.getOrNull(i) ?: run {
            error { "Not found remote by identifier $identifier" }
            return
        }
        info { "#dispatch remote: ${remote.name} i: $i" }
        val config = EmulateConfig(
            keyPath = ffPath,
            keyType = FlipperKeyType.INFRARED,
            args = remote.name,
            index = i,
            isPressRelease = isOneTime
        )
        dispatch(
            config = config,
            identifier = identifier,
            onDispatched = onDispatched
        )
    }

    override fun dismissBusyDialog() {
        _state.value = DispatchSignalApi.State.Pending
    }

    override fun dispatch(
        config: EmulateConfig,
        identifier: IfrKeyIdentifier,
        onDispatched: () -> Unit
    ) {
        if (latestDispatchJob?.isActive == true) return
        latestDispatchJob = viewModelScope.launch {
            _state.emit(DispatchSignalApi.State.Pending)
            val isPressReleaseSupported = fFeatureProvider
                .get<FEmulateFeatureApi>()
                .map { status -> status as? FFeatureStatus.Supported<FEmulateFeatureApi> }
                .map { status -> status?.featureApi }
                .flatMapLatest { feature -> feature?.isInfraredEmulationSupported ?: flowOf(false) }
                .first()
            vibrator?.vibrateCompat(
                VIBRATOR_TIME,
                settings.data.first().disabled_vibration
            )
            _state.emit(DispatchSignalApi.State.Emulating(identifier))
            try {
                val fEmulateFeatureApi = fFeatureProvider.getSync<FEmulateFeatureApi>() ?: run {
                    error { "#dispatch could not find FEmulateFeatureApi" }
                    _state.value = DispatchSignalApi.State.Error
                    return@launch
                }
                fEmulateFeatureApi.getEmulateHelper().startEmulate(
                    scope = this,
                    config = config,
                )
                if (config.isPressRelease && isPressReleaseSupported) {
                    _state.emit(DispatchSignalApi.State.Pending)
                } else if (config.isPressRelease) {
                    delay(DEFAULT_SIGNAL_DELAY)
                    _state.emit(DispatchSignalApi.State.Pending)
                }
                fEmulateFeatureApi.getEmulateHelper().stopEmulate(
                    scope = this,
                    isPressRelease = config.isPressRelease && isPressReleaseSupported
                )
                onDispatched.invoke()
            } catch (ignored: AlreadyOpenedAppException) {
                _state.emit(DispatchSignalApi.State.FlipperIsBusy)
            } catch (e: Exception) {
                error(e) { "#tryLoad uncaught exception: could not dispatch signal" }
                _state.emit(DispatchSignalApi.State.Pending)
            }
        }
    }

    override fun stopEmulate() {
        viewModelScope.launch {
            val fEmulateFeatureApi = fFeatureProvider.getSync<FEmulateFeatureApi>() ?: run {
                error { "#dispatch could not find FEmulateFeatureApi" }
                _state.value = DispatchSignalApi.State.Error
                return@launch
            }
            vibrator?.vibrateCompat(
                VIBRATOR_TIME,
                settings.data.first().disabled_vibration
            )
            fEmulateFeatureApi.getEmulateHelper().stopEmulate(this)
            _state.emit(DispatchSignalApi.State.Pending)
        }
    }

    override fun onDestroy() {
        if (_state.value is DispatchSignalApi.State.Emulating) {
            closeEmulateAppTaskHolder.closeEmulateApp()
        }
        super<DecomposeViewModel>.onDestroy()
    }

    companion object {
        private const val DEFAULT_SIGNAL_DELAY = 500L
        private const val VIBRATOR_TIME = 100L
    }
}
