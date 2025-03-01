package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.emotify.R
import com.example.emotify.ViewModel.MainViewModel


class Home : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)


        val imageView25: View = rootView.findViewById(R.id.imageView25)
        imageView25.setOnClickListener {
            val intent = Intent(activity, journalCalendar::class.java)
            startActivity(intent)
        }
        val imageView26: View = rootView.findViewById(R.id.imageView26)
        imageView26.setOnClickListener {
            val intent = Intent(activity, trackerMain::class.java)
            startActivity(intent)
        }
        val imageView27: View = rootView.findViewById(R.id.imageView27)
        imageView27.setOnClickListener {
            val countryCode = viewModel.countryCode.value // Get the current value
            val intent = Intent(activity, GetHelp::class.java)
            intent.putExtra("COUNTRY_CODE", countryCode)
            startActivity(intent)
        }
        val imageButton: View = rootView.findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            val intent = Intent(activity, trackerCalendar::class.java)
            startActivity(intent)
        }
        return rootView
    }
}
