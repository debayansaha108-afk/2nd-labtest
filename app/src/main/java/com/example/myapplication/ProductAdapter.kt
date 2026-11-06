package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvTitle.text = product.title
        holder.tvPrice.text = "$${product.price}"
        holder.tvDescription.text = product.description

        Glide.with(holder.itemView.context)
            .load(product.thumbnail)
            .into(holder.ivThumbnail)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    }
}
