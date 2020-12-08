package com.example.subsmanager2.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    /*  isPending 변수는 setValue로 새로운 이벤트를 받으면 true로 바뀌고
        그 이벤트가 실행되면 false로 돌아갑니다.
        - 멀티쓰레딩 환경에서 동시성을 보장하는 AtomicBoolean.
        - 초기값은 false로 초기화
     */
     private val isPending = AtomicBoolean(false)

    /* View(Activity or Fragment 등 LifeCycleOwner)가 활성화 상태가 되거나
        setValue로 값이 바뀌었을 때 호출되는 observe 함수.
       - 내부에 등록된 Observer는 isPending이 true인지 확인하고,
         true일 경우 다시 false로 돌려 놓은 후에 이벤트가 호출되었다고 알립니다.
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        //observer가 여러개 들어와도 오직 1개만 처리하고자 하는 매커니즘
        super.observe(owner, Observer<T> { t ->
            /* isPending변수가 true면 if문 내의 로직을 처리하고 false로 바꾼다
               - 아래의 setValue를 통해서만 pending값이 true로 바뀌기 때문에,
                 Configuration Changed가 일어나도 pending값은 false이기 때문에
                 observe가 데이터를 전달하지 않음      */
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }


    /* 1. 값이 변경되면 false였던 isPending이 true로 바뀌고, Observer가 호출
        - compareAndSet과 setValue의 true로 바꿔주는 작업을 통해,
          setValue를 한 번 하면 observer의 코드도 단 한번만 수행
        - 이렇게 setValue를 한 번 했다면 observer는 두 번할 수 없게 함으로서,
          '단 한번의 이벤트에 대한 Observing'을 구현
     */
    @MainThread
    override fun setValue(t: T?) {
        isPending.set(true)
        super.setValue(t)
    }

    /* 데이터의 속성을 지정해주지 않아도 call만으로 setValue를 호출 가능
     * 이미 세팅된 값이라도 call()를 사용하면 null값을 방출합니다.
     * (변수를 비웁니다.)
     */
    @MainThread
    fun call() {
        value = null
    }
}
