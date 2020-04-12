package com.example.perfectcalc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perfectcalc.R
import com.example.perfectcalc.adapter.HistoryListAdapter
import com.example.perfectcalc.vm.ConverterViewModel
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment() {
    lateinit var converterViewModel: ConverterViewModel
    lateinit var historyAdapter: HistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupHistoryList()

        converterViewModel = ViewModelProvider(activity!!).get(ConverterViewModel::class.java)

        converterViewModel.resultLiveData.observe(viewLifecycleOwner, Observer {
            showResult(it)
        })
        converterViewModel.editLiveData.observe(viewLifecycleOwner, Observer {
            showEdit(it)
        })
        converterViewModel.needUpdateHistory.observe(viewLifecycleOwner, Observer {
            updateHistory()
        })

    }

    private fun setupHistoryList() {
        context ?: return
        historyAdapter = HistoryListAdapter()
        recyclerViewHistory.adapter = historyAdapter
        recyclerViewHistory.layoutManager =
            LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        recyclerViewHistory.setHasFixedSize(true)
    }


    private fun updateHistory(){
        historyAdapter.items = converterViewModel.history.getAll().reversed()
        historyAdapter.snapToStart()
    }

    private fun showEdit(edit: String) {
        textViewEdit.setText(edit)
    }

    private fun showResult(result: String) {
        textViewResultMain.setText(result)
        textViewResultMain.isSelected = true
    }


}