package com.linkknown.ilearning.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Android Drawable Tint 着色工具类
 * 【【致命 bug】】
 * if you instantiate two Drawable objects from the same image resource,
 * then change a property (such as the alpha) for one of the Drawables,
 * then it will also affect the other. So when dealing with multiple
 * instances of an image resource, instead of directly transforming the
 * Drawable, you should perform a tween animation.
 * 如果从同一个图像资源实例化两个可绘制对象，然后更改其中一个可绘制对象的属性（如alpha），
 * 则它也将影响另一个可绘制对象。因此，在处理一个图像资源的多个实例时，应该执行中间动画，而不是直接转换可绘制的。
 *
 * ConstantState 享元模式
 * 不同的Drawble如果加载的是同一个资源，那么将拥有共同的状态，这是google对Drawable 做的内存优化。
 * 在Drawable 中的表现为 ConstantState，ConstantState是抽象静态内部类，Drawable 的子类如
 * ColorDrawble，BitmapDrawable 也分别都进行了不同的实现。而在ConstantState 内部类中保存的就是
 * Drawable 需要展示的信息，在ColorDrawable 中ConstantState 的实现类是ColorState，其中包含了
 * 一些颜色信息；在BitmapDrawable 中ConstantState的实现类是BitmapState，其中包含了Paint，
 * Bitmap，ColorStateList等一些属性，不同的Drawable子类依靠其对应的ConstantState实现类来刷新渲染视图。
 * 默认情况下，从同一资源加载的所有drawables实例都共享一个公共状态，如果修改一个实例的状态，所有其他实例将接收相同的修改。
 *
 * mutate() 使Drawable可变,那么如何才能打破这种状态
 * 上面说到如果要实现对同一个Drawable进行不同着色就必须要打破这种共享状态。默认情况下，从同一资源加载的所有
 * drawables实例都共享一个公共状态; 如果修改一个实例的状态，所有其他实例将接收相同的修改。而mutate() 方法
 * 就是使drawable 可变， 一个可变的drawable不与任何其他drawable共享它的状态，这样如果只修改可变drawable
 * 的属性就不会影响到其他与它加载同一个资源的drawable。
 */
public class DrawableUtil {

    /**
     * 对目标Drawable 进行着色
     *
     * @param drawable 目标Drawable
     * @param color    着色的颜色值
     * @return 着色处理后的Drawable
     */
    public static Drawable tintDrawable(@NonNull Drawable drawable, int color) {
        // 获取此drawable的共享状态实例
        Drawable wrappedDrawable = getCanTintDrawable(drawable);
        // 进行着色
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    /**
     * 对目标Drawable 进行着色。
     * 通过ColorStateList 指定单一颜色
     *
     * @param drawable 目标Drawable
     * @param color    着色值
     * @return 着色处理后的Drawable
     */
    public static Drawable tintListDrawable(@NonNull Drawable drawable, int color) {
        return tintListDrawable(drawable, ColorStateList.valueOf(color));
    }

    /**
     * 对目标Drawable 进行着色
     *
     * @param drawable 目标Drawable
     * @param colors   着色值
     * @return 着色处理后的Drawable
     */
    public static Drawable tintListDrawable(@NonNull Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = getCanTintDrawable(drawable);
        // 进行着色
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 获取可以进行tint 的Drawable
     * <p>
     * 对原drawable进行重新实例化  newDrawable()
     * 包装  warp()
     * 可变操作 mutate()
     *
     * @param drawable 原始drawable
     * @return 可着色的drawable
     */
    @NonNull
    private static Drawable getCanTintDrawable(@NonNull Drawable drawable) {
        // 获取此drawable的共享状态实例
        Drawable.ConstantState state = drawable.getConstantState();
        // 对drawable 进行重新实例化、包装、可变操作
        return DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
    }

}
