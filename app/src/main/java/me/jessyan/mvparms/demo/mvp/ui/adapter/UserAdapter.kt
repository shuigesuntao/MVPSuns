package me.jessyan.mvparms.demo.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import me.jessyan.mvparms.demo.R
import me.jessyan.mvparms.demo.mvp.model.entity.User

/**
 * @author sun
 * @date 2018/1/31
 * UserAdapter
 */
class UserAdapter(data: List<User>) : BaseQuickAdapter<User, BaseViewHolder>(R.layout.recycle_list, data) {

    override fun convert(helper: BaseViewHolder, item: User) {
        helper.setText(R.id.tv_name, item.login)
        ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader().loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(item.avatarUrl)
                        .imageView(helper.getView(R.id.iv_avatar))
                        .build())
    }
}