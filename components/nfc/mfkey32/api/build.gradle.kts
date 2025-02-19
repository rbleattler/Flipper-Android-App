plugins {
    id("flipper.android-lib")
}

android.namespace = "com.flipperdevices.nfc.mfkey32.api"

dependencies {
    implementation(projects.components.core.ui.decompose)
    implementation(libs.bundles.decompose)

    implementation(projects.components.bridge.connection.feature.storage.api)

    implementation(libs.kotlin.coroutines)
}
