package com.overdraw.plugin.modify

import com.android.utils.ILogger
import com.overdraw.plugin.model.AnalyticsConfig
import com.overdraw.plugin.model.MethodModel
import org.objectweb.asm.*

public class AsmModify {

    public byte[] modifyClass(InputStream inputStream, ILogger logger) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            String mClassName;
            String mSuperName;
            String[] mInterfaces;
            //visit( 类版本 , 修饰符 , 类名 , 泛型信息 , 继承的父类 , 实现的接口)
            @Override
            void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                mClassName = name
                mSuperName = superName
                mInterfaces = interfaces;
                super.visit(version, access, name, signature, superName, interfaces)
            }

            //visitMethod(修饰符 , 方法名 , 方法签名 , 泛型信息 , 抛出的异常)
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc,
                                             String signature, String[] exceptions) {
                logger.info("mv:" + mSuperName + "--" + mClassName + "--" + name + "--" + desc + "--interface:" + mInterfaces)

                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                if(mInterfaces != null && mInterfaces.length >0){
                    final MethodModel methodModel = AnalyticsConfig.sInterfaceMethods.get(name + desc);
                    if(methodModel!=null && mInterfaces.contains(methodModel.parent)){
                        mv = new MethodVisitor(Opcodes.ASM4, mv) {
                            //表示ASM开始扫描这个方法。
                            @Override
                            void visitCode() {
                                super.visitCode()
                            }

                            //该方法是visitEnd之前调用的方法，可以反复调用。用以确定类方法在执行时候的堆栈大小。
                            @Override
                            void visitMaxs(int maxStack, int maxLocals) {
                                super.visitMaxs(maxStack + methodModel.paramsCount, maxLocals)
                            }

                            //
                            @Override
                            void visitInsn(int opcode) {
                                if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                                    visitMethodWithParams(mv,Opcodes.INVOKESTATIC,AnalyticsConfig.sAnalyticsClassName, methodModel.agentName, methodModel.agentDesc,methodModel.paramsStart,methodModel.paramsCount,methodModel.opcodes)
                                }
                                super.visitInsn(opcode)
                            }
                        };
                    }
                }
                //fragment 生命周期 方法注入
//                if(instanceOfFragment(mSuperName)){
//                    MethodModel methodModel = AnalyticsConfig.sFragmentMethods.get(name + desc)
//                    if(methodModel != null){
//                        //已存在的方法
//                        mVisitedFragMethods.add(name + desc)
//                        mv = new MethodVisitor(Opcodes.ASM4, mv) {
//                            @Override
//                            void visitCode() {
//                                super.visitCode()
//                            }
//
//                            @Override
//                            void visitMaxs(int maxStack, int maxLocals) {
//                                super.visitMaxs(maxStack + methodModel.paramsCount, maxLocals)
//                            }
//
//                            @Override
//                            void visitInsn(int opcode) {
//                                if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
//                                    visitMethodWithParams(mv,Opcodes.INVOKESTATIC,AnalyticsConfig.sAnalyticsClassName, methodModel.agentName, methodModel.agentDesc,methodModel.paramsStart,methodModel.paramsCount,methodModel.opcodes)
//                                }
//                                super.visitInsn(opcode)
//                            }
//                        };
//                    }
//                }
                return mv;
            }
//表示方法输出完毕。
//            @Override
//            void visitEnd() {
//                if(instanceOfFragment(mSuperName)){
//                    Iterator<Map.Entry<String,MethodModel>> iterator = AnalyticsConfig.sFragmentMethods.entrySet().iterator();
//                    while (iterator.hasNext()){
//                        Map.Entry<String,MethodModel> modelEntry = iterator.next();
//                        String key = modelEntry.getKey();
//                        if(mVisitedFragMethods.contains(key)){
//                            return
//                        }
//                        MethodModel methodModel = modelEntry.getValue();
//                        //注入不存在的方法
//                        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC,methodModel.name,methodModel.desc,null,null)
//                        mv.visitCode()
//                        //调用父类的方法
//                        visitMethodWithParams(mv, Opcodes.INVOKESPECIAL, mSuperName, methodModel.name, methodModel.desc, methodModel.paramsStart, methodModel.paramsCount, methodModel.opcodes)
//                        // call injected method
//                        visitMethodWithParams(mv, Opcodes.INVOKESTATIC, AnalyticsConfig.sAnalyticsClassName, methodModel.agentName, methodModel.agentDesc, methodModel.paramsStart, methodModel.paramsCount, methodModel.opcodes)
//                        mv.visitInsn(Opcodes.RETURN);
//                        mv.visitMaxs(methodModel.paramsCount, methodModel.paramsCount);
//                        mv.visitEnd();
//
//                    }
//                }
//                super.visitEnd()
//            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    /**
     *
     * @param mv
     * @param opcode
     * @param owner
     * @param methodName
     * @param methodDesc
     * @param start
     * @param count
     * @param paramOpcodes
     */
    public  void visitMethodWithParams(MethodVisitor mv,int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes){
        for (int i = start; i < start + count; i++) {
            mv.visitVarInsn(paramOpcodes[i - start], i);
        }
        mv.visitMethodInsn(opcode, owner, methodName, methodDesc, false);
    }

    /**
     * 判断父类是否为fragment
     * @param superName
     * @return
     */
    private static boolean instanceOfFragment(String superName) {
        return superName.equals('android/app/Fragment') || superName.equals('android/support/v4/app/Fragment')
    }

    /**
     * 判断父类是否为fragment
     * @param superName
     * @return
     */
    private static boolean instanceOfActivity(String superName) {
        return superName.equals('android/app/Activity') || superName.equals('android/support/v4/app/FragmentActivity')
    }


}
