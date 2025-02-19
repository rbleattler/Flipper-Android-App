plugins {
<#if needCompose>
    id("flipper.android-compose")
<#else>
    id("flipper.android-lib")
</#if>
    id("flipper.anvil")
}

android.namespace = "${packageName}.impl"

dependencies {
    implementation(projects.components.${__moduleName}.api)

    implementation(projects.components.core.di)
<#if needCompose>
    implementation(projects.components.core.ui.theme)
</#if>

<#if needCompose>
    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
</#if>

<#if needViewModel>
    // ViewModel
    implementation(libs.lifecycle.compose)

</#if>

<#if needTest>
    // Testing
    testImplementation(projects.components.core.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.ktx.testing)
    testImplementation(libs.roboelectric)
    testImplementation(libs.lifecycle.test)
    testImplementation(libs.kotlin.coroutines.test)
</#if>
}
