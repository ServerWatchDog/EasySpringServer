package i.watch.handler.inject.config

import i.watch.utils.getLogger
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * 自定义Bean 解析器
 */
@Component
class ConfigRegistryProcessor : BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private val logger = getLogger()
    private lateinit var applicationContext: ApplicationContext

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        Reflections(
            "i.watch.*",
            TypeAnnotationsScanner(),
            SubTypesScanner()
        ).getTypesAnnotatedWith(SoftConfig::class.java)
            .forEach {
                logger.info("Register custom Config: {}.", it.name)
                BeanDefinitionBuilder.genericBeanDefinition(it)
                    .rawBeanDefinition
                    .apply {
                        this.constructorArgumentValues.apply {
                            addGenericArgumentValue(it.kotlin)
                            addGenericArgumentValue(applicationContext)
                        }
                        setBeanClass(ConfigFactoryBean::class.java)
                        autowireMode = GenericBeanDefinition.AUTOWIRE_CONSTRUCTOR
                    }.run { registry.registerBeanDefinition(it.simpleName, this) }
            }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}
