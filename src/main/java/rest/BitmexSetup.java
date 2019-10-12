package rest;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitmex.BitmexExchange;

public class BitmexSetup {

    public static String getKey() {
        return key;
    }

    public static String getSecret() {
        return sec;
    }

    //replace with your API keys
    private static String key = "APIKEY";
    private static String sec = "APISECRET";


    public static Exchange createExchange() {

        // Use the factory to get Bitmex exchange API using default settings
        Exchange bitmex = ExchangeFactory.INSTANCE.createExchange(BitmexExchange.class);

        ExchangeSpecification bitmexSpec = bitmex.getDefaultExchangeSpecification();

         bitmexSpec.setApiKey(key);
         bitmexSpec.setSecretKey(sec);

        bitmex.applySpecification(bitmexSpec);

        return bitmex;
    }


}
