package com.mycompany.property_management.client;

import com.mycompany.property_management.client.model.PropertyDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PropertyApiService {

    @GET("api/v1/properties")
    Call<List<PropertyDTO>> getAllProperties();

    @POST("api/v1/properties/save")
    Call<PropertyDTO> saveProperty(@Body PropertyDTO property);

    @PUT("api/v1/properties/{propertyId}")
    Call<PropertyDTO> updateProperty(
            @Path("propertyId") Long propertyId,
            @Body PropertyDTO property
    );

    @PATCH("api/v1/properties/update_price/{propertyId}")
    Call<Void> updatePropertyPrice(
            @Path("propertyId") Long propertyId,
            @Body PropertyDTO property
    );

    @DELETE("api/v1/properties/{propertyId}")
    Call<Void> deleteProperty(@Path("propertyId") Long propertyId);
}