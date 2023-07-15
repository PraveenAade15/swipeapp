package com.example.swipeapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeapp.R
import com.example.swipeapp.databinding.FragmentListingProductBinding
import com.example.swipeapp.models.SwipeAllProductResponse
import com.example.swipeapp.utils.NetworkResult
import com.example.swipeapp.viewmodel.SwipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingProductFragment : Fragment() {
    private var _binding: FragmentListingProductBinding? = null
    private val binding get() = _binding!!
    private val swipeViewModel by viewModels<SwipeViewModel>()
    lateinit var swipeListOfProduct: SwipeListOfProductAdapter
    val filteredList = ArrayList<SwipeAllProductResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListingProductBinding.inflate(inflater, container, false)
        swipeViewModel.getAllProduct()
        swipeListOfProduct = SwipeListOfProductAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = swipeListOfProduct
        bindObservers()
        mvvmItemClick()

    }


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

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.isShimmerStarted
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmer()
        super.onPause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}