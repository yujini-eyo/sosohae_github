package com.eum.config;

import javax.sql.DataSource;
import java.util.Objects;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource(value = "classpath:db.properties")
@EnableTransactionManagement
@MapperScan("com.eum.help.dao")
public class RootConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        String url = Objects.requireNonNull(env.getProperty("jdbc.url"), "jdbc.url 가 없습니다");
        String user = Objects.requireNonNull(env.getProperty("jdbc.username"), "jdbc.username 가 없습니다");
        String pass = Objects.requireNonNull(env.getProperty("jdbc.password"), "jdbc.password 가 없습니다");
        String drv  = env.getProperty("jdbc.driverClassName"); // 없어도 동작하도록 선택

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        if (drv != null && !drv.isBlank()) {
            cfg.setDriverClassName(drv); // 비워두면 Hikari가 URL로 드라이버 자동탐지
        }
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        return new HikariDataSource(cfg);
    }

 // ...
    @Bean
    public org.apache.ibatis.session.SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource);
        fb.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mappers/*.xml"));
        fb.setTypeAliasesPackage("com.eum.help.vo");

        // 여기! import 대신 풀네임 사용
        org.apache.ibatis.session.Configuration mybatisConf = new org.apache.ibatis.session.Configuration();
        mybatisConf.setMapUnderscoreToCamelCase(true);
        fb.setConfiguration(mybatisConf);

        return fb.getObject();
    }


    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
