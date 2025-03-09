package com.circulation.m3t;

import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.hander.ReloadHandler;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.ZenClass;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.*;

public class M3TCrtAPI {

    public static final String CrtClass = "mods.m3t.";

    static void register() throws IOException {
        String packageName = "com.circulation.m3t.crt";
        String packagePath = packageName.replace('.', '/');
        String pathPrefix = packagePath + "/";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL jarUrl = resources.nextElement();
            if (!"jar".equals(jarUrl.getProtocol())) continue;

            String jarPath = jarUrl.getPath().split("!")[0];
            jarPath = new File(URI.create(jarPath)).getAbsolutePath();
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name());

            try (JarFile jar = new JarFile(jarPath)) {
                jar.stream()
                    .filter(entry -> entry.getName().startsWith(pathPrefix) && entry.getName().endsWith(".class"))
                    .forEach(entry -> {
                        String className = entry.getName()
                            .substring(0, entry.getName().length() - 6) // 移除 ".class"
                            .replace('/', '.'); // 转换为类名格式
                        try {
                            Class<?> clazz = Class.forName(className, false, classLoader);
                            if (clazz.isAnnotationPresent(ZenClass.class)) {
                                MineTweakerAPI.registerClass(clazz);
                                Object obj = clazz.getDeclaredConstructor().newInstance();
                                if (obj instanceof M3TCrtReload){
                                    ReloadHandler.reloads.add((M3TCrtReload) obj);
                                }
                                M3Tweaker.logger.info("loading {}", clazz.getName());
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError | InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException ignored) {
                        }
                    });
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid JAR path: " + jarPath, e);
            }
        }
    }

}
