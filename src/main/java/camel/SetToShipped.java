package camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SetToShipped implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Lire le JSON depuis le corps du message
        String orderJson = exchange.getMessage().getBody(String.class);

        // Convertir le JSON en Map
        Map<String, Object> order = objectMapper.readValue(orderJson, Map.class);

        // Mettre à jour le statut
        order.put("status", "SHIPPED");

        // Reconversion en JSON
        String updatedOrderJson = objectMapper.writeValueAsString(order);

        // Mettre le JSON mis à jour dans le corps du message
        exchange.getMessage().setBody(updatedOrderJson);
    }
}
