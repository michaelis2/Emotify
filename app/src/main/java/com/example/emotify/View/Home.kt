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

/**
 * Home fragment that serves as the main navigation hub.
 */

class Home : Fragment() {

    // ViewModel shared across multiple fragments within the same activity
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment and get the root view
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up click listener for journal feature
        val imageView25: View = rootView.findViewById(R.id.imageView25)
        imageView25.setOnClickListener {
            val intent = Intent(activity, journalCalendar::class.java)
            startActivity(intent) // Starts the journal calendar activity
        }

        // Set up click listener for tracker feature
        val imageView26: View = rootView.findViewById(R.id.imageView26)
        imageView26.setOnClickListener {
            val intent = Intent(activity, trackerMain::class.java)
            startActivity(intent) // Starts the main tracker activity
        }

        // Set up click listener for help feature, passing country code from ViewModel
        val imageView27: View = rootView.findViewById(R.id.imageView27)
        imageView27.setOnClickListener {
            val countryCode = viewModel.countryCode.value // Get the current value
            val intent = Intent(activity, GetHelp::class.java)
            intent.putExtra("COUNTRY_CODE", countryCode) // Pass country code to GetHelp activity
            startActivity(intent) // Starts the get help activity
        }

        // Set up click listener for tracker calendar
        val imageButton: View = rootView.findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            val intent = Intent(activity, trackerCalendar::class.java)
            startActivity(intent) // Starts the tracker calendar activity
        }
        return rootView
    }
}
