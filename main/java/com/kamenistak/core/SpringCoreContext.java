package com.kamenistak.core;

import com.kamenistak.groovy.engine.GroovyEngineFactory;
import com.kamenistak.groovy.engine.IGroovyEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import static com.kamenistak.groovy.engine.GroovyEngineFactory.GroovyEngineType;

@Configuration
@ComponentScan(basePackages = {"com.kamenistak"})
@PropertySource(value = {"classpath:application.properties"})
public class SpringCoreContext {

    @Bean
    @Qualifier("GroovyEngineImplDEV")
    public IGroovyEngine groovyEngineDEVInstance() throws Exception {
        return GroovyEngineFactory.createInstanceOf(GroovyEngineType.GroovyEngineImplDEV);
    }

    @Bean
    @Qualifier("GroovyEngineClassLoaderImplDEV")
    public IGroovyEngine groovyEngineClassLoaderDEVInstance() throws Exception {
        return GroovyEngineFactory.createInstanceOf(GroovyEngineType.GroovyEngineClassLoaderImplDEV);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
