package com.a116042018022.sockword;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            //if (Parser.sPhoneCallState == TelephonyManager.CALL_STATE_IDLE) {
            // 手机状态为未来电的空闲状态
            Log.v(this.getClass().getSimpleName(),"Received ACTION_SCREEN_ON");
            Intent lockScreen = new Intent(context, MainActivity.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            //标志位FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS，是为了避免在最近使用程序列表出现Service所启动的Activity
            /**
             * 启动Activity时Intent的Flag，如果不添加FLAG_ACTIVITY_NEW_TASK的标志位，会出现“Calling startActivity()
             * from outside of an Activity”的运行时异常，因为我们是从Service启动的Activity。
             * Activity要存在于activity的栈中，而Service在启动activity时必然不存在一个activity的栈，
             * 所以要新起一个栈，并装入启动的activity。使用该标志位时，也需要在AndroidManifest中声明taskAffinity，
             * 即新task的名称，否则锁屏Activity实质上还是在建立在原来App的task栈中。
             */
            context.startActivity(lockScreen);

            //}
        }
    }




}
