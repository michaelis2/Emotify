package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emotify.R


class Home : Fragment() {

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
            val intent = Intent(activity, GetHelp::class.java) // Navigating to Settings activity
            startActivity(intent)
        }
        val button4: View = rootView.findViewById(R.id.button4)
        button4.setOnClickListener {
            val intent = Intent(activity, trackerHistory::class.java)
            startActivity(intent)
        }
        return rootView
    }
}
