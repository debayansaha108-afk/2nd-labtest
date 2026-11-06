package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class UserDetailFragment : Fragment() {

    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var phone: String? = null
    private var image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstName = it.getString(ARG_FIRST_NAME)
            lastName = it.getString(ARG_LAST_NAME)
            email = it.getString(ARG_EMAIL)
            phone = it.getString(ARG_PHONE)
            image = it.getString(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_detail, container, false)

        val ivAvatar = view.findViewById<ImageView>(R.id.iv_detail_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_detail_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_detail_email)
        val tvPhone = view.findViewById<TextView>(R.id.tv_detail_phone)

        tvName.text = "$firstName $lastName"
        tvEmail.text = email
        tvPhone.text = phone

        image?.let {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(ivAvatar)
        }

        return view
    }

    companion object {
        private const val ARG_FIRST_NAME = "first_name"
        private const val ARG_LAST_NAME = "last_name"
        private const val ARG_EMAIL = "email"
        private const val ARG_PHONE = "phone"
        private const val ARG_IMAGE = "image"

        fun newInstance(firstName: String, lastName: String, email: String, phone: String, image: String): UserDetailFragment {
            val fragment = UserDetailFragment()
            val args = Bundle().apply {
                putString(ARG_FIRST_NAME, firstName)
                putString(ARG_LAST_NAME, lastName)
                putString(ARG_EMAIL, email)
                putString(ARG_PHONE, phone)
                putString(ARG_IMAGE, image)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
