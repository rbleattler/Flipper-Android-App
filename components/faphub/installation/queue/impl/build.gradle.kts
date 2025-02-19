plugins {
    id("flipper.android-lib")
    id("flipper.anvil")
}

android.namespace = "com.flipperdevices.faphub.installation.queue.impl"

dependencies {
    implementation(projects.components.faphub.installation.queue.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.data)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.progress)
    implementation(projects.components.core.preference)

    implementation(projects.components.settings.api)

    implementation(projects.components.faphub.utils)
    implementation(projects.components.faphub.dao.api)
    implementation(projects.components.faphub.target.api)
    implementation(projects.components.faphub.installation.manifest.api)

    implementation(projects.components.analytics.metric.api)

    implementation(projects.components.bridge.connection.feature.common.api)
    implementation(projects.components.bridge.connection.feature.provider.api)
    implementation(projects.components.bridge.connection.feature.storage.api)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.immutable.collections)
    implementation(libs.ktor.client)

    implementation(libs.coil)
}
