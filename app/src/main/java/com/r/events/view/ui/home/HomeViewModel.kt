package com.r.events.view.ui.home

import android.content.Context
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.r.events.MainViewModel
import com.r.events.adapter.EventAdapter
import com.r.events.model.list

//ToDo сделать так, чтобы context не нужно было постоянно ложить в каждый метод
class HomeViewModel : ViewModel(){

    fun setupRecyclerView(context: Context, recyclerView: RecyclerView){
        val eventAdapter = EventAdapter(context, list)
        eventAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = eventAdapter
        val ss = list
    }

    fun getData( fragment: Fragment, context: Context)
    {
        val mainViewModel = ViewModelProviders.of(fragment).get(MainViewModel::class.java)
        mainViewModel.getContext(context)
    }
}