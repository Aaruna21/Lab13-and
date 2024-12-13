package com.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.FakeStoreApi
import com.example.api.NetworkModule
import com.example.api.Product
import com.example.api.ProductRepository
import com.example.api.ProductViewModel
import com.example.databinding.ProductsFragmentBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsFragment : Fragment() {

    private var _binding: ProductsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter()

        adapter.itemClick = {product ->
            val action =
                ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                    product.id
                )
            findNavController().navigate(action)
        }




        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val products = NetworkModule.api.getProducts()
                adapter.submitList(products)
            } catch (e: Exception) {
                Log.e("ProductListFragment", "Error fetching products", e)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


