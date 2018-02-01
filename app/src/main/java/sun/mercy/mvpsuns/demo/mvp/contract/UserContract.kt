package sun.mercy.mvpsuns.demo.mvp.contract

import com.mercy.suns.mvp.IModel
import com.mercy.suns.mvp.IView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.User


/**
 * @author sun
 * @date 2018/1/31
 * UserContract
 */
interface UserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        //申请权限
        fun getRxPermissions(): RxPermissions
        fun showEmpty()
        fun showError()
        fun showContent()
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
    interface Model : IModel {
        fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>>
    }
}