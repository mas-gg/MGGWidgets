package com.mgg.demo.mggwidgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mgg.demo.mggwidgets.util.ViewBindingUtil

open class BaseFragment<VB : ViewBinding> : Fragment() {
    lateinit var binding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ViewBindingUtil.inflateWithGeneric(this, inflater, container, false)
        return binding.root
    }
}