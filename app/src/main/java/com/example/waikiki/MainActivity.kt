package com.example.waikiki

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import com.example.dao.viewmodel.*
import com.example.dao.viewmodel.TopicViewModel.Companion.currentTopicId
import com.example.waikiki.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSplashScreen()
        binding= ActivityMainBinding.inflate(layoutInflater)
        AppBarView.binding=binding
        BottomBarView.binding=binding
        setContentView(binding.root)

        binding.floatMainButton.setOnClickListener(){
            onClickAdd()
        }
        binding.button1.setOnClickListener() {
            onClickPage1()
        }
        binding.button2.setOnClickListener() {
            onClickPage2()
        }
        binding.button3.setOnClickListener() {
            onClickPage3()
        }
        binding.imageAppBack.setOnClickListener() {
            if(AppBarView.currentPage!=0) clickClosePages()
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack("Topics")
            add(R.id.fragment_container_view, TopicFragment(),"Topic")
        }
    }

    private fun handleSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            // Create your custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenViewProvider.view,
                View.ALPHA,
                0f
            )
            slideUp.interpolator = LinearInterpolator()
            slideUp.duration = 1000L
            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenViewProvider.remove() }
            // Run your animation.
            slideUp.start()
        }
    }

    fun onClickPage1() {
        AppBarView.appBarShow(1)
        if(supportFragmentManager.findFragmentByTag("Info") != null) {
            supportFragmentManager.popBackStack("Info",0)
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(R.anim.empty,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out_left)
                addToBackStack("Info")
                add(R.id.fragment_container_view, InfoFragment(),"Info")
            }
        }
    }
    fun onClickPage2() {
        AppBarView.appBarShow(2)
        if(supportFragmentManager.findFragmentByTag("Input") != null) {
            supportFragmentManager.popBackStack("Input",0)
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(R.anim.slide_to_top,R.anim.fade_out,R.anim.fade_in,R.anim.slide_to_end)
                addToBackStack("Input")
                add(R.id.fragment_container_view, InputFragment(),"Input")
            }
        }
    }
    fun onClickPage3() {
        AppBarView.appBarShow(3)
        if(supportFragmentManager.findFragmentByTag("Contact") != null) {
            supportFragmentManager.popBackStack("Contact",0)
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out)
                addToBackStack("Contact")
                add(R.id.fragment_container_view, ContaktFragment(),"Contact")
            }
        }
    }

    fun clickClosePages() {
        AppBarView.appBarShow(0)
        supportFragmentManager.popBackStack("Topics",0)
        InfoViewModel.currentInfoId = 0
        InitViewModel.currentInfoId = 0
        ContaktViewModel.currentContaktId = 0
        TopicViewModel.recyclerAnim = true
    }

    override fun onBackPressed() {
        if(AppBarView.currentPage==0) {
            finish()
        }
        else clickClosePages()
    }

    fun onClickAdd() {
        when (AppBarView.currentPage) {
            1 -> ContentEditFragment(0,currentTopicId,0).show(supportFragmentManager,"tag")
            2 -> ContentEditFragment(0,currentTopicId,1).show(supportFragmentManager,"tag")
            3 -> ContaktEditFragment(0,currentTopicId).show(supportFragmentManager,"tag")
        else -> {
            TopicViewModel.recyclerAnim = false
            TopicEditFragment(0).show(supportFragmentManager, "tag")
            }
        }
    }

    fun onClickEdit() {
        when (AppBarView.currentPage) {
            1 -> ContentEditFragment(InfoViewModel.currentInfoId, currentTopicId,0).show(supportFragmentManager,"tag")
            2 -> ContentEditFragment(InitViewModel.currentInfoId, currentTopicId,1).show(supportFragmentManager,"tag")
            3 -> ContaktEditFragment(ContaktViewModel.currentContaktId,currentTopicId).show(supportFragmentManager, "tag")
        else -> {
            TopicViewModel.recyclerAnim = false
            TopicEditFragment(currentTopicId).show(supportFragmentManager, "tag")
            }
        }
    }

}