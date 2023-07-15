package com.example.swipeapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swipeapp.R
import com.example.swipeapp.databinding.ItemProductBinding
import com.example.swipeapp.models.get.SwipeAllProductResponse


/**
 * RecyclerView adapter for displaying a list of products.
 * @param context The context of the adapter.
 */
class SwipeListOfProductAdapter(private val context: Context) :
    RecyclerView.Adapter<SwipeListOfProductViewHolder>() {

    private var details = mutableListOf<SwipeAllProductResponse>()
    private var filteredDetails = mutableListOf<SwipeAllProductResponse>()

    init {
        filteredDetails.addAll(details)
    }

    /**
     * Sets the list of products and updates the adapter.
     * @param swipeResponse The list of products to display.
     */
    fun setProductList(swipeResponse: List<SwipeAllProductResponse>) {
        details.clear()
        details.addAll(swipeResponse)
        filterData("")
        notifyDataSetChanged()
    }

    /**
     * Filters the data based on the given query.
     * @param query The query to filter the data.
     */
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

    /**
     * Creates a ViewHolder for the RecyclerView.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SwipeListOfProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwipeListOfProductViewHolder(binding)
    }

    /**
     * Binds the data to the ViewHolder.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SwipeListOfProductViewHolder, position: Int) {
        val detail = filteredDetails[position]
        holder.binding.apply {
            Glide.with(context)
                .load(detail.image)
                .centerCrop()
                .placeholder(R.drawable.ic_swipe)
                .thumbnail(0.5f)
                .into(this.imageView)
            val productName = detail.productName.toString().replace("\"", "")
            this.tvProductName.text = "Product Name: $productName"
            this.tvProductType.text = "Product Type: ${detail.productType.toString()}"
            this.tvPrice.text = "Price: ${detail.price.toString()}"
            this.tvTax.text = "Tax: ${detail.tax.toString()}"
        }
    }

    /**
     * Returns the number of items in the list.
     */
    override fun getItemCount(): Int {
        return filteredDetails.size
    }

    /**
     * Filters the data based on the given query and updates the adapter.
     * @param query The query to filter the data.
     */
    fun filter(query: String) {
        filterData(query)
        notifyDataSetChanged()
    }
}

/**
 * ViewHolder for the SwipeListOfProductAdapter.
 * @param binding The ViewBinding for the item layout.
 */
class SwipeListOfProductViewHolder(val binding: ItemProductBinding) :
    RecyclerView.ViewHolder(binding.root)
