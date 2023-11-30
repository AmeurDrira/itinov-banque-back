package com.fr.itinov.banque.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fr.itinov.banque.BanqueMangerApplication;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.*;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.*;

/**
 * Annotation dédiée pour les tests d'intégration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
        classes = BanqueMangerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(beanName = "dataSource", type = POSTGRES, refresh = AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public @interface IntegrationTest {
}
