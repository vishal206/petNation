package com.example.petnation;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.petnation.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdd extends Fragment {
    ImageView camera;
    Button open_camera,upload;
    EditText editAddress, editDesc;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Boolean state=false;
    ProgressBar progressBar;

    String name="",phone="",email="";

    private StorageReference storageRef;
    byte bb[];

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAdd() {
        // Required empty public constructor
    }

    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("users");
        open_camera = v.findViewById(R.id.button);
        progressBar=v.findViewById(R.id.progressBar);
        editDesc =v.findViewById(R.id.editTextDesc);
        editAddress = v.findViewById(R.id.editTextAddress);
        camera = v.findViewById(R.id.imageView);
        upload = v.findViewById(R.id.button_upload);
        storageRef = FirebaseStorage.getInstance().getReference();
        progressBar.setVisibility(View.INVISIBLE);

        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                name = u.getName();
                phone =u.getPhone();
                email = u.getEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    onCaptureImageResult(result.getData());

                }
            }
        });
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_details_Firebase(bb);
                progressBar.setVisibility(View.VISIBLE);
            }

        });

        return v;
    }

    public void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, 101);
        activityResultLauncher.launch(cameraIntent);
    }

    void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        bb= bytes.toByteArray();
        String file = Base64.encodeToString(bb,Base64.DEFAULT);
        camera.setImageBitmap(thumbnail);

    }

    private void upload_details_Firebase(byte bb[]) {
        Map<String, Object> m = new HashMap<>();
        String address= editAddress.getText().toString().trim();
        String desc= editDesc.getText().toString().trim();

        if(address.isEmpty()){
            editAddress.setError("Required Field");
            editAddress.requestFocus();
            return;
        }
        if(desc.isEmpty()){
            editDesc.setError("Required Field");
            editDesc.requestFocus();
            return;
        }

//        m.put("name",name);
        m.put("name",name);
        m.put("phone",phone);

        m.put("Address",address);
        m.put("Description",desc);
        StorageReference sr = storageRef.child("animalImages");
        UUID randomId = UUID.randomUUID();
        String imageName =randomId+".jpg";
        sr.child(imageName).putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("image","UPLOADed IN FIREBASE storage");

                StorageReference storageReference2 = FirebaseStorage.getInstance().getReference("animalImages").child(imageName);
                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String filepath=uri.toString();
                        m.put("image",filepath);


                        reference.child(mAuth.getCurrentUser().getUid()).child("animals_reported").push().updateChildren(m).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "uploaded!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                editAddress.setText("");
                                editDesc.setText("");
                                camera.setImageResource(R.drawable.camera);
                                Log.d("image","UPLOADed IN realtime database ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error uploading !", Toast.LENGTH_SHORT).show();
                                Log.d("image","NOT UPLOADed IN RD");
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("image","fAILED TO UPLOAD IN FIREBASE");
            }
        });
    }
}