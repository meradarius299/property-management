package com.mycompany.property_management.client;

import com.mycompany.property_management.client.api.CalculatorApi;
import com.mycompany.property_management.client.api.PropertiesApi;
import com.mycompany.property_management.client.invoker.ApiClient;
import com.mycompany.property_management.client.model.PropertyDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

import java.util.List;

/**
 * Exemplu de utilizare a clientului Retrofit2 generat din
 * ../openapi/property-management-api.yaml (rulează: mvn generate-sources).
 *
 * Rulează serverul mai întâi (modulul "server"), apoi rulează această clasă:
 *   mvn -pl client compile exec:java -Dexec.mainClass=com.mycompany.property_management.client.ClientDemo
 */
public class ClientDemo {

    public static void main(String[] args) throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8080");
        apiClient.setHttpClient(httpClient);

        PropertiesApi propertiesApi = apiClient.createService(PropertiesApi.class);
        CalculatorApi calculatorApi = apiClient.createService(CalculatorApi.class);

        // 1) Creează o proprietate
        PropertyDTO newProperty = new PropertyDTO();
        newProperty.setTitle("Apartament 2 camere, Cluj-Napoca");
        newProperty.setDescription("Aproape de centru, renovat recent");
        newProperty.setOwnerName("Ion Popescu");
        newProperty.setOwnerEmail("ion.popescu@example.com");
        newProperty.setPrice(85000.0);
        newProperty.setAddress("Str. Memorandumului 10, Cluj-Napoca");

        Response<PropertyDTO> saveResponse = propertiesApi.saveProperty(newProperty).execute();
        System.out.println("Salvat -> status " + saveResponse.code() + " : " + saveResponse.body());

        // 2) Listează toate proprietățile
        Response<List<PropertyDTO>> listResponse = propertiesApi.getAllProperties().execute();
        System.out.println("Total proprietăți: " +
                (listResponse.body() == null ? 0 : listResponse.body().size()));

        // 3) Actualizează doar prețul, dacă am creat cu succes o proprietate
        if (saveResponse.body() != null && saveResponse.body().getId() != null) {
            Long id = saveResponse.body().getId();
            PropertyDTO priceUpdate = new PropertyDTO();
            priceUpdate.setPrice(82000.0);
            Response<PropertyDTO> priceResp = propertiesApi.updatePropertyPrice(id, priceUpdate).execute();
            System.out.println("Preț actualizat -> " + priceResp.body());
        }

        // 4) Endpoint-ul de calculator
        Response<Double> sumResponse = calculatorApi.addNumbers(14.2, 13.3).execute();
        System.out.println("14.2 + 13.3 = " + sumResponse.body());
    }
}
