package com.kamenistak.groovy.engine;

/**
 * Created by mkamenistak on 17.6.2016.
 */

public class GroovyEngineFactory {

    public enum GroovyEngineType {
        GroovyEngineImplDEV, GroovyEngineImplPROD,
        GroovyEngineClassLoaderImplDEV, GroovyEngineClassLoaderImplPROD;
    }

    //TODO: move constants to property file
    public static IGroovyEngine createInstanceOf(GroovyEngineType groovyEngineType) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        switch (groovyEngineType) {

            case GroovyEngineImplDEV:
                return new GroovyEngineImpl(cl, new String[]{"src/main/groovy", "src/test/groovy"}, true);
            case GroovyEngineImplPROD:
                return new GroovyEngineImpl(cl, new String[]{"src/main/groovy"}, false);

            case GroovyEngineClassLoaderImplDEV:
                return new GroovyEngineClassLoaderImpl(cl, "src/test/groovy", true);
            case GroovyEngineClassLoaderImplPROD:
                return new GroovyEngineClassLoaderImpl(cl, "src/test/groovy", false);

            default:
                throw new IllegalArgumentException("Incorrect groovyEngineType: " + groovyEngineType);
        }
    }
}