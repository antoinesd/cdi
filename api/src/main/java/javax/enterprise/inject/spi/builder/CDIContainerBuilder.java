/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.enterprise.inject.spi.builder;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * CDI container builder for Java SE
 * <p>
 * <p>
 * Typical usage looks like this:
 * </p>
 * <p>
 * <pre>
 * CDI<Object> cdi = Builders.CDIContainer().initialize();
 * container.select(Foo.class).get();
 * container.event().select(Bar.class).fire(new Bar());
 * container.shutdown();
 * </pre>
 * <p>
 * <p>
 * The {@link CDI} interface implements AutoCloseable:
 * </p>
 * <p>
 * <pre>
 * try (CDI<Object> cdi = Builders.CDIContainer().initialize()) {
 *     container.select(Foo.class).get();
 * }
 * </pre>
 * <p>
 * <p>
 * By default, the discovery is enabled so that all beans from all discovered bean archives are considered. However, it's possible to define a "synthetic" bean
 * archive, or the set of bean classes and enablement respectively:
 * </p>
 * <p>
 * <pre>
 * CDI<Object> cdi = Builders.CDIContainer().beanClasses(Foo.class, Bar.class).alternatives(Bar.class).initialize());
 * </pre>
 * <p>
 * <p>
 * Moreover, it's also possible to disable the discovery completely so that only the "synthetic" bean archive is considered:
 * </p>
 * <p>
 * <pre>
 * CDI<Object> cdi = Builders.CDIContainer().disableDiscovery().beanClasses(Foo.class, Bar.class).initialize());
 * </pre>
 * <p>
 * <p>
 * <p>
 * In the same manner, it is possible to explicitly declare interceptors, decorators, extensions and implementation specific options using the builder.
 * </p>
 * <p>
 * <pre>
 * CDIContainerBuilder builder = Builders.CDIContainer()
 *    .disableDiscovery()
 *    .packages(Main.class, Utils.class)
 *    .interceptors(TransactionalInterceptor.class)
 *    .property("property", true);
 * CDI<Object> cdi = builder.initialize();
 * </pre>
 * <p>
 * <p>
 * This builder is not reusable.
 * </p>
 *
 * @since 2.0
 * @author Antoine Sabot-Durand
 * @author Martin Kouba
 * @author John D. Ament
 */
public interface CDIContainerBuilder {

    /**
     * Containers must have a unique identifier assigned when running multiple CDI instances at the same time.
     *
     * @param containerId The container identifier
     * @return self
     */
    CDIContainerBuilder containerId(String containerId);

    /**
     * @return the current container identifier
     */
    String getContainerId();

    /**
     * Define the set of bean classes for the synthetic bean archive.
     *
     * @param classes classes to add to the synthetic bean archive
     * @return self
     */
    CDIContainerBuilder beanClasses(Class<?>... classes);

    /**
     * Add a bean class to the set of bean classes for the synthetic bean archive.
     *
     * @param beanClass class to add to the synthetic archive
     * @return self
     */
    CDIContainerBuilder addBeanClass(Class<?> beanClass);

    /**
     * All classes from the packages of the specified classes will be added to the set of bean classes for the synthetic bean archive.
     * <p>
     * <p>
     * Note that the scanning possibilities are limited. Therefore, only directories and jar files from the filesystem are supported.
     * </p>
     * <p>
     * <p>
     * Scanning may also have negative impact on bootstrap performance.
     * </p>
     *
     * @param packageClasses classes whose packages will be added to the synthetic bean archive
     * @return self
     */
    CDIContainerBuilder packages(Class<?>... packageClasses);

    /**
     * Packages of the specified classes will be scanned and found classes will be added to the set of bean classes for the synthetic bean archive.
     *
     * @param scanRecursively should subpackages be scanned or not
     * @param packageClasses classes whose packages will be scanned
     * @return self
     */
    CDIContainerBuilder addPackages(boolean scanRecursively, Class<?>... packageClasses);

    /**
     * A package of the specified class will be scanned and found classes will be added to the set of bean classes for the synthetic bean archive.
     *
     * @param scanRecursively should subpackages be scanned or not
     * @param packageClass class whose package will be scanned
     * @return self
     */
    CDIContainerBuilder addPackage(boolean scanRecursively, Class<?> packageClass);


    /**
     * Returns a {@link AnnotatedTypeConfigurator} based on the provided type
     *
     * @param type that the {@link AnnotatedType} to configure represents
     * @param <T> required type
     * @return an AnnotatedTypeConfigurator whose configuration will be added as new AnnotatedType
     */
    <T> AnnotatedTypeConfigurator<T> configureAnnotatedType(Class<T> type);


    /**
     * Add a given set of {@link AnnotatedType} to the set of discovered types.
     *
     *
     * @param annotatedTypes the AnnotatedTypes to add
     * @return self
     */
    CDIContainerBuilder addAnnotatedTypes(AnnotatedType<?>... annotatedTypes);


    /**
     * Define the set of extensions.
     *
     * @param extensions extensions to use in the container
     * @return self
     */
    CDIContainerBuilder extensions(Extension... extensions);

    /**
     * Add an extension to the set of extensions.
     *
     * @param extension extension to add
     * @return self
     */
    CDIContainerBuilder addExtension(Extension extension);

    /**
     * Enable interceptors for a synthetic bean archive. Interceptor classes are automatically added to the set of bean classes.
     *
     * @param interceptorClasses classes of the interceptors to enable.
     * @return self
     */
    CDIContainerBuilder interceptors(Class<?>... interceptorClasses);

    /**
     * Add an interceptor class to the list of enabled interceptors for a synthetic bean archive.
     *
     * @param interceptorClass class of the interceptor to add
     * @return self
     */
    CDIContainerBuilder addInterceptor(Class<?> interceptorClass);

    /**
     * Enable decorators for a synthetic bean archive. Decorator classes are automatically added to the set of bean classes for the synthetic bean archive.
     *
     * @param decoratorClasses classes of the decorators to enable.
     * @return self
     */
    CDIContainerBuilder decorators(Class<?>... decoratorClasses);

    /**
     * Add a decorator class to the list of enabled decorators for a synthetic bean archive.
     *
     * @param decoratorClass class of the decorator to add.
     * @return self
     */
    CDIContainerBuilder addDecorator(Class<?> decoratorClass);

    /**
     * Select alternatives for a synthetic bean archive.
     *
     * @param alternativeClasses classes of the alternatives to select
     * @return self
     */
    CDIContainerBuilder alternatives(Class<?>... alternativeClasses);

    /**
     * Add an alternative class to the list of selected alternatives for a synthetic bean archive.
     *
     * @param alternativeClass class of the alternatives to add
     * @return self
     */
    CDIContainerBuilder addAlternative(Class<?> alternativeClass);

    /**
     * Select alternative stereotypes for a synthetic bean archive.
     *
     * @param alternativeStereotypeClasses alternatives stereotypes to select
     * @return self
     */
    CDIContainerBuilder alternativeStereotypes(Class<? extends Annotation>... alternativeStereotypeClasses);

    /**
     * Add an alternative stereotype class to the list of selected alternative stereotypes for a synthetic bean archive.
     *
     * @param alternativeStereotypeClass alternative stereotype to add
     * @return self
     */
    CDIContainerBuilder addAlternativeStereotype(Class<? extends Annotation> alternativeStereotypeClass);

    /**
     * Set the configuration property for the container
     *
     * @param key property name
     * @param value property value
     * @return self
     */
    CDIContainerBuilder property(String key, Object value);

    /**
     * Set all the configuration properties.
     *
     * @param properties a map containing properties to add
     * @return self
     */
    CDIContainerBuilder properties(Map<String, Object> properties);

    /**
     * Returns a {@link BeanConfigurator} intialized with the provided type
     * to help you define an additional bean to add to the deployment.
     *
     * Bean will be created when initialize is called
     *
     * @param type type of the bean
     * @return a BeanConfigurator ro define a custom bean
     */
    <T> BeanConfigurator<T> configureBean(Class<T> type);


    /**
     * Add the provided beans to set of discovered beans.
     *
     * @param beans beans to add
     * @return self
     */
    CDIContainerBuilder AddBeans(Bean<?>... beans);

    /**
     *
     * Add the provided bean to set of discovered beans.
     *
     * @param bean bean to add
     * @return self
     */
    CDIContainerBuilder addBean(Bean<?> bean);


    /**
     * @return self
     * @see #disableDiscovery()
     */
    CDIContainerBuilder enableDiscovery();

    /**
     * By default, the discovery is enabled. However, it's possible to disable the discovery completely so that only the "synthetic" bean archive is considered.
     *
     * @return self
     */
    CDIContainerBuilder disableDiscovery();

    /**
     * @return <code>true</code> if the discovery is enabled, <code>false</code> otherwise
     * @see #disableDiscovery()
     */
    boolean isDiscoveryEnabled();

    /**
     * Set a {@link ClassLoader}. The given {@link ClassLoader} will be scanned automatically for bean archives if scanning is enabled.
     *
     * @param classLoader the class loader to use
     * @return self
     */
    CDIContainerBuilder setClassLoader(ClassLoader classLoader);

    /**
     * <p>
     * Initializes a CDI Container.
     * </p>
     * <p>
     * Cannot be called within an application server.
     * </p>
     *
     * @return the {@link CDI} instance associated with the container.  This is the same instance returned by using
     * {@link CDI#current()}
     * @throws UnsupportedOperationException if called within an application server
     */
    CDI<Object> initialize();

    /**
     * <p>
     * Initializes a CDI Container with custom params.
     * </p>
     * <p>
     * Cannot be called within an application server.
     * </p>
     *
     * @param params optional parameters, may be null or empty.  May also be immutable.
     * @return the {@link CDI} instance associated with the container.  This is the same instance returned by using
     * {@link CDI#current()}
     * @throws UnsupportedOperationException if called within an application server
     */
    CDI<Object> initialize(Map<String, Object> params);

    /**
     * Determines whether or not this CDIProvider has been initialized or not.
     *
     * @return true if initialized, false if not.
     * @since 2.0
     */
    boolean isInitialized();


}
