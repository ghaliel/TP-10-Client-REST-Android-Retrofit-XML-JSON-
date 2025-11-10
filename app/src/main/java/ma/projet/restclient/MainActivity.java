package ma.projet.restclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ma.projet.restclient.adapter.CompteAdapter;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.repository.CompteRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CompteAdapter.OnDeleteClickListener, CompteAdapter.OnUpdateClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private FloatingActionButton addbtn;
    private CompteRepository compteRepository;
    private String currentFormat = "json"; // Default format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupAddButton();
        setupFormatSwitcher();

        compteRepository = new CompteRepository();
        loadData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        addbtn = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompteAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        addbtn.setOnClickListener(v -> showAddCompteDialog());
    }

    private void setupFormatSwitcher() {
        RadioGroup formatGroup = findViewById(R.id.formatGroup);
        formatGroup.check(R.id.radioJson);

        formatGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioJson) {
                currentFormat = "json";
            } else if (checkedId == R.id.radioXml) {
                currentFormat = "xml";
            }
            loadData();
        });
    }

    private void showAddCompteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        builder.setView(view);

        final EditText etSolde = view.findViewById(R.id.etSolde);
        final EditText etDate = view.findViewById(R.id.etDate);
        final RadioGroup typeGroup = view.findViewById(R.id.typeGroup);

        etDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        builder.setTitle("Ajouter un compte")
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String solde = etSolde.getText().toString();
                    String date = etDate.getText().toString();
                    String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant ? "COURANT" : "EPARGNE";
                    Compte compte = new Compte(null, Double.parseDouble(solde), type, date);
                    addCompte(compte);
                })
                .setNegativeButton("Annuler", null);

        builder.create().show();
    }

    private void addCompte(Compte compte) {
        compteRepository.addCompte(currentFormat, compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte ajouté avec succès");
                    loadData();
                } else {
                    handleApiError(response, "ajout");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                handleApiFailure(t, "ajout");
            }
        });
    }

    private void loadData() {
        compteRepository.getAllComptes(currentFormat, new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    runOnUiThread(() -> adapter.updateData(response.body()));
                } else {
                    handleApiError(response, "chargement");
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                handleApiFailure(t, "chargement");
            }
        });
    }

    @Override
    public void onUpdateClick(Compte compte) {
        showUpdateCompteDialog(compte);
    }

    private void showUpdateCompteDialog(Compte compte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        builder.setView(view);

        final EditText etSolde = view.findViewById(R.id.etSolde);
        final EditText etDate = view.findViewById(R.id.etDate);
        final RadioGroup typeGroup = view.findViewById(R.id.typeGroup);

        etSolde.setText(String.valueOf(compte.getSolde()));
        etDate.setText(compte.getDateCreation());
        if (compte.getType().equalsIgnoreCase("COURANT")) {
            typeGroup.check(R.id.radioCourant);
        } else {
            typeGroup.check(R.id.radioEpargne);
        }

        builder.setTitle("Modifier un compte")
                .setPositiveButton("Modifier", (dialog, which) -> {
                    compte.setSolde(Double.parseDouble(etSolde.getText().toString()));
                    compte.setDateCreation(reformatDate(etDate.getText().toString()));
                    compte.setType(typeGroup.getCheckedRadioButtonId() == R.id.radioCourant ? "COURANT" : "EPARGNE");
                    updateCompte(compte);
                })
                .setNegativeButton("Annuler", null);

        builder.create().show();
    }

    private void updateCompte(Compte compte) {
        compteRepository.updateCompte(currentFormat, compte.getId(), compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte modifié avec succès");
                    loadData();
                } else {
                    handleApiError(response, "modification");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                handleApiFailure(t, "modification");
            }
        });
    }

    @Override
    public void onDeleteClick(Compte compte) {
        showDeleteConfirmationDialog(compte);
    }

    private void showDeleteConfirmationDialog(Compte compte) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation de suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce compte ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteCompte(compte))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteCompte(Compte compte) {
        compteRepository.deleteCompte(compte.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Compte supprimé avec succès");
                    loadData();
                } else {
                    handleApiError(response, "suppression");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                handleApiFailure(t, "suppression");
            }
        });
    }

    private void handleApiError(Response<?> response, String action) {
        String errorMsg = "Erreur lors de " + action + ": " + response.code();
        if (response.errorBody() != null) {
            try {
                errorMsg += "\n" + response.errorBody().string();
            } catch (IOException e) {
                Log.e(TAG, "Error reading error body", e);
            }
        }
        Log.e(TAG, errorMsg);
        showToast(errorMsg);
    }

    private void handleApiFailure(Throwable t, String action) {
        String errorMsg = "Échec de " + action + ": " + t.getMessage();
        Log.e(TAG, errorMsg, t);
        showToast(errorMsg);
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());
    }

    private String reformatDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            SimpleDateFormat initialFormat = new SimpleDateFormat("yyyy-MM-d", Locale.US);
            Date date = initialFormat.parse(dateStr);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return targetFormat.format(date);
        } catch (ParseException e) {
            // If parsing fails, it might already be in the correct format
            return dateStr;
        }
    }
}
