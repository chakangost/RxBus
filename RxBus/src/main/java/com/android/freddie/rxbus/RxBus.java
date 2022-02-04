import android.os.Bundle;
import android.util.SparseArray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Call back 지옥을 벗어나기 위한 Reactivex Bus (Observer, Singleton, holder 디자인 패턴을 사용함)
 */
public class BnkRxBus {

    private BnkRxBus() {}

    private static class InnerInstanceClazz {
        private static final BnkRxBus uniqueInstance = new BnkRxBus();
    }

    public static BnkRxBus getInstance() {
        return InnerInstanceClazz.uniqueInstance;
    }

    private final SparseArray<PublishSubject<Object>> subjectMap = new SparseArray<>();
    private final HashMap<Object, CompositeDisposable> subscriptionMap = new HashMap<>();

    private PublishSubject<Object> getSubject(int subjectCode) {
        PublishSubject<Object> subject = subjectMap.get(subjectCode);
        if (subject == null) {
            subject = PublishSubject.create();
            subjectMap.put(subjectCode, subject);
        }
        return subject;
    }

    private CompositeDisposable getCompositeDisposable(Object obj) {
        CompositeDisposable compositeDisposable = subscriptionMap.get(obj);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            subscriptionMap.put(obj, compositeDisposable);
        }
        return compositeDisposable;
    }

    /**
     * 이벤트를 구독
     *
     * @param subject : Event Integer Define
     * @param lifecycle : Context
     */
    public void subscribe(int subject, @NotNull Object lifecycle, @NotNull Consumer<Object> action) {
        Disposable disposable = getSubject(subject).subscribe(action);
        getCompositeDisposable(lifecycle).add(disposable);
    }

    public void unregister(Object lifecycle) {
        CompositeDisposable compositeDisposable = subscriptionMap.remove(lifecycle);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    /**
     * 이벤트를 패치함
     *
     * @param subject : Event Integer Define
     * @param message : Data
     */
    public void publish(int subject, @Nullable Object message) {
        PublishSubject<Object> sbj = getSubject(subject);
        if (message == null) {
            Bundle bundle = new Bundle();
            sbj.onNext(bundle);
        } else {
            sbj.onNext(message);
        }
    }
}
