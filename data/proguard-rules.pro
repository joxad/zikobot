# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Jocelyn/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-keepattributes *Annotation*

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
}

-keep class com.joxad.zikobot.data.** {
    *;
}
-keep public class java.beans.ConstructorProperties
-dontwarn java.beans.ConstructorProperties

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Do not optimize/shrink LibVLC, because of native code
-keep class org.videolan.libvlc.** { *; }
# Same for MediaLibrary
-keep class org.videolan.medialibrary.** { *; }
-keep class com.spotify.sdk.android.** { *; }
