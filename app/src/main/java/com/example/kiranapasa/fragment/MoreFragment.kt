package com.example.kiranapasa.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kiranapasa.activity.LoginActivity
import com.example.kiranapasa.activity.UserActivity

import com.example.kiranapasa.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMoreBinding.inflate(layoutInflater)
        binding.updateDetails.setOnClickListener{
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}

