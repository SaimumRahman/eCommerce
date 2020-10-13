package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Interface.itemOnClickListener;
import ViewHolder.ProductViewHolder;

public class Chech_New_Products extends AppCompatActivity {
private RecyclerView approve_recycler;
RecyclerView.LayoutManager layoutManager_approve;
private DatabaseReference approveRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chech__new__products);

        approve_recycler=findViewById(R.id.admin_approve_recycler);
        approve_recycler.setHasFixedSize(true);
        layoutManager_approve=new LinearLayoutManager(this);
        approve_recycler.setLayoutManager(layoutManager_approve);

        approveRef= FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(approveRef.orderByChild("product_state").equalTo("not_approved"),Products.class)
                .build();

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
                        final String productId= model.getPid();

                        CharSequence option[]=new CharSequence[]

                                {
                                        "Yes",
                                        "No"

                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(Chech_New_Products.this);
                        builder.setTitle("Do You Want to Approve this Product? ");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    ChangeProductState(productId);
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
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder productViewHolder=new ProductViewHolder(view);
                return productViewHolder;
            }
        };
        approve_recycler.setAdapter(adapter);
        adapter.startListening();

    }

    private void ChangeProductState(String productId) {

        approveRef.child(productId).child("product_state").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Item has been Approved",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}