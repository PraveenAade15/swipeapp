package com.example.swipeapp.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.swipeapp.R
import com.example.swipeapp.databinding.FragmentAddProductBinding
import com.example.swipeapp.utils.Constants
import com.example.swipeapp.utils.Constants.TAG
import com.example.swipeapp.utils.NetworkResult
import com.example.swipeapp.viewmodel.SwipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


/**
 * Fragment for adding a product.
 * created by Praveen Aade
 */
@AndroidEntryPoint
class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val swipeViewModel by viewModels<SwipeViewModel>()
    private var product_type: String? = null
    private val PICK_FILES_REQUEST_CODE = 123
    var profileImageBody: MultipartBody.Part? = null

    /**
     * Creates the view for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Initializes the view components and sets up listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProduct()
        mvvmItemClick()
        validation()
        setupSpinner()
    }

    /**
     * Observes the product and handles the add product button click event.
     */
    private fun observeProduct() {
        binding.btnAddProduct.setOnClickListener {
                if (validateFields()) {
                    binding.btnAddProduct.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    val productName = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etProductName.text.toString())
                    val productType = RequestBody.create("text/plain".toMediaTypeOrNull(), product_type.toString())
                    val price = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etProductPrice.text.toString())
                    val tax = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etTaxrate.text.toString())
                    val response = swipeViewModel.addProduct(productName, productType, price, tax, profileImageBody)
                    swipeViewModel.addProductResponse.observe(viewLifecycleOwner) { response ->
                        if (response.isSuccessful) {
                            dialog()
                            binding.btnAddProduct.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        } else {
                            Toast.makeText(
                                requireContext(),
                                response.message().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

        }
    }

    /**
     * Sets up the spinner for selecting the product category.
     */
    private fun setupSpinner() {
        val personNames =
            arrayOf("Select Category", "Service", "Books", "Food", "Product", "Electronics")
        val spinner = binding.tvProductSpinner
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, personNames)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.selectedItem
                product_type = item.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    /**
     * Handles the click event of the back button.
     */
    private fun mvvmItemClick() {
        binding.backBtn.setOnClickListener {
            val isSuccess = findNavController().navigateUp()
            if (!isSuccess) requireActivity().onBackPressed()
        }
    }

    /**
     * Handles the click event for selecting an image from the gallery.
     */
    private fun validation() {
        binding.buttonfile.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type =
                "image/*" // You can adjust the MIME type according to your requirements
            galleryIntent.putExtra(
                Intent.EXTRA_ALLOW_MULTIPLE,
                true
            ) // Allow selecting multiple files
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Select Picture"),
                PICK_FILES_REQUEST_CODE
            )
        }
    }

    /**
     * Handles the result of selecting an image from the gallery.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            binding.imageViewPreview.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(uri)
                .centerCrop()
                .into(binding.imageViewPreview)
            val inputStream = context?.contentResolver?.openInputStream(uri!!)
            val tempFile = createTempFile("temp", null, context?.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            val requestFile: RequestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())

            val profileImage: RequestBody = tempFile
                .asRequestBody("image/jpg".toMediaTypeOrNull())

            profileImageBody =
                MultipartBody.Part.createFormData(
                    "image_url",
                    tempFile.name, profileImage
                )
            Log.d(TAG, "onActivityResultHHHBJ: $profileImageBody")
        }
    }

    /**
     * Validates the input fields.
     */
    private fun validateFields(): Boolean {
        val productName = binding.etProductName.text.toString().trim()
        val productPrice = binding.etProductPrice.text.toString().trim()
        val taxRate = binding.etTaxrate.text.toString().trim()

        if (productName.isEmpty()) {
            binding.etProductName.error = "Product name cannot be empty"
            return false
        }

        if (productPrice.isEmpty()) {
            binding.etProductPrice.error = "Product price cannot be empty"
            return false
        }

        val productPricePattern = Regex("^[0-9]+(\\.[0-9]{1,2})?$")
        if (!productPrice.matches(productPricePattern)) {
            binding.etProductPrice.error = "Invalid product price"
            return false
        }

        if (taxRate.isEmpty()) {
            binding.etTaxrate.error = "Tax rate cannot be empty"
            return false
        }
        val productPricePatterno = Regex("^[0-9]+(\\.[0-9]{1,2})?$")
        if (!taxRate.matches(productPricePatterno)) {
            binding.etTaxrate.error = "Invalid tax rate"
            return false
        }

        if (product_type == "Select Category") {
            Toast.makeText(requireContext(), "Please Select Category", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    /**
     * Shows the success dialog.
     */
    private fun dialog() {
        val dialog = Dialog(requireContext())
        //dialog.setCancelable(false)
        dialog.setContentView(R.layout.item_success)
        // val body = dialog.findViewById(R.id.body) as TextView
        val yesBtn = dialog.findViewById(R.id.ok) as Button
        val tvInformation = dialog.findViewById(R.id.textView14) as TextView
        val tvMessage = dialog.findViewById(R.id.textView15) as TextView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    /**
     * Clears the binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    @SuppressLint("SuspiciousIndentation")
//    private fun upload() {
//        val filesDir = requireActivity().applicationContext.filesDir
//        val file = File(filesDir, "image.png")
//        if (imageUri !=null){
//            try {
//                val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
//                val outputStream = FileOutputStream(file)
//                inputStream!!.copyTo(outputStream)
//                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//                profileImageBody  = MultipartBody.Part.createFormData("image", file.name, requestBody)
//            }catch (e:Exception){
//
//            }
//
//        }else{
//            Log.d(Constants.TAG, "upload: $imageUri")
//            println()
//        }
//        Log.d(Constants.TAG, "uploadImage: $profileImageBody.")
//        try {
//            val productName = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etProductName.text.toString())
//            val productType = RequestBody.create("text/plain".toMediaTypeOrNull(), product_type.toString())
//            val price = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etProductPrice.text.toString())
//            val tax = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etTaxrate.text.toString())
//            val response = swipeViewModel.addProduct(productName, productType, price, tax, profileImageBody)
//            swipeViewModel.addProductResponse.observe(viewLifecycleOwner) {
//                if (it.isSuccessful) {
//                    dialog()
//                    binding.btnAddProduct.visibility = View.VISIBLE
//                    binding.progressBar.visibility = View.GONE
//                } else {
//                    binding.btnAddProduct.visibility = View.VISIBLE
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), it.message(), Toast.LENGTH_SHORT).show()
//                }
//            }
//
//
//        }catch (e:Exception){
//            println()
//
//        }
//
//
//
//    }
}







