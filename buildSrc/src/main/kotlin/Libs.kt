import kotlin.String

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Update this file with
 *   `$ ./gradlew buildSrcVersions`
 */
object Libs {
    /**
     * https://github.com/google/dagger
     */
    const val dagger: String = "com.google.dagger:dagger:" + Versions.com_google_dagger

    /**
     * https://github.com/google/dagger
     */
    const val dagger_compiler: String = "com.google.dagger:dagger-compiler:" +
            Versions.com_google_dagger

    /**
     * https://github.com/JakeWharton/butterknife/
     */
    const val butterknife: String = "com.jakewharton:butterknife:" + Versions.com_jakewharton

    /**
     * https://github.com/JakeWharton/butterknife/
     */
    const val butterknife_compiler: String = "com.jakewharton:butterknife-compiler:" +
            Versions.com_jakewharton

    /**
     * https://github.com/JakeWharton/butterknife/
     */
    const val butterknife_gradle_plugin: String = "com.jakewharton:butterknife-gradle-plugin:" +
            Versions.com_jakewharton

    /**
     * https://developer.android.com/studio
     */
    const val com_android_tools_build_gradle: String = "com.android.tools.build:gradle:" +
            Versions.com_android_tools_build_gradle

    /**
     * https://developer.android.com/testing
     */
    const val androidx_test_ext_junit: String = "androidx.test.ext:junit:" +
            Versions.androidx_test_ext_junit

    /**
     * http://junit.org
     */
    const val junit_junit: String = "junit:junit:" + Versions.junit_junit

    const val de_fayard_buildsrcversions_gradle_plugin: String =
            "de.fayard.buildSrcVersions:de.fayard.buildSrcVersions.gradle.plugin:" +
            Versions.de_fayard_buildsrcversions_gradle_plugin

    /**
     * http://developer.android.com/tools/extras/support-library.html
     */
    const val localbroadcastmanager: String =
            "androidx.localbroadcastmanager:localbroadcastmanager:" + Versions.localbroadcastmanager

    /**
     * http://tools.android.com
     */
    const val constraintlayout: String = "androidx.constraintlayout:constraintlayout:" +
            Versions.constraintlayout

    /**
     * https://developer.android.com/testing
     */
    const val espresso_core: String = "androidx.test.espresso:espresso-core:" +
            Versions.espresso_core

    /**
     * https://developer.android.com/topic/libraries/architecture/index.html
     */
    const val core_testing: String = "androidx.arch.core:core-testing:" + Versions.core_testing

    /**
     * https://github.com/mockito/mockito
     */
    const val mockito_core: String = "org.mockito:mockito-core:" + Versions.mockito_core

    /**
     * https://developer.android.com/studio
     */
    const val lint_gradle: String = "com.android.tools.lint:lint-gradle:" + Versions.lint_gradle

    /**
     * http://developer.android.com/tools/extras/support-library.html
     */
    const val annotation: String = "androidx.annotation:annotation:" + Versions.annotation

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val appcompat: String = "androidx.appcompat:appcompat:" + Versions.appcompat

    /**
     * https://github.com/ReactiveX/RxAndroid
     */
    const val rxandroid: String = "io.reactivex.rxjava2:rxandroid:" + Versions.rxandroid

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val core_ktx: String = "androidx.core:core-ktx:" + Versions.core_ktx

    /**
     * https://github.com/ReactiveX/RxJava
     */
    const val rxjava: String = "io.reactivex.rxjava2:rxjava:" + Versions.rxjava

    /**
     * https://developer.android.com/studio
     */
    const val aapt2: String = "com.android.tools.build:aapt2:" + Versions.aapt2
}
