package com.example.daggerfirst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daggerfirst.R
import com.example.daggerfirst.adapters.SearchRepoAdapter
import com.example.daggerfirst.adapters.SearchUserAdapter
import com.example.daggerfirst.viewModels.HomeViewModel
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModelInstance: HomeViewModel by viewModels()
    private val searchUserAdapter = SearchUserAdapter()
    private val searchRepoAdapter = SearchRepoAdapter()
    private val searchOrgAdapter = SearchRepoAdapter()
    private lateinit var rvSearchFragment: RecyclerView
    private lateinit var layoutNoResultFound: LinearLayout
    private lateinit var layoutSearchInProgress: LinearLayout
    private lateinit var layoutOrgDetails: LinearLayout
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutNoResultFound = view.findViewById(R.id.layout_searchNoResults)
        layoutSearchInProgress = view.findViewById(R.id.layout_searchInProgress)
        layoutOrgDetails = view.findViewById(R.id.layout_orgDisplay)
        rvSearchFragment = view.findViewById(R.id.recycler_view_user_search_result)
        searchEditText = view.findViewById(R.id.editText_searchQuery); searchEditText.setText("")
        val imgViewOrgLogo: ImageView = view.findViewById(R.id.imgView_orgLogo)
        val tvOrgName: TextView = view.findViewById(R.id.tv_orgName)
        val searchChipGroup: ChipGroup = view.findViewById(R.id.layout_chipGroupSearch)

        searchEditText.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                layoutNoResultFound.visibility = View.GONE; rvSearchFragment.visibility = View.GONE
                layoutSearchInProgress.visibility = View.VISIBLE
                when (searchChipGroup.checkedChipId) {
                    R.id.chip_userSearch -> viewModelInstance.searchUser(it.toString(), 1)
                    R.id.chip_repoSearch -> viewModelInstance.searchRepo(it.toString(), 1)
                    R.id.chip_orgSearch -> viewModelInstance.searchOrg(it.toString(), 1)
                }
            } else {
                viewModelInstance.cancelAllSearch()
                layoutSearchInProgress.visibility = View.GONE; rvSearchFragment.visibility =
                    View.GONE; layoutOrgDetails.visibility = View.GONE
                layoutNoResultFound.visibility = View.VISIBLE
            }
        }

        // setting up the chip listener to change
        // respective adapter of the recycler view
        searchChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.contains(R.id.chip_repoSearch)) changeRvAdapter("repoSearch")
            else if (checkedIds.contains(R.id.chip_orgSearch)) changeRvAdapter("orgSearch")
            else changeRvAdapter("userSearch")
        }

        viewModelInstance.userSearchResultStates.observe(this.viewLifecycleOwner) {
            layoutSearchInProgress.visibility = View.GONE
            if (it.isNotEmpty()) {
                layoutNoResultFound.visibility = View.GONE
                searchUserAdapter.submitDifferList(it); rvSearchFragment.visibility = View.VISIBLE
            }
        }

        viewModelInstance.repoSearchResultStates.observe(this.viewLifecycleOwner) {
            layoutSearchInProgress.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                layoutNoResultFound.visibility = View.GONE
                searchRepoAdapter.submitDifferList(it); rvSearchFragment.visibility = View.VISIBLE
            }
        }

        viewModelInstance.orgSearchResultStates.observe(this.viewLifecycleOwner) {
            layoutSearchInProgress.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                layoutNoResultFound.visibility = View.GONE; layoutOrgDetails.visibility =
                    View.VISIBLE
                Glide.with(view).load(it[0]?.owner?.avatar_url).error(R.drawable.empty_circle)
                    .placeholder(R.drawable.empty_circle).into(imgViewOrgLogo)
                tvOrgName.text = searchEditText.text
                searchOrgAdapter.submitDifferList(it); rvSearchFragment.visibility = View.VISIBLE
            }
        }
    }

    private fun changeRvAdapter(currSearch: String) {
        viewModelInstance.cancelAllSearch(); searchEditText.setText("")

        when (currSearch) {
            "repoSearch" -> {
                searchRepoAdapter.submitDifferList(listOf())
                rvSearchFragment.apply {
                    adapter = searchRepoAdapter; layoutManager =
                    LinearLayoutManager(this@SearchFragment.context)
                }; searchEditText.hint = "Enter Repo Name"
                layoutOrgDetails.visibility = View.GONE
            }
            "orgSearch" -> {
                searchOrgAdapter.submitDifferList(listOf())
                rvSearchFragment.apply {
                    adapter = searchOrgAdapter; layoutManager =
                    LinearLayoutManager(this@SearchFragment.context)
                }; searchEditText.hint = "Enter Organization Name"
            }
            else -> {
                searchUserAdapter.submitDifferList(listOf())
                rvSearchFragment.apply {
                    adapter = searchUserAdapter; layoutManager =
                    LinearLayoutManager(this@SearchFragment.context)
                }; searchEditText.hint = "Enter User Name"
                layoutOrgDetails.visibility = View.GONE
            }
        }
    }
}