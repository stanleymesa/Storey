package com.example.storey.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storey.R
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.database.StoryWidget
import com.example.storey.data.local.datastore.UserPreferences
import com.example.storey.data.local.model.UserModel
import com.example.storey.data.local.statics.StaticData
import com.example.storey.data.remote.api.ApiConfig
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.data.remote.response.LoginResponse
import com.example.storey.data.repository.RetrofitRepository
import com.example.storey.databinding.ActivityMainBinding
import com.example.storey.ui.adapter.AllStoriesAdapter
import com.example.storey.ui.adapter.LoadingStateAdapter
import com.example.storey.ui.viewmodel.*

private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), AllStoriesAdapter.OnItemClickCallback,
    View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(UserPreferences.getInstance(dataStore))
    }
    private val mainViewModel by viewModels<MainViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private lateinit var storyWidgetViewModel: StoryWidgetViewModel
    private lateinit var adapter: AllStoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        setToolbar()
        setStoryViewModel()
        checkIfFromLogin()
        setAdapter()
        observeData()
        scrollToTopListAdapter()
        setSwipeRefresh()
        binding.fab.setOnClickListener(this)
    }

    private fun setStoryViewModel() {
        val db = MainDatabase.getDatabase(applicationContext)
        val roomViewModelFactory = RoomViewModelFactory.getInstance(db, db.storyWidgetDao())
        storyWidgetViewModel =
            ViewModelProvider(this, roomViewModelFactory)[StoryWidgetViewModel::class.java]
    }

    private fun setToolbar() {
        setSupportActionBar(binding.mainToolbar)
    }

    private fun checkIfFromLogin() {
        intent.getParcelableExtra<LoginResponse>(StaticData.KEY_INTENT_PREF)?.let { loginResponse ->
            val userModel = UserModel(
                token = loginResponse.loginResult.token,
                isLogin = true
            )
            prefViewModel.login(userModel)
        }
    }

    private fun setAdapter() {
        adapter = AllStoriesAdapter(this)
        binding.includeMain.rv.layoutManager = LinearLayoutManager(this)
        binding.includeMain.rv.setHasFixedSize(true)
        binding.includeMain.rv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
    }

    private fun observeData() {
        prefViewModel.getSession().observe(this, { userModel ->

            mainViewModel.getStories(
                token = resources.getString(R.string.input_token, userModel.token),
                apiServices = ApiConfig.getApiServices(),
                database = MainDatabase.getDatabase(applicationContext)
            ).observe(this, {
                adapter.submitData(lifecycle, it)
            })

            mainViewModel.getStoriesWidget(
                token = resources.getString(R.string.input_token, userModel.token),
                size = 5
            )

        })

        mainViewModel.storiesWidget.observe(this, { response ->
            storyWidgetViewModel.delete()
            response.listStory.map {
                storyWidgetViewModel.insert(StoryWidget(photoUrl = it.photoUrl))
            }
        })
    }


    private fun scrollToTopListAdapter() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                binding.includeMain.rv.scrollToPosition(0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    private fun setSwipeRefresh() {
        binding.includeMain.srl.setOnRefreshListener {
            observeData()
            binding.includeMain.srl.isRefreshing = false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.open_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(StaticData.FROM_ACTIVITY, StaticData.MAIN_ACTIVITY)
                startActivity(intent)
            }
            R.id.change_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.logout -> {
                prefViewModel.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onItemClicked(story: ListStoryItem, optionsCompat: ActivityOptionsCompat) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(StaticData.KEY_INTENT_DETAIL, story)
        startActivity(intent, optionsCompat.toBundle())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
            }
        }
    }
}