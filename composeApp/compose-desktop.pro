# Keep Koin dependency injection classes
-keep class org.koin.** { *; }
-keep class com.yourpackage.** { *; }  # Replace with your actual package name

# Keep Database classes (e.g., SQLDelight)
-keep class com.example.Database { *; }
-keep class org.softsuave.bustlespot.SessionManager { *; }
-keep class com.squareup.sqldelight.** { *; }

-dontwarn kotlinx.serialization.KSerializer
-dontwarn kotlinx.serialization.Serializable
-dontwarn kotlinx.datetime.serializers.**
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
# Keep serializable classes
-keep @kotlinx.serialization.Serializable class ** { *; }

# Keep generated serializers
-keepclassmembers class **$$serializer {
    *;
}
### your config ....

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
   static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
   static **$* *;
}
-keepclassmembers class <2>$<3> {
   kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
   public static ** INSTANCE;
}
-keepclassmembers class <1> {
   public static <1> INSTANCE;
   kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Keep serialization infrastructure
-keep class kotlinx.serialization.** { *; }
-keep class kotlinx.serialization.json.** { *; }
-keepclassmembers class kotlinx.serialization.json.** { *; }
# Keep ALL classes in request/response packages
-keep class org.softsuave.bustlespot.data.network.models.request.** { *; }
-keep class org.softsuave.bustlespot.data.network.models.response.** { *; }
# Core Coil 3.x classes
-keep class coil3.** { *; }
-keep class coil3.compose.** { *; }
-keep class coil3.request.** { *; }

# ImageLoader and builders
-keep class coil3.ImageLoader { *; }
-keep class coil3.ImageLoader$Builder { *; }


# Request builders and crossfade
-keep class coil3.request.ImageRequest { *; }
-keep class coil3.request.ImageRequest$Builder { *; }


# Decoders and components
-keep class coil3.decode.** { *; }
-keep class coil3.component.** { *; }

# Service loader configuration
-keep class coil3.ComponentRegistry { *; }
-keep class coil3.ComponentRegistry$** { *; }

# If using network layers
-keep class coil3.network.** { *; }
-keep class coil3.fetch.** { *; }

# Keep Coil's internal dependencies
-keep class coil3.util.** { *; }
-keep class coil3.annotation.** { *; }

# Keep generated serializers for these packages
-keep class org.softsuave.bustlespot.data.network.models.request.**$$serializer { *; }
-keep class org.softsuave.bustlespot.data.network.models.response.**$$serializer { *; }

# Keep class members (prevents property removal/obfuscation)
-keepclassmembers class org.softsuave.bustlespot.data.network.models.request.** {
    *;
}
-keepclassmembers class org.softsuave.bustlespot.data.network.models.response.** {
    *;
}


# Keep auth signin data classes
-keep class org.softsuave.bustlespot.auth.signin.data.** { *; }

# Keep generated serializers for these classes
-keep class org.softsuave.bustlespot.auth.signin.data.**$$serializer { *; }

# Preserve class members (fields/methods)
-keepclassmembers class org.softsuave.bustlespot.auth.signin.data.** {
    *;
}

# Prevent field/method name obfuscation
-keepnames class org.softsuave.bustlespot.auth.signin.data.** { *; }

# Prevent property name mangling
-keepnames class org.softsuave.bustlespot.data.network.models.request.** { *; }
-keepnames class org.softsuave.bustlespot.data.network.models.response.** { *; }
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
-keep class org.slf4j.**

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

# Existing rules (partial list from your query)
-keep class org.koin.** { *; }
-keep class com.yourpackage.** { *; }
-keep class com.example.Database { *; }
-keep class org.softsuave.bustlespot.SessionManager { *; }
-keep class com.squareup.sqldelight.** { *; }
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# New rules for SQLite JDBC
-keep class org.sqlite.** { *; }
-keep interface org.sqlite.** { *; }
#-keepresources org/sqlite/native/**

# Optional: For jnativehook (if needed)
-keep class com.github.kwhat.jnativehook.** { *; }

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
## ----------------------------------------- ktor ------------------------------------------------- #
#-dontwarn org.slf4j.**
#
#
## ----------------------------------------- FFmpegMediaMetadataRetriever ------------------------- #
#-keep public class wseemann.media.**{*;}
#
#
## ----------------------------------------- App Rules -------------------------------------------- #
## Change here com.github.panpf.sketch.sample
#-keepclassmembers @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.** {
#    # lookup for plugin generated serializable classes
#    *** Companion;
#    # lookup for serializable objects
#    *** INSTANCE;
#    kotlinx.serialization.KSerializer serializer(...);
#}
## lookup for plugin generated serializable classes
#-if @kotlinx.serialization.Serializable class com.github.panpf.sketch.sample.**
#-keepclassmembers class com.github.panpf.sketch.sample.<1>$Companion {
#    kotlinx.serialization.KSerializer serializer(...);
#}
