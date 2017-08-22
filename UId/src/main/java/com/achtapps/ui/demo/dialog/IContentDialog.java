package com.achtapps.ui.demo.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public interface IContentDialog {

    void createDialog(Context context, Builder builder);

    void setCancelBtn(ImageView imageView, Builder builder);

    void setTitleImage(ImageView imageView, Builder builder);

    void setTitleTxt(TextView textView, Builder builder);

    void setNegativeBtn(TextView textView, Builder builder);

    void setPositiveBtn(TextView textView, Builder builder);

}
