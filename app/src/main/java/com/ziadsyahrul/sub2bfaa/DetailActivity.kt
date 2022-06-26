package com.ziadsyahrul.sub2bfaa

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.ziadsyahrul.sub2bfaa.adapter.SectionPagerAdapter
import com.ziadsyahrul.sub2bfaa.databinding.ActivityDetailBinding
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.COMPANY
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.CONTENT_URI
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FAVORITE
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FOLLOWERS
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FOLLOWING
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.LOCATION
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.NAME
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.PHOTO
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.REPOSITORY
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.USERNAME
import com.ziadsyahrul.sub2bfaa.helper.Constant
import com.ziadsyahrul.sub2bfaa.helper.FavoriteHelper
import com.ziadsyahrul.sub2bfaa.model.FavoriteModel

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false
    private lateinit var favHelper: FavoriteHelper
    private var favorites: FavoriteModel? = null
    private lateinit var imagePhoto: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        favorites = intent.getParcelableExtra(Constant.EXTRA_FAVORITE)
        if (favorites != null) {
            with(binding) {
                val data = intent.getParcelableExtra<FavoriteModel>(Constant.EXTRA_FAVORITE)
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
            isFavorite = true
            val check: Int = R.drawable.ic_baseline_favorite_24
            binding.btnFavorite.setImageResource(check)
        } else {
            with(binding) {
                val data = intent.getParcelableExtra<User>(Constant.EXTRA_USER)
                nameDetail.text = data?.name
                usernameDetail.text = data?.user_name
                companyDetail.text = data?.company
                locationDetail.text = data?.location
                repositoryDetail.text = data?.repo
                followersDeatil.text = data?.followers
                followingDetail.text = data?.following
                Picasso.get().load(data?.photo).into(imgDetail)
                imagePhoto = data?.photo.toString()
            }

        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        binding.btnFavorite.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        with(binding) {

            val check: Int = R.drawable.ic_baseline_favorite_24
            val uncheck: Int = R.drawable.ic_favorite_border_black
            if (p0?.id == R.id.btn_favorite) {
                if (isFavorite) {
                    favHelper.deleteByUsername(favorites?.username.toString())
                    Toast.makeText(
                        this@DetailActivity,
                        "Dihapus dari list favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnFavorite.setImageResource(uncheck)
                    isFavorite = false
                } else {
                    val username = usernameDetail.text.toString()
                    val name = nameDetail.text.toString()
                    val photo = imagePhoto
                    val company = companyDetail.text.toString()
                    val location = locationDetail.text.toString()
                    val repo = repositoryDetail.text.toString()
                    val followers = followersDeatil.text.toString()
                    val following = followingDetail.text.toString()
                    val dataFav = "1"

                    val values = ContentValues()
                    values.put(USERNAME, username)
                    values.put(NAME, name)
                    values.put(PHOTO, photo)
                    values.put(COMPANY, company)
                    values.put(LOCATION, location)
                    values.put(REPOSITORY, repo)
                    values.put(FOLLOWERS, followers)
                    values.put(FOLLOWING, following)
                    values.put(FAVORITE, dataFav)

                    isFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(
                        this@DetailActivity,
                        "Ditambahkan ke list favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnFavorite.setImageResource(check)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }
}