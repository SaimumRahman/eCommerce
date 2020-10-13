package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.ProductViewHolder;

public class SearchProductsActivity extends AppCompatActivity {
private Button search_button;
private EditText search_input_text;
private RecyclerView search_List;
private String search_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        search_input_text=findViewById(R.id.search_product_name);
        search_button=findViewById(R.id.search_button);
        search_List=findViewById(R.id.search_list);

        search_List.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_input=search_input_text.getText().toString();
               // Toast.makeText(getApplicationContext(),"searching",Toast.LENGTH_LONG).show();
                onStart();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("product_name").startAt(search_input),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.textProductName.setText(model.getProduct_name());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price: " +model.getPrice() + "$");

                Picasso.get().load(model.getImage()).into(holder.productImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);


                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        search_List.setAdapter(adapter);
        adapter.startListening();
    }
}