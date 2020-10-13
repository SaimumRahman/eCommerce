package Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {
private String categoryName, description,price, productNames,saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
private Button addNewProductButton;
private EditText inputProductName,inputProductDescription,inputProductPrice;
private ImageView inputProductImage;
private static final int galleryPick=1;
private Uri imageUri;
private StorageReference productImagesRef;
private DatabaseReference productsRef,sellerRef;
private ProgressDialog loadingBars;

private String sellerName,sellerEmail, sellerAddress,sellerPhone,sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        categoryName=getIntent().getExtras().get("category").toString();
        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef=FirebaseDatabase.getInstance().getReference().child("Seller");

        addNewProductButton=findViewById(R.id.add_new_product);
        inputProductName=findViewById(R.id.product_name);
        inputProductDescription=findViewById(R.id.product_description);
        inputProductPrice=findViewById(R.id.product_price);
        inputProductImage=findViewById(R.id.select_product_image);
        loadingBars = new ProgressDialog(this);
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            sellerName=snapshot.child("Seller_Name").getValue().toString();
                            sellerEmail=snapshot.child("Seller_Email").getValue().toString();
                            sellerAddress=snapshot.child("Seller_Name").getValue().toString();
                            sellerPhone=snapshot.child("Seller_Phone").getValue().toString();
                            sellerId=snapshot.child("Seller_Id").getValue().toString();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        
    }



    private void OpenGallery() {

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            inputProductImage.setImageURI(imageUri);

        }
    }
    private void ValidateProductData() {

        description=inputProductDescription.getText().toString();
        price=inputProductPrice.getText().toString();
        productNames=inputProductName.getText().toString();

        if(imageUri==null){
            Toast.makeText(getApplicationContext(),"Product Image is Mendatory",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(getApplicationContext(),"Please Enter Product Description",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(getApplicationContext(),"Please Enter Product Price",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productNames)){
            Toast.makeText(getApplicationContext(),"Please Enter Product Name",Toast.LENGTH_LONG).show();
        }
        else {

           StoreProductInformation();

        }

    }

    private void StoreProductInformation() {

        loadingBars.setTitle("Adding New Product");
        loadingBars.setMessage("Please wait, while we are adding new Product.");
        loadingBars.setCanceledOnTouchOutside(false);
        loadingBars.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate + saveCurrentTime;


        final StorageReference filePath=productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getApplicationContext(),"Error: "+message,Toast.LENGTH_LONG).show();
                loadingBars.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image Uploaded Successfully ",Toast.LENGTH_LONG).show();

                Task<Uri> url = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                      if(!task.isSuccessful()){
                        throw task.getException();

                        }
                      downloadImageUrl=filePath.getDownloadUrl().toString();
                      return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                    downloadImageUrl=task.getResult().toString();
                  Toast.makeText(getApplicationContext(),"getting Product Image Url Successfully ",Toast.LENGTH_LONG).show();

                  SaveProductInfoToDatabase();

                   }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("Image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("product_name",productNames);

        productMap.put("seller_name",sellerName);
        productMap.put("seller_address",sellerAddress);
        productMap.put("seller_phone",sellerPhone);
        productMap.put("seller_email",sellerEmail);
        productMap.put("seller_id",sellerId);
        productMap.put("product_state","not_approved");

        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent=new Intent(getApplicationContext(), Seller_Product_category_Activity.class);
                    startActivity(intent);

                    loadingBars.dismiss();
                    Toast.makeText(getApplicationContext(),"Image added Successfully ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    loadingBars.dismiss();
                    String e=task.getException().toString();
                    Toast.makeText(getApplicationContext(),"Error: "+e,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}