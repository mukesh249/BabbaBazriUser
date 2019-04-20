package babbabazrii.com.bababazri.Activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Recycler_Listener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener myListener;


    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    GestureDetector myGestureDetector;

    public Recycler_Listener(Context context,OnItemClickListener listener){
        myListener = listener;
        myGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
      View childView = rv.findChildViewUnder(e.getX(),e.getY());
      if (childView!=null && myListener != null && myGestureDetector.onTouchEvent(e))
      {
          myListener.onItemClick(childView,rv.getChildLayoutPosition(childView));
          return true;
      }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
