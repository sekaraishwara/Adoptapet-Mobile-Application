package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPetsNGO extends AppCompatActivity {
    private ImageView ivAddPetNGO;
    private EditText etAddPetAgeNGO, etAddPetBreedNGO,etAddPetAboutNGO,etAddPetAddressNGO;
    private RadioGroup rgAddPetGenderNGO, rgAddPetTypeNGO;
    private RadioButton rbAddPetMaleNGO, rbAddPetFemaleNGO, rbAddPetCatNGO, rbAddPetDogNGO;
    private Button btnAddPetNGO;
    private String petGen,petType;
    private boolean imageControl = false;
    private Uri imageUri;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lost_Approval_req");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("NGO");
    // DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pets_ngo);

        ivAddPetNGO = findViewById(R.id.ivAddPetNGO);
        etAddPetAgeNGO = findViewById(R.id.etAddPetAgeNGO);
        etAddPetBreedNGO = findViewById(R.id.etAddPetBreedNGO);
        etAddPetAddressNGO=findViewById(R.id.etAddPetAddressNGO);
        etAddPetAboutNGO=findViewById(R.id.etAddPetAboutNGO);
        rgAddPetGenderNGO = findViewById(R.id.rgAddPetGenderNGO);
        rgAddPetTypeNGO = findViewById(R.id.rgAddPetTypeNGO);
        rbAddPetMaleNGO = findViewById(R.id.rbAddPetMaleNGO);
        rbAddPetFemaleNGO = findViewById(R.id.rbAddPetFemaleNGO);
        rbAddPetDogNGO = findViewById(R.id.rbAddPetDogNGO);
        rbAddPetCatNGO = findViewById(R.id.rbAddPetCatNGO);
        btnAddPetNGO=findViewById(R.id.btnAddPetNGO);

        String UID = FirebaseAuth.getInstance().getUid();
        System.out.println("#############"+UID);

        ivAddPetNGO.setOnClickListener(view -> {
            imageChooser();
        });

        btnAddPetNGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbAddPetMaleNGO.isChecked()) {
                    petGen = rbAddPetMaleNGO.getText().toString();
                } else if (rbAddPetFemaleNGO.isChecked()) {
                    petGen = rbAddPetFemaleNGO.getText().toString();
                }

                if (rbAddPetCatNGO.isChecked()) {
                    petType = rbAddPetCatNGO.getText().toString();
                } else if (rbAddPetDogNGO.isChecked()) {
                    petType = rbAddPetDogNGO.getText().toString();
                }
                if(petGen != "" && petType != ""){
                    if(imageControl){
                        insertData(petGen,petType,UID);
                    }else{
                        Toast.makeText(AddPetsNGO.this, "Please add and Image of Pet", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AddPetsNGO.this, "Check radio button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData(String setPetGen,String setPetType,String UID) {

        String age = etAddPetAgeNGO.getText().toString();
        String breed = etAddPetBreedNGO.getText().toString();
        String about = etAddPetAboutNGO.getText().toString();
        String address=etAddPetAddressNGO.getText().toString();


        if (age.isEmpty()) {
            etAddPetAgeNGO.setError("This field cannot be empty");
            etAddPetAgeNGO.requestFocus();
            return;
        }

        if (breed.isEmpty()) {
            etAddPetBreedNGO.setError("This field cannot be empty");
            etAddPetBreedNGO.requestFocus();
            return;
        }

        if (about.isEmpty()) {
            etAddPetAboutNGO.setError("This field cannot be empty");
            etAddPetAboutNGO.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            etAddPetAddressNGO.setError("This field cannot be empty");
            etAddPetAddressNGO.requestFocus();
            return;
        }
        Map<String,Object>map =new HashMap<>();
        map.put("PetAge" ,age);
        map.put("PetBreed" ,breed);
        map.put("PetAbout" ,about);
        map.put("PetAddress",address);
        map.put("PetGender",setPetGen);
        map.put("PetType",setPetType);
        map.put("PetUser",UID);
        DatabaseReference newRef = reference.push();
        String key = newRef.getKey();
        newRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddPetsNGO.this, "Approval Requested", Toast.LENGTH_SHORT).show();
                     // insertDataUser(key,UID);
                        startActivity(new Intent(AddPetsNGO.this,HomepageNGO.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPetsNGO.this, "Failed to add", Toast.LENGTH_SHORT).show();
                    }
                });

        UUID randomID = UUID.randomUUID();
        String imageName = "images/"+randomID+".jpg";
        System.out.println(imageName);
        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference myStorageRef = storage.getReference(imageName);
                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference().child("Lost_Approval_req")
                                .child(key).child("ImageUrl").setValue(uri.toString());
                    }
                });
            }
        });


    }
    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode ==RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(ivAddPetNGO);
            imageControl = true;
        }else {
            imageControl = false;
        }
    }

}
