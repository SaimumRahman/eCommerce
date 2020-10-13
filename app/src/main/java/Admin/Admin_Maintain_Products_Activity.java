package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import Sellers.Seller_Product_category_Activity;

public class Admin_Maintain_Products_Activity extends AppCompatActivity {
private Button applychangesButton,deleteProductButton;
private EditText name,price, description;
private ImageView imageView;
private String pid="";
private DatabaseReference maintainRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__maintain__products_);

        pid=getIntent().getStringExtra("pid").toString();

        maintainRef= FirebaseDatabase.getInstance().getReference().child("Products").child(pid);

        applychangesButton=findViewById(R.id.apply_changes_button);
        deleteProductButton=findViewById(R.id.delete_products_button);
        name=findViewById(R.id.maintain_product_Name);
        price=findViewById(R.id.maintain_product_price);
        description=findViewById(R.id.maintain_product_description);
        imageView=findViewById(R.id.maintain_product_image);

        DisplaySpecificProductInfo();

        applychangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct();
            }
        });

    }


    private void DisplaySpecificProductInfo() {

        maintainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String productName=snapshot.child("product_name").getValue().toString();
                    String productPrice=snapshot.child("price").getValue().toString();
                    String productDescription=snapshot.child("description").getValue().toString();
                    String productImage=snapshot.child("Image").getValue().toString();

                    name.setText(productName);
                    price.setText(productPrice);
                    description.setText(productDescription);
                    Picasso.get().load(productImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void ApplyChanges() {

        String editName=name.getText().toString();
        String editDescription=description.getText().toString();
        String editPrice=price.getText().toString();

        if(editDescription.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Description",Toast.LENGTH_LONG).show();
        }
        else if(editName.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Name",Toast.LENGTH_LONG).show();
        }
        else if(editPrice.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Price",Toast.LENGTH_LONG).show();
        }
        else {

            HashMap<String, Object> productMap=new HashMap<>();
            productMap.put("pid",pid);
            productMap.put("description",editDescription);
            productMap.put("price",editPrice);
            productMap.put("product_name",editName);

            maintainRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Changes Applied",Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(getApplicationContext(), Seller_Product_category_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }

    private void DeleteProduct() {

        maintainRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "The Product is Deleted Successfully",Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(), Seller_Product_category_Activity.class);
                startActivity(intent);
                finish();

            }
        });

    }


}