package com.metashift
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
import org.springframework.boot.autoconfigure.jta.JtaAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@EnableAutoConfiguration(exclude = [JmxAutoConfiguration,
                                    HibernateJpaAutoConfiguration,
                                    JtaAutoConfiguration,
                                    JpaRepositoriesAutoConfiguration])
@PropertySource(value = ['classpath:default.properties', 'file:${properties.location}'],
                ignoreResourceNotFound = true)
class MetashiftApplication {

    static void main(String[] args) {
        SpringApplication sa = new SpringApplication(MetashiftApplication)
        sa.setShowBanner false
        sa.setWebEnvironment false
        sa.run args
    }

}
