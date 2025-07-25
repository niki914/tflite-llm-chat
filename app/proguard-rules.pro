-keepnames class kotlinx.coroutines.internal.* { *; }
-dontwarn kotlinx.coroutines.flow.**
-dontwarn kotlinx.coroutines.internal.**
-dontwarn kotlinx.coroutines.debug.**

-keep class com.google.mediapipe.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于 Kotlin 反射和泛型，通常也需要一些规则
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.Continuation
-keep class kotlin.coroutines.intrinsics.IntrinsicsKt
-keep class kotlin.jvm.internal.DefaultConstructorMarker { *; }

# 对于 suspend 函数的通用规则 (如果遇到问题可以添加)
-keep class * extends kotlin.coroutines.Continuation { *; }

-dontwarn **