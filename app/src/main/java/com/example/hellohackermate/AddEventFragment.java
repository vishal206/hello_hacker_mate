package com.example.hellohackermate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends Fragment {

    public AddEventFragment(){

    }

    FirebaseAuth firebaseAuth;
    EditText title,desc,link;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];
    ImageView image;
    String edittitle,editdesc,editimage,editlink;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;

//    ProgressDialog pd;

    private Uri imageUri;
    private static final int IMAGE_REQUEST=2;

    Uri imageuri=null;
    String uName,uid,dp,phone;
    CollectionReference collectionReference;
    Button btn_startEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_event, container, false);

        title=view.findViewById(R.id.edt_eTitle);
        desc=view.findViewById(R.id.edt_eDesc);
        link=view.findViewById(R.id.edt_eLink);
        image=view.findViewById(R.id.event_img);
        btn_startEvent=view.findViewById(R.id.btn_startEvent);
        Intent intent=getActivity().getIntent();

//        pd=new ProgressDialog(getContext());
//        pd.setCanceledOnTouchOutside(false);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        FirebaseFirestore.getInstance().collection("Users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                uName=value.getString("name");
                phone=value.getString("phone");
            }

        });

        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "clicked image", Toast.LENGTH_SHORT).show();
                showImagePicDialog();
//                openImage();
            }
        });

        btn_startEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl=""+title.getText().toString().trim();
                String description=""+desc.getText().toString().trim();
                String lin=""+link.getText().toString().trim();

                if(TextUtils.isEmpty(titl)){
                    title.setError("Title cant be empty");
                    Toast.makeText(getContext(), "Title can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(imageuri==null){
//                    Toast.makeText(getContext(), "Select an Image", Toast.LENGTH_SHORT).show();
//                    return;
//                }else{
//                    uploadData(titl,description,lin);
//                }
                uploadData2(titl,description,lin);
            }
        });

        return view;
    }

    private void uploadData2(String titl, String description, String lin) {
        final String timestamp=String.valueOf(System.currentTimeMillis());
        DocumentReference ref=FirebaseFirestore.getInstance().collection("events").document(timestamp);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("uname",uName);
        hashMap.put("phone",phone);
        hashMap.put("title",titl);
        hashMap.put("link",lin);
        hashMap.put("description",description);
        hashMap.put("ptime",timestamp);
        hashMap.put("pinterested","0");
        ref.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
                    title.setText("");
                    desc.setText("");
                    link.setText("");
//                      image.setImageURI(null);
//                            imageuri=null;
                    startActivity(new Intent(getContext(),dashBoardHostActivity.class));
                    getActivity().finish();
                }
            }
        });

    }


    private void showImagePicDialog() {
        String option[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image From");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                }else if(which==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }


    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    // if request access given the pick data
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getContext(), "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }

            // function end
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    // if request access given the pick data
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getContext(), "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

//    private void uploadData(String titl, String description, String lin) {
////        pd.setMessage("Publishing post");
////        pd.show();
//        final String timestamp=String.valueOf(System.currentTimeMillis());
//        String filepathname="Posts/"+"post"+timestamp;
////        Bitmap bitmap=((BitmapDrawable) image.getDrawable()).getBitmap();
////        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
////        byte[] data=byteArrayOutputStream.toByteArray();
//
//        StorageReference storageReference1= FirebaseStorage.getInstance().getReference().child(filepathname);
//        storageReference1.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
//                while(!uriTask.isSuccessful());
//                String dowmloadUri=uriTask.getResult().toString();
//                if(uriTask.isSuccessful()){
//                    HashMap<String,Object> hashMap=new HashMap<>();
//                    hashMap.put("uid",uid);
//                    hashMap.put("uname",uName);
//                    hashMap.put("phone",phone);
//                    hashMap.put("title",titl);
//                    hashMap.put("description",description);
////                    hashMap.put("uimage",dowmloadUri);
//                    hashMap.put("ptime",timestamp);
//                    hashMap.put("pinterested","0");
//
//                    DocumentReference ref=FirebaseFirestore.getInstance().collection("Posts").document(timestamp);
//                    ref.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
////                            pd.dismiss();
//                            Toast.makeText(getContext(), "Published", Toast.LENGTH_SHORT).show();
//                            title.setText("");
//                            desc.setText("");
////                            image.setImageURI(null);
////                            imageuri=null;
//                            startActivity(new Intent(getContext(),dashBoardHostActivity.class));
//                            getActivity().finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
////                            pd.dismiss();
//                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
////                pd.dismiss();
//                Toast.makeText(getContext(), "Failed2", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == IMAGEPICK_GALLERY_REQUEST){
                imageuri=data.getData();
                image.setImageURI(imageuri);
            }
            if(requestCode==IMAGE_PICKCAMERA_REQUEST){
                image.setImageURI(imageuri);
            }
        }


    }

}