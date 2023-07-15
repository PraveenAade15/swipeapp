package com.example.swipeapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swipeapp.R
import com.example.swipeapp.databinding.ItemProductBinding
import com.example.swipeapp.models.SwipeAllProductResponse


class SwipeListOfProductAdapter(private val context: Context) :
    RecyclerView.Adapter<SwipeListOfProductViewHolder>() {

    private var details = mutableListOf<SwipeAllProductResponse>()
    private var filteredDetails = mutableListOf<SwipeAllProductResponse>()

    init {
        filteredDetails.addAll(details)
    }

    fun setProductList(swipeResponse: List<SwipeAllProductResponse>) {
        details.clear()
        details.addAll(swipeResponse)
        filterData("")
        notifyDataSetChanged()
    }

    private fun filterData(query: String) {
        filteredDetails.clear()
        if (query.isBlank()) {
            filteredDetails.addAll(details)
        } else {
            val lowerCaseQuery = query.toLowerCase()
            for (detail in details) {
                if (detail.productName?.toLowerCase()!!.contains(lowerCaseQuery) ||
                    detail.productType?.toLowerCase()!!.contains(lowerCaseQuery)
                ) {
                    filteredDetails.add(detail)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SwipeListOfProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwipeListOfProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SwipeListOfProductViewHolder, position: Int) {
        val detail = filteredDetails[position]
        holder.binding.apply {
            Glide.with(context)
                .load(detail.image)
                .centerCrop()
                .placeholder(R.drawable.ic_swipe)
                .thumbnail(0.5f)
                .into(this.imageView)
            this.tvProductName.text = "Product Name: ${detail.productName}"
            this.tvProductType.text = "Product Type: ${detail.productType}"
            this.tvPrice.text = "Price: ${detail.price.toString()}"
            this.tvTax.text = "Tax: ${detail.tax.toString()}"
        }
    }

    override fun getItemCount(): Int {
        return filteredDetails.size
    }

    fun filter(query: String) {
        filterData(query)
        notifyDataSetChanged()
    }
}

class SwipeListOfProductViewHolder(val binding: ItemProductBinding) :
    RecyclerView.ViewHolder(binding.root)
