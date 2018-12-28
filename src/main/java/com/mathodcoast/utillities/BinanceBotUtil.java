package com.mathodcoast.utillities;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.mathodcoast.exception.FileReaderException;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BinanceBotUtil {

    private BinanceBotUtil() {
    }

    public static BinanceBotUtil getInstance(){
        if(binanceBotUtil == null){
            binanceBotUtil = new BinanceBotUtil();
        }
        return binanceBotUtil;
    }

    private static BinanceBotUtil binanceBotUtil;
    private  final String API_KEY = setApiFromFileToList().get(0);
    private  final String SECRET_KEY = setApiFromFileToList().get(1);
    private static final String API_FILE_NAME = "confidential/UserExchangeApi.txt";

    private  BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(API_KEY,SECRET_KEY);

    @Getter
    private BinanceApiRestClient client = factory.newRestClient();

    @Getter
    private BinanceApiWebSocketClient webSocketClient = factory.newWebSocketClient();

    @Getter
    private String listenKey = client.startUserDataStream();

   // public static ExchangeInfo exchangeInfo = noApiClient.getExchangeInfo();

    public double increaseValueOnCoefficient(double value,double coefficient){
        return value + value * coefficient;
    }

    public double decreaseValueOnCoefficient(double value,double coefficient){
        return value - value * coefficient;
    }

    private  Path createPathFromFileName(String fileName){
        Objects.requireNonNull(fileName);
        URL fileUrl = BinanceBotUtil.class.getClassLoader().getResource(fileName);
        try {
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new FileReaderException("Invalid file URL", e);
        }
    }

    public  List<String> setApiFromFileToList(){
        Stream<String> lines;
        try {
            lines = Files.lines(createPathFromFileName(API_FILE_NAME));
           return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileReaderException("API Stream error",e);
        }
    }
}
