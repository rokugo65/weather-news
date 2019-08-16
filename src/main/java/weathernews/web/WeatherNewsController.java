package weathernews.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
//import java.net.HttpURLConnection;
//import java.net.URL;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Imports the Google Cloud client library
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

 
@Controller
public class WeatherNewsController {

    @RequestMapping(value = {"weathernews"}, method = RequestMethod.GET)
    String top(Model model){

        List<WeatherNews> WNList = new ArrayList<WeatherNews>();
        WeatherNews tokyoWN = new WeatherNews();
        WeatherNews kanagawaWN = new WeatherNews();
        WeatherNews chibaWN = new WeatherNews();
        WeatherNews saitamaWN = new WeatherNews();
        WeatherNews ibarakiWN = new WeatherNews();
        WeatherNews tochigiWN = new WeatherNews();
        WeatherNews gunmaWN = new WeatherNews();

        tokyoWN = getWeatherNews("130010");
        kanagawaWN = getWeatherNews("140010");
        chibaWN = getWeatherNews("120010");
        saitamaWN = getWeatherNews("110010");
        ibarakiWN = getWeatherNews("080010");
        tochigiWN = getWeatherNews("090010");
        gunmaWN = getWeatherNews("100010");

        WNList.add(tokyoWN);
        WNList.add(kanagawaWN);
        WNList.add(chibaWN);
        WNList.add(saitamaWN);
        WNList.add(ibarakiWN);
        WNList.add(tochigiWN);
        WNList.add(gunmaWN);

        /*
        for(WeatherNews WN : WNList){
            WN.printWeatherNews();
        }
        */

        model.addAttribute("num_1",1);
        model.addAttribute("num_2",2);
        model.addAttribute("WNList",WNList);

        return "top"; // templates/top.htmlがViewとして使用される
    }


    WeatherNews getWeatherNews(String _inCityCode){
        WeatherNews _outWN = new WeatherNews();
        System.setProperty("http.proxyHost",     "proxy.toppan.co.jp");
        System.setProperty("http.proxyPort",     "8088");
        final String authUser = "yasuhiro.matsumoto";
        final String authPassword = "toppan0402";
        java.net.Authenticator.setDefault(
            new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        authUser, authPassword.toCharArray());
                }
            }
        );

        try {
            Client client = Client.create();
            String URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=";
            URL += _inCityCode; 
            //東京130010
            WebResource webResource = client.resource(URL);
            String json = webResource.accept("application/json").get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);

            _outWN.state = 1;

            _outWN.area = node.get("location").get("area").asText();
            _outWN.prefecture = node.get("location").get("prefecture").asText();
            _outWN.city = node.get("location").get("city").asText();

            _outWN.title = node.get("title").asText();
            _outWN.text = node.get("description").get("text").asText();

            _outWN.dateLabel_1 = node.get("forecasts").get(0).get("dateLabel").asText();
            _outWN.telop_1 = node.get("forecasts").get(0).get("telop").asText();
            _outWN.date_1 = node.get("forecasts").get(0).get("date").asText();
            if (node.get("forecasts").get(0).get("temperature").get("min").get("celsius") != null ){
                _outWN.temperatureMin_1 = node.get("forecasts").get(0).get("temperature").get("min").get("celsius").asInt();
            } else if(node.get("forecasts").get(1).get("temperature").get("min").get("celsius") != null) {
                _outWN.temperatureMin_1 = node.get("forecasts").get(1).get("temperature").get("min").get("celsius").asInt();
            }
            if (node.get("forecasts").get(0).get("temperature").get("max").get("celsius") != null ){
                _outWN.temperatureMax_1 = node.get("forecasts").get(0).get("temperature").get("max").get("celsius").asInt();
            } else if(node.get("forecasts").get(1).get("temperature").get("max").get("celsius") != null) {
                _outWN.temperatureMax_1 = node.get("forecasts").get(1).get("temperature").get("max").get("celsius").asInt();
            }
            
            _outWN.dateLabel_2 = node.get("forecasts").get(1).get("dateLabel").asText();
            _outWN.telop_2 = node.get("forecasts").get(1).get("telop").asText();
            _outWN.date_2 = node.get("forecasts").get(1).get("date").asText();
            if (node.get("forecasts").get(1).get("temperature").get("min").get("celsius") != null ){
                _outWN.temperatureMin_2 = node.get("forecasts").get(1).get("temperature").get("min").get("celsius").asInt();
            }
            if (node.get("forecasts").get(1).get("temperature").get("max").get("celsius") != null ){
                _outWN.temperatureMax_2 = node.get("forecasts").get(1).get("temperature").get("max").get("celsius").asInt();
            }

            if (node.get("forecasts").get(2).get("dateLabel") != null ){
                _outWN.dateLabel_3 = node.get("forecasts").get(2).get("dateLabel").asText();
            }
            if (node.get("forecasts").get(2).get("telop") != null ){
                _outWN.telop_3 = node.get("forecasts").get(2).get("telop").asText();
            } else if (node.get("forecasts").get(1).get("telop") != null ){
                _outWN.telop_3 = node.get("forecasts").get(1).get("telop").asText();
            }
            _outWN.date_3 = node.get("forecasts").get(2).get("date").asText();
            if (node.get("forecasts").get(2).get("temperature").get("min").get("celsius") != null ){
                _outWN.temperatureMin_3 = node.get("forecasts").get(2).get("temperature").get("min").get("celsius").asInt();
            } else if(node.get("forecasts").get(1).get("temperature").get("min").get("celsius") != null) {
                _outWN.temperatureMin_3 = node.get("forecasts").get(1).get("temperature").get("min").get("celsius").asInt();
            }
            if (node.get("forecasts").get(2).get("temperature").get("max").get("celsius") != null ){
                _outWN.temperatureMax_3 = node.get("forecasts").get(2).get("temperature").get("max").get("celsius").asInt();
            } else if(node.get("forecasts").get(1).get("temperature").get("max").get("celsius") != null) {
                _outWN.temperatureMax_3 = node.get("forecasts").get(1).get("temperature").get("max").get("celsius").asInt();
            }
            return _outWN;
        } catch (Exception ex) {
            System.out.println(ex);
            _outWN.state = -1;
            return _outWN;
        }
    }

}

@Data // lombok - コンパイル時にgetter/setter, toString, equlqsメソッドなど生成
class WeatherNews {

    int state;

    String area;
    String prefecture;
    String city;

    String title;
    String text;

    String dateLabel_1;
    String telop_1;
    String date_1;
    int temperatureMin_1;
    int temperatureMax_1;

    String dateLabel_2;
    String telop_2;
    String date_2;
    int temperatureMin_2;
    int temperatureMax_2;

    String dateLabel_3;
    String telop_3;
    String date_3;
    int temperatureMin_3;
    int temperatureMax_3;

    void printWeatherNews(){

        System.out.println("state:" + state);

        System.out.println("area:" + area);
        System.out.println("prefecture:" + prefecture);
        System.out.println("city:" + city);
    
        System.out.println("title:" + title);
        System.out.println("text:" + text);
    
        System.out.println("dateLabel_1:" + dateLabel_1);
        System.out.println("telop_1:" + telop_1);
        System.out.println("date_1:" + date_1);
        System.out.println("temperatureMin_1:" + temperatureMin_1);
        System.out.println("temperatureMax_1:" + temperatureMax_1);
    
        System.out.println("dateLabel_2:" + dateLabel_2);
        System.out.println("telop_2:" + telop_2);
        System.out.println("date_2:" + date_2);
        System.out.println("temperatureMin_2:" + temperatureMin_2);
        System.out.println("temperatureMax_2:" + temperatureMax_2);
    
        System.out.println("dateLabel_3:" + dateLabel_3);
        System.out.println("telop_3:" + telop_3);
        System.out.println("date_3:" + date_3);
        System.out.println("temperatureMin_3:" + temperatureMin_3);
        System.out.println("temperatureMax_3:" + temperatureMax_3);

    }
    
}
