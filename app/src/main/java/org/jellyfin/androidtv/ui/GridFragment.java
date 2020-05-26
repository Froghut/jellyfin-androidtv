package org.jellyfin.androidtv.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jellyfin.androidtv.R;
import org.jellyfin.androidtv.TvApp;
import org.jellyfin.androidtv.itemhandling.BaseRowItem;
import org.jellyfin.androidtv.itemhandling.ItemRowAdapter;
import org.jellyfin.androidtv.model.FilterOptions;
import org.jellyfin.androidtv.presentation.HorizontalGridPresenter;
import org.jellyfin.androidtv.util.InfoLayoutHelper;
import org.jellyfin.androidtv.util.Utils;
import org.jellyfin.apiclient.model.entities.SortOrder;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

public class GridFragment extends Fragment {
    private static final String TAG = "HorizontalGridFragment";
    private static boolean DEBUG = false;

    protected TextView mTitleView;
    private TextView mStatusText;
    private TextView mCounter;
    protected FrameLayout mSpinner;
    protected ViewGroup mGridDock;
    protected LinearLayout mInfoRow;
    protected LinearLayout mToolBar;
    private ItemRowAdapter mAdapter;
    private Presenter mGridPresenter;
    private Presenter.ViewHolder mGridViewHolder;
    private BaseGridView mGridView;
    private OnItemViewSelectedListener mOnItemViewSelectedListener;
    private OnItemViewClickedListener mOnItemViewClickedListener;
    private int mSelectedPosition = -1;

    protected int SMALL_CARD = Utils.convertDpToPixel(TvApp.getApplication(), 116);
    protected int MED_CARD = Utils.convertDpToPixel(TvApp.getApplication(), 175);
    protected int LARGE_CARD = Utils.convertDpToPixel(TvApp.getApplication(), 210);
    protected int SMALL_BANNER = Utils.convertDpToPixel(TvApp.getApplication(), 58);
    protected int MED_BANNER = Utils.convertDpToPixel(TvApp.getApplication(), 88);
    protected int LARGE_BANNER = Utils.convertDpToPixel(TvApp.getApplication(), 105);

    /**
     * Sets the grid presenter.
     */
    public void setGridPresenter(HorizontalGridPresenter gridPresenter) {
        if (gridPresenter == null) {
            throw new IllegalArgumentException("Grid presenter may not be null");
        }
        gridPresenter.setOnItemViewSelectedListener(mRowSelectedListener);
        if (mOnItemViewClickedListener != null) {
            gridPresenter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
        mGridPresenter = gridPresenter;
    }

    /**
     * Sets the grid presenter.
     */
    public void setGridPresenter(VerticalGridPresenter gridPresenter) {
        if (gridPresenter == null) {
            throw new IllegalArgumentException("Grid presenter may not be null");
        }
        gridPresenter.setOnItemViewSelectedListener(mRowSelectedListener);
        if (mOnItemViewClickedListener != null) {
            gridPresenter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
        mGridPresenter = gridPresenter;
    }

    /**
     * Returns the grid presenter.
     */
    public Presenter getGridPresenter() {
        return mGridPresenter;
    }

    /**
     * Sets the object adapter for the fragment.
     */
    public void setAdapter(ItemRowAdapter adapter) {
        mAdapter = adapter;
        updateAdapter();
    }

    /**
     * Returns the object adapter.
     */
    public ObjectAdapter getAdapter() {
        return mAdapter;
    }

    public int getGridHeight() {
        return Utils.convertDpToPixel(TvApp.getApplication(), 400);
    }

    public void setItem(BaseRowItem item) {
        if (item != null) {
            mTitleView.setText(item.getFullName());
            InfoLayoutHelper.addInfoRow(getActivity(), item, mInfoRow, true, true);
        } else {
            mTitleView.setText("");
            mInfoRow.removeAllViews();
        }
    }

    public class SortOption {
        public String name;
        public String value;
        public SortOrder order;

        public SortOption(String name, String value, SortOrder order) {
            this.name = name;
            this.value = value;
            this.order = order;
        }
    }

    protected Map<Integer, SortOption> sortOptions = new HashMap<>();
    {
        sortOptions.put(0, new SortOption(TvApp.getApplication().getString(R.string.lbl_name), "SortName", SortOrder.Ascending));
        sortOptions.put(1, new SortOption(TvApp.getApplication().getString(R.string.lbl_date_added), "DateCreated,SortName", SortOrder.Descending));
        sortOptions.put(2, new SortOption(TvApp.getApplication().getString(R.string.lbl_premier_date), "PremiereDate,SortName", SortOrder.Descending));
        sortOptions.put(3,new SortOption(TvApp.getApplication().getString(R.string.lbl_rating), "OfficialRating,SortName", SortOrder.Ascending));
        sortOptions.put(4,new SortOption(TvApp.getApplication().getString(R.string.lbl_critic_rating), "CriticRating,SortName", SortOrder.Descending));
        sortOptions.put(5,new SortOption(TvApp.getApplication().getString(R.string.lbl_last_played), "DatePlayed,SortName", SortOrder.Descending));
    }

    protected String getSortFriendlyName(String value) {
        return getSortOption(value).name;
    }

    protected SortOption getSortOption(String value) {
        for (Integer key : sortOptions.keySet()) {
            SortOption option = sortOptions.get(key);
            if (option.value.equals(value)) return option;
        }

        return new SortOption("Unknown","",SortOrder.Ascending);
    }

    public void setTitle(String text) {
        mTitleView.setText(text);
    }

    public void setStatusText(String folderName) {
        String text = TvApp.getApplication().getResources().getString(R.string.lbl_showing) + " ";
        FilterOptions filters = mAdapter.getFilters();
        if (filters == null || (!filters.isFavoriteOnly() && !filters.isUnwatchedOnly())) {
            text += TvApp.getApplication().getResources().getString(R.string.lbl_all_items);
        } else {
            text += (filters.isUnwatchedOnly() ? TvApp.getApplication().getResources().getString(R.string.lbl_unwatched) : "") + " " +
                    (filters.isFavoriteOnly() ? TvApp.getApplication().getResources().getString(R.string.lbl_favorites) : "");
        }

        if (mAdapter.getStartLetter() != null) {
            text += " " + TvApp.getApplication().getResources().getString(R.string.lbl_starting_with) + " " + mAdapter.getStartLetter();
        }

        text += " " + TvApp.getApplication().getString(R.string.lbl_from) + " '" + folderName + "' " + TvApp.getApplication().getString(R.string.lbl_sorted_by) + " " + getSortFriendlyName(mAdapter.getSortBy());

        mStatusText.setText(text);
    }

    public LinearLayout getToolBar() { return mToolBar; }

    final private OnItemViewSelectedListener mRowSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                           RowPresenter.ViewHolder rowViewHolder, Row row) {
                    int position = mGridView.getSelectedPosition();
                    if (DEBUG) Log.v(TAG, "row selected position " + position);
                    onRowSelected(position);
                    if (mOnItemViewSelectedListener != null && position >= 0) {
                        mOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
                                rowViewHolder, row);
                    }
                }
            };

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(OnItemViewSelectedListener listener) {
        mOnItemViewSelectedListener = listener;
    }

    private void onRowSelected(int position) {
        if (position != mSelectedPosition) {
            mSelectedPosition = position;
        }
        // Update the counter
        updateCounter(position+1);
    }

    public void updateCounter(int position) {
        if (mAdapter != null) {
            mCounter.setText((position)+" | "+ mAdapter.getTotalItems());
        }

    }

    /**
     * Sets an item clicked listener.
     */
    public void setOnItemViewClickedListener(OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mGridPresenter != null) {
            if (mGridPresenter instanceof HorizontalGridPresenter)
                ((HorizontalGridPresenter)mGridPresenter).setOnItemViewClickedListener(mOnItemViewClickedListener);
            else if (mGridPresenter instanceof VerticalGridPresenter)
                ((VerticalGridPresenter)mGridPresenter).setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    /**
     * Returns the item clicked listener.
     */
    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    public void showSpinner() {
        if (getActivity() == null || getActivity().isFinishing() || mSpinner == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideSpinner() {
        if (getActivity() == null || getActivity().isFinishing() || mSpinner == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.horizontal_grid_browse,
                container, false);

        mTitleView = (TextView) root.findViewById(R.id.title);
        mStatusText = (TextView) root.findViewById(R.id.statusText);
        mInfoRow = (LinearLayout) root.findViewById(R.id.infoRow);
        mToolBar = (LinearLayout) root.findViewById(R.id.toolBar);
        mCounter = (TextView) root.findViewById(R.id.counter);
        mSpinner = (FrameLayout) root.findViewById(R.id.spinner);
        mGridDock = (ViewGroup) root.findViewById(R.id.rowsFragment);

        // Hide the description because we don't have room for it
        ((NowPlayingBug)root.findViewById(R.id.npBug)).showDescription(false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createGrid();
    }

    protected void createGrid() {
        mGridViewHolder = mGridPresenter.onCreateViewHolder(mGridDock);
        if (mGridViewHolder instanceof HorizontalGridPresenter.ViewHolder)
            mGridView = ((HorizontalGridPresenter.ViewHolder)mGridViewHolder).getGridView();
        else if (mGridViewHolder instanceof VerticalGridPresenter.ViewHolder)
            mGridView = ((VerticalGridPresenter.ViewHolder)mGridViewHolder).getGridView();

        mGridView.setFocusable(true);
        mGridDock.removeAllViews();
        mGridView.setGravity(Gravity.CENTER_VERTICAL);
        mGridDock.addView(mGridViewHolder.view);

        updateAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void focusGrid() {
        if (mGridView != null) mGridView.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGridView = null;
    }

    /**
     * Sets the selected item position.
     */
    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        if(mGridView != null && mGridView.getAdapter() != null) {
            mGridView.setSelectedPositionSmooth(position);
        }
    }

    private void updateAdapter() {
        if (mGridView != null) {
            mGridPresenter.onBindViewHolder(mGridViewHolder, mAdapter);
            if (mSelectedPosition != -1) {
                mGridView.setSelectedPosition(mSelectedPosition);
            }
        }
    }
}
