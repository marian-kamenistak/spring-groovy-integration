package com.kamenistak.groovy.engine;

import com.google.common.base.MoreObjects;
import groovy.lang.*;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

class GroovyEngineImpl extends GroovyEngineAbstractImpl {

    private static Logger LOG = LoggerFactory.getLogger(GroovyEngineImpl.class);

    private final GroovyScriptEngine groovyEngine;

    public GroovyEngineImpl(String engineRoot) throws IOException {
        this(
                new String[]{new File(engineRoot).getAbsolutePath()}
        );
    }

    public GroovyEngineImpl(String[] engineRoots) throws IOException {
        this(
                Thread.currentThread().getContextClassLoader(), engineRoots,
                true
        );
    }

    public GroovyEngineImpl(ClassLoader parentClassLoader, String[] engineRoots, boolean shouldRecompile) throws IOException {
        validateGroovyEngineConstructorArguments(parentClassLoader, engineRoots);

        groovyEngine = new GroovyScriptEngine(engineRoots, parentClassLoader);

        LOG.debug("New instance of Groovy script engine has been initialized Roots: {},  shouldRecompile: {}", Arrays.toString(engineRoots), shouldRecompile);
    }

    private static void validateGroovyEngineConstructorArguments(ClassLoader parentClassLoader, String[] engineRoots) {
        checkArgument(engineRoots != null && engineRoots.length > 0, "Groovy Engine roots have to be specified");
        for (String engineRoot : engineRoots) {
            File engineRootFile = new File(engineRoot);
            checkArgument(engineRootFile.exists() && engineRootFile.isDirectory(), "Groovy Engine root has to exist as directory: " + engineRoot + ", absolute path: " + engineRootFile.getAbsolutePath());
        }
        checkNotNull(parentClassLoader, "Class loader is missing for Groovy Engine");
    }

    @Override
    protected GroovyClassLoader getGroovyClassLoader() {
        return groovyEngine.getGroovyClassLoader();
    }

    @Override
    public Script createScript(Class<? extends GroovyObject> groovyClass, Binding binding) throws ResourceException, ScriptException {
        String pathToSourceCode = toFullPathOfGroovySourceCodeForClass(groovyClass);
        return groovyEngine.createScript(pathToSourceCode, binding);
    }

    @Override
    public GroovyCodeSource getSourceCodeOfGroovyClass(Class<?> groovyClass) throws ResourceException, IOException {
        String pathToSourceCode = toFullPathOfGroovySourceCodeForClass(groovyClass);
        try {
            Path sourceCodePath = Paths.get(groovyEngine.getResourceConnection(pathToSourceCode).getURL().toURI());
            return new GroovyCodeSource(sourceCodePath.toFile());
        } catch (URISyntaxException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("refreshable", isSourceCodeRefreshable())
                .toString();
    }
}
