package com.kamenistak.groovy.engine;

import com.google.common.base.MoreObjects;
import groovy.lang.*;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class GroovyEngineClassLoaderImpl extends GroovyEngineAbstractImpl {

    private static Logger LOG = LoggerFactory.getLogger(GroovyEngineClassLoaderImpl.class);

    private final GroovyClassLoader groovyClassLoader;
    private final String groovyRelativeRoot;

    public GroovyEngineClassLoaderImpl(ClassLoader parentClassLoader, String groovyRelativeRoot, CompilerConfiguration compilerConfiguration, boolean shouldRecompile) throws IOException {
        groovyClassLoader = new GroovyClassLoader(parentClassLoader, compilerConfiguration);
        groovyClassLoader.setShouldRecompile(shouldRecompile);
        this.groovyRelativeRoot = groovyRelativeRoot;

        LOG.debug("New instance of Groovy class loader engine has been initialized: {}", toString());
    }

    public GroovyEngineClassLoaderImpl(ClassLoader parentClassLoader, String groovyRelativeRoot, boolean shouldRecompile) throws IOException {
        this(parentClassLoader, groovyRelativeRoot, CompilerConfiguration.DEFAULT, shouldRecompile);
    }

    public GroovyEngineClassLoaderImpl(String groovyRelativeRoot, boolean shouldRecompile) throws IOException {
        this(Thread.currentThread().getContextClassLoader(), groovyRelativeRoot, shouldRecompile);
    }

    @Override
    public GroovyClassLoader getGroovyClassLoader() {
        return groovyClassLoader;
    }

    @Override
    public String toFullPathOfGroovySourceCodeForClass(Class<?> groovyClass) {
        String relativePath = groovyRelativeRoot + '/' + super.toFullPathOfGroovySourceCodeForClass(groovyClass);//TODO: use File.separator
        LOG.trace("Source code relative location of class '{}' is: '{}'", groovyClass, relativePath);
        return relativePath;
    }

    @Override
    public Script createScript(Class<? extends GroovyObject> groovyClass, Binding binding) throws ResourceException, ScriptException {
        String pathToSourceCode = toFullPathOfGroovySourceCodeForClass(groovyClass);

        File file = new File(pathToSourceCode);
        if (!file.exists()) {
            String fileNotFoundMsg = String.format("File '%s' doesn`t exist. Full path: '%s'", pathToSourceCode, file.getAbsolutePath());
            throw new ResourceException(fileNotFoundMsg);
        }

        try {
            Class clazz = groovyClassLoader.parseClass(file);
            return InvokerHelper.createScript(clazz, binding);
        } catch (IOException e) {
            throw new ResourceException(pathToSourceCode, e);
        }
    }

    @Override
    public GroovyCodeSource getSourceCodeOfGroovyClass(Class<?> groovyClass) throws ResourceException, IOException {
        String pathToSourceCode = toFullPathOfGroovySourceCodeForClass(groovyClass);
        return new GroovyCodeSource(new File(pathToSourceCode));

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("groovyRelativeRoot", groovyRelativeRoot)
                .add("refreshable", isSourceCodeRefreshable())
                .toString();
    }
}