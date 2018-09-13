package com.pos.printer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.pos.utils.ApplicationContext;

public class PrintTicket {

    private ApplicationContext mContext;

    public void printTicket(Context context){
        this.mContext = (ApplicationContext) context;

        String param = "TAMESA \n" +
                "Risiti ya kutest\n" +
                "GUTA\t\t5\t\t500 TZS\n" +
                "ADULT\t\t10\t\t200\n" +
                "Asante sana kwa kutumia huduma zetu";

        byte[] bytes = param.getBytes();
        mContext.getObject().ASCII_PrintBuffer(mContext.getState(), bytes,
                bytes.length);
        mContext.getObject().ASCII_PrintBuffer(mContext.getState(),
                new byte[]{0x0a}, 1);
    }

}
