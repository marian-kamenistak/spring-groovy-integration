package com.kamenistak.groovy.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class GroovyEngineAbstractImpl implements IGroovyEngine {

    private static Logger LOG = LoggerFactory.getLogger(GroovyEngineAbstractImpl.class);

    protected abstract GroovyClassLoader getGroovyClassLoader();

    @Override
    public boolean isSourceCodeRefreshable() {
        return getGroovyClassLoader().isShouldRecompile();
    }

    protected String getGroovyClassDescription(Class<?> groovyClass) {
        return groovyClass.getCanonicalName();
    }

    public String toFullPathOfGroovySourceCodeForClass(Class<?> groovyClass) {
        String relativePath = groovyClass.getCanonicalName().replace('.', '/') + ".groovy";
        LOG.trace("Source code relative location for the class loader of class '{}' is: '{}'", groovyClass, relativePath);
        return relativePath;
    }

    /******************** script execution methods:    *********************/

    @Override
    public Object executeGroovyScript(Class<? extends GroovyObject> groovyClass, Binding binding) throws ResourceException, ScriptException {
        String groovyClassDescription = getGroovyClassDescription(groovyClass);
        LOG.debug("Executing Groovy script '{}' with binding: {}", groovyClassDescription, binding.getVariables());
        final Script script = createScript(groovyClass, binding);
        final Object valueReturned = script.run();
        LOG.debug("Groovy script '{}' with binding: {}, returned value: {}", groovyClassDescription, binding.getVariables(), valueReturned);
        return valueReturned;
    }

    @Override
    public Object executeGroovyScript(Class<? extends GroovyObject> groovyClass) throws ResourceException, ScriptException {
        return executeGroovyScript(groovyClass, new Binding());
    }

    @Override
    public Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, Binding binding, String scriptMethodName, Object args) throws ResourceException, ScriptException {
        LOG.debug("Executing Groovy method {}.{}'  with binding: {} and args {}", groovyClass, scriptMethodName, binding.getVariables(), args);
        Script script = createScript(groovyClass, binding);
        Object result = script.invokeMethod(scriptMethodName, args);
        LOG.debug("Groovy method {}.{}'  execution returned value: {}", groovyClass, scriptMethodName, result);
        return result;
    }

    @Override
    public Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, String scriptMethodName, Object args) throws ResourceException, ScriptException {
        return executeGroovyMethod(groovyClass, new Binding(), scriptMethodName, args);
    }

    @Override
    public Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, String scriptMethodName) throws ResourceException, ScriptException {
        return executeGroovyMethod(groovyClass, scriptMethodName, null);
    }

    @Override
    public Object evaluateGroovyExpression(String expression, Object args, Binding binding) {
        Class clazz = getGroovyClassLoader().parseClass(expression);
        Script script = InvokerHelper.createScript(clazz, binding);
        Object result = script.invokeMethod("run", args);
        return result;
    }

    @Override
    public Object evaluateGroovyExpression(String expression, Object args) {
        return evaluateGroovyExpression(expression, args, new Binding());
    }

    @Override
    public Object evaluateGroovyExpression(String expression) {
        return evaluateGroovyExpression(expression, null);
    }

}
