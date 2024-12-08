package camel;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sjms2.Sjms2Component;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

@ApplicationScoped
public class PointToPointExample extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // site e-commerce - génère une commande toutes les 5s
        from("timer:generateOrders?period=5000") 
            .process(new OrderGenerator())
            .to("sjms2:queue:orders")
            .to("file:data/orders");




        // opérateur de stock 1
        from("sjms2:queue:orders")
            .process(new SetToShipped())
            .to("file:data/operateur1");


        // // opérateur de stock 2
        // from("sjms2:queue:orders")
        //     .process(new SetToShipped())
        //     .to("file:data/operateur2");


        // opérateur de stock 3
        from("sjms2:queue:orders")
            .process(new SetToShipped())
            .to("file:data/operateur3");


    }

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        ActiveMQConnectionFactory activeMqConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Sjms2Component component = new Sjms2Component();
        component.setConnectionFactory(activeMqConnectionFactory);
        context.addComponent("sjms2", component);

        context.addRoutes(new PointToPointExample());

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}