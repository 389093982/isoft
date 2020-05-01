package com.linkknown.ilearning;

import android.Manifest;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.linkknown.ilearning.activity.CourseListActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.adapter.MainActivityFragmentAdapter;
import com.linkknown.ilearning.fragment.ClassifyFragment;
import com.linkknown.ilearning.fragment.HomeFragment;
import com.linkknown.ilearning.fragment.MineFragment;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.viewpage.MainActivityViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 存储首页对应的三个片段
    private List<Fragment> mFragments;

    @BindView(R.id.mViewPage)
    public MainActivityViewPager mViewPage;
    @BindView(R.id.radioGroup)
    public RadioGroup radioGroup;
    @BindView(R.id.imageButton0)
    public RadioButton rb_home;
    @BindView(R.id.imageButton1)
    public RadioButton rb_category;
    @BindView(R.id.imageButton2)
    public RadioButton rb_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.initFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton0:
                mViewPage.setCurrentItem(0);
                break;
            case R.id.imageButton1:
                mViewPage.setCurrentItem(1);
                break;
            case R.id.imageButton2:
                mViewPage.setCurrentItem(2);
                break;
            default:
                break;
        }
        // 设置按钮组样式
        this.setRadioGroupStatus();
    }

    //设置选中和未选择的状态
    private void setRadioGroupStatus() {
        this.setRadioButtonStatus(this.rb_home);
        this.setRadioButtonStatus(this.rb_category);
        this.setRadioButtonStatus(this.rb_mine);
    }

    private void setRadioButtonStatus(RadioButton rb) {
        if (rb.isChecked()) {
            rb.setTextColor(ContextCompat.getColor(this, R.color.button_press));
        } else {
            rb.setTextColor(ContextCompat.getColor(this, R.color.button_normal));
        }
    }

    private void initFragment () {
        mFragments = new ArrayList<>();
        // 创建 3 个片段
        mFragments.add(new HomeFragment());
        mFragments.add(new ClassifyFragment());
        mFragments.add(new MineFragment());

        MainActivityFragmentAdapter fragmentAdapter = new MainActivityFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPage.setAdapter(fragmentAdapter);
        //预渲染页面数量
        mViewPage.setOffscreenPageLimit(2);
        //禁用滑动
        mViewPage.setOnTouchListener((v, event) -> true);

        this.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.imageButton0:
                    mViewPage.setCurrentItem(0);
                    //底部ImageView点击之后变色
                    break;
                case R.id.imageButton1:
                    mViewPage.setCurrentItem(1);
                    break;
                case R.id.imageButton2:
                    mViewPage.setCurrentItem(2);
                    break;
                default:
                    break;
            }
        });
    }
}
