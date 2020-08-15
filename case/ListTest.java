import java.util.List;
import java.lang.StringBuffer;

public class ListTest {
    List<Integer> l;
    StringBuffer buf;

    void m1() {
        l.add(0);
    }

    void m2() {
    	if(l.contains(0)) {
            return;
    	}
    }

    void m3() {
	    buf.append("\n");
    }

}

