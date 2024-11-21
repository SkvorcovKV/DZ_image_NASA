import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=CVUwP4gqewsnaJuovYwcSH6ld2NEQWt1pbQFEfDe&date=2024-11-20");
        CloseableHttpResponse response = httpClient.execute(request);

        ObjectMapper mapper = new ObjectMapper();
        NasaAnswer answer = mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);

        String imageUrl = answer.url;
        String[] splittedAnswer = imageUrl.split("/");
        String filename = "Image/" + splittedAnswer[splittedAnswer.length - 1];

        HttpGet imageRequest = new HttpGet(imageUrl);

        CloseableHttpResponse image = httpClient.execute(imageRequest);

        FileOutputStream fos = new FileOutputStream(filename);
        image.getEntity().writeTo(fos);

    }
}





