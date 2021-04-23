# 忽略警告，避免打包时某些警告出现
-ignorewarnings
-dontwarn sun.**
-dontwarn javax.**
-dontwarn java.awt.**
-dontwarn org.apache.**
#-android

# 重命名抛出异常时的文件名称
-renamesourcefileattribute SourceFile
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify

-keepattributes *Annotation*,Signature,InnerClasses,SourceFile,LineNumberTable
-keepattributes *JavascriptInterface*

# 反射
-keepattributes EnclosingMethod
# 异常
-keepattributes Exceptions
#google推荐算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# 改变作用域提高优化效果
-allowaccessmodification

# 合并相关接口
-mergeinterfacesaggressively

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
# 在本项目中 初始设置为7 观察编译 发现到3的时候已经是最终结果 故改为3 仅适用本项目
-optimizationpasses 3

#把混淆类中的方法名也混淆了
#-useuniqueclassmembernames
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**