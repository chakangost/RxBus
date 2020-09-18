package com.android.freddie.rxbus

import android.os.Bundle
import android.util.SparseArray
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class RxBus {
    companion object {
        private val subjectMap: SparseArray<PublishSubject<Any>> = SparseArray()
        private val subscriptionMap: MutableMap<Any, CompositeDisposable> = mutableMapOf()

        private fun getSubject(subjectCode: Int): PublishSubject<Any> {
            var subject = subjectMap.get(subjectCode)
            if (subject == null) {
                subject = PublishSubject.create<Any>()
                subjectMap.put(subjectCode, subject)
            }
            return subject
        }

        private fun getCompositeDisposable(obj: Any): CompositeDisposable {
            var compositeDisposable = subscriptionMap[obj]
            if (compositeDisposable == null) {
                compositeDisposable = CompositeDisposable()
                subscriptionMap[obj] = compositeDisposable
            }

            return compositeDisposable
        }

        fun subscribe(subject: Int, lifecycle: Any, action: (Any) -> Unit) {
            val disposable = getSubject(subject).subscribe(action)
            getCompositeDisposable(lifecycle).add(disposable)
        }

        fun unregister(lifecycle: Any) {
            val compositeDisposable = subscriptionMap.remove(lifecycle)
            compositeDisposable?.dispose()
        }

        fun publish(subject: Int, message: Any? = null) {
            val sbj = getSubject(subject)
            if (message == null) {
                val bundle = Bundle()
                sbj.onNext(bundle)
            } else {
                sbj.onNext(message)
            }
        }
    }
}