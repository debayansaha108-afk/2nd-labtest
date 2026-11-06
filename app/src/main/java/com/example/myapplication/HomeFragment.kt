package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnFetchUsers = view.findViewById<Button>(R.id.btn_fetch_users)
        val btnFetchProducts = view.findViewById<Button>(R.id.btn_fetch_products)

        btnFetchUsers.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(UsersFragment())
        }

        btnFetchProducts.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(ProductsFragment())
        }

        return view
    }
}
