package com.example.daggerfirst.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.daggerfirst.R
import com.example.daggerfirst.models.UserRepoInfoResponse
import com.example.daggerfirst.models.UserRepoInfoResponseItem

class UserRepoAdapter : RecyclerView.Adapter<UserRepoAdapter.UserRepoViewHolder>() {
    inner class UserRepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRepoName: TextView = itemView.findViewById(R.id.tv_repo_item_repoName)
        val tvStarCount: TextView = itemView.findViewById(R.id.tv_repo_item_starCount)
        val tvIssuesCount: TextView = itemView.findViewById(R.id.tv_repo_item_issueCount)
        val tvRepoUserName: TextView = itemView.findViewById(R.id.tv_repo_item_userName)
        val tvForkCount: TextView = itemView.findViewById(R.id.tv_repo_item_forkCount)
        val ivLanguageCircle: ImageView = itemView.findViewById(R.id.tv_repo_item_langCircle)
        val tvLanguage: TextView = itemView.findViewById(R.id.tv_repo_item_langText)
    }

    private val colorArray = arrayOf(
        R.color.lang_tile_1,
        R.color.lang_tile_2,
        R.color.lang_tile_3,
        R.color.lang_tile_4,
        R.color.lang_tile_5
    )

    private val differCallBack = object : DiffUtil.ItemCallback<UserRepoInfoResponseItem>() {
        override fun areItemsTheSame(
            oldItem: UserRepoInfoResponseItem,
            newItem: UserRepoInfoResponseItem
        ): Boolean {
            return oldItem.html_url == newItem.html_url
        }

        override fun areContentsTheSame(
            oldItem: UserRepoInfoResponseItem,
            newItem: UserRepoInfoResponseItem
        ): Boolean {
            return oldItem.html_url == newItem.html_url
        }
    }

    private val differList = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRepoViewHolder {
        return UserRepoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_user_repo_rv_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserRepoViewHolder, position: Int) {
        val currPosData = differList.currentList[position]

        holder.tvRepoName.text = currPosData.name; holder.tvStarCount.text =
            currPosData.stargazers_count.toString()
        holder.tvIssuesCount.text =
            currPosData.open_issues_count.toString(); holder.tvRepoUserName.text =
            currPosData.owner.login
        holder.tvForkCount.text = holder.itemView.context.getString(
            R.string.forkCount_text,
            currPosData.forks_count
        )
        holder.tvLanguage.text = currPosData.language
        if(currPosData.language != null && currPosData.language.isNotEmpty()) {
            holder.tvLanguage.setTextColor(colorArray[(0..4).random()])
            holder.ivLanguageCircle.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, colorArray[(0..4).random()])
        }
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    fun submitDifferList(userRepoInfoResponse: UserRepoInfoResponse) {
        differList.submitList(userRepoInfoResponse)
    }
}