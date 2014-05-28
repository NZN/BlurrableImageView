package com.nzn.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BlurrableImageView extends ImageView {

	public BlurrableImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BlurrableImageView, defStyle, 0);
		float intensity = array.getFloat(R.styleable.BlurrableImageView_intensity, 5f);
		array.recycle();

		blurResource(intensity);
	}

	@SuppressLint("InlinedApi")
	private void blurResource(float intensity) {
		Bitmap resource = ((BitmapDrawable) getDrawable()).getBitmap();

		final RenderScript rs = RenderScript.create(getContext());
		final Allocation input = Allocation.createFromBitmap(rs, resource, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(intensity);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(resource);

		setImageBitmap(resource);
	}
}