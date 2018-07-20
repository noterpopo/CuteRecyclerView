package com.popo.cuterecyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class CuteAdapter extends RecyclerView.Adapter<CuteAdapter.CuteViewHolder>{
    private List<Data> dataList;
    private Context context;
    private SlidingMenu slidingMenu=null;
    private boolean isShow=false;
    private OnItemListener onItemListener;
    public ArrayList<Integer> isCheckedList=new ArrayList <>();
    private GifDrawable gifDrawable;
    private Handler handler;
    private Runnable task;
    private Button btSeletcAll;
    private Button btDelete;

    public CuteAdapter(final List<Data> dataList, final Context context ) {
        this.dataList = dataList;
        this.context = context;
        try {
            gifDrawable=new GifDrawable(context.getResources(),R.drawable.bomb);
        }catch (Exception e){
            e.printStackTrace();
        }
        btSeletcAll=(Button)MainActivity.getMainActivity().findViewById(R.id.select_all);
        btSeletcAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<dataList.size();++i){
                    if(!isCheckedList.contains(i)){
                        isCheckedList.add(i);
                    }
                    notifyItemRangeChanged(0,dataList.size());
                }
            }
        });
        btDelete=(Button)MainActivity.getMainActivity().findViewById(R.id.delete);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int position:isCheckedList){
                    closeOpenMenu();
//                    if(isCheckedList.contains(position)){
//                        isCheckedList.remove((Integer) position);
//                    }
                    upadeteIsCheckedList(position);
                    onItemListener.onMenuClick(position);
                    dataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,dataList.size());
                }
                isCheckedList.clear();
                final View buttomMenu=MainActivity.getMainActivity().findViewById(R.id.bottommenu);
                buttomMenu.animate()
                        .translationY(buttomMenu.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                buttomMenu.setVisibility(View.GONE);
                            }
                        });
                isShow=false;
                notifyItemRangeChanged(0,dataList.size());
            }
        });

    }

    @NonNull
    @Override
    public CuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CuteViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CuteViewHolder holder, final int position) {
        if(isCheckedList.contains(position)){
            holder.checkBox.setChecked(true);
        }
        if(isShow){
            ((SlidingMenu)holder.linearLayout.getParent().getParent()).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }else {
            ((SlidingMenu)holder.linearLayout.getParent().getParent()).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
        holder.tvTitle.setText("Item"+position);
        if(isShow&&holder.checkBox.getVisibility()!=View.VISIBLE){
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(500);
            holder.checkBox.startAnimation(mShowAction);
            holder.checkBox.setVisibility(View.VISIBLE);
        }else if(!isShow&&holder.checkBox.getVisibility()!=View.INVISIBLE){
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            mHiddenAction.setDuration(500);
            holder.checkBox.startAnimation(mHiddenAction);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if(isCheckedList.contains(position)){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCheckedList.contains(position)){
                    isCheckedList.remove((Integer) position);
                }else {
                    isCheckedList.add(position);
                }
                if(((CheckBox)v).isChecked()){
                    startShakeByPropertyAnim(((View)(v.getParent().getParent())).findViewById(R.id.logo),0.95f,1.05f,5,800);
                }
                int flag=0;
                for(int i=0;i<dataList.size();++i){
                    if(isCheckedList.contains(i)){
                        flag=1;
                        break;
                    }
                }
                if(flag==0){
                    final View buttomMenu=MainActivity.getMainActivity().findViewById(R.id.bottommenu);
                    buttomMenu.animate()
                            .translationY(buttomMenu.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    buttomMenu.setVisibility(View.GONE);
                                }
                            });
                    isShow=false;
                    notifyItemRangeChanged(0,dataList.size());
                }
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isShow){
                    return false;
                }
                if(slidingMenu!=null&&slidingMenu.isOpen()){
                    return false;
                }
                final View buttomMenu=MainActivity.getMainActivity().findViewById(R.id.bottommenu);
                buttomMenu.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                buttomMenu.setVisibility(View.VISIBLE);
                            }
                        });
                isShow=true;
                isCheckedList.add(position);
                notifyItemRangeChanged(0,dataList.size());
                startShakeByPropertyAnim(v.findViewById(R.id.logo),0.95f,1.05f,5,800);
                ((CheckBox)v.findViewById(R.id.check)).setChecked(true);
                return false;
            }
        });

        holder.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOpenMenu();
                if(isCheckedList.contains(position)){
                    isCheckedList.remove((Integer) position);
                }
                upadeteIsCheckedList(position);
                onItemListener.onMenuClick(position);
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataList.size());
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CuteViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        LinearLayout linearLayout;
        LinearLayout menuLayout;
        ImageView imageView;
        TextView tvTitle;
        TextView tvDes;
        public CuteViewHolder(View itemView) {
            super(itemView);
            checkBox=(CheckBox)itemView.findViewById(R.id.check);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.content);
            menuLayout=(LinearLayout)itemView.findViewById(R.id.menu);
            imageView=(ImageView)itemView.findViewById(R.id.logo);
            tvTitle=(TextView)itemView.findViewById(R.id.item_title);
            tvDes=(TextView)itemView.findViewById(R.id.item_des);
        }
    }

    public void holdOpenMenu(final SlidingMenu slidingMenu){
        this.slidingMenu=slidingMenu;
        GifImageView gifImageView=(GifImageView)slidingMenu.findViewById(R.id.gif);
        gifDrawable.reset();
        gifImageView.setImageDrawable(gifDrawable);

        BoomView boomView=(BoomView)slidingMenu.findViewById(R.id.bomb);
        boomView.animateStart();

        handler=new Handler();
        task=new Runnable() {
            @Override
            public void run() {
                LinearLayout menu=(LinearLayout) slidingMenu.findViewById(R.id.menu);
                menu.performClick();
            }
        };
        handler.postDelayed(task,1500);
    }

    public void closeOpenMenu(){
        if(slidingMenu!=null&&slidingMenu.isOpen()){
            slidingMenu.closeMenu();
            GifImageView gifImageView=(GifImageView)slidingMenu.findViewById(R.id.gif);
            gifDrawable.stop();
            gifImageView.setImageDrawable(gifDrawable);

            handler.removeCallbacks(task);
        }
    }

    public void setOnItemListener(OnItemListener itemListener){
        this.onItemListener=itemListener;
    }

    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
    private void upadeteIsCheckedList(int position){
        for(int i=0;i<isCheckedList.size();++i){
            int num=isCheckedList.get(i);
            if(num>position){
                isCheckedList.set(i,--num);
            }
        }
    }


}
