/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.overdraw.plugin.modify

import com.android.SdkConstants
import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.transforms.JarMerger
import com.android.build.gradle.internal.transforms.JarMergingTransform
import com.android.builder.packaging.ZipEntryFilter
import com.android.utils.FileUtils
import com.android.utils.ILogger
import com.overdraw.plugin.extension.OverDrawExtension
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static com.android.utils.FileUtils.deleteIfExists
import static com.google.common.base.Preconditions.checkNotNull
/**
 * A transform that merges all the incoming inputs (folders and jars) into a single jar in a
 * single combined output.
 *
 * This only packages the class files. It ignores other files.
 */
public class JarMergingHook extends Transform {

    private JarMergingTransform mTransform
    private ILogger mLogger
    private OverDrawExtension mExt
    private ApplicationVariant mAppVariant

    public JarMergingHook(JarMergingTransform transform, OverDrawExtension extension, ApplicationVariant appVariant, ILogger logger) {
        mTransform = transform
        mExt = extension
        mAppVariant = appVariant
        mLogger = logger
    }

    //transform的名称
    //transformClassesWithMyClassTransformForDebug 运行时的名字
    //transformClassesWith + getName() + For + Debug或Release
    @NonNull
    @Override
    public String getName() {
        return mTransform.getName()
    }

    //需要处理的数据类型，有两种枚举类型
    //CLASSES和RESOURCES，CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
    //参考TransformManager，例如：TransformManager.CONTENT_CLASS;
    @NonNull
    @Override
    public Set<ContentType> getInputTypes() {
        return mTransform.getInputTypes()
    }

    @NonNull
    @Override
    public Set<Scope> getScopes() {
        return mTransform.getScopes()
    }

    @Override
    public boolean isIncremental() {
        return mTransform.isIncremental()
    }

    @Override
    public void transform(@NonNull TransformInvocation invocation) throws TransformException,
                                                                          IOException {
        TransformOutputProvider outputProvider = invocation.getOutputProvider()
        checkNotNull(outputProvider, "Missing output object for transform " + getName())

        // all the output will be the same since the transform type is COMBINED.
        // and format is SINGLE_JAR so output is a jar
        File jarFile = outputProvider.getContentLocation("combined", getOutputTypes(), getScopes(),
                Format.JAR)
        FileUtils.mkdirs(jarFile.getParentFile())
        deleteIfExists(jarFile)

        JarMerger jarMerger = new JarMerger(jarFile)

        try {
            jarMerger.setFilter(new ZipEntryFilter() {
                @Override
                public boolean checkEntry(String archivePath) {
                    return archivePath.endsWith(SdkConstants.DOT_CLASS)
                }
            })

            for (TransformInput input : invocation.getInputs()) {
                for (JarInput jarInput : input.getJarInputs()) {
                    jarMerger.addJar(jarInput.getFile())
                }

                for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                    jarMerger.addFolder(directoryInput.getFile())
                }
            }
        } catch (FileNotFoundException e) {
            throw new TransformException(e)
        } catch (IOException e) {
            throw new TransformException(e)
        } finally {
            jarMerger.close()
        }
        unifr(jarFile)

    }

    //------------------
    public void unifr(File allClassesJar) {
        //备份 combined.jar
        File bakJar = new File(allClassesJar.getParent(), allClassesJar.name + ".bak")
        if (bakJar.exists()) {
            bakJar.delete()
        }
        org.apache.commons.io.FileUtils.copyFile(allClassesJar, bakJar)

        //获取jar文件md5
        String md5AllClassesJar = getMd5ByFile(allClassesJar)
        File unifyRJar = new File(allClassesJar.getParent(), md5AllClassesJar + ".jar.opt")
        if (!unifyRJar.exists()) {
            //清楚md5值不同的jar.opt
            File parentDir = allClassesJar.getParentFile()
            File[] subFiles = parentDir.listFiles()
            for (int i = 0; i < subFiles.length; i++) {
                File subFile = subFiles[i]
                if (subFile.name.endsWith(".jar.opt")) {
                    subFile.delete()
                }
            }
            modifyR(bakJar, unifyRJar)
        }
        //unifyRJar to allClassesJar
        if (allClassesJar.exists()) {
            org.apache.commons.io.FileUtils.forceDelete(allClassesJar)
        }
        org.apache.commons.io.FileUtils.copyFile(unifyRJar, allClassesJar)
    }

    /**
     * modify R
     * @param allClassesJar
     * @param unifyRJar
     * @param rootPackagePrefix
     */
    private
    void modifyR(File allClassesJar, File unifyRJar) {
        JarFile jarFile = new JarFile(allClassesJar)
        Enumeration enumeration = jarFile.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(unifyRJar))

        AsmModify asmModify = new  AsmModify()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = jarFile.getInputStream(jarEntry)
            if (entryName.endsWith(".class") && !entryName.contains("R\$")) {
                inputStream = new ByteArrayInputStream(asmModify.modifyClass(inputStream,mLogger))
            }
            jarOutputStream.putNextEntry(zipEntry)
            jarOutputStream.write(IOUtils.toByteArray(inputStream))
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        jarFile.close()
    }



    public String getMd5ByFile(File file) {
        String value = null
        InputStream stream = new FileInputStream(file)
        try {
            value = DigestUtils.md5Hex(stream)
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            IOUtils.closeQuietly(stream)
        }
        return value
    }

}
