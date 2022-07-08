package com.example.jemputbarangekspedisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jemputbarangekspedisi.adapter.UserAdapter;
import com.example.jemputbarangekspedisi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;

    //inisialisasi ke firebase untuk dihubungkan
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<User> list = new ArrayList<>();
    private UserAdapter userAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        btnAdd = findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog((MainActivity.this));
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data..");
        userAdapter = new UserAdapter(getApplicationContext(), list);
        userAdapter.setDialog(new UserAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        switch (i){
                            /**
                             * Melempar data ke class berikutnya
                             */
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                                intent.putExtra("nama pengirim", list.get(pos).getNama());
                                intent.putExtra("nama barang", list.get(pos).getBarang());
                                intent.putExtra("berat", list.get(pos).getBerat());
                                intent.putExtra("id", list.get(pos).getId());
                                startActivity(intent);
                                break;
                            case 1:
                                /**
                                 * memanggil class delete data
                                 */
                                deleteData(list.get(pos).getId());
                                break;


                        }
                    }
                });
                dialog.show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(userAdapter);

        btnAdd.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), EditorActivity.class));
        });
    }

    /**menampilkan data pada saat app peretama kali di run*/

    @Override
    protected void onStart(){
        super.onStart();
        getData();
    }

    /** method mengambil data dari database**/

    private void getData(){
        progressDialog.show();
        /** mengambil data dari firestore**/
        db.collection("jemputbarang")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if (task.isSuccessful()){
                            /** code ini mengambil data dari collection**/
                            for (QueryDocumentSnapshot document : task.getResult()){
                                /** Data apa saja yang ingi diambil dari collection**/
                                User user = new User(document.getString("nama pengirim"), document.getString("nama barang"), document.getString("berat"));
                                user.setId(document.getId());
                                list.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                });
    }
    /** method untuk menghapus data**/
    private void deleteData(String id){
        progressDialog.show();
        db.collection("jemputbarang").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Data gagal di hapus", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        getData();
                    }
                });
    }

}