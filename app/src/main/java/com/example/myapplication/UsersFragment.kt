package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserAdapter
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        progressBar = view.findViewById(R.id.progress_bar)

        adapter = UserAdapter(emptyList()) { user ->
            // Handle item click -> Navigate to Detail Fragment
            val detailFragment = UserDetailFragment.newInstance(user.firstName, user.lastName, user.email, user.phone, user.image)
            (activity as? MainActivity)?.navigateTo(detailFragment)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }

        progressBar.visibility = View.VISIBLE
        fetchData()

        return view
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getUsers()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        adapter.updateUsers(response.body()!!.users)
                    } else {
                        Toast.makeText(context, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                    }
                    hideLoading()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }
        }
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }
}
