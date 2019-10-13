package xin.neoto.redspread

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bridgefy.sdk.client.*
import com.bridgefy.sdk.client.MessageListener
import com.bridgefy.sdk.client.StateListener
import com.bridgefy.sdk.framework.exceptions.MessageException

import xin.neoto.redspread.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            val data = HashMap<String, String>()
            data.put("foo", "Hello world")
            val message =
                Message.Builder().setContent(data).build()
            Bridgefy.sendBroadcastMessage(message)
            Snackbar.make(window.decorView.rootView, "Message sent", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(item_list)


        BridgefyUtils.enableBluetooth(applicationContext)
        val b1 = BridgefyUtils.checkBluetoothPermission(applicationContext)
        val b2 = BridgefyUtils.checkLocationPermissions(applicationContext)

        if (!b1 || !b2) {
            Snackbar.make(window.decorView.rootView, "Permission denied", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val builder = Config.Builder()
        builder.setEnergyProfile(BFEnergyProfile.HIGH_PERFORMANCE)
        builder.setEncryption(false)
        Bridgefy.initialize(
            this.applicationContext,
            "f1c2dd65-085a-4fe6-8a3d-dc29aa61dbd6",
            object: RegistrationListener() {
                override fun onRegistrationSuccessful(bridgefyClient: BridgefyClient) {
                    Snackbar.make(window.decorView.rootView, "Reg successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    // Bridgefy is ready to start
                    Bridgefy.start(
                        object: MessageListener() {
                            override fun onBroadcastMessageReceived(message: Message?) {
                                Snackbar.make(window.decorView.rootView, "Message !!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }

                            override fun onMessageSent(messageId: String?) {
                                Snackbar.make(window.decorView.rootView, "Sent !!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }

                            override fun onMessageFailed(message: Message?, e: MessageException?) {
                                super.onMessageFailed(message, e)
                            }
                        },
                        object: StateListener() {
                            override fun onStarted() {
                                Snackbar.make(window.decorView.rootView, "Started!!! ", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                            override fun onStartError(message: String?, errorCode: Int) {
                                Snackbar.make(window.decorView.rootView, "Start Error!!! " + message, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                            override fun onDeviceConnected(device: Device?, session: Session?) {
                                Snackbar.make(window.decorView.rootView, "Device!!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                        }, builder.build())
                }

                override fun onRegistrationFailed(errorCode: Int, message: String) {
                    Snackbar.make(window.decorView.rootView, "Bluetooth failed, " + message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    // Something went wrong: handle error code, maybe print the message
                }
            }
        )
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private val values: List<DummyContent.DummyItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
