# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/embed/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)

-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**


-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

-keep class com.google.android.gms.ads.identifier.** { *; }

# joda time
-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**

# jackson
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.**



# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

-dontwarn okhttp3.**


-dontwarn org.slf4j.**

# EventBus 3.0
-keepclassmembers class ** {
    public void onEvent*(**);
}



# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#For Dagger
-dontwarn com.google.errorprone.annotations.**

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepattributes SourceFile, LineNumberTable
-keep,allowshrinking,allowoptimization class * { <methods>; }


-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.okhttp.**

-keep public class com.couchbase.lite.** { *; }
-dontwarn com.couchbase.lite.**
-keep public class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.**

-dontwarn org.apache.commons.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-keep class mypackage.MyCarGrid { *; }

# Twilio Client
-keep class com.twilio.** { *; }

#WebRtc
-keep class org.webrtc.** { *; }

#for glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclassmembers enum * { *; }
