package util.jdk.exception;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-10-22 17:28
 */
public class tryCatchFinallyTest {

    public static class VeryImportantException extends Exception {

        @Override
        public String toString() {
            return "A Very Important Exception";
        }
    }

    public static class NormalException extends Exception {

        @Override
        public String toString() {
            return "A Normal Exception";
        }
    }

    public static class LostMessage {

        void f() throws VeryImportantException {
            throw new VeryImportantException();
        }

        void v() throws NormalException {
            throw new NormalException();
        }

        public static void main(String[] args) {
            try {
                LostMessage lostMessage = new LostMessage();
                try {
                    lostMessage.f();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lostMessage.v();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
