package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import Interface.itemOnClickListener;


public class sellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName,productDescription,productPrice,productStatus;
    public ImageView productImageView;
    public itemOnClickListener Listener;

    public sellerItemViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView=itemView.findViewById(R.id.seller_product_image);
        textProductName=itemView.findViewById(R.id.seller_product_name);
        productPrice=itemView.findViewById(R.id.seller_product_price);
        productDescription=itemView.findViewById(R.id.seller_product_description);
        productStatus=itemView.findViewById(R.id.seller_product_status);

    }
    public void setItemOnClickListener(itemOnClickListener itemOnClickListener){
        this.Listener=itemOnClickListener;
    }

    @Override
    public void onClick(View v) {
        Listener.onClick(v,getAdapterPosition(),false);
    }
}