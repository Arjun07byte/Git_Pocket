package com.example.daggerfirst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daggerfirst.R
import com.example.daggerfirst.adapters.SearchUserAdapter
import com.example.daggerfirst.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModelInstance: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutNoResultFound: LinearLayout = view.findViewById(R.id.layout_searchNoResults)
        val layoutSearchInProgress: LinearLayout = view.findViewById(R.id.layout_searchInProgress)
        val rvSearchFragment: RecyclerView =
            view.findViewById(R.id.recycler_view_user_search_result)
        val searchEditText: EditText =
            view.findViewById(R.id.editText_searchQuery); searchEditText.setText("")

        searchEditText.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                layoutNoResultFound.visibility = View.GONE; rvSearchFragment.visibility = View.GONE
                layoutSearchInProgress.visibility = View.VISIBLE
                viewModelInstance.searchUser(it.toString(), 1)
            } else {
                layoutSearchInProgress.visibility = View.GONE; rvSearchFragment.visibility =
                    View.GONE
                layoutNoResultFound.visibility = View.VISIBLE
            }
        }

        val searchUserAdapter = SearchUserAdapter()
        rvSearchFragment.apply {
            adapter = searchUserAdapter; layoutManager =
            LinearLayoutManager(this@SearchFragment.context)
        }
        viewModelInstance.userSearchResultStates.observe(this.viewLifecycleOwner) {
            layoutSearchInProgress.visibility = View.GONE; layoutNoResultFound.visibility =
            View.GONE
            searchUserAdapter.submitDifferList(it); rvSearchFragment.visibility = View.VISIBLE
        }
    }
}