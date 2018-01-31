package com.overdraw.plugin.model
import jdk.internal.org.objectweb.asm.Opcodes

public class AnalyticsConfig {
    public static final String sAnalyticsClassName = 'com/gome/ganalytics/GMClick'

    public static final HashMap<String, MethodModel> sInterfaceMethods = new HashMap<>()
    static {
//        在所有android/view/View$OnClickListener类的实现方法onClick，参数为(Landroid/view/View;)V
//        替换为com/gome/ganalytics/GMClick.onEvent，方法参数为(Landroid/view/View;)V
        sInterfaceMethods.put("onClick(Landroid/view/View;)V", new MethodModel(
                'onClick',
                '(Landroid/view/View;)V',
                'android/view/View$OnClickListener',
                'onEvent',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put("onPause()V",new MethodModel(
                "onPause",
                "()V",
                "","","",1,1,[Opcodes.ALOAD]))
    }
}