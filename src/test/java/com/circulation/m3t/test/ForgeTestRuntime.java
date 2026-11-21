package com.circulation.m3t.test;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinTweaker;
import org.spongepowered.asm.mixin.EnvironmentStateTweaker;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ForgeTestRuntime {

    private static final String MINECRAFT_VERSION = "1.7.10";

    private ForgeTestRuntime() {}

    public static void run(Class<?> scenario, String method) throws Exception {
        run(scenario, method, false);
    }

    public static void runWithMixins(Class<?> scenario, String method) throws Exception {
        run(scenario, method, true);
    }

    private static void run(Class<?> scenario, String method, boolean mixins) throws Exception {
        String[] classpath = System.getProperty("java.class.path")
            .split(File.pathSeparator);
        URL[] urls = new URL[classpath.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = new File(classpath[i]).toURI()
                .toURL();
        }
        try (LaunchClassLoader forge = new LaunchClassLoader(urls) {

            @Override
            public List<URL> getSources() {
                // Register the mod's late mixins without discovering dependency coremods.
                return mixins ? Collections.emptyList() : super.getSources();
            }
        }) {
            forge.addClassLoaderExclusion("org.junit.");
            forge.registerTransformer("cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer");
            PrintStream out = System.out;
            PrintStream err = System.err;
            try {
                Class<?> runtime = forge.loadClass(ForgeTestRuntime.class.getName());
                runtime.getMethod("initialize")
                    .invoke(null);
                if (mixins) {
                    invokeWithMixins(forge, scenario, method);
                } else {
                    invoke(forge, scenario, method);
                }
            } finally {
                System.setOut(out);
                System.setErr(err);
            }
        }
    }

    private static void invokeWithMixins(LaunchClassLoader forge, Class<?> scenario, String method) throws Exception {
        LaunchClassLoader previousLoader = Launch.classLoader;
        Map<String, Object> previousBlackboard = Launch.blackboard;
        File previousHome = Launch.minecraftHome;
        Thread thread = Thread.currentThread();
        ClassLoader contextLoader = thread.getContextClassLoader();
        Launch.classLoader = forge;
        Launch.minecraftHome = new File(".");
        Launch.blackboard = new Object2ObjectOpenHashMap<>();
        Launch.blackboard.put("TweakClasses", new ObjectArrayList<>());
        Launch.blackboard.put("Tweaks", new ObjectArrayList<>());
        thread.setContextClassLoader(forge);
        try {
            initialize();
            setField(CoreModManager.class, "loadPlugins", null, new ObjectArrayList<>());
            forge.addClassLoaderExclusion("io.github.legacymoddingmc.unimixins.");
            forge.addClassLoaderExclusion("org.spongepowered.asm.");
            forge.addClassLoaderExclusion("com.llamalad7.mixinextras.");
            new MixinTweaker();
            MixinExtrasBootstrap.init();
            MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
            environment.setSide(MixinEnvironment.Side.SERVER);
            environment.setOption(MixinEnvironment.Option.DISABLE_REFMAP, true);
            Mixins.addConfiguration("mixins.m3t.json");
            new EnvironmentStateTweaker().getLaunchArguments();
            invoke(forge, scenario, method);
        } finally {
            thread.setContextClassLoader(contextLoader);
            Launch.classLoader = previousLoader;
            Launch.blackboard = previousBlackboard;
            Launch.minecraftHome = previousHome;
        }
    }

    private static void invoke(LaunchClassLoader forge, Class<?> scenario, String method) throws Exception {
        forge.loadClass(scenario.getName())
             .getMethod(method)
             .invoke(null);
    }

    public static void initialize() throws Exception {
        File home = new File(".");
        Loader.injectData("7", "10", "99", "99", MINECRAFT_VERSION, "test", home, Collections.emptyList());
        setField(FMLInjectionData.class, "minecraftHome", null, home);
        setField(FMLInjectionData.class, "mccversion", null, MINECRAFT_VERSION);
        setField(FMLLaunchHandler.class, "side", null, Side.SERVER);
        setField(FMLRelaunchLog.class, "side", null, Side.SERVER);
    }

    public static void setField(Class<?> owner, String name, Object target, Object value) throws Exception {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
