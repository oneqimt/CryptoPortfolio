package com.imtmobileapps.cryptoportfolio.view


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.CryptoListViewModel
import kotlinx.android.synthetic.main.fragment_coin_list.*

class CoinListFragment : Fragment() {
    
    private lateinit var viewModel: CryptoListViewModel
    private val coinListAdapter = CoinListAdapter(arrayListOf())
    
    private var prefHelper = PreferencesHelper()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        val view = inflater.inflate(R.layout.fragment_coin_list, container, false)
        
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        
        viewModel = ViewModelProvider(this).get(CryptoListViewModel::class.java)
       
        // If they are a new user, they will have no holdings in the database
        // CHECK IF DATABASE HOLDINGS are empty first
        val doesPersonHaveHoldings = viewModel.checkIfPersonHasHoldings()
        if (doesPersonHaveHoldings){
            viewModel.refresh(prefHelper.getCurrentPersonId()!!)
        }else{
            viewModel.refreshBypassCache(prefHelper.getCurrentPersonId()!!)
        }
        println("$TAG ${CoinApp.TEST_APP} PERSON ID is ${prefHelper.getCurrentPersonId()}")
        
        CoinApp.fromWeb = false
        
        coinsListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinListAdapter
        }
        
        //swipe refresh layout
        refreshLayout.setOnRefreshListener {
            coinsListView.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache(prefHelper.getCurrentPersonId()!!)
            refreshLayout.isRefreshing = false
        }
        
        
        observeModel()
        
    }
    
    fun observeModel() {
        viewModel.coins.observe(viewLifecycleOwner, Observer { coins ->
            coins?.let {
                
                if (coins.isEmpty()) {
                    loadingView.visibility = View.GONE
                    coinsListView.visibility = View.GONE
                    noHoldingsYet.visibility = View.VISIBLE
                } else {
                    coinsListView.visibility = View.VISIBLE
                    noHoldingsYet.visibility = View.GONE
                    coinListAdapter.updateCoinList(coins)
                }
                
            }
            
        })
        
        viewModel.cryptosLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    coinsListView.visibility = View.GONE
                    noHoldingsYet.visibility = View.GONE
                }
            }
        })
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.coin_list_menu, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        
        when (item.itemId) {
            R.id.actionSettings -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(CoinListFragmentDirections.actionSettings())
                }!!
            }
            R.id.actionLogout -> {
                viewModel.logout()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                
            }
            R.id.addicon -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(CoinListFragmentDirections.actionCoinListFragmentToAddHoldingFragment())
                }!!
                
            }
        }
        
        return super.onOptionsItemSelected(item)
    }
    
    companion object {
        private val TAG = CoinListFragment::class.java.simpleName
    }
    
    
}
