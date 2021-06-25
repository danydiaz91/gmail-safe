package io.dany.gmail.safe.context;

import com.fasterxml.jackson.databind.Module;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VavrContext {

    @Bean
    public Module vavrModule() {
        return new VavrModule();
    }
}
