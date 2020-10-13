package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ViewHolder.Cart_View_Holder;

public class adminUserProductsActivity extends AppCompatActivity {
private RecyclerView productsLists;
RecyclerView.LayoutManager layoutManager;
private DatabaseReference cartRef;
private String userId= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userId= getIntent().getStringExtra("uid");
//        Toast.makeText(getApplicationContext(),userId,Toast.LENGTH_LONG).show();

        productsLists=findViewById(R.id.products_lists);
        productsLists.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsLists.setLayoutManager(layoutManager);

        cartRef= FirebaseDatabase.getInstance().getReference().child("Cart_List").child("Admin_View")
                .child(userId).child("Products");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, Cart_View_Holder> adapter=new FirebaseRecyclerAdapter<Cart, Cart_View_Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cart_View_Holder holder, int position, @NonNull Cart model) {
                holder.textProductQuantity.setText("Quantity: " + model.getQuantity());
                holder.textProductPrice.setText("Total Price: " + model.getProduct_Price()+"$");
                holder.textProductName.setText(model.getProduct_Name());
            }

            @NonNull
            @Override
            public Cart_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                Cart_View_Holder holder=new Cart_View_Holder(view);
                return holder;
            }
        };

        productsLists.setAdapter(adapter);
        adapter.startListening();
    }
}