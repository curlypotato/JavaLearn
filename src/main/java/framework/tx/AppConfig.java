package framework.tx;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan("framework.tx.app.service")
@MapperScan("framework.tx.app.mapper")
public class AppConfig {

    @ConfigurationProperties("jdbc")
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource, DatabasePopulator populator) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(populator);
        return dataSourceInitializer;
    }

    @Bean
    public DatabasePopulator databasePopulator() {
        return new ResourceDatabasePopulator(new ClassPathResource("account.sql"));
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    // ?????? beanFactory ??? allowBeanDefinitionOverriding==true, ??????????????? @Bean ???????????? @ConditionalOnMissingBean ????????????????????????????????? @Bean ?????????
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource(false);
    }

}
