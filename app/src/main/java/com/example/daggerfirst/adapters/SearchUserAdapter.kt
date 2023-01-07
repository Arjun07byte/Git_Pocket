package com.example.daggerfirst.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daggerfirst.R
import com.example.daggerfirst.models.UserInfoSuccessResponse

class SearchUserAdapter : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {
    inner class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tv_repo_item_userName)
        val tvUserFollowCount: TextView = itemView.findViewById(R.id.tv_repo_item_followCount)
        val tvUserRepoCount: TextView = itemView.findViewById(R.id.tv_repo_item_repoCount)
        val imgViewUserProfilePic: ImageView =
            itemView.findViewById(R.id.imgView_rv_item_user_profilePic)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<UserInfoSuccessResponse?>() {
        override fun areItemsTheSame(
            oldItem: UserInfoSuccessResponse,
            newItem: UserInfoSuccessResponse
        ): Boolean {
            return oldItem.html_url == newItem.html_url
        }

        override fun areContentsTheSame(
            oldItem: UserInfoSuccessResponse,
            newItem: UserInfoSuccessResponse
        ): Boolean {
            return oldItem.html_url == newItem.html_url
        }
    }

    private val differList = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        return SearchUserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_search_user_rv_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val currPosData = differList.currentList[position]

        if (currPosData != null) {
            holder.tvUserName.text = currPosData.login; Glide.with(holder.itemView)
                .load(currPosData.avatar_url).placeholder(R.color.lang_tile_1)
                .error(R.color.lang_tile_1).into(holder.imgViewUserProfilePic)
            holder.tvUserRepoCount.text =
                holder.itemView.context.getString(R.string.repoCount_text, currPosData.public_repos)
            holder.tvUserFollowCount.text = holder.itemView.context.getString(
                R.string.followCount_text,
                currPosData.followers,
                currPosData.following
            )
        }
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    fun submitDifferList(userRepoInfoResponse: List<UserInfoSuccessResponse?>) {
        differList.submitList(userRepoInfoResponse)
    }
}