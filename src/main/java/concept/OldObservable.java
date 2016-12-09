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
        intObservable.addObserver((Observable o, Object arg) -> log.info("{}", arg));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(intObservable);
        log.info("Shutdown");
        executor.shutdown();
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
