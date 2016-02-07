package com.tx.njuptjsy.redpacketterminator;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class LooterService extends AccessibilityService {

    private AccessibilityNodeInfo mRootNodeInfo = null;//代表整个窗口视图的快照

    private AccessibilityNodeInfo mCurrentNodeInfo;
    private boolean isFirst = true;
    /**
     * 处理传进来的event事件
     *
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (isFirst){
            loopThread();
            isFirst = false;
        }

        mRootNodeInfo = accessibilityEvent.getSource();
        if (mRootNodeInfo == null){
            return;
        }

        //如果当前的类型是窗口内容出现了变化，则判断是否有红包出现
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            List<AccessibilityNodeInfo> redPacketsList = mRootNodeInfo.findAccessibilityNodeInfosByText("微信红包");
            List<AccessibilityNodeInfo> clickedList = mRootNodeInfo.findAccessibilityNodeInfosByText("你领取了");
            if(redPacketsList.size() > 0 && redPacketsList.size() > clickedList.size()){

                Log.e("LooterService:onAccessibilityEvent", "new red packets found");
                mCurrentNodeInfo = redPacketsList.get(redPacketsList.size() - 1);//始终得到当前视图中最新的一个红包

                mCurrentNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//得到微信红包字样所在视图的父视图，接着执行点击操作
                Log.e("LooterService:onAccessibilityEvent=====", mCurrentNodeInfo.toString());
            }
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){

            List<AccessibilityNodeInfo> infoDetails = mRootNodeInfo.findAccessibilityNodeInfosByText("红包详情");
            if (infoDetails != null && infoDetails.size() > 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = infoDetails.get(infoDetails.size() - 1);
                accessibilityNodeInfo.getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }

            List<AccessibilityNodeInfo> infoSlows = mRootNodeInfo.findAccessibilityNodeInfosByText("手慢了");
            if (infoSlows != null && infoSlows.size() > 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = infoSlows.get(infoSlows.size() - 1);
                accessibilityNodeInfo.getParent().getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }

            List<AccessibilityNodeInfo> infoOpens = mRootNodeInfo.findAccessibilityNodeInfosByText("发了一个红包");
            if (infoOpens != null && infoOpens.size() > 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = infoOpens.get(infoOpens.size() - 1);
                //int size = accessibilityNodeInfo.getParent().getChildCount();

                for (int i = 0; i < accessibilityNodeInfo.getParent().getChildCount(); i++) {
                    accessibilityNodeInfo.getParent().getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void loopThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MyApplication.getRunning()){}
            }
        }).start();
    }

}
