package com.example.dao.viewmodel


import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModel
import com.example.waikiki.R
import com.example.waikiki.databinding.ActivityMainBinding

class AppBarView: ViewModel() {
    companion object {
        fun appBarShow(page: Int) {
            currentPage = page
            if (currentPage == 0) {
                with(binding) {
                    appBar.layoutParams.height = 155
                    textApp.setText(R.string.app_name)
                    textApp.setTextSize(1, 20F)
                    imageAppBack.setImageResource(R.drawable.waikiki)
                    bottomAppBar.visibility= VISIBLE
                }
            } else {
                with(binding) {
                    appBar.layoutParams.height = 285
                    textApp.text = TopicViewModel.currentTopicName
                    textApp.setTextSize(1, 16F)
                    imageAppBack.setImageResource(R.drawable.arrow_left)
                    button1.setTextColor(getColor(button1.context, R.color.white))
                    button2.setTextColor(getColor(button2.context, R.color.white))
                    button3.setTextColor(getColor(button3.context, R.color.white))
                    when (currentPage) {
                        1 -> button1.setTextColor(getColor(button1.context, R.color.color1))
                        2 -> button2.setTextColor(getColor(button2.context, R.color.color1))
                        3 -> button3.setTextColor(getColor(button3.context, R.color.color1))
                    }
                    bottomAppBar.visibility= GONE
                }
            }
        }

        lateinit var binding: ActivityMainBinding
        var currentPage = 0

     }
}