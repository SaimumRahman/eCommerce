package ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import Interface.itemOnClickListener;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName,productDescription,productPrice;
    public ImageView productImageView;
    public itemOnClickListener Listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView=itemView.findViewById(R.id.product_image);
        textProductName=itemView.findViewById(R.id.product_name);
        productPrice=itemView.findViewById(R.id.product_price);
        productDescription=itemView.findViewById(R.id.product_description);

    }
    public void setItemOnClickListener(itemOnClickListener itemOnClickListener){
        this.Listener=itemOnClickListener;
    }

    @Override
    public void onClick(View v) {
        Listener.onClick(v,getAdapterPosition(),false);
    }
}
