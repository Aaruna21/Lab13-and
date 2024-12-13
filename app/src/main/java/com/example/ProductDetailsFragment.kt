package com.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.api.NetworkModule
import com.example.api.Product
import com.example.databinding.FragmentProductDetailsBinding
import kotlinx.coroutines.launch

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!


    private val args: ProductDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchProductDetails(args.productId)
    }

    private fun fetchProductDetails(productId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val product = NetworkModule.api.getProductDetails(productId)
                bindProductDetails(product)
            } catch (e: Exception) {
                Log.e("ProductDetailsFragment", "Error fetching product details", e)
            }
        }
    }

    private fun bindProductDetails(product: Product) {
        with(binding) {
            productTitle.text = product.title
            productCategory.text = "Category: ${product.category}"
            productPrice.text = "Price: $${product.price}"
            productDescription.text = product.description
            productRating.text = "Rating: ${product.rating.rate} (${product.rating.count})"
            Glide.with(requireContext()).load(product.image).into(productImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
