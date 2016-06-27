package com.kamenistak.groovy.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;

/**
 * TODO: introduce @Nullable + @NonNull to clean API spec: javax vs intellij vs guava vs Optional type.
 */
public interface IGroovyEngine {

    Object executeGroovyScript(Class<? extends GroovyObject> groovyClass, Binding binding) throws ResourceException, ScriptException;

    Object executeGroovyScript(Class<? extends GroovyObject> groovyClass) throws ResourceException, ScriptException;

    Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, Binding binding, String scriptMethodName, Object args) throws ResourceException, ScriptException;

    Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, String scriptMethodName, Object args) throws ResourceException, ScriptException;

    Object executeGroovyMethod(Class<? extends GroovyObject> groovyClass, String scriptMethodName) throws ResourceException, ScriptException;

    Object evaluateGroovyExpression(String expression, Object args, Binding binding);

    Object evaluateGroovyExpression(String expression, Object args);

    Object evaluateGroovyExpression(String expression);

    boolean isSourceCodeRefreshable();

    /**
     * TODO: consider to discard/expose to test cases only
     */
    GroovyCodeSource getSourceCodeOfGroovyClass(Class<?> groovyClass) throws ResourceException, IOException;

    /**
     * TODO: consider to discard/expose to test cases only
     */
    Script createScript(Class<? extends GroovyObject> groovyClass, Binding binding) throws ResourceException, ScriptException;

}
