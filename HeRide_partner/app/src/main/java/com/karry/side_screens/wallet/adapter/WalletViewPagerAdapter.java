package com.karry.side_screens.wallet.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <h1>WalletViewPagerAdapter</h1>
 * This class is used for adding the pager in wallet
 */


public class WalletViewPagerAdapter extends FragmentStatePagerAdapter
{
    private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private final List<String> mFragmentTitleList = new ArrayList<String>();

    @Inject
    public WalletViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * <h2>addFragment</h2>
     * This method is used for adding the fragment
     * @param fragment fragment which is need to be added
     * @param title title for the fragment
     */
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);


    }

    @Override
    public Fragment getItem(int position) {
        return  mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
