package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Buyer.HomeActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addtocart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productId="", state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId=getIntent().getStringExtra("pid");

        addtocart=findViewById(R.id.add_to_cart_buttons);
        numberButton=findViewById(R.id.number_btn);
        productImage=findViewById(R.id.product_details_image);
        productDescription=findViewById(R.id.product_description_details);
        productName=findViewById(R.id.product_name_details);
        productPrice=findViewById(R.id.product_price_details);

        GetProductDetails(productId);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(getApplicationContext(),"You can Continue shopping after the purchase is confirmed",Toast.LENGTH_LONG).show();
                }
                else {
                    AddToCartList();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();

    }

    private void AddToCartList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(callForDate.getTime());

       final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart_List");

        final HashMap<String, Object> cartListMap=new HashMap<>();
        cartListMap.put("pid", productId);
        cartListMap.put("Product_Name", productName.getText().toString());
        cartListMap.put("Product_Price", productPrice.getText().toString());
        cartListMap.put("Date", saveCurrentDate);
        cartListMap.put("Time", saveCurrentTime);
        cartListMap.put("Quantity", numberButton.getNumber());
        cartListMap.put("Discount","");

        cartListRef.child("User_View").child(Prevelent.currentOnlineUser.getPhoneNumber())
                .child("Products").child(productId).updateChildren(cartListMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    cartListRef.child("Admin_View").child(Prevelent.currentOnlineUser.getPhoneNumber())
                            .child("Products").child(productId).updateChildren(cartListMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_LONG).show();
                                   Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                   startActivity(intent);
                               }
                                }
                            });

                }
            }
        });


    }

    private void GetProductDetails(String productId) {

        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products= snapshot.getValue(Products.class);

                    productName.setText(products.getProduct_name());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private  void CheckOrderState(){

        DatabaseReference orderCheckRef=FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevelent.currentOnlineUser.getPhoneNumber());

        orderCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState=snapshot.child("Status").getValue().toString();


                    if(shippingState.equals("shipped")){

                        state="Order Shipped";

                    }
                    else if (shippingState.equals("not Shipped")){
                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}