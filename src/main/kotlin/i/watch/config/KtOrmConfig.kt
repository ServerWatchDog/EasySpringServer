package i.watch.config

import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class KtOrmConfig(private val dataSource: DataSource) {

    @Bean
    fun database(): Database {
        return Database.connectWithSpringSupport(dataSource)
    }
}
