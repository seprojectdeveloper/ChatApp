// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

}
buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.2") // Check for the latest version
    }
}