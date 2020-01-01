package com.imtmobileapps.cryptoportfolio.view


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager

import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.viewmodel.CryptoListViewModel
import kotlinx.android.synthetic.main.fragment_coin_list.*

/**
 * A simple [Fragment] subclass.
 */
class CoinListFragment : Fragment() {

    private lateinit var viewModel: CryptoListViewModel
    private val coinListAdapter = CoinListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CryptoListViewModel::class.java)
        viewModel.refresh(1)

        coinsListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinListAdapter
        }

        //swipe refresh layout
        refreshLayout.setOnRefreshListener {
            coinsListView.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refresh(1)
            refreshLayout.isRefreshing = false
        }


        observeModel()

    }

    fun observeModel() {
        viewModel.coins.observe(this, Observer { coins ->
            coins?.let {
                coinsListView.visibility = View.VISIBLE
                coinListAdapter.updateCoinList(coins)
            }

        })

        viewModel.cryptosLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    coinsListView.visibility = View.GONE
                }
            }
        })
    }


}
