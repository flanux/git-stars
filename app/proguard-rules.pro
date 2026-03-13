# GitHub API
-keep class org.kohsuke.github.** { *; }
-dontwarn org.kohsuke.github.**

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Data classes
-keep class com.flanux.gitstars.data.** { *; }
