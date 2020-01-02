package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
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
            viewModel.refreshBypassCache(1)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.coin_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.actionSettings -> {
                view?.let{ Navigation.findNavController(it).navigate(CoinListFragmentDirections.actionSettings() ) }
            }
        }


        return super.onOptionsItemSelected(item)
    }


}
