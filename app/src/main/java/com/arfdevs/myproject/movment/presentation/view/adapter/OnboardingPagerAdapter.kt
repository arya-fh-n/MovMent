package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.movment.R

class OnboardingPagerAdapter(private val imageList: List<Int>, private val titleList: List<String>): RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {
    class OnboardingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_onboarding_titles)
        val imageView: ImageView = itemView.findViewById(R.id.iv_onboarding_pages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_onboarding_images,
            parent,
            false
        )

        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        imageList[position].let {
            holder.imageView.setImageResource(it)
        }

        titleList[position].let {
            holder.textView.text = it
        }
    }

    override fun getItemCount(): Int = imageList.size
}