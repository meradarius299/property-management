package com.mycompany.property_management.client;

import com.mycompany.property_management.client.model.PropertyDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class RetrofitClient {

    private static final String BASE_URL = "http://localhost:8080/";

    public static void main(String[] args) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PropertyApiService apiService = retrofit.create(PropertyApiService.class);

        try {
            PropertyDTO newProperty = new PropertyDTO();
            newProperty.setTitle("2-room apartment, Cluj-Napoca");
            newProperty.setDescription("Recently renovated, central area");
            newProperty.setOwnerName("Darius Mera");
            newProperty.setOwnerEmail("dariusmera@example.com");
            newProperty.setPrice(85000.0);
            newProperty.setAddress("10 Memorandumului Street, Cluj-Napoca");

            Response<PropertyDTO> createResponse = apiService.saveProperty(newProperty).execute();
            System.out.println("Creation Status: " + createResponse.code());

            Response<List<PropertyDTO>> listResponse = apiService.getAllProperties().execute();
            if (listResponse.isSuccessful() && listResponse.body() != null) {
                System.out.println("Properties found on server:");
                for (PropertyDTO prop : listResponse.body()) {
                    System.out.println(" - " + prop.getId() + ": " + prop.getTitle() + " (" + prop.getPrice() + " EUR)");
                }
            }

            if (createResponse.body() != null && createResponse.body().getId() != null) {
                Long id = createResponse.body().getId();
                PropertyDTO priceUpdate = new PropertyDTO();
                priceUpdate.setPrice(82000.0);

                Response<Void> patchResponse = apiService.updatePropertyPrice(id, priceUpdate).execute();
                System.out.println("Price update status: " + patchResponse.code());
            }

        } catch (Exception e) {
            System.err.println("Error communicating with REST server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}