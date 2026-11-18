package com.circulation.m3t.test;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;

import net.minecraft.launchwrapper.LaunchClassLoader;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;

public final class ForgeTestRuntime {

    private ForgeTestRuntime() {}

    public static void run(Class<?> scenario, String method) throws Exception {
        String[] classpath = System.getProperty("java.class.path")
            .split(File.pathSeparator);
        URL[] urls = new URL[classpath.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = new File(classpath[i]).toURI()
                .toURL();
        }
        try (LaunchClassLoader forge = new LaunchClassLoader(urls)) {
            forge.addClassLoaderExclusion("org.junit.");
            forge.registerTransformer("cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer");
            PrintStream out = System.out;
            PrintStream err = System.err;
            try {
                Class<?> runtime = forge.loadClass(ForgeTestRuntime.class.getName());
                runtime.getMethod("initialize")
                    .invoke(null);
                forge.loadClass(scenario.getName())
                    .getMethod(method)
                    .invoke(null);
            } finally {
                System.setOut(out);
                System.setErr(err);
            }
        }
    }

    public static void initialize() throws Exception {
        Loader.injectData("7", "10", "99", "99", "1.7.10", "test", new File("."), Collections.emptyList());
        Field logSide = FMLRelaunchLog.class.getDeclaredField("side");
        logSide.setAccessible(true);
        logSide.set(null, Side.SERVER);
    }
}
