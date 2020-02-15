package line.challenge.memo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MemoListAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<Memo> memoList;

    public MemoListAdapter(Context context, int resource, List<Memo> memoList) {
        this.context = context;
        this.resource = resource;
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
