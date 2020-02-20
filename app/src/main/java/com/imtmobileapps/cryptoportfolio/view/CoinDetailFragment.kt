package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentCoinDetailBinding
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.util.createEmptyList
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.fragment_coin_detail.*


class CoinDetailFragment : Fragment() {
    
    var selectedCoin: CryptoValue? = null
    
    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var dataBinding: FragmentCoinDetailBinding
    var prefHelper = PreferencesHelper()
    var totalValues: TotalValues? = null
    
    var coin: Coin = Coin("0", "", "", 0, "")
    var cryptoValue: CryptoValue = CryptoValue("", coin, "0", "0", "0", "", 0.0)
    var totalv = TotalValues(0, "0", "0", "0", "")
    var newsListAdapter = NewsListAdapter(cryptoValue, totalv, arrayListOf())
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        return dataBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        println("$TAG ${CoinApp.TEST_APP} IN onViewCreated()")
        // if args are not null
        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).selectedCoin
        }
        
        (activity as AppCompatActivity).supportActionBar?.setTitle("${selectedCoin?.coin?.coinName} Details")
        
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        
        
        viewModel.refresh(prefHelper.getCurrentPersonId()!!)
        viewModel.setCrypto(selectedCoin)
        
        recyclerDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
            newsListAdapter.setRecyclerView(this)
            // pass this to the adapter
            val errorStr = activity?.resources?.getString(R.string.news_error_text)
            newsListAdapter.setNewsErrorString(errorStr)
            
            val resId: Int = R.anim.layout_animation_fall_down
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(activity, resId)
            recyclerDetails.animation = animation.animation
            
        }
        
        println("${CoinApp.TEST_APP} $TAG and selectedcoin is: ${selectedCoin.toString()}")
        
        selectedCoin?.let {
            
            if (CoinApp.fromWeb) {
                viewModel.refreshNews()
            } else {
                viewModel.getCoinNews(it.coin.slug.toString())
            }
            
        }
        
        observeViewModel()
        
    }
    
    fun observeViewModel() {
        
        viewModel.cryptoLiveData.observe(this, Observer { crypto ->
            selectedCoin = crypto
            crypto?.let {
                dataBinding.crypto = crypto
                newsListAdapter.updateCryptoValue(crypto)
            }
        })
        
        viewModel.totals.observe(this, Observer { totals ->
            totalValues = totals
            totals?.let {
                dataBinding.total = totals
                newsListAdapter.updateTotalValues(totals)
                
            }
            
        })
        
        
        viewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                newsListAdapter.updateNewsList(articles)
                CoinApp.newsList = articles
                
            }
        })
        
        viewModel.totalsLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
            
            }
        })
        
        viewModel.newsLoading.observe(this, Observer { newsIsLoading ->
            newsIsLoading?.let {
                println("$TAG ${CoinApp.TEST_APP} viewModel.newsloading is : $it")
                newsListAdapter.showPreloader(it)
                
                
            }
        })
        
        // Could pass the actual error from here - would need to send a string not a boolean
        viewModel.newsLoadError.observe(this, Observer { newsError ->
            newsError?.let {
                println("$TAG ${CoinApp.TEST_APP} newsLoadError is : $it")
                
                val errorStr = activity?.resources?.getString(R.string.news_error_text)
                if (it) {
                    newsListAdapter.updateNewsList(createEmptyList())
                    newsListAdapter.showPreloader(false)
                    newsListAdapter.updateNewsError(it, errorStr)
                }
                
            }
        })
    }
    
    companion object {
        private val TAG = CoinDetailFragment::class.java.simpleName
    }
}
