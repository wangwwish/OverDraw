package com.overdraw.plugin


import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.pipeline.TransformTask
import com.android.build.gradle.internal.transforms.JarMergingTransform
import com.android.utils.ILogger
import com.android.utils.StdLogger
import com.overdraw.plugin.extension.OverDrawExtension
import com.overdraw.plugin.modify.JarMergingHook
import org.gradle.api.*
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

import java.lang.reflect.Field


public class OverDrawPlugin implements Plugin<Project>{
    public static final String EXT_UNIFR = "overdrawanalytics"
    public static final String EXT_ANDROID = "android"
    private Project mProject
    private OverDrawExtension mExt

    @Override
    void apply(Project project) {
        mProject = project
        if (!project.plugins.hasPlugin(AppPlugin.class)) {
            throw new GradleException("the project is not app")
        }
        mExt = mProject.extensions.create(EXT_UNIFR, OverDrawExtension)
        AppExtension android = (AppExtension) project.extensions.findByName(EXT_ANDROID)

        project.afterEvaluate {
            android.applicationVariants.all { ApplicationVariant variant ->
                inject(mProject, variant, mExt, mLogger)
            }
        }
    }

    /**
     * inject transform
     * @param project
     * @param appVariant
     * @param extension
     * @param logger
     */
    private void inject(Project project,
                        final ApplicationVariant appVariant,
                        final OverDrawExtension extension, final ILogger logger) {
        project.getGradle().getTaskGraph().addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
            @Override
            public void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                String buildType = appVariant.name.capitalize()
                for (Task task : taskExecutionGraph.getAllTasks()) {
                    if (task instanceof TransformTask && task.getName().toLowerCase().contains(buildType.toLowerCase())) {

                        Transform transform = ((TransformTask) task).getTransform()
                        Field field = FieldUtils.getDeclaredField(TransformTask.class, "transform", true)

                        if (transform instanceof JarMergingTransform && !(transform instanceof JarMergingHook)) {
                            try {
                                JarMergingTransform jarMergingTransform = (JarMergingTransform) transform

                                JarMergingHook jarMergingHook = new JarMergingHook(jarMergingTransform, extension, appVariant, logger)

                                FieldUtils.writeField(field, task, jarMergingHook, true)
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace()
                            } catch (IllegalAccessException e) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })
    }
}