package com.example.waikiki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.constants.KeyConstants.CAMERA
import com.example.constants.KeyConstants.INFOGALLERY
import com.example.constants.KeyConstants.MYGALLERY
import com.example.dao.adapters.GalleryAdapter
import com.example.dao.adapters.GalleryAdapterMini
import com.example.dao.data.CollectedInfo
import com.example.dao.data.InitialInfo
import com.example.dao.viewmodel.*
import com.example.waikiki.camera.CameraActivity
import com.example.waikiki.databinding.FragmentContentEditBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ContentEditFragment(var curId: Int, val curTopicId: Int, val amount: Int) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentContentEditBinding
    var currentDate = ""
    lateinit var adapter: GalleryAdapterMini
    lateinit var recyclerView: RecyclerView
    lateinit var layerManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentContentEditBinding.bind(inflater.inflate(R.layout.fragment_content_edit,container))
        binding.text.setHorizontallyScrolling(false)
        binding.text.setLines(4)
        binding.comment.setHorizontallyScrolling(false)
        binding.comment.setLines(3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MediaView.myPhoto.clear()

        recyclerView = binding.rwPhoto
        adapter = GalleryAdapterMini()
        MediaView.adapterGaleryMini = adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL,false)
        layerManager = binding.rwPhoto.layoutManager as LinearLayoutManager

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        currentDate = sdf.format(Date())

        if(curId!=0) {
            binding.textOperName.text = getText(R.string.text_edit_content)
            var text =""
            var comment = ""
            var photoList = ""
            lifecycleScope.launchWhenResumed {
                if(amount==0) {
                    val curById: List<CollectedInfo> =
                        ServApplication.database.collectedDao().getByID(curId)
                    var curContent = curById.firstOrNull()?.let { it }
                    text = curContent?.text.toString()
                    comment = curContent?.comment.toString()
                    currentDate = curContent?.dateInfo.toString()
                    photoList = curContent?.photo.toString()
                 }
                else {
                    val curById: List<InitialInfo> =
                        ServApplication.database.initialDao().getByID(curId)
                    var curContent = curById.firstOrNull()?.let { it }
                    text=curContent?.text.toString()
                    comment = curContent?.comment.toString()
                    photoList = curContent?.photo.toString()
                }
                if(!text.isNullOrEmpty()) {
                    binding.text.setText(text)
                    binding.textContent.visibility = VISIBLE
                    binding.cardText.visibility = GONE
                }

                if(!photoList.isNullOrEmpty()) {
                    var photo = photoList.split(",")
                    if (!photo.isNullOrEmpty()) {
                        for (i in photo) {
                            if (i.isNotEmpty() && File(i).isFile) {
                                MediaView.myPhoto.add(File(i))
                            }
                        }
                        refreshPhoto()
                    }
                }

                binding.comment.setText(comment)
            }

        } else {
            binding.textOperName.text=getText(R.string.text_new_content)
            binding.butdelete.visibility= GONE
        }

        binding.ActionButtonText.setOnClickListener() {
            binding.textContent.visibility= VISIBLE
            binding.cardText.visibility= GONE
        }

        // Photo
        binding.ActionButtonPhoto.setOnClickListener() {
            MediaView.media_patch = 1
            val intent = Intent(it.context, CameraActivity::class.java)
            with(intent) {
                putExtra("oper", CAMERA)
                startActivity(this)
            }
            activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.ActionButtonMyGallery.setOnClickListener() {
            MediaView.media_patch=1
            val intent = Intent(it.context, CameraActivity::class.java)
            with(intent) {
                putExtra("oper", MYGALLERY)
                startActivity(this)
            }
        }
        binding.ActionButtonMainGallery.setOnClickListener() {
            MediaView.media_patch=1
            val intent = Intent(it.context, CameraActivity::class.java)
            with(intent) {
                putExtra("oper", MYGALLERY)
                startActivity(this)
            }
        }

        // текущая галерея
        adapter.setOnItemClickListener(

            object : GalleryAdapterMini.OnItemClickListener {
                override fun onItemClick() {
                    MediaView.media_patch=1
                    val intent = Intent(requireContext(), CameraActivity::class.java)
                    with(intent) {
                        putExtra("oper", INFOGALLERY)
                        startActivity(this)
                    }
                    activity?.overridePendingTransition(R.anim.slide_to_top, android.R.anim.fade_out)
                }
            })

        binding.butexit.setOnClickListener(){
            dismiss()
        }

        binding.butsave.setOnClickListener() {
            val text = binding.text.text.toString()
            var photo = ""
            if(!MediaView.myPhoto.isNullOrEmpty()) {
                for(i in MediaView.myPhoto) photo+=","+i.toString()
            }
            val video = ""
            val sound = ""
            val comment = binding.comment.text.toString()
            if (amount == 0) {
                val mark = 0
                if (text.isNotEmpty()||comment.isNotEmpty()||photo.isNotEmpty()) {
                    insertInfo(
                        CollectedInfo(
                            curId,
                            curTopicId,
                            currentDate,
                            text,
                            photo,
                            video,
                            sound,
                            comment,
                            mark
                    ))
                }
            } else {
                if (text.isNotEmpty()||comment.isNotEmpty()||photo.isNotEmpty()) {
                      insertInit(
                          InitialInfo(
                              curId,
                              curTopicId,
                              text,
                              photo,
                              video,
                              sound,
                              comment
                      ))
                  }
            }
            dismiss()
        }

        binding.butdelete.setOnClickListener(){
            val dialog = MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.but_delete_txt)
                .setIcon(R.drawable.warning)
                .setMessage(R.string.txt_delete_content)
                .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
                    deleteContent(curId)
                    dismiss()
                }
                .setNegativeButton(R.string.but_no_txt) { dialog, _ -> }
                .create()
            dialog.show()
        }
    }

    fun deleteContent(curId: Int){
        if(amount==0) {
            lifecycleScope.launchWhenResumed {
                ServApplication.database.collectedDao().deleteByID(curId)
                activity?.runOnUiThread {
                    InfoViewModel.currentInfoId = 0
                }
            }
        } else {
            lifecycleScope.launchWhenResumed {
                ServApplication.database.initialDao().deleteByID(curId)
                activity?.runOnUiThread {
                    InitViewModel.currentInfoId = 0
                }
            }
        }
    }

    fun insertInfo(content: CollectedInfo) {
        lifecycleScope.launch {
            val new= ServApplication.database.collectedDao().insertAll(content).toInt()
            activity?.runOnUiThread {
                InfoViewModel.currentInfoId = new
            }
        }
    }
    fun insertInit(content: InitialInfo) {
        lifecycleScope.launch {
            val new= ServApplication.database.initialDao().insertAll(content).toInt()
            activity?.runOnUiThread {
                InitViewModel.currentInfoId = new
            }
        }
    }

    fun refreshPhoto() {
        if(MediaView.myPhoto.size==0)
            binding.cardPhotoView.visibility= GONE
        else {
            binding.cardPhotoView.visibility = VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshPhoto()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(amount==0) {
            BottomBarView.viewModel_info.refresh(TopicViewModel.currentTopicId)
            BottomBarView.adapter_info.notifyDataSetChanged()
            AppBarView.appBarShow(1)
            //BottomBarView.bottomBarShow(1, InfoViewModel.currentInfoId, BottomBarView.adapter_info.itemCount)
        } else {
            BottomBarView.viewModel_init.refresh(TopicViewModel.currentTopicId)
            BottomBarView.adapter_init.notifyDataSetChanged()
            AppBarView.appBarShow(2)
            //BottomBarView.bottomBarShow(2, InitViewModel.currentInfoId, BottomBarView.adapter_init.itemCount)

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    with(binding) {
                        if(slideOffset < -0.3) {
                            if(slideOffset > -0.7) layoutCollapsed.alpha = 1.5F+2*slideOffset
                        } else layoutCollapsed.alpha = 1F
                    }
                }
            })
        }
    }
}