package camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderGenerator implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Générer une commande sous forme de Map
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", UUID.randomUUID().toString()); // UUID unique
        order.put("status", "PENDING"); // Statut initial

        // Convertir en JSON
        String orderJson = objectMapper.writeValueAsString(order);

        // Mettre le JSON dans le corps du message
        exchange.getMessage().setBody(orderJson);
    }
}
