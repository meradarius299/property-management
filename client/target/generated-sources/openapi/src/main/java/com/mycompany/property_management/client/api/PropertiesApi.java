package com.mycompany.property_management.client.api;

import com.mycompany.property_management.client.invoker.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import com.mycompany.property_management.client.model.PropertyDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PropertiesApi {
  /**
   * Sterge o proprietate
   * 
   * @param propertyId  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("api/v1/properties/{propertyId}")
  Call<Void> deleteProperty(
    @retrofit2.http.Path("propertyId") Long propertyId
  );

  /**
   * Listeaza toate proprietatile
   * 
   * @return Call&lt;List&lt;PropertyDTO&gt;&gt;
   */
  @GET("api/v1/properties")
  Call<List<PropertyDTO>> getAllProperties();
    

  /**
   * Creeaza o proprietate noua
   * 
   * @param propertyDTO  (required)
   * @return Call&lt;PropertyDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/properties/save")
  Call<PropertyDTO> saveProperty(
    @retrofit2.http.Body PropertyDTO propertyDTO
  );

  /**
   * Actualizeaza complet o proprietate
   * 
   * @param propertyId  (required)
   * @param propertyDTO  (required)
   * @return Call&lt;PropertyDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("api/v1/properties/{propertyId}")
  Call<PropertyDTO> updateProperty(
    @retrofit2.http.Path("propertyId") Long propertyId, @retrofit2.http.Body PropertyDTO propertyDTO
  );

  /**
   * Actualizeaza doar descrierea unei proprietati
   * 
   * @param propertyId  (required)
   * @param propertyDTO  (required)
   * @return Call&lt;PropertyDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PATCH("api/v1/properties/update_description/{propertyId}")
  Call<PropertyDTO> updatePropertyDescription(
    @retrofit2.http.Path("propertyId") Long propertyId, @retrofit2.http.Body PropertyDTO propertyDTO
  );

  /**
   * Actualizeaza doar prețul unei proprietati
   * 
   * @param propertyId  (required)
   * @param propertyDTO  (required)
   * @return Call&lt;PropertyDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PATCH("api/v1/properties/update_price/{propertyId}")
  Call<PropertyDTO> updatePropertyPrice(
    @retrofit2.http.Path("propertyId") Long propertyId, @retrofit2.http.Body PropertyDTO propertyDTO
  );

}
