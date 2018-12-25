package com.mathodcoast.utillities;


import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.mathodcoast.exception.FileReaderException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class BinanceBotUtill {

    private static  String apiKey;
    private static  String secretApiKey;
    private static final String API_FILE_NAME = "UserExchangeApi.txt";

    private static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretApiKey);
    public static BinanceApiRestClient client = factory.newRestClient();

   // private static BinanceApiClientFactory noApiFactory = BinanceApiClientFactory.newInstance();
   // public static BinanceApiRestClient noApiClient = noApiFactory.newRestClient();
    public static BinanceApiWebSocketClient webSocketClient = factory.newWebSocketClient();

   // public static ExchangeInfo exchangeInfo = noApiClient.getExchangeInfo();

    public static double increaseValueOnCoefficient(double value,double coefficient){
        return value + value * coefficient;
    }

    private static Path createPathFromFileName(String fileName){
        Objects.requireNonNull(fileName);
        URL fileUrl = BinanceBotUtill.class.getClassLoader().getResource(fileName);
        try {
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new FileReaderException("Invalid file URL", e);
        }
    }

    public static void getApiFromFile(){
        Stream<String> lines;
        try {
            lines = Files.lines(createPathFromFileName(API_FILE_NAME));
            apiKey = String.valueOf(lines.toArray()[0]);
            secretApiKey = String.valueOf(lines.toArray()[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
