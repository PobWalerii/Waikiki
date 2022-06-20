package com.example.waikiki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.example.dao.data.Contakt
import com.example.dao.viewmodel.*
import com.example.waikiki.databinding.FragmentContaktEditBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ContaktEditFragment(var curId: Int, var curTopicId: Int) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentContaktEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentContaktEditBinding.bind(inflater.inflate(R.layout.fragment_contakt_edit,container))
        binding.contaktComment.setHorizontallyScrolling(false)
        binding.contaktComment.setLines(3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(curId!=0) {
            binding.textOperName.text = getText(R.string.text_edit_contakt)
            lifecycleScope.launchWhenResumed {
                val curById: List<Contakt> = ServApplication.database.contaktDao().getByID(curId)
                val curContakt = curById.firstOrNull()?.let { it }
                binding.textContaktName.editText?.setText(curContakt?.contaktName)
                binding.textContaktPhone.editText?.setText(curContakt?.contaktPhone)
                binding.textContaktComment.editText?.setText(curContakt?.contaktComment)
            }
        } else {
            binding.textOperName.text=getText(R.string.text_new_contakt)
            binding.butdelete.visibility= GONE
        }

        binding.butexit.setOnClickListener(){
            dismiss()
        }

        binding.butsave.setOnClickListener(){
            val name = binding.textContaktName.editText?.text.toString()
            if (name.isNotEmpty()) {
                insertContakt(Contakt(
                    curId,
                    curTopicId,
                    name,
                    binding.textContaktPhone.editText?.text.toString(),
                    binding.textContaktComment.editText?.text.toString()
                ))
                dismiss()
            }
        }

        binding.butdelete.setOnClickListener(){
            val dialog = MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.but_delete_txt)
                .setIcon(R.drawable.warning)
                .setMessage(R.string.txt_delete_contakt)
                .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
                    deleteContakt(curId)
                    dismiss()
                }
                .setNegativeButton(R.string.but_no_txt) { dialog, _ -> }
                .create()
            dialog.show()
        }
    }

    fun deleteContakt(curId: Int){
        lifecycleScope.launchWhenResumed {
            ServApplication.database.contaktDao().deleteByID(curId)
            activity?.runOnUiThread {
                ContaktViewModel.currentContaktId = 0
            }
        }
    }

    fun insertContakt(contakt: Contakt) {
        lifecycleScope.launch {
            val new= ServApplication.database.contaktDao().insertAll(contakt).toInt()
            activity?.runOnUiThread {
                ContaktViewModel.currentContaktId = new
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BottomBarView.viewModel_contakt.refresh(TopicViewModel.currentTopicId)
        BottomBarView.adapter_contakt.notifyDataSetChanged()
        AppBarView.appBarShow(3)
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