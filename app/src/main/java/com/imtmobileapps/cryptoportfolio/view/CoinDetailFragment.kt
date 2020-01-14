package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.ColumnInfo

import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentCoinDetailBinding
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.fragment_coin_detail.*
import kotlinx.android.synthetic.main.fragment_coin_list.*


class CoinDetailFragment : Fragment() {

    var selectedCoin: CryptoValue? = null
    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var dataBinding: FragmentCoinDetailBinding
    var prefHelper = PreferencesHelper()
    var totalValues: TotalValues? = null


    var coin : Coin = Coin("0", "BITCOIN", "BTC", "", "")
    var cryptoValue : CryptoValue = CryptoValue("BITCOIN", coin, "111", "45%", "22", "increase", 1.2)
    var totalv = TotalValues(1, "2", "3", "1", "increase")
    private val newsListAdapter = NewsListAdapter(cryptoValue, totalv, arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if args are not null
        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).selectedCoin
        }

        val newList = ArrayList<News>()
        newList.add(News(0, "TitleOne", "a cool desc"))
        newList.add(News(0, "Titletwo", "a better desc"))
        newList.add(News(0, "Titlethree", "a even better desc"))
        newList.add(News(0, "Titlefour", "a four better desc"))
        newList.add(News(0, "Titlefive", "a five better desc"))
        newList.add(News(0, "Titlesix", "a six better desc"))


        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


       // viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.refresh(prefHelper.getCurrentPersonId()!!)
        viewModel.setNewsList(newList)
        viewModel.setCrypto(selectedCoin)


        recyclerDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
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

        viewModel.news.observe(this, Observer { news ->
            news?.let {
                recyclerDetails.visibility = View.VISIBLE
                newsListAdapter.updateNewsList(news)
            }

        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {

            }
        })
    }
}
