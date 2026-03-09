package com.example.timesheets2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Profile("!test")
@Configuration
public class ClockConfig {
    @Bean
    public Clock Clock() {
        return Clock.systemDefaultZone();
    }
}
