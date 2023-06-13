package fr.insee.rem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.insee.rem.application.config.PropertiesLogger;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class RemApplication {

    public static void main(String[] args) {
        configure(new SpringApplicationBuilder()).build().run(args);
    }

    protected static SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RemApplication.class).listeners(new PropertiesLogger());
    }

    @EventListener
    public void handleApplicationReady(ApplicationReadyEvent event) {
        log.info("=============== REM  has successfully started. ===============");

    }

}
