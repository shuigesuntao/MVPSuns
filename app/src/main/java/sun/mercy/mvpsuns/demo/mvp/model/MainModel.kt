package sun.mercy.mvpsuns.demo.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.mercy.suns.di.scope.ActivityScope
import com.mercy.suns.integration.IRepositoryManager
import com.mercy.suns.mvp.BaseModel
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.app.utils.convert
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.contract.MainContract
import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.db.UserInfoDb
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.Friend
import sun.mercy.mvpsuns.demo.mvp.model.resp.BlackListResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.FriendResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.GroupMemberResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.GroupsResp
import timber.log.Timber
import javax.inject.Inject

/**
 * ================================================
 * MainModel
 * Created by sun on 2018/2/11
 * ================================================
 */
@ActivityScope
class MainModel @Inject constructor(repositoryManager: IRepositoryManager):
        BaseModel(repositoryManager), MainContract.Model{



    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }
}