package Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Admin.Chech_New_Products;
import Buyer.MainActivity;
import ViewHolder.ProductViewHolder;
import ViewHolder.sellerItemViewHolder;

public class SellerHomeActivities<AppCompatActivity> extends androidx.appcompat.app.AppCompatActivity {

    private TextView nTextMessage;
    private RecyclerView seller_recycler;
    RecyclerView.LayoutManager layoutManager_seller;
    private DatabaseReference sellerUncheckRef;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch ((item.getItemId())){

                case R.id.navigation_home:
                    nTextMessage.setText(R.string.title_home);
                    Intent intentsHome=new Intent(getApplicationContext(), SellerHomeActivities.class);
                    startActivity(intentsHome);
                    return true;

                case R.id.navigation_add:
                    Intent intents=new Intent(getApplicationContext(), Seller_Product_category_Activity.class);
                    startActivity(intents);

                    return true;


                case R.id.navigation_logout:
                    final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        mAuth.signOut();
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    finish();
                    return true;

            }

            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navigationView=findViewById(R.id.nav_view);
       // nTextMessage=findViewById(R.id.)
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        seller_recycler=findViewById(R.id.seller_home_recycler);
        seller_recycler.setHasFixedSize(true);
        layoutManager_seller=new LinearLayoutManager(this);
        seller_recycler.setLayoutManager(layoutManager_seller);

        sellerUncheckRef= FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(sellerUncheckRef.orderByChild("seller_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, sellerItemViewHolder> adapter=new FirebaseRecyclerAdapter<Products, sellerItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull sellerItemViewHolder holder, int position, @NonNull final Products model) {
                holder.textProductName.setText(model.getProduct_name());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price: " +model.getPrice() + "$");
                holder.productStatus.setText(model.getProduct_state());
                Picasso.get().load(model.getImage()).into(holder.productImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productId= model.getPid();

                        CharSequence option[]=new CharSequence[]

                                {
                                        "Yes",
                                        "No"

                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivities.this);
                        builder.setTitle("Do You Want to Delete this Product? ");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    DeleteProdct(productId);
                                }
                                else if(which==1){

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public sellerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.selleritemview,parent,false);
                sellerItemViewHolder sellerViewHolder=new sellerItemViewHolder(view);
                return sellerViewHolder;
            }
        };
        seller_recycler.setAdapter(adapter);
        adapter.startListening();
    }

    private void DeleteProdct(String productId) {

        sellerUncheckRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(getApplicationContext(),"Product deleted Successfully",Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}