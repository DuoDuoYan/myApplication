package com.yan.restaurant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yan.restaurant.Holder.ChildViewHolder;
import com.yan.restaurant.Holder.ParentViewHolder;
import com.yan.restaurant.listener.ItemClickListener;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.DataBean;
import com.yan.restaurant.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private Context context;
        private Runnable runnable;
        private List<DataBean> dataBeanList;
        private LayoutInflater mInflater;
        private OnScrollListener mOnScrollListener;
        private OnItemClickListener mListener;

        public RecycleAdapter(Context context, List<DataBean> dataBeanList) {
            this.context = context;
            this.dataBeanList = dataBeanList;
            this.mInflater = LayoutInflater.from(context);
        }

    @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = null;

            switch (viewType){
                case DataBean.PARENT_ITEM:
                    v = mInflater.inflate(R.layout.recycleview_item_parent, parent, false);
                    return new ParentViewHolder(context, v,mListener);
                case DataBean.CHILD_ITEM:
                    v = mInflater.inflate(R.layout.recycleview_item_child, parent, false);
                    return new ChildViewHolder(context, v);
                default:
                    v = mInflater.inflate(R.layout.recycleview_item_parent, parent, false);
                    return new ParentViewHolder(context, v,mListener );
            }

        }

        /**
         * 根据不同的类型绑定View
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {


            switch (getItemViewType(position)){
                case DataBean.PARENT_ITEM:
                    ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                    parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                    break;
                case DataBean.CHILD_ITEM:
                    ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                    childViewHolder.bindView(dataBeanList.get(position), position);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return dataBeanList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return dataBeanList.get(position).getType();
        }

        private ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onExpandChildren(DataBean bean) {
                int position = getCurrentPosition(bean.getID());//确定当前点击的item位置
                List<DataBean> children = getChildDataBean(bean.getChild());//获取要展示的子布局数据对象，注意区分onHideChildren方法中的getChildBean()。
                if (children == null) {
                    return;
                }
                for(int i=0;i<children.size();i++){
                    add(children.get(i), position + i+1);//在当前的item下方插入
                }

                if (position == dataBeanList.size() - 2 && mOnScrollListener != null) { //如果点击的item为最后一个
                    mOnScrollListener.scrollTo(position + 1);//向下滚动，使子布局能够完全展示
                }
            }

            @Override
            public void onHideChildren(DataBean bean) {
                int position = getCurrentPosition(bean.getID());//确定当前点击的item位置
                List<DataBean> children = bean.getChild();//获取子布局对象
                if (children == null) {
                    return;
                }
                Log.v("Adapter","hidden:"+children);
                Log.v("Adapter","position:"+position);
                for(int i=0;i<children.size();i++){
                    Log.v("Adapter","position i:"+i);
                    int a = position + i+1;
                    Log.v("Adapter","delete position :"+a);

                    remove(position + 1);//删除
                }
                if (mOnScrollListener != null) {
                    mOnScrollListener.scrollTo(position);
                }
            }
        };

        /**
         * 在父布局下方插入一条数据
         * @param bean
         * @param position
         */
        public void add(DataBean bean, int position) {
            dataBeanList.add(position, bean);
            notifyItemInserted(position);
        }

        /**
         *移除子布局数据
         * @param position
         */
        protected void remove(int position) {
            dataBeanList.remove(position);
            notifyItemRemoved(position);
        }

        /**
         * 确定当前点击的item位置并返回
         * @param uuid
         * @return
         */
        protected int getCurrentPosition(String uuid) {
            for (int i = 0; i < dataBeanList.size(); i++) {
                if (uuid.equalsIgnoreCase(dataBeanList.get(i).getID())) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 封装子布局数据对象并返回
         * 注意，此处只是重新封装一个DataBean对象，为了标注Type为子布局数据，进而展开，展示数据
         * 要和onHideChildren方法里的getChildBean()区分开来
         * @param bean
         * @return
         */
        private List<DataBean> getChildDataBean(List<DataBean> bean){
            DataBean child = new DataBean();
            child.setType(1);
            List<DataBean> childList = new ArrayList<>();
            for(int i=0;i<bean.size();i++){
                child.setFood(bean.get(i).getFood());
                child.setNum(bean.get(i).getNum());
                childList.add(child);
            }
            return childList;
        }

        /**
         * 滚动监听接口
         */
        public interface OnScrollListener{
            void scrollTo(int pos);
        }

        public void setOnScrollListener(OnScrollListener onScrollListener){
            this.mOnScrollListener = onScrollListener;
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.mListener = listener;
        }


}

