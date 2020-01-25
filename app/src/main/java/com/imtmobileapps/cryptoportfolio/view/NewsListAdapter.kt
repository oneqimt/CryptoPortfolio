package com.imtmobileapps.cryptoportfolio.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.ItemCryptoHeaderBinding
import com.imtmobileapps.cryptoportfolio.databinding.ItemHoldingsBinding
import com.imtmobileapps.cryptoportfolio.databinding.ItemListNewsBinding
import com.imtmobileapps.cryptoportfolio.model.Article
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.getPublishedFormat
import kotlinx.android.synthetic.main.item_preloader_news.view.*

class NewsListAdapter(
    var cryptoValue: CryptoValue,
    var totalValues: TotalValues,
    val articleList: ArrayList<Article>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    var isLoadingNews: Boolean = true
    var newsError: Boolean = false
    var mRecyclerView : RecyclerView? = null
    var errorString : String? = null
    
    fun setRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        
    }
    
    fun setNewsErrorString(errorStr: String?){
        this.errorString = errorStr
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        // set a default
        val view = DataBindingUtil.inflate<ItemListNewsBinding>(
            inflater,
            R.layout.item_list_news,
            parent,
            false
        )
        
        when (viewType) {
            CellType.CRYPTO_HEADER.ordinal -> {
                
                val cryptoview = DataBindingUtil.inflate<ItemCryptoHeaderBinding>(
                    inflater,
                    R.layout.item_crypto_header,
                    parent,
                    false
                )
                return CryptoHeaderViewHolder(cryptoview)
            }
            CellType.TOTAL_HOLDINGS.ordinal -> {
                val holdingsview = DataBindingUtil.inflate<ItemHoldingsBinding>(
                    inflater,
                    R.layout.item_holdings,
                    parent,
                    false
                )
                
                return HoldingsViewHolder(holdingsview)
            }
            
            CellType.PRELOADER_NEWS.ordinal -> {
                val preloaderView = inflater.inflate(R.layout.item_preloader_news, parent, false)
                
                return PreloaderViewHolder(preloaderView)
            }
            
            CellType.NEWS_ITEM.ordinal -> {
    
                /*println("$TAG ${CoinApp.TEST_APP} and in onCreateViewHolder() isLoadingNews is $isLoadingNews")
                println("$TAG ${CoinApp.TEST_APP} and in onCreateViewHolder() newsError is $newsError")*/
    
                val newsview = DataBindingUtil.inflate<ItemListNewsBinding>(
                    inflater,
                    R.layout.item_list_news,
                    parent,
                    false
                )
                return NewsViewHolder(newsview, articleList)
                
            }
            
        }
        // should not get here
        return NewsViewHolder(view, articleList)
        
    }
    
    fun showPreloader(show: Boolean){
        
        isLoadingNews = show
        notifyItemChanged(1)
        
    }
    
    fun updateNewsError(hasError : Boolean, errorStr : String?){
    
        newsError = hasError
        errorString = errorStr
        notifyItemChanged(1)
        
    }
    
    fun updateNewsList(newArticleList: List<Article>) {
        articleList.clear()
        articleList.addAll(newArticleList)
        notifyDataSetChanged()
    }
    
    fun updateCryptoValue(cryptoValue: CryptoValue) {
        this.cryptoValue = cryptoValue
        notifyDataSetChanged()
    }
    
    fun updateTotalValues(totalValues: TotalValues) {
        this.totalValues = totalValues
        notifyDataSetChanged()
    }
    
    override fun getItemCount(): Int = articleList.size + 3
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        
        when (getItemViewType(position)) {
            CellType.CRYPTO_HEADER.ordinal -> {
                val cryptoViewHolder = holder as CryptoHeaderViewHolder
                cryptoViewHolder.view.cryptoItemHeader = cryptoValue
            }
            CellType.TOTAL_HOLDINGS.ordinal -> {
                val holdingsViewHolder = holder as HoldingsViewHolder
                holdingsViewHolder.view.total = totalValues
                holdingsViewHolder.view.cryptoHoldings = cryptoValue
            }
            CellType.PRELOADER_NEWS.ordinal -> {
                val preloaderViewHolder = holder as PreloaderViewHolder
                println("$TAG ${CoinApp.TEST_APP} in onBindViewHolder and isLoadingNews is $isLoadingNews")
                println("$TAG ${CoinApp.TEST_APP} in onBindViewHolder and newsError is $newsError")
                
                if (isLoadingNews){
                    preloaderViewHolder.preloader.visibility = View.VISIBLE
                    mRecyclerView?.smoothScrollToPosition(position)
                    
                    if (newsError){
                        preloaderViewHolder.preloader.visibility = View.GONE
                        preloaderViewHolder.errorText.visibility = View.VISIBLE
                        preloaderViewHolder.errorText.text = errorString
                    }else{
                        preloaderViewHolder.errorText.text = ""
                    }
                
                }else{
                    preloaderViewHolder.preloader.visibility = View.GONE
                    if (newsError){
                        preloaderViewHolder.errorText.visibility = View.VISIBLE
                        preloaderViewHolder.errorText.text = errorString
                    }else{
                        preloaderViewHolder.errorText.text = ""
                    }
                }
                
                
            }
           
            CellType.NEWS_ITEM.ordinal -> {
                if (!newsError){
                    val newsViewHolder = holder as NewsViewHolder
                    newsViewHolder.view.article = articleList[position - 3]
                    val str = getPublishedFormat(articleList[position - 3].publishedAt)
                    newsViewHolder.newsPublishedAt.text = str
                    newsViewHolder.view.listener = newsViewHolder
                }
                
            }
        }
        
    }
    
    override fun getItemViewType(position: Int): Int {
        
        return when (position) {
            0 -> CellType.CRYPTO_HEADER.ordinal
            1 -> CellType.TOTAL_HOLDINGS.ordinal
            2 -> CellType.PRELOADER_NEWS.ordinal
            else -> CellType.NEWS_ITEM.ordinal
        }
        
    }
    
    class CryptoHeaderViewHolder(
        var view: ItemCryptoHeaderBinding
    ) : RecyclerView.ViewHolder(view.root)
    
    class HoldingsViewHolder(var view: ItemHoldingsBinding) :
        RecyclerView.ViewHolder(view.root)
    
    class PreloaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        
        val preloader = itemView.loadingNews
        val errorText = itemView.newsErrorText
        
    }
    
    class NewsViewHolder(var view: ItemListNewsBinding, var articleList: ArrayList<Article>) :
        RecyclerView.ViewHolder(view.root), NewsClickListener {
        
        var selectedNews: Article? = null
        
        val newsPublishedAt = view.newsPublishedAt
        
        override fun onNewsClicked(v: View) {
            selectedNews = articleList[adapterPosition - 3]
            println("$TAG ${CoinApp.TEST_APP} selectedArticle is : ${selectedNews?.toString()}")
            
            val action = CoinDetailFragmentDirections.actionWebFragment()
            action.selectedArticle = selectedNews
            Navigation.findNavController(v).navigate(action)
            
        }
    }
    
    @Suppress("UNUSED_PARAMETER")
    enum class CellType(viewType: Int) {
        CRYPTO_HEADER(0),
        TOTAL_HOLDINGS(1),
        PRELOADER_NEWS(2),
        NEWS_ITEM(3)
    }
    
    companion object {
        private val TAG = NewsListAdapter::class.java.simpleName
    }
    
}