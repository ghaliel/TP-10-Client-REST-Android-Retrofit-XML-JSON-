package ma.projet.restclient.api;

import java.util.List;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.entities.CompteList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompteService {

    // XML Endpoints
    @GET("api/comptes")
    @Headers("Accept: application/xml")
    Call<CompteList> getAllComptesXml();

    // JSON Endpoints
    @GET("api/comptes")
    @Headers("Accept: application/json")
    Call<List<Compte>> getAllComptesJson();

    @POST("api/comptes")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Compte> addCompteJson(@Body Compte compte);

    @POST("api/comptes")
    @Headers({"Content-Type: application/xml", "Accept: application/xml"})
    Call<Compte> addCompteXml(@Body Compte compte);

    @PUT("api/comptes/{id}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Compte> updateCompteJson(@Path("id") Long id, @Body Compte compte);

    @PUT("api/comptes/{id}")
    @Headers({"Content-Type: application/xml", "Accept: application/xml"})
    Call<Compte> updateCompteXml(@Path("id") Long id, @Body Compte compte);

    @DELETE("api/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);
}
