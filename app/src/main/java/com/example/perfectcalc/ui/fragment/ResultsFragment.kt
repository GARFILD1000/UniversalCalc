package com.example.perfectcalc.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.perfectcalc.R
import com.example.perfectcalc.vm.ResultsViewModel
import kotlinx.android.synthetic.main.fragment_results.*
import java.lang.StringBuilder

class ResultsFragment : Fragment() {
    lateinit var resultsViewModel: ResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resultsViewModel = ViewModelProvider(activity!!).get(ResultsViewModel::class.java)

        resultsViewModel.resultLiveData.observe(viewLifecycleOwner, Observer {
            showResult(it)
        })

        resultsViewModel.needUpdateHistory.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                updateHistory()
                resultsViewModel.needUpdateHistory.value = false
            }
        })
    }

    private fun showResult(result: String) {
        scrollViewResults.postDelayed({
            scrollViewResults.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)
        textViewResultMain.setText(result)
    }

    private fun updateHistory() {
        val string = StringBuilder()
        for (i in 0 until resultsViewModel.lastResults.size-1) {
            string.append(resultsViewModel.lastResults.get(i)).append("\n")
        }
        textViewResultLast.setText(string)
    }
}