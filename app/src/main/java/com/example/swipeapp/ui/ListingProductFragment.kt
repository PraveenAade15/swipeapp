package com.example.swipeapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeapp.R
import com.example.swipeapp.databinding.FragmentListingProductBinding
import com.example.swipeapp.models.get.SwipeAllProductResponse
import com.example.swipeapp.utils.Constants
import com.example.swipeapp.utils.NetworkResult
import com.example.swipeapp.viewmodel.SwipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 * Fragment for listing products.
 * created by Praveen Aade
 */
@AndroidEntryPoint
class ListingProductFragment : Fragment() {
    private var _binding: FragmentListingProductBinding? = null
    private val binding get() = _binding!!
    private val swipeViewModel by viewModels<SwipeViewModel>()
    lateinit var swipeListOfProduct: SwipeListOfProductAdapter
    val filteredList = ArrayList<SwipeAllProductResponse>()

    /**
     * Creates the view for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListingProductBinding.inflate(inflater, container, false)
        swipeViewModel.getAllProduct()
        swipeListOfProduct = SwipeListOfProductAdapter(requireContext())
        return binding.root
    }

    /**
     * Initializes the view components and sets up listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = swipeListOfProduct
        try {
            if (!Constants.isNetworkAvailable(requireContext())) {
                showNoInternetDialog()
            }else{
                bindObservers()
            }

        }catch (e:Exception){

        }

        mvvmItemClick()
    }

    /**
     * Handles the click events.
     */
    private fun mvvmItemClick() {
        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_listingProductFragment_to_addProductFragment)
        }
        binding.searchButton.setOnClickListener {
            if (binding.clSearch.visibility == View.VISIBLE) {
                binding.clSearch.visibility = View.GONE
                binding.searchButton.visibility = View.VISIBLE
            } else {
                binding.clSearch.visibility = View.VISIBLE
                binding.searchButton.visibility = View.GONE
            }
        }
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                swipeListOfProduct.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Binds the observers to observe the product data.
     */
    private fun bindObservers() {
        swipeViewModel.swipeLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.clShimmerStart.visibility = View.VISIBLE
                    val response = it.data as ArrayList<SwipeAllProductResponse>
                    swipeListOfProduct.setProductList(response)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.clShimmerStart.visibility = View.VISIBLE
                }

                is NetworkResult.Loading -> {
                    binding.shimmerFrameLayout.isShimmerStarted
                    binding.clShimmerStart.visibility = View.GONE
                }
            }
        })
    }

    /**
     * Resumes the fragment and starts the shimmer animation.
     */
    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.isShimmerStarted
    }

    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                activity?.finish()
            }

        val dialog = builder.create()
        dialog.show()
    }


    /**
     * Pauses the fragment and stops the shimmer animation.
     */
    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmer()
        super.onPause()
    }

    /**
     * Clears the binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
