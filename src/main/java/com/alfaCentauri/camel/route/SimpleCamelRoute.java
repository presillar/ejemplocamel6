package com.alfaCentauri.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {
    @Autowired
    Environment environment;

    @Override
    public void configure() throws Exception {
        from("{{startRoute}}")
                .log("Timer Invoked and the body \n!!! " + environment.getProperty("message") + " ¡¡¡")
                .choice()
                    .when( ( header("env").isNotEqualTo("mock")))
                        .pollEnrich("{{fromRoute}}")
                    .otherwise()
                        .log("Mock env flow and the body is ${body}")
                .to("{{toRoute1}}");
    }
}
