package com.ziadsyahrul.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.ziadsyahrul.consumerapp.adapter.SectionPagerAdapter
import com.ziadsyahrul.consumerapp.databinding.ActivityDetailBinding
import com.ziadsyahrul.consumerapp.model.FavoriteModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_POSISI = "extra_posisi"
        const val EXTRA_FAVORITE = "extra_favorite"
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    private lateinit var imagePhoto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f


        with(binding) {
            val data = intent.getParcelableExtra<FavoriteModel>(EXTRA_FAVORITE)
            nameDetail.text = data?.name
            usernameDetail.text = data?.username
            companyDetail.text = data?.company
            locationDetail.text = data?.location
            repositoryDetail.text = data?.repo
            followersDeatil.text = data?.followers
            followingDetail.text = data?.following
            Picasso.get().load(data?.photo).into(imgDetail)
            imagePhoto = data?.photo.toString()
        }
    }
}