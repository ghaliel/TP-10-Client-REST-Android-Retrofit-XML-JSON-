package ma.projet.restclient.repository;

import java.util.List;
import ma.projet.restclient.api.CompteService;
import ma.projet.restclient.config.RetrofitClient;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.entities.CompteList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteRepository {
    private CompteService compteServiceJson;
    private CompteService compteServiceXml;

    public CompteRepository() {
        compteServiceJson = RetrofitClient.getClient("json").create(CompteService.class);
        compteServiceXml = RetrofitClient.getClient("xml").create(CompteService.class);
    }

    public void getAllComptes(String format, Callback<List<Compte>> callback) {
        if (format.equals("json")) {
            compteServiceJson.getAllComptesJson().enqueue(callback);
        } else {
            compteServiceXml.getAllComptesXml().enqueue(new Callback<CompteList>() {
                @Override
                public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResponse(null, Response.success(response.body().getComptes()));
                    } else {
                        callback.onFailure(null, new Throwable("XML data fetch failed"));
                    }
                }

                @Override
                public void onFailure(Call<CompteList> call, Throwable t) {
                    callback.onFailure(null, t);
                }
            });
        }
    }

    public void addCompte(String format, Compte compte, Callback<Compte> callback) {
        if (format.equals("json")) {
            compteServiceJson.addCompteJson(compte).enqueue(callback);
        } else {
            compteServiceXml.addCompteXml(compte).enqueue(callback);
        }
    }

    public void updateCompte(String format, Long id, Compte compte, Callback<Compte> callback) {
        if (format.equals("json")) {
            compteServiceJson.updateCompteJson(id, compte).enqueue(callback);
        } else {
            compteServiceXml.updateCompteXml(id, compte).enqueue(callback);
        }
    }

    public void deleteCompte(Long id, Callback<Void> callback) {
        // Delete is format-agnostic, can use either service
        compteServiceJson.deleteCompte(id).enqueue(callback);
    }
}
