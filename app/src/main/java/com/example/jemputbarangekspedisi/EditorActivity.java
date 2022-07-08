package com.example.jemputbarangekspedisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private EditText editNama, editBarang, editBerat;
    private Button btnSave;
    /**inisialisasi firestore**/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editNama = findViewById(R.id.nama);
        editBarang = findViewById(R.id.barang);
        editBerat = findViewById(R.id.berat);
        btnSave = findViewById(R.id.btnUpdate);

        progressDialog = new ProgressDialog(EditorActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");

        btnSave.setOnClickListener(v ->{
            if (editNama.getText().length() > 0 && editBarang.getText().length() > 0 && editBerat.getText().length() > 0) {
                /** Memanggil method save data**/
                saveData(editNama.getText().toString(), editBarang.getText().toString(), editBerat.getText().toString());

            }else {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });
        /** mendapatkan dara dari main activity**/

        Intent intent = getIntent();
        if(intent!=null){
            id = intent.getStringExtra("id");
            editNama.setText(intent.getStringExtra("nama pengirim"));
            editBarang.setText(intent.getStringExtra("nama barang"));
            editBerat.setText(intent.getStringExtra("berat"));

        }
    }
    private void saveData(String nama, String barang, String berat){
        Map<String, Object> pemesanan = new HashMap<>();
        pemesanan.put("nama pengirim", nama);
        pemesanan.put("nama barang", barang);
        pemesanan.put("berat", berat);

        progressDialog.show();
        /**
         * Jika id kosong maka akan edit data
         */
        if (id!=null){
            /**
             * KOde untu edit data firestore dengan mengambil id
             */
            db.collection("jemputbarang").document(id)
                    .set(pemesanan)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();                                                                                                                                                            keText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            /**
             * Kode untu menambhakan data dengan .add
             *
             */

            db.collection("jemputbarang")
                    .add(pemesanan)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                        @Override
                        public void onSuccess(DocumentReference documentReference){
                            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }


}