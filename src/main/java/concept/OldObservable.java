package concept;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * 자바 9에서 Deprecated 된 Observable의 간단한 쓰임<br/>
 * https://www.youtube.com/watch?v=8fenTR3KOJo 링크의 내용을 재작성
 */
@Slf4j
public class OldObservable {

    public static void main(String[] args) {

        /**
         * Functional Interface -> 다음 문단의 2줄로 대체 가능
        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                log.info("{}", arg);
            }
        });
         */

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(generateObserver());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(intObservable);
        log.info("Shutdown");
        executor.shutdown();
    }

    private static Observer generateObserver() {

        /**
         * 에러가 난 경우는? 처리가 완료된 경우는?<br/>
         * 직접 이 2가지 경우를 대응할 수는 있지만 Observable 자체에서 제공해주지는 않는다.<br/>
         * reactive는 이 2가지 문제를 어떻게 해결하고 있을까? 아래 링크를 참고 <br/>
         * https://github.com/reactive-streams/reactive-streams-jvm/tree/v1.0.0
         */
        return (Observable o, Object arg) -> log.info("{}", arg);
    }

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i=0; i<=10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }
}
