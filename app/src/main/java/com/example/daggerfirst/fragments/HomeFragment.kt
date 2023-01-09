package com.example.daggerfirst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daggerfirst.R
import com.example.daggerfirst.adapters.UserRepoAdapter
import com.example.daggerfirst.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpHomeContent(view, getCurrentUser())
    }

    private fun getCurrentUser(): String? {
        val mySharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preference_key),
            AppCompatActivity.MODE_PRIVATE
        )
        return mySharedPreferences.getString(getString(R.string.sp_github_key), null)
    }

    private fun setUpHomeContent(view: View, currentUser: String?) {
        // setting up the Home Screen Adapters and Views Variables
        val myViewModel: HomeViewModel by viewModels()
        val tvUserName: TextView = view.findViewById(R.id.tv_home_userName)
        val tvUserFollowCount: TextView = view.findViewById(R.id.tv_home_user_followCount)
        val tvUserRepoCount: TextView = view.findViewById(R.id.tv_home_user_repoCount)
        val userRepoAdapter = UserRepoAdapter()
        val rvHomeScreenPosts: RecyclerView = view.findViewById(R.id.recycler_view_user_repo_items)
        val layoutHomeLoading: LinearLayout = view.findViewById(R.id.layout_main_fragment_loading)
        val layoutHomeInfo: RelativeLayout = view.findViewById(R.id.layout_main_fragment_info)

        // attaching adapter and observers to change and post values accordingly
        rvHomeScreenPosts.apply {
            adapter = userRepoAdapter; layoutManager = LinearLayoutManager(view.context)
        }
        tvUserName.text = currentUser
        if (currentUser != null) {
            myViewModel.getSignedInUserInfo(currentUser); myViewModel.getUserRepos(currentUser)
            setUpSideBarDrawerUserName(currentUser)
        }

        myViewModel.signedInUserInfoResultStates.observe(this.viewLifecycleOwner) {
            if (it != null) {
                tvUserFollowCount.text =
                    view.context.getString(R.string.followCount_text, it.followers, it.following)
                setUpSideBarDrawerImage(it.avatar_url)
            }
        }

        myViewModel.signedInUserRepoResultStates.observe(this.viewLifecycleOwner) {
            if (it != null) {
                tvUserRepoCount.text = view.context.getString(R.string.repoCount_text, it.size)
                userRepoAdapter.submitDifferList(it)
                layoutHomeLoading.visibility = View.GONE; layoutHomeInfo.visibility = View.VISIBLE
            } else {
                layoutHomeLoading.visibility = View.VISIBLE; layoutHomeInfo.visibility = View.GONE
            }
        }
    }

    private fun setUpSideBarDrawerImage(imageUrl: String) {
        val signedInUserImage: ImageView? =
            activity?.findViewById(R.id.imgView_userProfileSideDrawer)
        if (signedInUserImage != null) Glide.with(this).load(imageUrl).into(signedInUserImage)
    }

    private fun setUpSideBarDrawerUserName(currentUser: String) {
        val signedInUserName: TextView? = activity?.findViewById(R.id.tv_sideBarMenu_userName)
        signedInUserName?.text = currentUser
    }
}