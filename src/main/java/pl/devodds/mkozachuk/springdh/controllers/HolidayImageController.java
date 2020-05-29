package pl.devodds.mkozachuk.springdh.controllers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HolidayImageController {

    Conf conf;

    public HolidayImageController(Conf conf){
        this.conf = conf;
    }

    private static final int HTTP_REQUEST_TIMEOUT = 3 * 600000;
    public List<Result> search(String keyword){
        Customsearch customsearch= null;


        try {
            customsearch = new Customsearch(new NetHttpTransport(),new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) {
                    try {
                        // set connect and read timeouts
                        httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
                        httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Result> resultList=null;
        try {
            Customsearch.Cse.List list = customsearch.cse().list();
            list.setKey(conf.getGoogleKey());
            list.setCx(conf.getCx());
            list.setQ(keyword);
            Search results=list.execute();
            resultList=results.getItems();
        }
        catch (  Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }



}
