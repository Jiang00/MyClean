# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/guoyang/Developer/android-sdk-macosx/tools/proguard/proguard-android.txt
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

-keep class com.ivy.kpa.NativeDaemonBase{*;}
-keep class com.ivy.kpa.nativ.NativeDaemonAPI20{*;}
-keep class com.ivy.kpa.nativ.NativeDaemonAPI21{*;}
-keep class com.ivy.kpa.DaemonApplication{*;}
-keep class com.ivy.kpa.DaemonClient{*;}
-keepattributes Exceptions,InnerClasses,...
-keep class com.ivy.kpa.DaemonConfigurations{*;}
-keep class com.ivy.kpa.DaemonConfigurations$*{*;}