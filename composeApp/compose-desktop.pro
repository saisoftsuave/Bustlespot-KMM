# Keep Koin dependency injection classes
-keep class org.koin.** { *; }
-keep class com.yourpackage.** { *; }  # Replace with your actual package name

# Keep Database classes (e.g., SQLDelight)
-keep class com.example.Database { *; }
-keep class org.softsuave.bustlespot.SessionManager { *; }
-keep class com.squareup.sqldelight.** { *; }

# Keep Jetpack Compose classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin Metadata for Reflection
-keepattributes *Annotation*, InnerClasses
-keep class kotlin.Metadata { *; }

# Prevent obfuscation of Ktor client (if using)
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep Coroutines (if using)
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Keep Multiplatform-related classes
-keep class platform.Foundation.** { *; }
-keep class platform.UIKit.** { *; }
-keep class platform.CoreGraphics.** { *; }

# ----------------------------------------- Basic ------------------------------------------------ #
-keepattributes *Annotation*

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


# ----------------------------------------- Okio ------------------------------------------------- #
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*


# ----------------------------------------- OkHttp ----------------------------------------------- #
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**


# ----------------------------------------- kotlinx serialization -------------------------------- #
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}


# ----------------------------------------- ktor ------------------------------------------------- #
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-keep class io.ktor.client.engine.cio.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

# Obfuscation breaks coroutines/ktor for some reason
-dontobfuscate


# ----------------------------------------- App Ruls --------------------------------------------- #
# Change here com.github.panpf.sketch.sample
-keepclassmembers @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.** {
    # lookup for plugin generated serializable classes
    *** Companion;
    # lookup for serializable objects
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
# lookup for plugin generated serializable classes
-if @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.**
-keepclassmembers class com.github.panpf.sketch.sample.<1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html


# ----------------------------------------- OkHttp ----------------------------------------------- #
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**


# ----------------------------------------- Okio ------------------------------------------------- #
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*


# ----------------------------------------- kotlinx serialization -------------------------------- #
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}


# ----------------------------------------- ktor ------------------------------------------------- #
-dontwarn org.slf4j.**


# ----------------------------------------- FFmpegMediaMetadataRetriever ------------------------- #
-keep public class wseemann.media.**{*;}


# ----------------------------------------- App Rules -------------------------------------------- #
# Change here com.github.panpf.sketch.sample
-keepclassmembers @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.** {
    # lookup for plugin generated serializable classes
    *** Companion;
    # lookup for serializable objects
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
# lookup for plugin generated serializable classes
-if @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.**
-keepclassmembers class com.github.panpf.sketch.sample.<1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
