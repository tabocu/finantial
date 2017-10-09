package sistemas.puc.com.finantialapp.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static sistemas.puc.com.finantialapp.util.Density.dip2px;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

    private final Context m_context;
    private final Drawable m_divider;

    private int m_paddingLeftDp = 0;
    private int m_paddingRightDp = 0;
    private boolean m_lastItemIncluded = false;

    public DividerItemDecoration(Context context) {
        m_context = context;
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        m_divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    public DividerItemDecoration(Context context, int resId) {
        m_context = context;
        m_divider = ContextCompat.getDrawable(context, resId);
    }

    public void setPaddingLeftDp(int dp) {
        m_paddingLeftDp = dp;
    }

    public int getPaddingLeftDp() {
        return m_paddingLeftDp;
    }

    public void setPaddingRightDp(int dp) {
        m_paddingRightDp = dp;
    }

    public int getPaddingRightDp() {
        return m_paddingRightDp;
    }

    public void setLastItemIncluded(boolean included) {
        m_lastItemIncluded = included;
    }

    public boolean isLastItemIncluded() {
        return m_lastItemIncluded;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + dip2px(m_context, m_paddingLeftDp);
        int right = parent.getWidth() - parent.getPaddingRight() - dip2px(m_context, m_paddingRightDp);

        int childCount = parent.getChildCount();
        if (!m_lastItemIncluded) --childCount;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + m_divider.getIntrinsicHeight();

            m_divider.setBounds(left, top, right, bottom);
            m_divider.draw(c);
        }
    }
}
