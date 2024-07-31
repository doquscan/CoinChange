package com.example.coinchange.CoinChangeRest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The Class ApplicationConfig serves 2 purposes.
 * (1) It provides a bean that classes can use to get to our properties.
 * (2) It allows the actuator and the /configprops endpoint to surface all bound/bindable properties (as documentation)
 * PlaceHolder to use with Spring Cloud Config to retrieve environment specific props and values.
 * @author dsozeri
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "coinchangerest")
//@RefreshScope // refreshes properties after app is running
@Getter
@Setter
public class CoinChangeApiProperties {

    @NotNull
    private String env;

    @NotNull
    private String propSource;

    @NotNull
    private String sampleProperty;

    @NotNull
    private String password;

    @NotNull
    private String[] origins;

}
