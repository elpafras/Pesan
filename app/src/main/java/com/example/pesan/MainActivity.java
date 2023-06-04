package com.example.pesan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        FloatingActionButton add = findViewById(R.id.fab);
        //nambah data
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View vieww = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_note_dialog, null);

                TextInputLayout titlelayout, tanggallayout, bulanlayout, tahunlayout, ayatlayout, contentlayout;
                titlelayout = vieww.findViewById(R.id.edtitlelayout);
                ayatlayout = vieww.findViewById(R.id.edayatlayout);
                contentlayout = vieww.findViewById(R.id.edcontentlayout);

                TextInputEditText edTitle, edTanggal, edBulan, edTahun, edAyat, edContent;
                edTitle = vieww.findViewById(R.id.edtitle);
                edAyat = vieww.findViewById(R.id.edayat);
                edContent = vieww.findViewById(R.id.edcontent);

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Tambah Renungan")
                        .setView(vieww).setPositiveButton("Tambah", (dialogInterface, i) -> {
                            if (edTitle.getText().toString().isEmpty()){
                                titlelayout.setError("Harus diisi ya bos!");
                            } else if (edAyat.getText().toString().isEmpty()) {
                                ayatlayout.setError("Harus diisi ya bos!");
                            } else if (edContent.getText().toString().isEmpty()) {
                                contentlayout.setError("Harus diisi ya bos!");
                            }else{
                                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                                dialog.setMessage("Menyimpan ke Database...");
                                dialog.show();
                                note n = new note();
                                n.setTitle(edTitle.getText().toString());
                                n.setAyat(edAyat.getText().toString());
                                n.setContent(edContent.getText().toString());
                                firebaseDatabase.getReference().child("notes").push().setValue(n)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                dialogInterface.dismiss();
                                                Toast.makeText(MainActivity.this, "Sukses Menyimpan", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();

            }
        });

        TextView empty = findViewById(R.id.empty);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        firebaseDatabase.getReference().child("notes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<note> notelist = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    note n = dataSnapshot.getValue(note.class);
                    Objects.requireNonNull(n).setKey(dataSnapshot.getKey());
                    notelist.add(n);
                }

                if (notelist.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                NoteAdapter adapter = new NoteAdapter(MainActivity.this, notelist);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(note note) {
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_note_dialog, null);

                        TextInputLayout titlelayout, tanggallayout, bulanlayout, tahunlayout, ayatlayout, contentlayout;
                        TextInputEditText titleed, tanggaled, bulaned, tahuned, ayated, contented;


                        titlelayout = view.findViewById(R.id.edtitlelayout);
                        ayatlayout = view.findViewById(R.id.edayatlayout);
                        contentlayout = view.findViewById(R.id.edcontentlayout);

                        titleed = view.findViewById(R.id.edtitle);
                        ayated = view.findViewById(R.id.edayat);
                        contented = view.findViewById(R.id.edcontent);

                        titleed.setText(note.getTitle());
                        ayated.setText(note.getAyat());
                        contented.setText(note.getContent());

                        ProgressDialog pd = new ProgressDialog(MainActivity.this);

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (titleed.getText().toString().isEmpty()){
                                            titlelayout.setError("Harus diisi ya bos!");
                                        } else if (ayated.getText().toString().isEmpty()) {
                                            ayatlayout.setError("Harus diisi ya bos!");
                                        } else if (contented.getText().toString().isEmpty()) {
                                            contentlayout.setError("Harus diisi ya bos!");
                                        }else{

                                            pd.setMessage("Update ke Database...");
                                            pd.show();
                                            note n = new note();
                                            n.setTitle(titleed.getText().toString());
                                            n.setAyat(ayated.getText().toString());
                                            n.setContent(contented.getText().toString());
                                            firebaseDatabase.getReference().child("notes").child(note.getKey()).setValue(n)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            pd.dismiss();
                                                            dialogInterface.dismiss();
                                                            Toast.makeText(MainActivity.this, "Sukses Mengupdate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            pd.dismiss();
                                                            Toast.makeText(MainActivity.this, "Gagal Mengupdate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pd.setTitle("Menghapus...");
                                        pd.show();
                                        firebaseDatabase.getReference().child("notes").child(note.getKey())
                                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        pd.dismiss();
                                                        Toast.makeText(MainActivity.this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        pd.dismiss();
                                                    }
                                                });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}