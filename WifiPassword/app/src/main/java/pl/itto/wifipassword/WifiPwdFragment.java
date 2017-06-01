package pl.itto.wifipassword;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import pl.itto.wifipassword.about.InfoActivity;
import pl.itto.wifipassword.data.DataSource;
import pl.itto.wifipassword.data.WifiItem;
import pl.itto.wifipassword.detail.DetailDialogFragment;
import pl.itto.wifipassword.util.Utils;

/**
 * Created by PL_itto on 5/23/2017.
 */

public class WifiPwdFragment extends Fragment implements WifiPskContract.View {
    private static final String TAG = "PL_itto.WifiPwdFragment";

    WifiPskContract.PresenterToView mPresenter;

    @Override
    public void setPresenter(WifiPskContract.PresenterToView presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRefreshing(boolean isRefreshed) {
        mRefreshLayout.setRefreshing(isRefreshed);
    }

    @Override
    public void notifyDataChanged(List<WifiItem> list) {
        mAdapter.updateDataList(list);
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mStateImg;
        ImageView mCopyImg;
        TextView mTxtSSID, mTxtPsk;
        LinearLayout mLayoutTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mLayoutTitle = (LinearLayout) itemView.findViewById(R.id.layout_title);
            mCopyImg = (ImageView) itemView.findViewById(R.id.img_save);
            mStateImg = (ImageView) itemView.findViewById(R.id.img_state);
            mTxtSSID = (TextView) itemView.findViewById(R.id.txt_ssid);
            mTxtPsk = (TextView) itemView.findViewById(R.id.txt_psk);
            mCopyImg.setOnClickListener(this);
            mLayoutTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_save:
                    WifiItem item = mAdapter.getItem(getPosition());
                    if (!item.isLocked()) {
                        showMsg(getString(R.string.text_copy_open_wifi));
                        return;
                    }

                    String psk = item.getPsk();
                    mPresenter.copyToClipBoard(new DataSource.CopyToClipBoardCallback() {
                        @Override
                        public void onCopySuccess() {
                            showMsg(getString(R.string.text_copy_success));
                        }
                    }, psk);
                    break;

                case R.id.layout_title:
                    DetailDialogFragment fragment = DetailDialogFragment.newInstance(mAdapter.getItem(getPosition()));
                    fragment.show(getFragmentManager(), null);
                    break;

            }
        }
    }

    class WifiListAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<WifiItem> mList;
        Context mContext;

        public WifiListAdapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public void updateDataList(List<WifiItem> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public WifiItem getItem(int pos) {
            if (mList != null)
                return mList.get(pos);
            return null;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.wifi_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WifiItem item = mList.get(position);
            holder.mStateImg.setImageResource(item.getResWifi());
            holder.mTxtSSID.setText(getString(R.string.title_ssid, item.getSSID()));
            holder.mTxtPsk.setText(getString(R.string.title_psk, item.getPsk()));

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mListWifi;
    private WifiListAdapter mAdapter;

    private AdView mAdView;
    private AdRequest mAdRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mAdRequest = new AdRequest.Builder().build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);
        /* Init View*/
        mListWifi = (RecyclerView) view.findViewById(R.id.wifi_list);
        mListWifi.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mAdapter = new WifiListAdapter(getContext());
        mListWifi.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadWifiList();
            }
        });
        /** Init AdView**/
        mAdView = (AdView) view.findViewById(R.id.adsView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadWifiList();
        mAdView.loadAd(mAdRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent i = new Intent(this.getContext(), InfoActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static WifiPwdFragment newInstance() {
        return new WifiPwdFragment();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.layout_title:

        }

    }
}
