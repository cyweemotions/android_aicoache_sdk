import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.aicoache.R

class CustomAdapter(private val context: Context, private val data: MutableList<List<String>>) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // 如果没有可复用的视图，则创建新的视图
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.date),
                view.findViewById(R.id.title)
            )
            view.tag = holder  // 将 ViewHolder 存储在 View 中，以便后续复用
        } else {
            // 如果有复用的视图，则直接使用
            view = convertView
            holder = view.tag as ViewHolder
        }

        // 设置 TextView 显示的数据
        holder.dateView.text = data[position][0]
        holder.titleView.text = data[position][1]



        return view
    }
    // ViewHolder 用于存储视图，避免每次都调用 findViewById
    private class ViewHolder(val dateView: TextView, val titleView: TextView,)

}

data class Item(val text1: String, val text2: String)
