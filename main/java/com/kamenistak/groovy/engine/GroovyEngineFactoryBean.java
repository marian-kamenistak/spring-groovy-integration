package com.kamenistak.groovy.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Allows IGroovyEngine type to be autowired from the factory
 */
@Component("GroovyEngineFactoryBean")
public class GroovyEngineFactoryBean extends AbstractFactoryBean<IGroovyEngine> {

    @Value("${groovyEngineType}")
    private String groovyEngineType;

    @Override
    protected IGroovyEngine createInstance() throws Exception {
        return GroovyEngineFactory.createInstanceOf(GroovyEngineFactory.GroovyEngineType.valueOf(groovyEngineType));
    }

    @Override
    public Class<IGroovyEngine> getObjectType() {
        return IGroovyEngine.class;
    }
}