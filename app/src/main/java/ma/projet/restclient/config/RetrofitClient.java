package ma.projet.restclient.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitJson = null;
    private static Retrofit retrofitXml = null;
    private static final String BASE_URL = "http://192.168.1.23:8082/";

    public static Retrofit getClient(String format) {
        if (format.equals("json")) {
            if (retrofitJson == null) {
                retrofitJson = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofitJson;
        } else if (format.equals("xml")) {
            if (retrofitXml == null) {
                retrofitXml = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                        .build();
            }
            return retrofitXml;
        }
        return null; // Should not happen
    }
}
