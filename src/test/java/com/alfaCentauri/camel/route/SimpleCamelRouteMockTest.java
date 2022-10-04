package com.alfaCentauri.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author ricardopresilla@gmail.com
 * @version 1.0
 **/
//@ActiveProfiles("mock")
//@RunWith(CamelSpringBootRunner.class)
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = { "camel.springboot.name=customName" }
)
public class SimpleCamelRouteMockTest extends CamelTestSupport {
    @Autowired
    CamelContext context;

    @Autowired
    Environment environment;

    @EndpointInject("mock:test")
    MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate producerTemplate;

    //Spring context fixtures
    @Configuration
    static class TestConfig {

        @Bean
        RoutesBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:input").to("mock:test");
                }
            };
        }
    }

    @Test
    public void shouldAutowireProducerTemplate() {
        assertNotNull(producerTemplate);
    }

    @Test
    public void shouldSetCustomName() {
        assertEquals("customName", producerTemplate.getCamelContext().getName());
    }

    @Test
    public void shouldInjectEndpoint() throws InterruptedException {
        mockEndpoint.setExpectedMessageCount(1);
        producerTemplate.sendBody("direct:test", "msg");
        mockEndpoint.assertIsSatisfied();
    }

//    @Test
//    public void testMoveFilesMock() throws InterruptedException {
//        String message = "type,sku#,itemdescription,price\n" +
//                "ADD,100,Samsung TV,500\n" +
//                "ADD,101,LG TV,500";
//        MockEndpoint mockEndpoint = new MockEndpoint();
//        mockEndpoint.setEndpointUriIfNotSpecified (environment.getProperty("toRoute1"));
//        mockEndpoint.expectedMessageCount(1);
//        mockEndpoint.expectedBodiesReceived(message);
//        producerTemplate.sendBodyAndHeader( environment.getProperty("startRoute"), message, "env", environment.getProperty("spring.profiles.active") );
//        assertMockEndpointsSatisfied();
//    }
}
