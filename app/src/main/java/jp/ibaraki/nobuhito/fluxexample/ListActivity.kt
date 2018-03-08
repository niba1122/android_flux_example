package jp.ibaraki.nobuhito.fluxexample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import jp.ibaraki.nobuhito.fluxexample.recyclerview.ListRecyclerAdapter
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private lateinit var adapter: ListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        adapter = ListRecyclerAdapter(this)

        listView.let {
            val layoutManager = LinearLayoutManager(this)
            it.layoutManager = layoutManager
            it.adapter = adapter
            layoutManager.stackFromEnd = false
            it.addOnScrollListener(EndlessScrollListener(2, layoutManager) {
                viewModel.onScrollToLast()
            })

        }

        viewModel.errorSnackbar.observe(this, Observer {
            it ?: return@Observer
            Snackbar.make(window.decorView, it.message ?: "error", Snackbar.LENGTH_SHORT).show()
        })

        viewModel.listItems.observe(this, Observer {
            it ?: return@Observer
            adapter.items = it
        })

        viewModel.isShownPageLoading.observe(this, Observer {
            it ?: return@Observer
            loadingView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.onInitialize()
    }
}


class EndlessScrollListener(private val visibleThreshold: Int,
                             private val layoutManager: LinearLayoutManager,
                             private val onScrolled: () -> Unit) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView ?: return

        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount

        if (totalItemCount <= firstVisibleItem + visibleItemCount + visibleThreshold) {
            onScrolled()
        }
    }
}

